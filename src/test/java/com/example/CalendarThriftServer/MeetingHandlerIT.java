package com.example.CalendarThriftServer;

import integrationTests.BaseIntegrationIT;
import org.apache.thrift.TException;
import org.example.CalendarThriftConfiguration.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MeetingHandlerIT extends BaseIntegrationIT {

    @Autowired
    private MeetingSvc.Iface meetingSvc;

    @Test
    public void cancelMeetingOfRemovedEmployeeTest_employeeWithNoMeetings() throws TException {
        String employeeWithNoMeetings = "xyz-2";
        boolean meetingsRemoved = meetingSvc.cancelMeetingOfRemovedEmployee(employeeWithNoMeetings);
        Assertions.assertTrue(meetingsRemoved);
    }

    @Test
    public void updateStatusForRemovedEmployeeTest_statusUpdated() throws TException{
        String employeeId = "xyz-3";
        boolean meetingsRemoved = meetingSvc.updateStatusOfRemovedEmployee(employeeId);
        Assertions.assertTrue(meetingsRemoved);

    }

    @Test
    public void checkEmployeeAvailability_busy() throws TException{
        List<String> listOfEmployeeId = Arrays.asList("xyz-10","xyz-13");
        Date date = new Date(1,8,2022);
        Time startTime = new Time(13,00,00);
        Time endTime = new Time(13,30,00);
        EmployeeAvailabilityDataRequest employeeAvailabilityDataRequest = new EmployeeAvailabilityDataRequest(listOfEmployeeId,startTime,endTime,date);
       List<String> listOfBusyEmployee = meetingSvc.checkEmployeeAvailability(employeeAvailabilityDataRequest);
        assertTrue(listOfBusyEmployee.size()>0);
    }
    @Test
    public void checkEmployeeAvailability_available() throws TException{
        List<String> listOfEmployeeId = Arrays.asList("xyz-3","xyz-4");
        Date date = new Date(1,8,2022);
        Time startTime = new Time(13,00,00);
        Time endTime = new Time(13,30,00);
        EmployeeAvailabilityDataRequest employeeAvailabilityDataRequest = new EmployeeAvailabilityDataRequest(listOfEmployeeId,startTime,endTime,date);
        List<String> listOfBusyEmployee = meetingSvc.checkEmployeeAvailability(employeeAvailabilityDataRequest);
        assertTrue(listOfBusyEmployee.size()==0);
    }

    @Test
    public void findFreeMeetingRoom_NoRoomFree() throws TException {
        List<Integer> listOfRoomId = Arrays.asList(1);
        Date date = new Date(1,8,2022);
        Time startTime = new Time(13,00,00);
        Time endTime = new Time(13,30,00);
        FindFreeMeetingRoomDataRequest findFreeMeetingRoomDataRequest = new FindFreeMeetingRoomDataRequest(listOfRoomId,date,startTime,endTime);
        int roomId = meetingSvc.findFreeMeetingRoom(findFreeMeetingRoomDataRequest);
        assertEquals(0,roomId);
    }
    @Test
    public void findFreeMeetingRoom_RoomFree() throws TException {
        List<Integer> listOfRoomId = Arrays.asList(1,2);
        Date date = new Date(1,8,2022);
        Time startTime = new Time(13,00,00);
        Time endTime = new Time(13,30,00);
        FindFreeMeetingRoomDataRequest findFreeMeetingRoomDataRequest = new FindFreeMeetingRoomDataRequest(listOfRoomId,date,startTime,endTime);
        int roomId = meetingSvc.findFreeMeetingRoom(findFreeMeetingRoomDataRequest);
        assertEquals(2,roomId);
    }

    @Test
    public void meetingRoomAvailable_notAvailable() throws TException{
        int roomId = 1;
        Date date = new Date(1,8,2022);
        Time startTime = new Time(13,00,00);
        Time endTime = new Time(13,30,00);
        MeetingRoomAvailableDataRequest meetingRoomAvailableDataRequest = new MeetingRoomAvailableDataRequest(roomId,date,startTime,endTime);
        boolean roomAvailability = meetingSvc.meetingRoomAvailable(meetingRoomAvailableDataRequest);
        assertFalse(roomAvailability);
    }
    @Test
    public void meetingRoomAvailable_available() throws TException{
        int roomId = 2;
        Date date = new Date(1,8,2022);
        Time startTime = new Time(13,00,00);
        Time endTime = new Time(13,30,00);
        MeetingRoomAvailableDataRequest meetingRoomAvailableDataRequest = new MeetingRoomAvailableDataRequest(roomId,date,startTime,endTime);
        boolean roomAvailability = meetingSvc.meetingRoomAvailable(meetingRoomAvailableDataRequest);
        assertTrue(roomAvailability);
    }
}