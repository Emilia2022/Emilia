package emilia.di;

import emilia.gifs.SearchQueryProvider;
import emilia.currency.CurrencyCalculator;
import emilia.currency.services.CurrencyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

@Configuration
public class AppConfig {

    public static final String DATE_FORMATTER = "date_formatter";
    public static final String SEARCH_QUERY_PROVIDER = "search_query_provider";
    public static final String CURRENCY_CALCULATOR = "currency_calculator";

    @Bean(DATE_FORMATTER)
    public SimpleDateFormat dateFormatter() {
        return new SimpleDateFormat("yyyy-MM-dd");
    }

    @Bean(SEARCH_QUERY_PROVIDER)
    public SearchQueryProvider searchQueryProvider(
            CurrencyService currencyService,
            @Value("${giphy.broke}") String broke,
            @Value("${giphy.rich}") String rich,
            @Value("${giphy.uncertain}") String uncertain
    ) {
        return new SearchQueryProvider(currencyService, broke, rich, uncertain);
    }

    @Bean(CURRENCY_CALCULATOR)
    public CurrencyCalculator currencyCalculator(@Value("${oxr.base}") String baseTicker) {
        return new CurrencyCalculator(baseTicker);
    }

}
