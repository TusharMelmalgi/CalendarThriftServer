package com.example.CalendarThriftServer;

import calendarpersistence.model.EmployeeMeeting;
import calendarpersistence.model.Meeting;
import calendarpersistence.repository.EmployeeMeetingRepository;
import calendarpersistence.repository.MeetingRepository;
import org.apache.thrift.TException;
import org.example.CalendarThriftConfiguration.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
@Service
public class MeetingHandler implements MeetingSvc.Iface
{
    @Autowired
    MeetingRepository meetingRepository;

    @Autowired
    EmployeeMeetingRepository employeeMeetingRepository;

    @Override
    public String isAlive() throws TException {
        return "Alive";
    }

    @Override
    public boolean cancelMeetingOfRemovedEmployee(String s) throws TException {
        LocalDate date = LocalDate.now();
        try {
            meetingRepository.cancelMeetingOfEmployee(s, date);
            employeeMeetingRepository.updateStatusForCancelledMeeting(s, date);
            return true;
        }catch (DataAccessException ex){
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public boolean updateStatusOfRemovedEmployee(String s) throws TException {
        LocalDate date = LocalDate.now();
        try {
            employeeMeetingRepository.updateStatusForRemovedEmployee(s,date);
            return true;
        }catch (DataAccessException ex){
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public List<String> checkEmployeeAvailability(EmployeeAvailabilityDataRequest employeeAvailabilityDataRequest) throws TException {
        Date date = employeeAvailabilityDataRequest.getDateOfMeeting();
        Time startTimeFormat = employeeAvailabilityDataRequest.getStartTime();
        Time endTimeFormat = employeeAvailabilityDataRequest.getEndTime();
        LocalDate dateOfMeeting = LocalDate.of(date.dayOfMonth,date.getMonth(),date.getYear());
        LocalTime startTime = LocalTime.of(startTimeFormat.getHours(),startTimeFormat.getMins(),startTimeFormat.getSeconds());
        LocalTime endTime = LocalTime.of(endTimeFormat.getHours(),endTimeFormat.getMins(),endTimeFormat.getSeconds());
        List<String > employeesNotAvailable = meetingRepository.checkEmployeeAvailability(employeeAvailabilityDataRequest.listOfEmployeeId,dateOfMeeting,startTime,endTime);
        if(employeesNotAvailable.size()>0){
            return employeesNotAvailable;
        }
        return Arrays.asList("All employees free");
    }

    @Override
    public String addMeetingDetails(MeetingDetails meetingDetails) throws TException {
        return null;
    }

    @Override
    public boolean addEmployeeMeetingStatus(List<EmployeeStatusDataRequest> list) throws TException {
        return false;
    }

    @Override
    public int findFreeMeetingRoom(FindFreeMeetingRoomDataRequest findFreeMeetingRoomDataRequest) throws TException {
        return 0;
    }

    @Override
    public boolean meetingRoomAvailable(MeetingRoomAvailableDataRequest meetingRoomAvailableDataRequest) throws TException {
        return false;
    }
}