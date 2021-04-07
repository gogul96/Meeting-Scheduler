package com.csw.api.scheduler;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.csw.api.scheduler.entity.Employee;
import com.csw.api.scheduler.entity.MeetingSchedule;
import com.csw.api.scheduler.model.Constants;
import com.csw.api.scheduler.model.EmployeeRepository;
import com.csw.api.scheduler.model.EmployeeScheduleService;

@SpringBootTest
public class EmployeeScheduleServiceTest {

	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private EmployeeScheduleService employeeScheduleService = new EmployeeScheduleService(employeeRepository);

	public Employee createEmployee(String employeeId, String startDate, String endate, String startTime,
			String duration, boolean repeatable, String frequency) {
		MeetingSchedule meetingSchedule = new MeetingSchedule();
		meetingSchedule.setStartDate(LocalDate.parse(startDate,DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)));
		meetingSchedule.setEndDate(LocalDate.parse(endate,DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)));
		meetingSchedule.setStartTime(LocalTime.parse(startTime,DateTimeFormatter.ofPattern(Constants.TIME_FORMAT)));
		meetingSchedule.setDuration(duration);
		meetingSchedule.setRepeatable(repeatable);
		meetingSchedule.setFrequency(frequency);
		Employee employee = new Employee();
		employee.setEmployeeId(employeeId);
		employee.setMeetingSchedule(meetingSchedule);
		return employee;
	}
	
	@BeforeEach
	public void createEmployees() {
		employeeScheduleService.storeSchedule(createEmployee("1001","01 Mar 2021", "01 Mar 2021", "09:00", "60", false, null));
		employeeScheduleService.storeSchedule(createEmployee("1002","01 Apr 2021", "01 May 2021", "09:00", "60", true, "Daily"));
		employeeScheduleService.storeSchedule(createEmployee("1003","01 Apr 2021", "01 May 2021", "09:00", "60", true, "Weekly"));
		employeeScheduleService.storeSchedule(createEmployee("1004","01 Apr 2021", "01 May 2021", "09:00", "60", true, "Weekdays"));
		employeeScheduleService.storeSchedule(createEmployee("1005","01 Apr 2021", "01 Jun 2021", "09:00", "60", true, "Monthly"));
	}
	
	@Test
	public void givenDifferentFrequencyValueTest() {
		assertThat(employeeScheduleService.isValidFrequency(null)).isEqualTo(false);
		assertThat(employeeScheduleService.isValidFrequency("Daily")).isEqualTo(true);
		assertThat(employeeScheduleService.isValidFrequency("Weekly")).isEqualTo(true);
		assertThat(employeeScheduleService.isValidFrequency("Weekdays")).isEqualTo(true);
		assertThat(employeeScheduleService.isValidFrequency("Monthly")).isEqualTo(true);
		assertThat(employeeScheduleService.isValidFrequency("Yearly")).isEqualTo(false);
		assertThat(employeeScheduleService.isValidFrequency("")).isEqualTo(false);
	}
	
	@Test
	public void givenDateFetchScheduleForNoRepeats() {
		assertThat(employeeScheduleService.getScheduleByDate(LocalDate.of(2021, 2, 1)).get().size()).isEqualTo(0);
		assertThat(employeeScheduleService.getScheduleByDate(LocalDate.of(2021, 3, 1)).get().size()).isEqualTo(1);
	}
	
	@Test
	public void givenDateFetchScheduleForDaily() {
		assertThat(employeeScheduleService.getScheduleByDate(LocalDate.of(2021, 4, 1)).get().size()).isEqualTo(4);
	}
	
	@Test
	public void givenDateFetchScheduleForWeekly() {
		assertThat(employeeScheduleService.getScheduleByDate(LocalDate.of(2021, 4, 8)).get().size()).isEqualTo(3);
	}
	@Test
	public void givenDateFetchScheduleForWeekdays() {
		assertThat(employeeScheduleService.getScheduleByDate(LocalDate.of(2021, 4, 9)).get().size()).isEqualTo(2);
		assertThat(employeeScheduleService.getScheduleByDate(LocalDate.of(2021, 4, 10)).get().size()).isEqualTo(1);
		assertThat(employeeScheduleService.getScheduleByDate(LocalDate.of(2021, 5, 1)).get().size()).isEqualTo(2);
	}
	@Test
	public void givenDateFetchScheduleForMonthly() {
		assertThat(employeeScheduleService.getScheduleByDate(LocalDate.of(2021, 4, 1)).get().size()).isEqualTo(4);
		assertThat(employeeScheduleService.getScheduleByDate(LocalDate.of(2021, 6, 1)).get().size()).isEqualTo(1);
	}
	
}
