package com.example.CalendarThriftServer.service.Implementation;

import com.example.CalendarThriftServer.calendarpersistence.model.Meeting;
import com.example.CalendarThriftServer.calendarpersistence.repository.MeetingRepository;
import com.example.CalendarThriftServer.service.Interface.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MeetingServiceImpl implements MeetingService {
    @Autowired
    MeetingRepository meetingRepository;

    @Override
    @Transactional
    public int addMeetingDetailsToDataBase(Meeting meetingToBeSaved){
        try {
            Meeting savedMeeting = meetingRepository.save(meetingToBeSaved);
            int id = savedMeeting.getMeetId();
            return id;
        }catch (DataAccessException ex){
            throw new RuntimeException(ex.getMessage());
        }catch (Exception ex){
            throw new RuntimeException(ex.getMessage());
        }
}
}
