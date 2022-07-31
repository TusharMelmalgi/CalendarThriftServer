package com.example.CalendarThriftServer.objectmapper;

import org.example.CalendarThriftConfiguration.EmployeeAvailabilityDataRequest;
import org.example.CalendarThriftConfiguration.MeetingDetails;

import java.time.LocalDate;

public class MeetingDetailsToEmployeeAvailability {
    public static EmployeeAvailabilityDataRequest map(MeetingDetails meetingDetails){
        EmployeeAvailabilityDataRequest employeeAvailabilityDataRequest = new EmployeeAvailabilityDataRequest(
                meetingDetails.getListOfEmployee(),
                meetingDetails.getStartTime(),
                meetingDetails.getEndTime(),
                meetingDetails.getDateOfMeeting()
        );
        return employeeAvailabilityDataRequest;
    }
}
