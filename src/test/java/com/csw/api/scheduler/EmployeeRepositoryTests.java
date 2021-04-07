package com.csw.api.scheduler;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.csw.api.scheduler.entity.Employee;
import com.csw.api.scheduler.entity.MeetingSchedule;
import com.csw.api.scheduler.model.EmployeeRepository;

@SpringBootTest
public class EmployeeRepositoryTests {

	@Autowired
	private EmployeeRepository employeeRepository;
	
	private Employee employee;
	private MeetingSchedule meetingSchedule;
	
	private static final String DEFAULT_EMPLOYEE_ID = Long.toString(Instant.now().toEpochMilli());
	@BeforeEach
	public void beforeEachTest() {
		meetingSchedule = new MeetingSchedule();
		meetingSchedule.setStartDate(LocalDate.now());
		meetingSchedule.setEndDate(LocalDate.now());
		meetingSchedule.setStartTime(LocalTime.now());
		meetingSchedule.setDuration("60");
		meetingSchedule.setRepeatable(false);
		meetingSchedule.setFrequency(null);
		employee = new Employee();
		employee.setEmployeeId(DEFAULT_EMPLOYEE_ID);
		employee.setMeetingSchedule(meetingSchedule);
	}
	
	@Test
	public void whenSavingEmployee() {
		assertThat(employeeRepository.save(employee)).isEqualTo(employee);
	}
	
	@Test
	public void whenFindingEmployee() {
		assertThat(employeeRepository.findByEmployeeId(DEFAULT_EMPLOYEE_ID).isPresent()).isEqualTo(true);
		assertThat(employeeRepository.findByEmployeeId(DEFAULT_EMPLOYEE_ID).get().getEmployeeId()).isEqualTo(DEFAULT_EMPLOYEE_ID);
	}
	
	@Test
	public void whenDeletingEmployee() {
		employeeRepository.delete(employee);
		assertThat(employeeRepository.findByEmployeeId(DEFAULT_EMPLOYEE_ID).isEmpty()).isEqualTo(true);
	}
}
