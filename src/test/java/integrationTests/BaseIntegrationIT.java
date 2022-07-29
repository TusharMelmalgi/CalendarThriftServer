package integrationTests;

import com.example.CalendarThriftServer.CalendarThriftServerApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import integrationTests.config.MysqlConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;



@SpringBootTest(classes = { MysqlConfiguration.class, CalendarThriftServerApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
public class BaseIntegrationIT {



}

