package com.example.CalendarThriftServer.objectmapper;

import org.example.CalendarThriftConfiguration.Date;
import org.example.CalendarThriftConfiguration.Time;

import java.time.LocalDate;
import java.time.LocalTime;

public class DateToLocalDateMapper {
    LocalDate dateOfMeeting;
    LocalTime startTime;
    LocalTime endTime;

    public DateToLocalDateMapper(LocalDate dateOfMeeting, LocalTime startTime, LocalTime endTime) {
        this.dateOfMeeting = dateOfMeeting;
        this.startTime = startTime;
        this.endTime = endTime;
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
    public static DateToLocalDateMapper map(Date meetDate, Time meetStart, Time meetEnd){
        LocalDate date = LocalDate.of(meetDate.getYear(),meetDate.getMonth(),meetDate.getDayOfMonth());
        LocalTime stime = LocalTime.of(meetStart.getHours(),meetStart.getMins(),meetStart.getSeconds());
        LocalTime etime = LocalTime.of(meetEnd.getHours(),meetEnd.getMins(),meetEnd.getSeconds());
        return new DateToLocalDateMapper(date,stime,etime);
    }
}
