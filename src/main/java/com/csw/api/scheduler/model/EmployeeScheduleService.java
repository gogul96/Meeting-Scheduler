package com.csw.api.scheduler.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.csw.api.scheduler.entity.Employee;
import com.csw.api.scheduler.entity.MeetingSchedule;

@Service
@Transactional
public class EmployeeScheduleService {
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	public EmployeeScheduleService(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}
	
	public boolean storeSchedule(Employee employee) {
		try {
			if(employee!=null && !employee.getEmployeeId().isEmpty() && 
					getSchedule(employee.getEmployeeId()).isEmpty() && isValidSchedule(employee.getMeetingSchedule())) {
				employeeRepository.save(employee);
				return true;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public Optional<Employee> getSchedule(String employeeId) {
		return employeeRepository.findByEmployeeId(employeeId);
	}

	public boolean deleteEmployee(String employeeId) {
		Optional<Employee> employee = getSchedule(employeeId);
		if(employee.isPresent()) {
			employeeRepository.delete(employee.get());
			return true;
		}
		return false;
	}
	
	public Optional<Employee> updateSchedule(String employeeId, MeetingSchedule schedule) {
		Optional<Employee> employee = getSchedule(employeeId);
		if(employee.isPresent()) {
			employee.get().setMeetingSchedule(schedule);
			employeeRepository.save(employee.get());
		}
		return employee;
	}
	
	public boolean isValidSchedule(MeetingSchedule meetingSchedule) {
		try {
			return meetingSchedule!=null && 
					(!meetingSchedule.isRepeatable() || isValidFrequency(meetingSchedule.getFrequency()));
		}catch(Exception e) {
			return false;
		}
	}

	public boolean isValidFrequency(String frequency) {
		Optional<String> optionalFrequency = Optional.ofNullable(frequency);
		if(optionalFrequency.isPresent()) {
			for(Constants.ACCECPTABLE_FREQUENCY values: Constants.ACCECPTABLE_FREQUENCY.values()) {
				if(values.name().equals(optionalFrequency.get())) {
					return true;
				}
			}
		}
		return false;
	}

	public Optional<List<Employee>> getScheduleByDate(LocalDate givenDate) {
		final List<Employee> employeeScheduleInGivenDate = new ArrayList<>();
		try { 
			employeeRepository.findAll().forEach(employee -> {
				//Check the StartDate and EndDate is inside givenDate
				MeetingSchedule schedule = employee.getMeetingSchedule();
				if(schedule.getStartDate().isEqual(givenDate) ||  
						(schedule.isRepeatable() && schedule.getStartDate().isBefore(givenDate) &&  
						(schedule.getEndDate().isAfter(givenDate) || schedule.getEndDate().isEqual(givenDate)) && 
								isValidDate(schedule.getFrequency(),schedule.getStartDate(), schedule.getEndDate(),givenDate))) {
						employeeScheduleInGivenDate.add(employee);
					}
			});
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return Optional.ofNullable(employeeScheduleInGivenDate);
	}

	private boolean isValidDate(String frequency,  LocalDate startDate, LocalDate endDate, LocalDate givenDate) {
		if(Constants.ACCECPTABLE_FREQUENCY.Daily.name().equals(frequency)) {
			LocalDate currentDate = startDate.plusDays(1);
			while(currentDate.isBefore(endDate) || currentDate.isEqual(givenDate)) {
				if(currentDate.isEqual(givenDate)) {
					return true;
				}
				currentDate = currentDate.plusDays(1);
			}
		}else if(Constants.ACCECPTABLE_FREQUENCY.Weekdays.name().equals(frequency)) {
			LocalDate currentDate = startDate.plusDays(1);
			while(currentDate.isBefore(endDate) || currentDate.isEqual(givenDate)) {
				if(currentDate.isEqual(givenDate)) {
					DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
					if(dayOfWeek.equals(DayOfWeek.SATURDAY) || dayOfWeek.equals(DayOfWeek.SUNDAY)){
						return false;
					}
					return true;
				}
				currentDate = currentDate.plusDays(1);
			}
		}else if(Constants.ACCECPTABLE_FREQUENCY.Weekly.name().equals(frequency)) {
			LocalDate currentDate = startDate.plusWeeks(1);
			while(currentDate.isBefore(endDate) || currentDate.isEqual(givenDate)) {
				if(currentDate.isEqual(givenDate)) {
					return true;
				}
				currentDate = currentDate.plusWeeks(1);
			}
		}else if(Constants.ACCECPTABLE_FREQUENCY.Monthly.name().equals(frequency)) {
			LocalDate currentDate = startDate.plusMonths(1);
			while(currentDate.isBefore(endDate) || currentDate.isEqual(givenDate)) {
				if(currentDate.isEqual(givenDate)) {
					return true;
				}
				currentDate = currentDate.plusMonths(1);
			}
		}
		return false;
	}

}
