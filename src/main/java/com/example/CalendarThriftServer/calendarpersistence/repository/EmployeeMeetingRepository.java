package com.example.CalendarThriftServer.calendarpersistence.repository;

import com.example.CalendarThriftServer.calendarpersistence.model.CompositeKey;
import com.example.CalendarThriftServer.calendarpersistence.model.EmployeeMeeting;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface EmployeeMeetingRepository extends CrudRepository<EmployeeMeeting, CompositeKey> {

    @Query(value = "UPDATE EmployeeMeeting em SET em.status = 'cancelled' WHERE meeting_id IN (SELECT m.meet_id FROM meeting WHERE m.owner_id = :employee_id AND AND m.date_of_meeting>:date)",nativeQuery = true)
    public boolean updateStatusForCancelledMeeting(@Param("employee_id") String employeeId,  @Param("date") LocalDate date);

    @Query(value = "UPDATE  EmployeeMeeting em SET em.status = 'removed' WHERE em.employeeId = :employee_id AND em.date>:date",nativeQuery = true)
    public boolean updateStatusForRemovedEmployee(@Param("employee_id")String employeeId, @Param("date") LocalDate date);
}
