package com.example.CalendarThriftServer;

import com.example.CalendarThriftServer.calendarpersistence.model.EmployeeMeeting;
import com.example.CalendarThriftServer.calendarpersistence.model.Meeting;
import com.example.CalendarThriftServer.calendarpersistence.repository.EmployeeMeetingRepository;
import com.example.CalendarThriftServer.calendarpersistence.repository.MeetingRepository;
import com.example.CalendarThriftServer.objectmapper.*;
import com.example.CalendarThriftServer.service.Implementation.MeetingServiceImpl;
import com.example.CalendarThriftServer.service.Interface.MeetingService;
import org.apache.thrift.TException;
import org.example.CalendarThriftConfiguration.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class MeetingHandler implements MeetingSvc.Iface
{
    Logger logger = LoggerFactory.getLogger(MeetingHandler.class);

    @Autowired
    MeetingService meetingService;
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
    public boolean cancelMeetingOfRemovedEmployee(String employeeId) throws TException {
        LocalDate date = LocalDate.now();
        try {
            meetingRepository.cancelMeetingOfEmployee(employeeId);
            employeeMeetingRepository.updateStatusForCancelledMeeting(employeeId);
            return true;
        }catch (DataAccessException ex){
            logger.error("error trying to cancel meetings for removed employee",ex);
            throw new RuntimeException(ex.getMessage());
        }catch (Exception ex){
            logger.error("error trying to cancel meetings for removed employee",ex);
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Transactional
    @Override
    public boolean updateStatusOfRemovedEmployee(String employeeId) throws TException {
        LocalDate date = LocalDate.now();
        try {
            employeeMeetingRepository.updateStatusForRemovedEmployee(employeeId);
            return true;
        }catch (DataAccessException ex){
            logger.error("error trying to update status for removed employee",ex);
            throw new RuntimeException(ex.getMessage());
        }catch (Exception ex){
            logger.error("error trying to update status for removed employee",ex);
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public List<String> checkEmployeeAvailability(EmployeeAvailabilityDataRequest employeeAvailabilityDataRequest) throws TException {
        DateToLocalDateMapper formattedDateTime = DateToLocalDateMapper.map(employeeAvailabilityDataRequest.dateOfMeeting,employeeAvailabilityDataRequest.getStartTime(),employeeAvailabilityDataRequest.getEndTime());
        List<String > employeesNotAvailable = meetingRepository.checkEmployeeAvailability(employeeAvailabilityDataRequest.getListOfEmployeeId(),formattedDateTime.getDateOfMeeting(),formattedDateTime.getStartTime(),formattedDateTime.getEndTime());
        if(employeesNotAvailable.size()>0){
            return employeesNotAvailable;
        }
        return Collections.emptyList();
    }

    @Override
    @Transactional
    public int addMeetingDetails(MeetingDetails meetingDetails) throws TException {
        Meeting meetingToBeAdded = MeetingDetailsToMeetingMapper.map(meetingDetails);
        MeetingRoomAvailableDataRequest checkMeetingRoom = new MeetingRoomAvailableDataRequest(
                meetingDetails.getRoomId(),
                meetingDetails.getDateOfMeeting(),
                meetingDetails.getStartTime(),
                meetingDetails.getEndTime()
        );
        boolean roomAvailability = meetingRoomAvailable(checkMeetingRoom);
        if(!roomAvailability){
            throw new RuntimeException("meeting room is occupied, unable to process request");
        }
        List<String> listOfEmployee = new ArrayList<>(meetingDetails.getListOfEmployee());
        listOfEmployee.add(meetingDetails.getOwnerId());
        EmployeeAvailabilityDataRequest employeeAvailabilityDataRequest = MeetingDetailsToEmployeeAvailability.map(meetingDetails);
        List<String> busyEmployee = checkEmployeeAvailability(employeeAvailabilityDataRequest);
        if(busyEmployee.size()>0){
            throw new RuntimeException("employee is busy, unable to process request");
        }
        int id = meetingService.addMeetingDetailsToDataBase(meetingToBeAdded);
        List<EmployeeMeeting> employeeMeetings = EmployeeListToStatusListMapper.map(meetingDetails,id);
        this.addEmployeeMeetingStatus(employeeMeetings);
        return id;
    }

    @Transactional
    public boolean addEmployeeMeetingStatus(List<EmployeeMeeting> employeeMeetingList) throws TException {
        try {
            employeeMeetingRepository.saveAll(employeeMeetingList);
            return true;
        }catch (DataAccessException ex){
            logger.error("error trying to add employee status",ex);
            throw new RuntimeException(ex.getMessage());
        }catch (Exception ex){
            logger.error("error trying to add employee status",ex);
            throw new RuntimeException(ex.getMessage());
        }

    }
    @Override
    public int findFreeMeetingRoom(FindFreeMeetingRoomDataRequest findFreeMeetingRoomDataRequest) throws TException {
        DateToLocalDateMapper formattedDateTime = DateToLocalDateMapper.map(findFreeMeetingRoomDataRequest.getDateOfMeeting(),findFreeMeetingRoomDataRequest.getStartTime(),findFreeMeetingRoomDataRequest.getEndTime());
        try {
            List<Integer> occupiedRooms = new ArrayList<>(meetingRepository.findFreeMeetingRoom(findFreeMeetingRoomDataRequest.getMeetingRoomsInOffice(),formattedDateTime.getDateOfMeeting(),formattedDateTime.getStartTime(),formattedDateTime.getEndTime()));
            List<Integer> roomsInOffice = new ArrayList<>(findFreeMeetingRoomDataRequest.getMeetingRoomsInOffice());
            roomsInOffice.removeAll(occupiedRooms);
            if(roomsInOffice.size()==0){
                return 0;
            }
            return roomsInOffice.get(0);
        }catch (Exception ex){
            logger.error("error trying to access meetings, to find free meeting room",ex);
            throw new RuntimeException(ex);
        }

    }

    @Override
    public boolean meetingRoomAvailable(MeetingRoomAvailableDataRequest meetingRoomAvailableDataRequest) throws TException {
        try {
            DateToLocalDateMapper formattedDateTime = DateToLocalDateMapper.map(meetingRoomAvailableDataRequest.getDateOfMeeting(),meetingRoomAvailableDataRequest.getStartTime(),meetingRoomAvailableDataRequest.getEndTime());
            int meetingsInRoom = meetingRepository.meetingRoomAvailable(meetingRoomAvailableDataRequest.getRoomId(),formattedDateTime.getDateOfMeeting(),formattedDateTime.getStartTime(),formattedDateTime.getEndTime());
            if(meetingsInRoom>0){
                return false;
            }
            return true;
        }catch (Exception ex){
            logger.error("error trying to check meeting room availability",ex);
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public List<EmployeeMeetingDetails> getEmployeeMeetingDetails(String employeeId, Date customDate) throws TException {
        try {
            LocalDate dateOfFetch = LocalDate.of(customDate.getYear(),customDate.getMonth(),customDate.getDayOfMonth());
            List<EmployeeMeeting> employeeMeetingList = employeeMeetingRepository.findMeetingsForEmployee(employeeId,dateOfFetch);
            List<Integer> meetingIdList = new ArrayList<>();
            for(EmployeeMeeting employeeMeeting:employeeMeetingList){
                meetingIdList.add(employeeMeeting.getCompositeKey().getMeetId());
            }
            System.out.println(meetingIdList);
            List<Meeting> meetingsForEmployee = meetingRepository.findAllById(meetingIdList);
            List<EmployeeMeetingDetails> employeeMeetingDetails = MeetingsToEmployeeMeetingDetails.map(employeeMeetingList,meetingsForEmployee);
            return employeeMeetingDetails;
        }catch (Exception ex){
            logger.error("error trying to fetch meetings of employee",ex);
            throw new RuntimeException(ex);
        }
    }

}