package integrationTests.config;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.example.CalendarThriftConfiguration.MeetingSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class ThriftServerLauncher {

    @Autowired
    private MeetingSvc.Iface meetingSvc;

    private TServer testServer;

    @PostConstruct
    public void init() throws TTransportException {

        TServerTransport serverTransport = new TServerSocket(9090);
        testServer = new TSimpleServer(new TServer.Args(serverTransport).processor(new MeetingSvc.Processor<>(meetingSvc)));
        System.out.print("Starting the server... ");
        testServer.serve();
    }

    @PreDestroy
    public void tearDown(){
        testServer.stop();
    }


}
