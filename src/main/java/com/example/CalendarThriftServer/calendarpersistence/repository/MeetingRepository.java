package com.example.CalendarThriftServer.calendarpersistence.repository;

import com.example.CalendarThriftServer.calendarpersistence.model.Meeting;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface MeetingRepository extends CrudRepository<Meeting,String> {

    @Modifying
    @Query(value = "UPDATE meeting m SET m.is_available = false WHERE m.owner_id=:employee_id AND m.date_of_meeting>:date",nativeQuery = true)
    public boolean cancelMeetingOfEmployee(@Param("employee_id") String employeeId, @Param("date")LocalDate date);

    @Query(value = "Select em.employee_id FROM meeting m JOIN emp_meetings em ON m.meet_id=em.meeting_id WHERE em.employee_id IN (:list_of_employee) AND m.date_of_meeting =:date AND em.status=’accepted’ AND (m.end_time>:starttime and m.start_time<:endtime)",nativeQuery = true)
    public List<String> checkEmployeeAvailability(@Param("list_of_employee")List<String> listOfEmployee,@Param("date")LocalDate date,@Param("starttime") LocalTime startTime,@Param("endtime")LocalTime endTime);

    @Query(value = "SELECT roomId FROM meeting m where m.date = :date AND m.isAvailable=true AND roomId IN (:list_of_rooms) AND (m.end_time>:starttime and m.start_time<:endtime))",nativeQuery = true)
    public List<Integer> findFreeMeetingRoom(@Param("list_of_rooms")List<Integer> listOfRooms,@Param("date")LocalDate date,@Param("starttime") LocalTime startTime,@Param("endtime")LocalTime endTime);

    @Query(value = "select count(1) from meeting m where m.room_id = :roomId AND m.isAvailable = true AND m.date =:date AND (m.end_time>:starttime and m.start_time<:endtime)",nativeQuery = true)
    public int meetingRoomAvailable(@Param("roomId")Integer roomId,@Param("date")LocalDate date,@Param("starttime") LocalTime startTime,@Param("endtime")LocalTime endTime);
}
