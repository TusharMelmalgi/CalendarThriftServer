package com.example.CalendarThriftServer.objectmapper;

import org.example.CalendarThriftConfiguration.Date;
import org.example.CalendarThriftConfiguration.EmployeeAvailabilityDataRequest;
import org.example.CalendarThriftConfiguration.Time;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
public class EmployeeAvailableRequestObject {
    List<String> listOfEmployee;
    LocalDate dateOfMeeting;
    LocalTime startTime;
    LocalTime endTime;

    public List<String> getListOfEmployee() {
        return listOfEmployee;
    }

    public LocalDate getDateOfMeeting() {
        return dateOfMeeting;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public EmployeeAvailableRequestObject(List<String> listOfEmployee, LocalDate dateOfMeeting, LocalTime startTime, LocalTime endTime) {
        this.listOfEmployee = listOfEmployee;
        this.dateOfMeeting = dateOfMeeting;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    public static EmployeeAvailableRequestObject mapDataRequestToObject(EmployeeAvailabilityDataRequest employeeAvailabilityDataRequest){
        Date date = employeeAvailabilityDataRequest.getDateOfMeeting();
        Time startTimeFormat = employeeAvailabilityDataRequest.getStartTime();
        Time endTimeFormat = employeeAvailabilityDataRequest.getEndTime();
        LocalDate meetingDate = LocalDate.of(date.getYear(),date.getMonth(),date.getDayOfMonth());
        LocalTime meetingStart = LocalTime.of(startTimeFormat.getHours(),startTimeFormat.getMins(),startTimeFormat.getSeconds());
        LocalTime meetingEnd = LocalTime.of(endTimeFormat.getHours(),endTimeFormat.getMins(),endTimeFormat.getSeconds());
        return new EmployeeAvailableRequestObject(employeeAvailabilityDataRequest.getListOfEmployeeId(),meetingDate,meetingStart,meetingEnd);
    }
}
