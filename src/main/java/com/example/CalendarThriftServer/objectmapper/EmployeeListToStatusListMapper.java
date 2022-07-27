package com.example.CalendarThriftServer.objectmapper;

import com.example.CalendarThriftServer.calendarpersistence.model.CompositeKey;
import com.example.CalendarThriftServer.calendarpersistence.model.EmployeeMeeting;
import org.example.CalendarThriftConfiguration.Date;
import org.example.CalendarThriftConfiguration.MeetingDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeListToStatusListMapper {

    CompositeKey compositeKey;

    public String status;

    public Date dateOfMeeting;

    public CompositeKey getCompositeKey() {
        return compositeKey;
    }

    public String getStatus() {
        return status;
    }

    public Date getDateOfMeeting() {
        return dateOfMeeting;
    }
    public static List<EmployeeMeeting> map(MeetingDetails meetingDetails, String meetingId){
        List<String> listOfEmployeeId = meetingDetails.getListOfEmployee();
        Date date = meetingDetails.getDateOfMeeting();
        LocalDate dateOfMeeting = LocalDate.of(date.getYear(),date.getMonth(),date.getDayOfMonth());
        List<EmployeeMeeting> employeeMeetings = new ArrayList<>();
        for (String employeeId : listOfEmployeeId)
        {
            CompositeKey compositeKey1 = new CompositeKey(employeeId,meetingId);
            EmployeeMeeting meetingStatus = new EmployeeMeeting(compositeKey1,"pending",dateOfMeeting);
            employeeMeetings.add(meetingStatus);
        }
        return employeeMeetings;
    }
}
