package com.csw.api.scheduler.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.csw.api.scheduler.entity.Employee;
import com.csw.api.scheduler.entity.MeetingSchedule;
import com.csw.api.scheduler.model.EmployeeScheduleService;

@RestController
@RequestMapping("/employee/api/schedule")
public class EmployeeScheduleController {

	@Autowired
	private EmployeeScheduleService employeeScheduleService;
	@PostMapping
	public ResponseEntity<?> createSchedule(@RequestBody Employee employee) {
		if(!employeeScheduleService.storeSchedule(employee)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		return ResponseEntity.ok(employee);
	}
	
	@GetMapping("/{employeeId}")
	public ResponseEntity<?> fetchSchedule(@PathVariable String employeeId) {
		Optional<Employee> employee = employeeScheduleService.getSchedule(employeeId);
		if(employee.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(employee);
	}
	
	@DeleteMapping("/{employeeId}")
	public ResponseEntity<?> cancelSchedule(@PathVariable String employeeId) {
		if(!employeeScheduleService.deleteEmployee(employeeId)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.ok().build();
	}
	
	@PutMapping("/{employeeId}")
	public ResponseEntity<?> updateSchedule(@PathVariable String employeeId,@RequestBody MeetingSchedule schedule) {
		Optional<Employee> employee = employeeScheduleService.updateSchedule(employeeId,schedule);
		if(employee.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		return ResponseEntity.ok(employee);
	}
	
	@GetMapping
	public ResponseEntity<?> fetchScheduleByGivenDate(@RequestParam(value = "date", required = true) String date) {
		LocalDate givenDate = null;
		try{
			givenDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(com.csw.api.scheduler.model.Constants.DATE_FORMAT));
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		Optional<List<Employee>> employeeList = employeeScheduleService.getScheduleByDate(givenDate);
		if(employeeList.isEmpty()) {
			return ResponseEntity.ok(Collections.emptyList());
		} else {
			List<Map<String,Object>> scheduleList = new ArrayList<Map<String,Object>>();
			employeeList.get().stream().forEach(schedule -> {
				Map<String,Object> employeeSchedule = new HashMap<String,Object>();
				employeeSchedule.put("EmployeeId", schedule.getEmployeeId());
				employeeSchedule.put("ScheduleDate", date);
				employeeSchedule.put("ScheduleTime", schedule.getMeetingSchedule().getStartTime());
				employeeSchedule.put("Duration", schedule.getMeetingSchedule().getDuration());
				scheduleList.add(employeeSchedule);
			});
			return ResponseEntity.ok(scheduleList);
		}
	}
}
