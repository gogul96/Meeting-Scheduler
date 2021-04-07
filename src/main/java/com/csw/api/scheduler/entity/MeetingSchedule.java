package com.csw.api.scheduler.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.lang.Nullable;

import com.csw.api.scheduler.model.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingSchedule {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int scheduleId;
	@JsonFormat(shape = Shape.STRING,pattern = Constants.DATE_FORMAT)
	private LocalDate startDate;
	@JsonFormat(shape = Shape.STRING,pattern = Constants.DATE_FORMAT)
	private LocalDate endDate;
	@JsonFormat(shape = Shape.STRING,pattern = Constants.TIME_FORMAT)
	private LocalTime startTime;
	private String duration;
	private boolean isRepeatable;
	@Nullable
	private String frequency;
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	public LocalTime getStartTime() {
		return startTime;
	}
	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public boolean isRepeatable() {
		return isRepeatable;
	}
	public void setRepeatable(boolean isRepeatable) {
		this.isRepeatable = isRepeatable;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
}
