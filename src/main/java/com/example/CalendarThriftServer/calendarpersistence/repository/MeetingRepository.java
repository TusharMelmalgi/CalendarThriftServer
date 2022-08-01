package com.example.CalendarThriftServer.calendarpersistence.repository;

import com.example.CalendarThriftServer.calendarpersistence.model.EmployeeMeeting;
import com.example.CalendarThriftServer.calendarpersistence.model.Meeting;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface MeetingRepository extends CrudRepository<Meeting,Integer> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE meeting m SET m.is_available = 0 WHERE m.owner_id = :employee_id AND m.date_of_meeting> CURDATE()" , nativeQuery = true)
    public Integer cancelMeetingOfEmployee(@Param("employee_id") String employeeId);

    @Query(value = "SELECT em.employee_id FROM meeting m JOIN employee_meeting em ON m.meet_id = em.meeting_id WHERE em.employee_id IN (:list_of_employee) AND m.date_of_meeting =:date AND em.status='accepted' AND (m.end_time>:starttime and m.start_time<:endtime)",nativeQuery = true)
    public List<String> checkEmployeeAvailability(@Param("list_of_employee")List<String> listOfEmployee,@Param("date")LocalDate date,@Param("starttime") LocalTime startTime,@Param("endtime")LocalTime endTime);

    @Query(value = "SELECT m.room_id FROM meeting m where m.date_of_meeting = :date AND m.is_available=true AND m.room_id IN (:list_of_rooms) AND (m.end_time>:starttime and m.start_time<:endtime)",nativeQuery = true)
    public List<Integer> findFreeMeetingRoom(@Param("list_of_rooms")List<Integer> listOfRooms,@Param("date")LocalDate date,@Param("starttime") LocalTime startTime,@Param("endtime")LocalTime endTime);

    @Query(value = "select count(1) from meeting m where m.room_id = :roomId AND m.is_available = true AND m.date_of_meeting =:date AND (m.end_time>:starttime and m.start_time<:endtime)",nativeQuery = true)
    public int meetingRoomAvailable(@Param("roomId")Integer roomId,@Param("date")LocalDate date,@Param("starttime") LocalTime startTime,@Param("endtime")LocalTime endTime);

    @Query(value = "select * from meeting m where m.meet_id IN (:listOfMeetingId)",nativeQuery = true)
    List<Meeting> findAllById(@Param("listOfMeetingId")List<Integer> listOfMeetingId);

}
