package com.nat.wsr.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;

import javax.servlet.ServletContext;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nat.wsr.model.KeyUpdates;
import com.nat.wsr.model.TaskUpdates;
import com.nat.wsr.payload.ApiResponse;
import com.nat.wsr.payload.ReportRequest;
import com.nat.wsr.security.CurrentUser;
import com.nat.wsr.security.UserPrincipal;
import com.nat.wsr.util.CommonUtil;
import com.nat.wsr.web.component.SystemConfigComponent;
import com.nat.wsr.web.service.WsrAutomatorService;

/**
 * 
 * @author tarun.leekha
 *
 */
@RestController
@RequestMapping("/api/report")
public class WSRController {

	@Autowired
	private WsrAutomatorService wsrAutomatorService;
	@Autowired
	private ServletContext servletContext;
	
    private static final Logger logger = LoggerFactory.getLogger(WSRController.class);

   /* @GetMapping
    public PagedResponse<PollResponse> getTasks(@CurrentUser UserPrincipal currentUser,
                                                @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
    	logger.info("WSRController.getTasks()");
        //return wsrAutomatorService.getAllTask(currentUser, page, size);
    }*/

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createReport(@CurrentUser UserPrincipal currentUser,@Valid @RequestBody ReportRequest reportRequest) {
    	
    	KeyUpdates keyUpdate = wsrAutomatorService.writeKeyUpdate(currentUser.getId(),reportRequest);
    	TaskUpdates taskUpdate = wsrAutomatorService.writeTaskUpdate(currentUser, reportRequest);
    	
    	
        /*URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{pollId}")
                .buildAndExpand(keyUpdate.getId()).toUri();
*/
        /*return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Report Inputs Saved Successfully"));*/
    	URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{pollId}")
                .buildAndExpand(keyUpdate.getId()).toUri();
    	return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Report Inputs Saved Successfully"));
    }


    /*@GetMapping("/{taskId}")
    public PollResponse getPollById(@CurrentUser UserPrincipal currentUser,
                                    @PathVariable Long taskId) {
        return wsrAutomatorService.getTaskById(taskId, currentUser);
    }*/
    
    
    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> getReportById(@CurrentUser UserPrincipal currentUser) {
    	Long reportId = wsrAutomatorService.getActiveReport().getId();
        wsrAutomatorService.createReportFromTemplate(reportId);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{pollId}")
                .buildAndExpand(reportId).toUri();
        File file = new File(SystemConfigComponent.TEMP_PATH+File.separator+"wsrautomator_generated.pptx");
		InputStreamResource resource = null;
		try {
			resource = new InputStreamResource(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MediaType mediaType = CommonUtil.getMediaTypeForFileName(this.servletContext, file.getName());
		/*return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Report Generated Successfully"));*/
		HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.setLocation(location);
	    responseHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName());
	    responseHeaders.set(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
	    
		return ResponseEntity
				.ok()
				.headers(responseHeaders)
				.contentType(mediaType)
				.contentLength(file.length())
				.body(resource);
    	
    }
    
    


}
