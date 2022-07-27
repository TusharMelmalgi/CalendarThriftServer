package com.example.CalendarThriftServer;

import calendarpersistence.model.CompositeKey;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
        LocalDate dateOfMeeting = LocalDate.of(date.getYear(),date.getMonth(),date.getDayOfMonth());
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
        try {
            Meeting savedMeeting = meetingRepository.save(addMeeting);
            String id = savedMeeting.getMeetId();
            return id;
        }catch (DataAccessException ex){
            throw new RuntimeException(ex.getMessage());
        }


    }

    @Override
    public boolean addEmployeeMeetingStatus(List<EmployeeStatusDataRequest> list) throws TException {
        List<EmployeeMeeting> employeeMeetingsStatus = new ArrayList<>();
        for(EmployeeStatusDataRequest requests : list){
            CompositeKey ck = new CompositeKey(requests.getEmployeeId(),requests.getMeetingId());
            Date dateOfMeeting = requests.getDateOfMeeting();
            LocalDate date = LocalDate.of(dateOfMeeting.getYear(),dateOfMeeting.getMonth(),dateOfMeeting.getDayOfMonth());
            EmployeeMeeting requestFormatter = new EmployeeMeeting(ck,requests.getStatus(),date);
            employeeMeetingsStatus.add(requestFormatter);
        }
        try {
            employeeMeetingRepository.saveAll(employeeMeetingsStatus);
            return true;
        }catch (DataAccessException ex){
            throw new RuntimeException(ex.getMessage());
        }

    }

    @Override
    public int findFreeMeetingRoom(FindFreeMeetingRoomDataRequest findFreeMeetingRoomDataRequest) throws TException {
        Date date = findFreeMeetingRoomDataRequest.getDateOfMeeting();
        Time startTimeFormat = findFreeMeetingRoomDataRequest.getStartTime();
        Time endTimeFormat = findFreeMeetingRoomDataRequest.getEndTime();
        LocalDate dateOfMeeting = LocalDate.of(date.getYear(),date.getMonth(),date.getDayOfMonth());
        LocalTime startTime = LocalTime.of(startTimeFormat.getHours(),startTimeFormat.getMins(),startTimeFormat.getSeconds());
        LocalTime endTime = LocalTime.of(endTimeFormat.getHours(),endTimeFormat.getMins(),endTimeFormat.getSeconds());
        List<Integer> occupiedRooms = new ArrayList<>(meetingRepository.findFreeMeetingRoom(findFreeMeetingRoomDataRequest.getMeetingRoomsInOffice(),dateOfMeeting,startTime,endTime));
        List<Integer> roomsInOffice = new ArrayList<>(findFreeMeetingRoomDataRequest.getMeetingRoomsInOffice());
        roomsInOffice.removeAll(occupiedRooms);
        if(roomsInOffice.size()==0){
            return 0;
        }
        return roomsInOffice.get(0);
    }

    @Override
    public boolean meetingRoomAvailable(MeetingRoomAvailableDataRequest meetingRoomAvailableDataRequest) throws TException {
        Date date = meetingRoomAvailableDataRequest.getDateOfMeeting();
        Time startTimeFormat = meetingRoomAvailableDataRequest.getStartTime();
        Time endTimeFormat = meetingRoomAvailableDataRequest.getEndTime();
        LocalDate dateOfMeeting = LocalDate.of(date.getYear(),date.getMonth(),date.getDayOfMonth());
        LocalTime startTime = LocalTime.of(startTimeFormat.getHours(),startTimeFormat.getMins(),startTimeFormat.getSeconds());
        LocalTime endTime = LocalTime.of(endTimeFormat.getHours(),endTimeFormat.getMins(),endTimeFormat.getSeconds());
        int meetingsInRoom = meetingRepository.meetingRoomAvailable(meetingRoomAvailableDataRequest.getRoomId(),dateOfMeeting,startTime,endTime);
        if(meetingsInRoom>0){
            return false;
        }
        return true;
    }
}