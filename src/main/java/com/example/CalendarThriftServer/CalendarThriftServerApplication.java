package com.example.CalendarThriftServer;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.example.CalendarThriftConfiguration.MeetingSvc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CalendarThriftServerApplication {

	public static void startServer() throws TTransportException {

		try {
			TServerTransport serverTransport = new TServerSocket(9090);
			TSimpleServer server = new TSimpleServer(new TServer.Args(serverTransport).processor(new MeetingSvc.Processor<>(new MeetingHandler())));
			System.out.print("Starting the server... ");
			server.serve();
		}catch (TTransportException ex){
			throw new RuntimeException(ex.getMessage());
		}

	}
	public static void main(String[] args) throws TTransportException {

		SpringApplication.run(CalendarThriftServerApplication.class, args);
		startServer();
	}



}
