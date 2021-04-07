package com.csw.api.scheduler;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.csw.api.scheduler.controller.EmployeeScheduleController;
import com.fasterxml.jackson.core.JsonProcessingException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SchedulerApplication.class)
class SchedulerApplicationTests {

	@Autowired
    private TestRestTemplate restTemplate;
    
    @SuppressWarnings("unchecked")
	private HttpEntity<String> getStringHttpEntity(Object object) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return (HttpEntity<String>) new HttpEntity(object, headers);
    }
    
    @Test
    public void shouldStoreSchedule() throws JsonProcessingException {
    	
    	String employeeBuilder = createDummyEmployee("10001");
    	HttpEntity<String> entity = getStringHttpEntity(employeeBuilder);
        ResponseEntity<String> response = restTemplate.postForEntity("/employee/api/schedule", entity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    
    @Test
    public void shouldFetchSchedule() throws JsonProcessingException {
    	
    	String employeeBuilder = createDummyEmployee("10002");
    	HttpEntity<String> entity = getStringHttpEntity(employeeBuilder);
        restTemplate.postForEntity("/employee/api/schedule", entity, String.class);
        ResponseEntity<String> response = restTemplate.getForEntity("/employee/api/schedule/10002", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldFetchScheduleOnDate() throws JsonProcessingException {
    	String employeeBuilder = createDummyEmployee("10003");
    	HttpEntity<String> entity = getStringHttpEntity(employeeBuilder);
        restTemplate.postForEntity("/employee/api/schedule", entity, String.class);
        ResponseEntity<String> response = restTemplate.getForEntity("/employee/api/schedule?date=01 Apr 2020", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
  
	private String createDummyEmployee(String employeeId) {
		return "{"
				+ "    \"meetingSchedule\": {"
				+ "        \"startDate\": \"01 Apr 2020\","
				+ "        \"endDate\": \"01 Apr 2020\","
				+ "        \"startTime\": \"10:00\","
				+ "        \"duration\": \"60\","
				+ "        \"repeatable\": false,"
				+ "        \"frequency\": \"Daily\""
				+ "    },"
				+ "    \"employeeId\": \""+employeeId+"\""
				+ "}";
	}
	
	private String createDummySchedule() {
		return "{"
				+ "        \"startDate\": \"01 Apr 2020\","
				+ "        \"endDate\": \"10 Apr 2020\","
				+ "        \"startTime\": \"10:00\","
				+ "        \"duration\": \"30\","
				+ "        \"repeatable\": true,"
				+ "        \"frequency\": \"Daily\""
				+ "}";
	}
}
