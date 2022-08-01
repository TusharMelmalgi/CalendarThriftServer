package com.example.CalendarThriftServer.objectmapper;

import com.example.CalendarThriftServer.calendarpersistence.model.CompositeKey;
import com.example.CalendarThriftServer.calendarpersistence.model.EmployeeMeeting;
import org.example.CalendarThriftConfiguration.Date;
import org.example.CalendarThriftConfiguration.MeetingDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeListToStatusListMapper {

    public static List<EmployeeMeeting> map(MeetingDetails meetingDetails, int meetingId){
        List<String> listOfEmployeeId = meetingDetails.getListOfEmployee();
        Date date = meetingDetails.getDateOfMeeting();
        LocalDate dateOfMeeting = LocalDate.of(date.getYear(),date.getMonth(),date.getDayOfMonth());
        List<EmployeeMeeting> employeeMeetings = new ArrayList<>();
        for (String employeeId : listOfEmployeeId)
        {
            CompositeKey compositeKey1 = new CompositeKey(employeeId,meetingId);
            EmployeeMeeting meetingStatus = new EmployeeMeeting(compositeKey1,"accepted",dateOfMeeting);
            employeeMeetings.add(meetingStatus);
        }
        return employeeMeetings;
    }
}
