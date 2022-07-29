package com.example.CalendarThriftServer;

import com.example.CalendarThriftServer.calendarpersistence.model.EmployeeMeeting;
import com.example.CalendarThriftServer.calendarpersistence.model.Meeting;
import com.example.CalendarThriftServer.calendarpersistence.repository.EmployeeMeetingRepository;
import com.example.CalendarThriftServer.calendarpersistence.repository.MeetingRepository;
import com.example.CalendarThriftServer.objectmapper.DateToLocalDateMapper;
import com.example.CalendarThriftServer.objectmapper.EmployeeListToStatusListMapper;
import com.example.CalendarThriftServer.objectmapper.MeetingDetailsToMeetingMapper;
import org.apache.thrift.TException;
import org.example.CalendarThriftConfiguration.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
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

    @Transactional
    @Override
    public boolean cancelMeetingOfRemovedEmployee(String s) throws TException {
        LocalDate date = LocalDate.now();
        try {
            meetingRepository.cancelMeetingOfEmployee(s);
            employeeMeetingRepository.updateStatusForCancelledMeeting(s);
            return true;
        }catch (DataAccessException ex){
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Transactional
    @Override
    public boolean updateStatusOfRemovedEmployee(String s) throws TException {
        LocalDate date = LocalDate.now();
        try {
            employeeMeetingRepository.updateStatusForRemovedEmployee(s);
            return true;
        }catch (DataAccessException ex){
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public List<String> checkEmployeeAvailability(EmployeeAvailabilityDataRequest employeeAvailabilityDataRequest) throws TException {
        DateToLocalDateMapper formattedDateTime = DateToLocalDateMapper.map(employeeAvailabilityDataRequest.dateOfMeeting,employeeAvailabilityDataRequest.getStartTime(),employeeAvailabilityDataRequest.getEndTime());
        System.out.println("inside this");
        List<String > employeesNotAvailable = meetingRepository.checkEmployeeAvailability(employeeAvailabilityDataRequest.getListOfEmployeeId(),formattedDateTime.getDateOfMeeting(),formattedDateTime.getStartTime(),formattedDateTime.getEndTime());
        System.out.println("outside this");
        System.out.println(employeesNotAvailable.size());
        if(employeesNotAvailable.size()>0){
            return employeesNotAvailable;
        }
        return Arrays.asList();
    }

    @Override
    @Transactional
    public int addMeetingDetails(MeetingDetails meetingDetails) throws TException {
        Meeting meetingToBeAdded = MeetingDetailsToMeetingMapper.map(meetingDetails);
        try {
            System.out.println("here");
            Meeting savedMeeting = meetingRepository.save(meetingToBeAdded);
            int id = savedMeeting.getMeetId();
            System.out.println("id " + id);
            List<EmployeeMeeting> employeeMeetings = EmployeeListToStatusListMapper.map(meetingDetails,id);
            System.out.println(employeeMeetings.size());
            this.addEmployeeMeetingStatus(employeeMeetings);
            return id;
        }catch (DataAccessException ex){
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Transactional
    public boolean addEmployeeMeetingStatus(List<EmployeeMeeting> list) throws TException {
        System.out.println("U are here");
        for (EmployeeMeeting em:list) {
            System.out.println(em.getCompositeKey().toString());
            System.out.println(em.toString());
        }
        try {
            employeeMeetingRepository.saveAll(list);
            return true;
        }catch (DataAccessException ex){
            System.out.println("in exception");
            throw new RuntimeException(ex.getMessage());
        }

    }
    @Override
    public int findFreeMeetingRoom(FindFreeMeetingRoomDataRequest findFreeMeetingRoomDataRequest) throws TException {
        DateToLocalDateMapper formattedDateTime = DateToLocalDateMapper.map(findFreeMeetingRoomDataRequest.getDateOfMeeting(),findFreeMeetingRoomDataRequest.getStartTime(),findFreeMeetingRoomDataRequest.getEndTime());
        List<Integer> occupiedRooms = new ArrayList<>(meetingRepository.findFreeMeetingRoom(findFreeMeetingRoomDataRequest.getMeetingRoomsInOffice(),formattedDateTime.getDateOfMeeting(),formattedDateTime.getStartTime(),formattedDateTime.getEndTime()));
        List<Integer> roomsInOffice = new ArrayList<>(findFreeMeetingRoomDataRequest.getMeetingRoomsInOffice());
        roomsInOffice.removeAll(occupiedRooms);
        if(roomsInOffice.size()==0){
            return 0;
        }
        return roomsInOffice.get(0);
    }

    @Override
    public boolean meetingRoomAvailable(MeetingRoomAvailableDataRequest meetingRoomAvailableDataRequest) throws TException {

        DateToLocalDateMapper formattedDateTime = DateToLocalDateMapper.map(meetingRoomAvailableDataRequest.dateOfMeeting,meetingRoomAvailableDataRequest.getStartTime(),meetingRoomAvailableDataRequest.getEndTime());
        int meetingsInRoom = meetingRepository.meetingRoomAvailable(meetingRoomAvailableDataRequest.getRoomId(),formattedDateTime.getDateOfMeeting(),formattedDateTime.getStartTime(),formattedDateTime.getEndTime());
        if(meetingsInRoom>0){
            return false;
        }
        return true;
    }
}