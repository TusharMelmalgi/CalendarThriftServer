package calendarpersistence.repository;

import calendarpersistence.model.Meeting;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface MeetingRepository extends CrudRepository<Meeting,String> {

    @Query(value = "UPDATE meeting m SET m.is_available = false WHERE m.owner_id=:employee_id AND m.date_of_meeting>:date",nativeQuery = true)
    public boolean cancelMeetingOfEmployee(@Param("employee_id") String employeeId, @Param("date")LocalDate date);

    @Query(value = "Select em.employee_id FROM meeting m JOIN emp_meetings em ON m.meet_id=em.meeting_id WHERE em.employee_id IN (:list_of_employee) AND m.date_of_meeting =:date AND em.status=’accepted’ AND (m.end_time>:starttime and m.start_time<:endtime)",nativeQuery = true)
    public List<String> checkEmployeeAvailability(@Param("list_of_employee")List<String> listOfEmployee,@Param("date")LocalDate date,@Param("starttime") LocalTime startTime,@Param("endtime")LocalTime endTime);

}
