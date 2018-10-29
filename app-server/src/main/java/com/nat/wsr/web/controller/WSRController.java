package com.nat.wsr.web.controller;

import java.net.URI;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nat.wsr.model.KeyUpdates;
import com.nat.wsr.model.TaskUpdates;
import com.nat.wsr.payload.ApiResponse;
import com.nat.wsr.payload.PagedResponse;
import com.nat.wsr.payload.PollResponse;
import com.nat.wsr.payload.ReportRequest;
import com.nat.wsr.security.CurrentUser;
import com.nat.wsr.security.UserPrincipal;
import com.nat.wsr.util.AppConstants;
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
    
    
    @PostMapping("/download")
    public ResponseEntity<?> getReportById(@CurrentUser UserPrincipal currentUser) {
    	Long reportId = wsrAutomatorService.getActiveReport().getId();
        wsrAutomatorService.createReportFromTemplate(reportId);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{pollId}")
                .buildAndExpand(reportId).toUri();
    	return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Report Generated Successfully"));
    }
    
    


}
