package ethereum.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class JdbcConfig {

    @Autowired
    Environment env;

    /*     public static final String MYSQL_URL = "MYSQL_URL";
    public static final String MYSQL_DATABASE = "MYSQLDATABASE";
    public static final String MYSQL_USER = "MYSQLUSER";
    public static final String MYSQL_PASSOWORD = "MYSQLPASSWORD";
    public static final String MYSQL_PORT = "MYSQLPORT";
    public static final String MYSQL_HOST = "MYSQLHOST"; */
    @Bean
    public DataSource dataSource() {

        String url;

        // if not on local dev
        if (!(env.getProperty("MYSQLHOST").equals("localhost"))) {

            StringBuilder urlBldr = new StringBuilder()
                    .append("jdbc:")
                    .append("mysql://")
                    .append(env.getProperty("MYSQLHOST"))
                    .append(":")
                    .append(env.getProperty("MYSQLPORT"))
                    .append("/")
                    .append(env.getProperty("MYSQLDATABASE")); // get shared env var
            url = urlBldr.toString();
        } else {
            // for local development
            url = env.getProperty("MYSQL_URL");
        }

        return DataSourceBuilder.create()
                .url(url)
                .password(env.getProperty("MYSQLPASSWORD"))
                .username(env.getProperty("MYSQLUSER"))
                .build();

    }
}
