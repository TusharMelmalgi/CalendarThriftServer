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
        public ServletRegistrationBean thriftMeetingServlet(TProtocolFactory protocolFactory, MeetingHandler handler) {
            TServlet tServlet = new TServlet(new MeetingSvc.Processor<>(handler), protocolFactory);

            return new ServletRegistrationBean(tServlet, "/isAlive");
        }
}

