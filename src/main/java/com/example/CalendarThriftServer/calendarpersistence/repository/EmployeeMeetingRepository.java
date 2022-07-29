package com.example.CalendarThriftServer.calendarpersistence.repository;

import com.example.CalendarThriftServer.calendarpersistence.model.CompositeKey;
import com.example.CalendarThriftServer.calendarpersistence.model.EmployeeMeeting;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeMeetingRepository extends CrudRepository<EmployeeMeeting, CompositeKey> {

    @Modifying
    @Query(value = "UPDATE employee_meeting em SET em.status = 'cancelled' WHERE em.meeting_id IN (SELECT m.meet_id FROM meeting m WHERE m.owner_id = :employee_id AND m.date_of_meeting> CURDATE())",nativeQuery = true)
    public Integer updateStatusForCancelledMeeting(@Param("employee_id") String employeeId);

    @Modifying
    @Query(value = "UPDATE  employee_meeting em SET em.status = 'removed' WHERE em.employee_id = :employee_id AND em.date> CURDATE()",nativeQuery = true)
    public Integer updateStatusForRemovedEmployee(@Param("employee_id")String employeeId);
}
