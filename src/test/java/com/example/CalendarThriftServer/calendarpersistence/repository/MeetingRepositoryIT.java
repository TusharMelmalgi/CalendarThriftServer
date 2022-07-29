package com.example.CalendarThriftServer.calendarpersistence.repository;

import integrationTests.BaseIntegrationIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;


class MeetingRepositoryIT extends BaseIntegrationIT {

    @Autowired
    private MeetingRepository meetingRepository;

    @Test
    public void cancelMeetingOfEmployeeTest_employeeWithNoMeetings() {
        String employeeWithNoMeetings = "xyz-2";
        int meetingsCancelled = meetingRepository.cancelMeetingOfEmployee(employeeWithNoMeetings);
        assertEquals(0, meetingsCancelled);
    }

}