package com.example.CalendarThriftServer.objectmapper;

import com.example.CalendarThriftServer.calendarpersistence.model.EmployeeMeeting;
import com.example.CalendarThriftServer.calendarpersistence.model.Meeting;
import org.example.CalendarThriftConfiguration.Date;
import org.example.CalendarThriftConfiguration.EmployeeMeetingDetails;
import org.example.CalendarThriftConfiguration.Time;

import java.util.ArrayList;
import java.util.List;

public class MeetingsToEmployeeMeetingDetails {
    public static List<EmployeeMeetingDetails> map(List<EmployeeMeeting> employeeMeetingList, List<Meeting> meetingList){
        List<EmployeeMeetingDetails> employeeMeetingDetails = new ArrayList<>();
        for(int i = 0; i< meetingList.size(); i++){
            int meetId = employeeMeetingList.get(i).getCompositeKey().getMeetId();
            String status = employeeMeetingList.get(i).getStatus();
            Meeting meeting = meetingList.get(i);
            Date dateOfMeeting = new Date(meeting.getDateOfMeeting().getDayOfMonth(),meeting.getDateOfMeeting().getMonthValue(),meeting.getDateOfMeeting().getYear());
            Time meetStart = new Time(meeting.getStartTime().getHour(),meeting.getStartTime().getMinute(),meeting.getStartTime().getSecond());
            Time meetEnd = new Time(meeting.getEndTime().getHour(),meeting.getEndTime().getMinute(),meeting.getEndTime().getSecond());
            EmployeeMeetingDetails eachMeeting = new EmployeeMeetingDetails(
                    meetId,
                    status,
                    meeting.getDescription(),
                    meeting.getAgenda(),
                    meeting.getOwnerId(),
                    dateOfMeeting,
                    meetStart,
                    meetEnd,
                    meeting.isAvailable(),
                    meeting.getRoomId()
            );
            employeeMeetingDetails.add(eachMeeting);
        }
        return employeeMeetingDetails;
    }
}
