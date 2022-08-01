package com.example.CalendarThriftServer.objectmapper;

import com.example.CalendarThriftServer.calendarpersistence.model.Meeting;
import org.example.CalendarThriftConfiguration.Date;
import org.example.CalendarThriftConfiguration.MeetingDetails;
import org.example.CalendarThriftConfiguration.Time;

import java.time.LocalDate;
import java.time.LocalTime;

public class MeetingDetailsToMeetingMapper {

    public static Meeting map(MeetingDetails meetingDetails){
        Date date = meetingDetails.getDateOfMeeting();
        Time startTimeFormat = meetingDetails.getStartTime();
        Time endTimeFormat = meetingDetails.getEndTime();
        LocalDate dateOfMeeting = LocalDate.of(date.getYear(),date.getMonth(),date.getDayOfMonth());
        LocalTime startTime = LocalTime.of(startTimeFormat.getHours(),startTimeFormat.getMins(),startTimeFormat.getSeconds());
        LocalTime endTime = LocalTime.of(endTimeFormat.getHours(),endTimeFormat.getMins(),endTimeFormat.getSeconds());
        Meeting  addMeeting = new Meeting(
                meetingDetails.getDescription(),
                meetingDetails.getAgenda(),
                meetingDetails.getOwnerId(),
                dateOfMeeting,
                startTime,
                endTime,
                meetingDetails.getRoomId(),
                meetingDetails.isIsAvailable()
        );
        return addMeeting;
    }
}
