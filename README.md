# Meeting Scheduler
A Tool to manage Meeting Schedules for Employees

You can schedule a meeting to a Employee and manage it using the Employee ID.
This tool can create, update, cancel schedule functions.

Project is build using Java 1.8 and used Maven for build.

Lets see example for each functions,

- Creating a schedule for Employee with Employee ID **MMT01**
    ##### POST   `/employee/api/schedule`
    Request Body
    ```JSON
    	{
    		"meetingSchedule": {
    			"startDate": "01 Apr 2021",
    			"endDate": "10 Apr 2021",
    			"startTime": "10:00",
    			"duration": "60",
    			"repeatable": false
    		},
    		"employeeId": "MMT01"
    	}
    ```
    Response HTTP Code will be, if success **200** else **400**

- Checking a schedule for a Employee using Employee ID **MMT01**
    ##### GET  `/employee/api/schedule/{employeeID}`
    Response Body
    ```JSON
    {
    	"meetingSchedule": {
    		"startDate": "01 Apr 2021",
    		"endDate": "01 Apr 2021",
    		"startTime": "10:00",
    		"duration": "60",
    		"repeatable": false,
    		"frequency": null
    	},
    	"employeeId": "MMT01"
    }
    ```

- Updating a schedule for a Employee using Employee ID **MMT01**
    ##### PUT  `/employee/api/schedule/{employeeID}`
    Request Body
    ```JSON
    {
    	"startDate": "01 Apr 2021",
    	"endDate": "10 Apr 2021",
    	"startTime": "10:00",
    	"duration": "60",
    	"repeatable": true,
    	"frequency": "Daily"
    }
    ```
    Response HTTP Code will be, if success **200** else **404**

- Deleting a schedule for a Employee using Employee ID **MMT01**
    ##### DELETE  `/employee/api/schedule/{employeeID}`
    Response HTTP Code will be, if success **200** else **404**

- Checking a schedule for a any given date **dd MMM yyyy**
    ##### GET  `/employee/api/schedule?date={date}`
    Response Body
    ```JSON
    [
        {
            "ScheduleTime": "10:00:00",
            "Duration": "60",
            "EmployeeId": "100022",
            "ScheduleDate": "01 Apr 2020"
        }
    ]
    ```
    HTTP Code will be, if success **200** else **400**
    
The above are functions available in the tools.

## End
