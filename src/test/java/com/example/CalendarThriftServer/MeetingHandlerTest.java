package com.example.CalendarThriftServer;


import calendarpersistence.model.CompositeKey;
import calendarpersistence.model.EmployeeMeeting;
import calendarpersistence.model.Meeting;
import calendarpersistence.repository.EmployeeMeetingRepository;
import calendarpersistence.repository.MeetingRepository;
import org.apache.thrift.TException;
import org.example.CalendarThriftConfiguration.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.dao.DataAccessException;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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

    @Test
    public void meetingHandlerTest_addMeetingDetails_Failed(){

        MeetingDetails meetingDetails = new MeetingDetails(
                Arrays.asList("xyz-12","xyz-13"),
                "test meeting",
                "testing save on repo",
                "xyz-12",
                new Date(10,8,2022),
                new Time(10,00,00),
                new Time(11,00,00),
                true,
                2
        );
        DataAccessException dataAccessException = new DataAccessException("Data cannot be accessed") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        };
        Mockito.when(meetingRepository.save(Mockito.any())).thenThrow(dataAccessException);
        assertThrows(RuntimeException.class, ()-> meetingHandler.addMeetingDetails(meetingDetails));
    }


    @Test
    public void meetingHandlerTest_addMeetingDetails_Success() throws TException {
        MeetingDetails meetingDetails = new MeetingDetails(
                Arrays.asList("xyz-12","xyz-23"),
                "test meeting",
                "testing save on repo",
                "xyz-12",
                new Date(10,8,2022),
                new Time(10,00,00),
                new Time(11,00,00),
                true,
                2
        );
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
                meetingTest.setMeetId("20145");
                return meetingTest;
            }
        });
        String meetingId = meetingHandler.addMeetingDetails(meetingDetails);
        assertEquals("20145",meetingId);
    }
    @Test
    public void meetingHandlerTest_addEmployeeMeetingStatus_Fail(){
        List<EmployeeMeeting> employeeStatusRequests = new ArrayList<EmployeeMeeting>();
        employeeStatusRequests.add(new EmployeeMeeting(new CompositeKey("xyz-12","2"),"pending",LocalDate.of(2022,8,12)));
        employeeStatusRequests.add(new EmployeeMeeting(new CompositeKey("xyz-14","2"),"pending",LocalDate.of(2022,8,12)));
        employeeStatusRequests.add(new EmployeeMeeting(new CompositeKey("xyz-16","2"),"pending",LocalDate.of(2022,8,12)));
        DataAccessException dataAccessException = new DataAccessException("Data cannot be accessed") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        };
        Mockito.when(employeeMeetingRepository.saveAll(Mockito.any())).thenThrow(dataAccessException);
        assertThrows(RuntimeException.class,()-> meetingHandler.addEmployeeMeetingStatus(employeeStatusRequests));
    }
    @Test
    public void meetingHandlerTest_addEmployeeMeetingStatus_Success() throws TException {
        List<EmployeeMeeting> employeeStatusRequests = new ArrayList<EmployeeMeeting>();
        employeeStatusRequests.add(new EmployeeMeeting(new CompositeKey("xyz-12","2"),"pending",LocalDate.of(2022,8,12)));
        employeeStatusRequests.add(new EmployeeMeeting(new CompositeKey("xyz-14","2"),"pending",LocalDate.of(2022,8,12)));
        employeeStatusRequests.add(new EmployeeMeeting(new CompositeKey("xyz-16","2"),"pending",LocalDate.of(2022,8,12)));
        boolean responseFromEmployeeMeeting = meetingHandler.addEmployeeMeetingStatus(employeeStatusRequests);
        assertTrue(responseFromEmployeeMeeting);
    }


    @Test
    public void meetingHandlerTest_findFreeMeetingRoom_Fail() throws TException{
        FindFreeMeetingRoomDataRequest freeMeetingRoomRequest = new FindFreeMeetingRoomDataRequest(
                Arrays.asList(2,3,4),
                new Date(11,8,2022),
                new Time(14,00,00),
                new Time(15,00,00)
        );
        DataAccessException dataAccessException = new DataAccessException("Data cannot be accessed") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        };
        Mockito.when(meetingRepository.findFreeMeetingRoom(Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any())).thenThrow(dataAccessException);
        assertThrows(DataAccessException.class, ()-> meetingHandler.findFreeMeetingRoom(freeMeetingRoomRequest));
    }
    @Test
    public void meetingHandlerTest_findFreeMeetingRoom_NoRoomAvailable() throws TException{
        FindFreeMeetingRoomDataRequest freeMeetingRoomRequest = new FindFreeMeetingRoomDataRequest(
                Arrays.asList(2,3,4),
                new Date(11,8,2022),
                new Time(14,00,00),
                new Time(15,00,00)
        );
        Mockito.when(meetingRepository.findFreeMeetingRoom(Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(Arrays.asList(2,3,4));
        int freeRoomId = meetingHandler.findFreeMeetingRoom(freeMeetingRoomRequest);
        assertEquals(0,freeRoomId);
    }
    @Test
    public void meetingHandlerTest_findFreeMeetingRoom_freeRoomAvailable() throws TException {
        FindFreeMeetingRoomDataRequest freeMeetingRoomRequest = new FindFreeMeetingRoomDataRequest(
                Arrays.asList(2,3,4),
                new Date(11,8,2022),
                new Time(14,00,00),
                new Time(15,00,00)
        );
        Mockito.when(meetingRepository.findFreeMeetingRoom(Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(Arrays.asList(4));
        int freeRoomId = meetingHandler.findFreeMeetingRoom(freeMeetingRoomRequest);
        assertEquals(2,freeRoomId);
    }

    @Test
    public void meetingHandlerTest_meetingRoomAvailable_notAvailable() throws TException {
        MeetingRoomAvailableDataRequest meetingRoomAvailableDataRequest = new MeetingRoomAvailableDataRequest(
                2,
                new Date(11,8,2022),
                new Time(14,00,00),
                new Time(15,00,00)
        );
        Mockito.when(meetingRepository.meetingRoomAvailable(2,LocalDate.of(2022,8,11),
                LocalTime.of(14,00,00),LocalTime.of(15,00,00))).thenReturn(1);
        boolean availability = meetingHandler.meetingRoomAvailable(meetingRoomAvailableDataRequest);
        assertFalse(availability);
    }
    @Test
    public void meetingHandlerTest_meetingRoomAvailable_Available() throws TException{
        MeetingRoomAvailableDataRequest meetingRoomAvailableDataRequest = new MeetingRoomAvailableDataRequest(
                2,
                new Date(11,8,2022),
                new Time(14,00,00),
                new Time(15,00,00)
        );
        Mockito.when(meetingRepository.meetingRoomAvailable(2,LocalDate.of(2022,8,11),
                LocalTime.of(14,00,00),LocalTime.of(15,00,00))).thenReturn(0);
        boolean availability = meetingHandler.meetingRoomAvailable(meetingRoomAvailableDataRequest);
        assertTrue(availability);
    }

}