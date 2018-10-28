package com.nat.wsr.payload;

import org.springframework.lang.NonNull;

public class ReportRequest {

	@NonNull
	private String keyUpdateValue;
	

	private String owner;

	
	private String task;
	
	
	private String actualStartDate;

	
	
	private String actualEndDate;

	
	
	private String status;

	
	private String remarks;





	public String getOwner() {
		return owner;
	}


	public void setOwner(String owner) {
		this.owner = owner;
	}


	public String getTask() {
		return task;
	}


	public void setTask(String task) {
		this.task = task;
	}


	public String getActualStartDate() {
		return actualStartDate;
	}


	public void setActualStartDate(String actualStartDate) {
		this.actualStartDate = actualStartDate;
	}


	public String getActualEndDate() {
		return actualEndDate;
	}


	public void setActualEndDate(String actualEndDate) {
		this.actualEndDate = actualEndDate;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getRemarks() {
		return remarks;
	}


	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}


	public String getKeyUpdateValue() {
		return keyUpdateValue;
	}


	public void setKeyUpdateValue(String keyUpdateValue) {
		this.keyUpdateValue = keyUpdateValue;
	}


}
