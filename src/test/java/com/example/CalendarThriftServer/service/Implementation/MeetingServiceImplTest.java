package com.example.CalendarThriftServer.service.Implementation;

import com.example.CalendarThriftServer.calendarpersistence.model.Meeting;
import com.example.CalendarThriftServer.calendarpersistence.repository.MeetingRepository;
import com.example.CalendarThriftServer.service.Interface.MeetingService;
import org.apache.thrift.TException;
import org.example.CalendarThriftConfiguration.Date;
import org.example.CalendarThriftConfiguration.MeetingDetails;
import org.example.CalendarThriftConfiguration.Time;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.dao.DataAccessException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MeetingServiceImplTest {

    @Mock
    MeetingRepository meetingRepository;
    @InjectMocks
    MeetingServiceImpl meetingService;

    @Test
    public void meetingServiceImplTest_addMeetingDetails_Failed(){

        Meeting meetingTest = new Meeting(
                "test meeting",
                "testing save on repo",
                "xyz-12",
                LocalDate.of(2022,8,8),
                LocalTime.of(10,00,00),
                LocalTime.of(11,00,00),
                2,
                true
        );
        DataAccessException dataAccessException = new DataAccessException("Data cannot be accessed") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        };
        Mockito.when(meetingRepository.save(Mockito.any())).thenThrow(dataAccessException);
        assertThrows(RuntimeException.class, ()-> meetingService.addMeetingDetailsToDataBase(meetingTest));
    }
    @Test
    public void meetingServiceImplTest_addMeetingDetails_FailedDueToRandomError(){

        Meeting meetingTest = new Meeting(
                "test meeting",
                "testing save on repo",
                "xyz-12",
                LocalDate.of(2022,8,8),
                LocalTime.of(10,00,00),
                LocalTime.of(11,00,00),
                2,
                true
        );
        Mockito.when(meetingRepository.save(Mockito.any())).thenThrow(NullPointerException.class);
        assertThrows(RuntimeException.class, ()-> meetingService.addMeetingDetailsToDataBase(meetingTest));
    }
    @Test
    public void meetingServiceImplTest_addMeetingDetails_Success(){

        Meeting meetingTest = new Meeting(
                "test meeting",
                "testing save on repo",
                "xyz-12",
                LocalDate.of(2022,8,8),
                LocalTime.of(10,00,00),
                LocalTime.of(11,00,00),
                2,
                true
        );
        Mockito.when(meetingRepository.save(Mockito.any(Meeting.class))).thenAnswer(new Answer<Object>() {
            @Override
            public Meeting answer(InvocationOnMock invocation) throws Throwable {
                meetingTest.setMeetId(20145);
                return meetingTest;
            }
        });
        int meetingId = meetingService.addMeetingDetailsToDataBase(meetingTest);
        assertEquals(20145,meetingId);    }

}