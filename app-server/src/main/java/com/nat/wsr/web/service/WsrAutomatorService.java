package com.nat.wsr.web.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFComments;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coreoz.ppt.PptMapper;
import com.nat.wsr.model.KeyUpdates;
import com.nat.wsr.model.Reports;
import com.nat.wsr.model.TaskUpdates;
import com.nat.wsr.model.User;
import com.nat.wsr.payload.ReportRequest;
import com.nat.wsr.security.UserPrincipal;
import com.nat.wsr.web.component.SystemConfigComponent;
import com.nat.wsr.web.repository.KeyUpdatesRepository;
import com.nat.wsr.web.repository.ReportsRepository;
import com.nat.wsr.web.repository.TaskUpdatesRepository;
import com.nat.wsr.web.repository.UserRepository;

/**
 * 
 * @author anupriya.gupta
 *
 */
@Service
public class WsrAutomatorService {

	private String inputFilePath;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private KeyUpdatesRepository keyUpdatesRepository;
	
	@Autowired
	private ReportsRepository reportsRepository;
	
	@Autowired
	private TaskUpdatesRepository taskUpdatesRepository;
	

	public String getTaskStatus(String task) {
		String taskStatus = task + " Written successfully";
		return taskStatus;
	}

	public KeyUpdates writeKeyUpdate(Long currentUserId,ReportRequest reportRequest) {
		KeyUpdates keyUpdates = new KeyUpdates();
		
		Instant now = Instant.now();
		keyUpdates.setCreatedAt(now);
		keyUpdates.setReports(getActiveReport());
		keyUpdates.setUser(getUser(currentUserId));
		keyUpdates.setUpdates(reportRequest.getKeyUpdateValue());
		return keyUpdatesRepository.save(keyUpdates);

	}
	
	public TaskUpdates writeTaskUpdate(UserPrincipal currentUser,ReportRequest reportRequest) {
		TaskUpdates taskUpdate = new TaskUpdates();
		
		Instant createdDate = Instant.now();
		taskUpdate.setCreatedAt(createdDate);
		Instant updatedDate = Instant.now();
		taskUpdate.setUpdatedAt(updatedDate);
		taskUpdate.setReports(getActiveReport());
		taskUpdate.setUser(getUser(currentUser.getId()));
		taskUpdate.setOwner(currentUser.getName());
		taskUpdate.setTask(reportRequest.getTask());
		taskUpdate.setActualStartDate(reportRequest.getActualStartDate());
		taskUpdate.setActualEndDate(reportRequest.getActualEndDate());
		taskUpdate.setStatus(reportRequest.getStatus());
		taskUpdate.setRemarks(reportRequest.getRemarks());
		return taskUpdatesRepository.save(taskUpdate);

	}
	
	public void createReportFromTemplate(Long reportId) {
		
		System.out.println("");
		List<User> users = userRepository.findAll();
		writeInputToFile(users,reportId);
		
	}

	
	public Reports getActiveReport() {
		return reportsRepository.findByIsActive(1).get();
	}
	 
	private User getUser(Long currentUserId) {
		return userRepository.findById(currentUserId).get();
	}
	
	private void writeInputToFile(List<User> users,Long reportId) {
		
		String file = getClass().getClassLoader().getResource("template/wsr_template_v1.pptx").getFile();
		this.setInputFilePath(file);
		try (FileOutputStream out = new FileOutputStream(SystemConfigComponent.TEMP_PATH+File.separator+"wsrautomator_generated.pptx")) {
			try {
				
				final PptMapper pptMapper = new PptMapper();
				List<KeyUpdates> keyUpdates = keyUpdatesRepository.findByReportsId(reportId);
				List<TaskUpdates> taskUpdates = taskUpdatesRepository.findByReportsId(reportId);
				List<User> filteredUser = users.stream().filter(user -> !(user.getName().equalsIgnoreCase("manager"))).collect(Collectors.toList());
				filteredUser.stream().forEach(user -> {
					Optional<KeyUpdates> filteredKeyUpdate = keyUpdates.stream().filter(keyUpdate -> keyUpdate.getUser().getId()==user.getId()).findFirst();
					Optional<TaskUpdates> filteredTaskUpdate = taskUpdates.stream().filter(taskUpdate -> taskUpdate.getUser().getId()==user.getId()).findFirst();
					if(filteredKeyUpdate.isPresent() && filteredTaskUpdate.isPresent()) {
						TaskUpdates taskUpdate = filteredTaskUpdate.get();
						pptMapper.text(user.getName()+"_keyupdate", filteredKeyUpdate.get().getUpdates());
						pptMapper.text(user.getName()+"_owner", user.getName());
						pptMapper.text(user.getName()+"_task", taskUpdate.getTask());
						pptMapper.text(user.getName()+"_asd", taskUpdate.getActualStartDate());
						pptMapper.text(user.getName()+"_aed", taskUpdate.getActualEndDate());
						pptMapper.text(user.getName()+"_status", taskUpdate.getStatus());
						pptMapper.text(user.getName()+"_remarks", taskUpdate.getRemarks());
					}
					
				});
				
				pptMapper.processTemplate(new FileInputStream(new File(this.getInputFilePath()))).write(out);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {

			e1.printStackTrace();
		}
	}

	// Method for formatting ppt text
	public void formatPPTText() {
		// creating an empty presentation
		File file = new File(this.getInputFilePath());
		try {

			FileInputStream inputstream = new FileInputStream(file);
			XMLSlideShow ppt = new XMLSlideShow(inputstream);
			System.out.println("Total number of input path slides " + ppt.getSlides().size());
			XSLFSlide firstSlide = ppt.getSlides().get(0);
			System.out.println("Tittle for first slide" + firstSlide.getTitle());
			XSLFComments pptComments = firstSlide.getComments();
			int comments = pptComments.getNumberOfComments();
			System.out.println("Total no of comments " + comments);
			ppt.close();

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public String getInputFilePath() {
		return inputFilePath;
	}

	public void setInputFilePath(String inputFilePath) {
		this.inputFilePath = inputFilePath;
	}

	
}
