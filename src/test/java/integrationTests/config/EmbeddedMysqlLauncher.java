package integrationTests.config;


import static com.wix.mysql.config.Charset.UTF8;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static com.wix.mysql.distribution.Version.v5_7_latest;

import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.Sources;
import com.wix.mysql.config.MysqldConfig;
import com.wix.mysql.config.SchemaConfig;
import java.io.File;
import java.net.URISyntaxException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class EmbeddedMysqlLauncher {

    private EmbeddedMysql mysqld;

    private final String SCHEMA_FILE="schema.sql";
    private final String DATA_FILE = "data.sql";

    @PostConstruct
    public void startEmbeddedDatabase() throws URISyntaxException {
        MysqldConfig config = aMysqldConfig(v5_7_latest)
                .withCharset(UTF8)
                .withPort(3060)
                .withUser("test", "test")
                .build();
        ClassLoader classLoader = MysqlConfiguration.class.getClassLoader();
        File schemaFile = new File(classLoader.getResource(SCHEMA_FILE).toURI());
        File dataFile = new File(classLoader.getResource(DATA_FILE).toURI());
        SchemaConfig schemaConfig = SchemaConfig
                .aSchemaConfig("test_database")
                .withScripts(Sources.fromFile(schemaFile), Sources.fromFile(dataFile))
                .build();
        mysqld = EmbeddedMysql.anEmbeddedMysql(config).addSchema(schemaConfig).start();
    }

    @PreDestroy
    public void tearDownEmbeddedDatabase(){
        mysqld.stop();
    }

}

