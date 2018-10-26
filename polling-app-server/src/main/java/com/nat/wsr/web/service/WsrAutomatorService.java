package com.nat.wsr.web.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.validation.Valid;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFComments;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.springframework.stereotype.Service;

import com.coreoz.ppt.PptMapper;
import com.nat.wsr.model.Poll;
import com.nat.wsr.payload.PagedResponse;
import com.nat.wsr.payload.PollRequest;
import com.nat.wsr.payload.PollResponse;
import com.nat.wsr.security.UserPrincipal;

/**
 * 
 * @author tarun.leekha
 *
 */
@Service
public class WsrAutomatorService {

	
	private String inputFilePath;

	public String getTaskStatus(String task) {
		String taskStatus = task + " Written successfully";
		return taskStatus;
	}

	public String writeTask(String ownerId,String task) {
		String taskStatus = task + " Written successfully";
		System.out.println("task recieved is " + task);
		this.writeInputToFile(ownerId,task);
		return taskStatus;
	}

	private void writeInputToFile(String ownerId,String input) {
    	this.setInputFilePath("C:\\Users\\tarun.leekha\\wsr_template\\wsr_template_v1.pptx");
        System.out.println( "Hello World!" );
        try(FileOutputStream out = new FileOutputStream("C:\\Users\\tarun.leekha\\wsr_template\\wsrautomator_generated.pptx")) {
        	  try {
				new PptMapper()
				    .text("task_"+ownerId, input)
				    .processTemplate(new FileInputStream(new File(this.getInputFilePath())))
				    .write(out);
				//this.formatPPTText();
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
        	} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				
				e1.printStackTrace();
			}
	}
	
	//Method for formatting ppt text 
    public void formatPPTText(){
    	//creating an empty presentation
    	File file = new File(this.getInputFilePath());
    	try {
    		
    		FileInputStream inputstream = new FileInputStream(file);
    		XMLSlideShow ppt = new XMLSlideShow(inputstream);
    		System.out.println("Total number of input path slides " + ppt.getSlides().size());
    		XSLFSlide firstSlide = ppt.getSlides().get(0);
    		System.out.println("Tittle for first slide"+firstSlide.getTitle());
    		XSLFComments pptComments = firstSlide.getComments();
    		int comments = pptComments.getNumberOfComments();
    		System.out.println("Total no of comments "+comments );
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

	public PagedResponse<PollResponse> getAllTask(UserPrincipal currentUser, int page, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	public Poll createTask(@Valid PollRequest pollRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	public PollResponse getTaskById(Long taskId, UserPrincipal currentUser) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
