package com.example.CalendarThriftServer;

import integrationTests.BaseIntegrationIT;
import org.apache.thrift.TException;
import org.example.CalendarThriftConfiguration.MeetingSvc;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MeetingHandlerIT extends BaseIntegrationIT {

    @Autowired
    private MeetingSvc.Iface meetingSvc;

    @Test
    public void cancelMeetingOfRemovedEmployeeTest_employeeWithNoMeetings() throws TException {
        String employeeWithNoMeetings = "xyz-2";
        boolean meetingsRemoved = meetingSvc.cancelMeetingOfRemovedEmployee(employeeWithNoMeetings);
        Assertions.assertTrue(meetingsRemoved);
    }

}