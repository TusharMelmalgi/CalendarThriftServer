package com.example.CalendarThriftServer;

import calendarpersistence.model.EmployeeMeeting;
import calendarpersistence.model.Meeting;
import calendarpersistence.repository.EmployeeMeetingRepository;
import calendarpersistence.repository.MeetingRepository;
import org.apache.thrift.TException;
import org.example.CalendarThriftConfiguration.Date;
import org.example.CalendarThriftConfiguration.EmployeeAvailabilityDataRequest;
import org.example.CalendarThriftConfiguration.Time;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import javax.persistence.Entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MeetingHandlerTest {
    @Mock
    MeetingRepository meetingRepository;

    @Mock
    EmployeeMeetingRepository employeeMeetingRepository;
    @InjectMocks
    MeetingHandler meetingHandler;

    @Test
    public void meetingHandlerTest_cancelMeetingForRemovedEmployeeFail() throws TException {
        String employeeId = "xyz-12";
        DataAccessException dataAccessException = new DataAccessException("Data cannot be accessed") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        };

        Mockito.when(meetingRepository.cancelMeetingOfEmployee(Mockito.anyString(),Mockito.any(LocalDate.class))).thenThrow(dataAccessException);
        assertThrows(RuntimeException.class,()->meetingHandler.cancelMeetingOfRemovedEmployee(employeeId));

    }

    @Test
    public void meetingHandlerTest_cancelMeetingForRemovedEmployeeSuccess() throws TException {
        String employeeId = "xyz-15";
        Mockito.when(employeeMeetingRepository.updateStatusForCancelledMeeting(Mockito.anyString(),Mockito.any(LocalDate.class))).thenReturn(true);
        Mockito.when(meetingRepository.cancelMeetingOfEmployee(Mockito.anyString(),Mockito.any(LocalDate.class))).thenReturn(true);
        assertTrue(meetingHandler.cancelMeetingOfRemovedEmployee(employeeId));
    }

    @Test
    public void meetingHandlerTest_updateStatusOfRemovedEmployeeFail() throws TException{
        String employeeId = "xyz-12";
        DataAccessException dataAccessException = new DataAccessException("Data cannot be accessed") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        };
        Mockito.when(employeeMeetingRepository.updateStatusForRemovedEmployee(Mockito.anyString(),Mockito.any(LocalDate.class))).thenThrow(dataAccessException);
        assertThrows(RuntimeException.class,()-> meetingHandler.updateStatusOfRemovedEmployee(employeeId));
    }

    @Test
    public void meetingHandlerTest_updateStatusOfRemovedEmployeeSuccess() throws TException{
        String employeeId = "xyz-12";
        Mockito.when(employeeMeetingRepository.updateStatusForRemovedEmployee(Mockito.anyString(),Mockito.any(LocalDate.class))).thenReturn(true);
        assertTrue(meetingHandler.updateStatusOfRemovedEmployee(employeeId));
    }

    @Test
    public void meetingHandlerTest_checkEmployeeAvailability_notAvailable() throws TException{
        List<String > listOfEmployee = Arrays.asList("xyz-12","xyz-15","xyz-344");

        EmployeeAvailabilityDataRequest employeeAvailabilityDataRequest = new EmployeeAvailabilityDataRequest(
                listOfEmployee,
                new Time(11,00,00),
                new Time(12,00,00),
                new Date(23,8,22)
        );

        Mockito.when(meetingRepository.checkEmployeeAvailability(Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(Arrays.asList("xyz-12"));
        List<String > employeeNotAvailable = meetingHandler.checkEmployeeAvailability(employeeAvailabilityDataRequest);
        assertTrue(employeeNotAvailable.size()>0);
    }

    @Test
    public void meetingHandlerTest_checkEmployeeAvailability_Available() throws TException{
        List<String > listOfEmployee = Arrays.asList("xyz-12","xyz-15","xyz-344");

        EmployeeAvailabilityDataRequest employeeAvailabilityDataRequest = new EmployeeAvailabilityDataRequest(
                listOfEmployee,
                new Time(11,00,00),
                new Time(12,00,00),
                new Date(23,8,22)
        );

        Mockito.when(meetingRepository.checkEmployeeAvailability(Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(Arrays.asList());
        List<String > employeeAvailable = meetingHandler.checkEmployeeAvailability(employeeAvailabilityDataRequest);
        assertEquals("All employees free",employeeAvailable.get(0));
    }


}