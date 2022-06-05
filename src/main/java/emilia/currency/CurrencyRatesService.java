package emilia.currency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CurrencyRatesService {

    private final OpenXchangeRatesClient client;
    private final SimpleDateFormat dateFormatter;
    @Value("${oxr.app.id}")
    private String appId;
    @Value("${oxr.base}")
    private String baseTicker;
    private long ratesRefreshTS = 0L;

    private CurrencyRates currentRates = null;
    private CurrencyRates yesterdaysRates = null;

    @Autowired
    public CurrencyRatesService(
            OpenXchangeRatesClient client,
            @Qualifier("date_formatter") SimpleDateFormat dateFormat) {
        this.client = client;
        this.dateFormatter = dateFormat;
    }

    public void checkRates() {
        if (!isRatesRefreshNeeded()) return;
        refreshRates();
        if (yesterdaysRates != null)
            yesterdaysRates = recalculateRatesWithNewBase(yesterdaysRates);
        if (currentRates != null)
            currentRates = recalculateRatesWithNewBase(currentRates);
    }

    public List<String> getAllTickers() {
        List<String> tickers = null;
        Map<String, Double> rates = currentRates.getRates();
        if (rates != null) {
            tickers = new ArrayList<>(rates.keySet());
        }
        return tickers;
    }

    public RateChange compareBaseAgainst(String ticker) {
        Double current = currentRates.getRates().get(ticker);
        Double old = yesterdaysRates.getRates().get(ticker);
        if (current == null || old == null) return RateChange.UNCHANGED;
        int result = Double.compare(current, old);
        switch (result) {
            case -1:
                return RateChange.FALLEN;
            case 0:
                return RateChange.UNCHANGED;
            case 1:
                return RateChange.GROWN;
        }
        return RateChange.UNCHANGED;
    }

    private CurrencyRates recalculateRatesWithNewBase(CurrencyRates original) {
        Map<String, Double> newRates = new HashMap<>();
        Map<String, Double> rates = original.getRates();
        Double ourBaseRate = rates.get(baseTicker);
        original.getRates().forEach((ticker, value) -> newRates.put(ticker, ourBaseRate / value));
        CurrencyRates newCurrencyRates = new CurrencyRates();
        newCurrencyRates.setBase(baseTicker);
        newCurrencyRates.setRates(newRates);
        newCurrencyRates.setTimestamp(original.getTimestamp());
        return newCurrencyRates;
    }

    private void refreshRates() {
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
