package emilia.gifs;

import emilia.currency.RateChange;
import emilia.currency.services.CurrencyService;
import org.springframework.beans.factory.annotation.Value;

public class SearchQueryProvider {

    private final CurrencyService currencyService;

    @Value("${giphy.broke}")
    private String broke;
    @Value("${giphy.rich}")
    private String rich;
    @Value("${giphy.uncertain}")
    private String uncertain;

    public SearchQueryProvider(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    public String get(String ticker) {
        RateChange rateChange = currencyService.evaluateAgainstBase(ticker);
        switch (rateChange) {
            case GROWN:
                return rich;
            case FALLEN:
                return broke;
            case UNCHANGED:
                return uncertain;
        }
        return null;
    }

}
