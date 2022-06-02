package emilia.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

@Configuration
public class DateTime {

    @Bean("date_formatter")
    public SimpleDateFormat dateFormatter() {
        return new SimpleDateFormat("yyyy-MM-dd");
    }

    @Bean("time_formatter")
    public SimpleDateFormat timeFormatter() {
        return new SimpleDateFormat("yyyy-MM-dd HH");
    }

}
