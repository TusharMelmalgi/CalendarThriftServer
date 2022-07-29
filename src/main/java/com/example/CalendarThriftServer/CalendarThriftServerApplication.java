package com.example.CalendarThriftServer;

import com.example.CalendarThriftServer.calendarpersistence.repository.MeetingRepository;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.example.CalendarThriftConfiguration.MeetingSvc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.example.CalendarThriftServer.calendarpersistence.repository")
@EntityScan("com.example.CalendarThriftServer.calendarpersistence.model")
public class CalendarThriftServerApplication {
	public static void startServer(MeetingHandler meetingHandler) throws TTransportException {

		try {
			TServerTransport serverTransport = new TServerSocket(9090);
			TSimpleServer server = new TSimpleServer(new TServer.Args(serverTransport).processor(new MeetingSvc.Processor<>(meetingHandler)));
			System.out.print("Starting the server... ");
			server.serve();
		}catch (TTransportException ex){
			throw new RuntimeException(ex.getMessage());
		}

	}
	public static void main(String[] args) throws TTransportException {

		ConfigurableApplicationContext applicationContext = SpringApplication.run(CalendarThriftServerApplication.class, args);
		startServer(applicationContext.getBean(MeetingHandler.class));
	}



}
