package emilia.gifs;

import emilia.currency.RateChange;
import emilia.currency.services.CurrencyService;

public class SearchQueryProvider {

    private final CurrencyService currencyService;
    private final String broke;
    private final String rich;
    private final String uncertain;

    public SearchQueryProvider(
            CurrencyService currencyService,
            String broke,
            String rich,
            String uncertain) {
        this.currencyService = currencyService;
        this.broke = broke;
        this.rich = rich;
        this.uncertain = uncertain;
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
