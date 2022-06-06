package emilia.currency.services;

import emilia.currency.CurrencyCalculator;
import emilia.currency.CurrencyRates;
import emilia.currency.OpenXchangeRatesClient;
import emilia.currency.RateChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

import static emilia.di.AppConfig.CURRENCY_CALCULATOR;
import static emilia.di.AppConfig.DATE_FORMATTER;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final OpenXchangeRatesClient client;
    private final SimpleDateFormat dateFormatter;
    private final CurrencyCalculator calculator;

    @Value("${oxr.app.id}")
    private String appId;

    private long ratesRefreshTS = 0L;

    private CurrencyRates currentRates = null;
    private CurrencyRates yesterdaysRates = null;

    @Autowired
    public CurrencyServiceImpl(
            OpenXchangeRatesClient client,
            @Qualifier(CURRENCY_CALCULATOR) CurrencyCalculator calculator,
            @Qualifier(DATE_FORMATTER) SimpleDateFormat dateFormat) {
        this.client = client;
        this.calculator = calculator;
        this.dateFormatter = dateFormat;
    }

    @Override
    public void checkRates() {
        refreshRatesIfNeeded();
    }

    @Override
    public List<String> getAllTickers() {
        refreshRatesIfNeeded();
        List<String> tickers = null;
        Map<String, Double> rates = currentRates.getRates();
        if (rates != null) {
            tickers = new ArrayList<>(rates.keySet());
        }
        return tickers;
    }

    @Override
    public RateChange evaluateAgainstBase(String ticker) {
        refreshRatesIfNeeded();
        return calculator.determineChange(ticker, currentRates, yesterdaysRates);
    }

    private void refreshRatesIfNeeded() {
        if (!isRatesRefreshNeeded()) return;
        currentRates = client.getCurrentRates(appId);
        String dateString = getDateStringForRatesRequest();
        yesterdaysRates = client.getRatesHistory(dateString, appId);
        ratesRefreshTS = System.currentTimeMillis();
    }

    private String getDateStringForRatesRequest() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return dateFormatter.format(calendar.getTime());
    }

    private boolean isRatesRefreshNeeded() {
        if (currentRates == null || yesterdaysRates == null) return true;
        long oneHourInMillis = 3600 * 1000;
        return System.currentTimeMillis() - ratesRefreshTS >= oneHourInMillis;
    }
}
