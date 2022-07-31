package com.example.CalendarThriftServer.service.Interface;

import com.example.CalendarThriftServer.calendarpersistence.model.Meeting;

public interface MeetingService {
    public int addMeetingDetailsToDataBase(Meeting meetingToBeSaved);
}
