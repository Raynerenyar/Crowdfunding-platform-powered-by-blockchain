package ethereum.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class JdbcConfig {

    @Autowired
    Environment env;

    @Value("${spring.datasource.url}")
    private String postgresqlUrl;

    @Bean
    public DataSource dataSource() {

        String url;

        // if not on local dev
        // if (!(env.getProperty("MYSQLHOST").equals("localhost"))) {

        // StringBuilder urlBldr = new StringBuilder()
        // .append("jdbc:")
        // .append("mysql://")
        // .append(env.getProperty("MYSQLHOST"))
        // .append(":")
        // .append(env.getProperty("MYSQLPORT"))
        // .append("/")
        // .append(env.getProperty("MYSQLDATABASE")); // get shared env var
        // url = urlBldr.toString();
        // } else {
        // // for local development
        url = env.getProperty("MYSQL_URL");
        // }

        return DataSourceBuilder.create()
                .url(url)
                // .password(env.getProperty("MYSQLPASSWORD"))
                // .username(env.getProperty("MYSQLUSER"))
                .build();

    }
}
