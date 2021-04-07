package com.csw.api.scheduler.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private int id;
	
	@Column(unique=true)
	private String employeeId;
	
	@OneToOne(cascade = {CascadeType.ALL})
	private MeetingSchedule meetingSchedule;

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public MeetingSchedule getMeetingSchedule() {
		return meetingSchedule;
	}

	public void setMeetingSchedule(MeetingSchedule meetingSchedule) {
		this.meetingSchedule = meetingSchedule;
	}
}
