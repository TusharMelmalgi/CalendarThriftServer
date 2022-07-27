package com.example.CalendarThriftServer;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TServlet;
import org.example.CalendarThriftConfiguration.MeetingSvc;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("test")
public class ServerConfig
{

        @Bean
        public TProtocolFactory tProtocolFactory() {
            return new TBinaryProtocol.Factory();
        }

        @Bean
        public ServletRegistrationBean isAliveServlet(TProtocolFactory protocolFactory, MeetingHandler handler) {
            TServlet tServlet = new TServlet(new MeetingSvc.Processor<>(handler), protocolFactory);

            return new ServletRegistrationBean(tServlet, "/isAlive");
        }
    @Bean
    public ServletRegistrationBean cancelMeetingOfRemovedEmployeeServlet(TProtocolFactory protocolFactory, MeetingHandler handler) {
        TServlet tServlet = new TServlet(new MeetingSvc.Processor<>(handler), protocolFactory);

        return new ServletRegistrationBean(tServlet, "/cancelMeetingOfRemovedEmployee");
    }

    @Bean
    public ServletRegistrationBean updateStatusOfRemovedEmployeeServlet(TProtocolFactory protocolFactory, MeetingHandler handler) {
        TServlet tServlet = new TServlet(new MeetingSvc.Processor<>(handler), protocolFactory);

        return new ServletRegistrationBean(tServlet, "/updateStatusOfRemovedEmployee");
    }
    @Bean
    public ServletRegistrationBean checkEmployeeAvailabilityServlet(TProtocolFactory protocolFactory, MeetingHandler handler) {
        TServlet tServlet = new TServlet(new MeetingSvc.Processor<>(handler), protocolFactory);

        return new ServletRegistrationBean(tServlet, "/checkEmployeeAvailability");
    }
    @Bean
    public ServletRegistrationBean addMeetingDetailsServlet(TProtocolFactory protocolFactory, MeetingHandler handler) {
        TServlet tServlet = new TServlet(new MeetingSvc.Processor<>(handler), protocolFactory);

        return new ServletRegistrationBean(tServlet, "/addMeetingDetails");
    }

    @Bean
    public ServletRegistrationBean addEmployeeMeetingStatusServlet(TProtocolFactory protocolFactory, MeetingHandler handler) {
        TServlet tServlet = new TServlet(new MeetingSvc.Processor<>(handler), protocolFactory);

        return new ServletRegistrationBean(tServlet, "/addEmployeeMeetingStatus");
    }
    @Bean
    public ServletRegistrationBean findFreeMeetingRoomServlet(TProtocolFactory protocolFactory, MeetingHandler handler) {
        TServlet tServlet = new TServlet(new MeetingSvc.Processor<>(handler), protocolFactory);

        return new ServletRegistrationBean(tServlet, "/findFreeMeetingRoom");
    }

    @Bean
    public ServletRegistrationBean meetingRoomAvailablemServlet(TProtocolFactory protocolFactory, MeetingHandler handler) {
        TServlet tServlet = new TServlet(new MeetingSvc.Processor<>(handler), protocolFactory);

        return new ServletRegistrationBean(tServlet, "/meetingRoomAvailable");
    }

}

