package integrationTests.config;


import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;


@Configuration
public class MysqlConfiguration {

    @Bean
    @Profile("test")
    @DependsOn("embeddedMysqlLauncher")
    public HikariDataSource dataSource(){

        return DataSourceBuilder.create()
                .url("jdbc:mysql://127.0.0.1:3060/test_database")
                .username("test")
                .password("test")
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .type(HikariDataSource.class).build();
    }

    @Bean
    public EmbeddedMysqlLauncher embeddedMysqlLauncher() {
        return new EmbeddedMysqlLauncher();
    }

}

