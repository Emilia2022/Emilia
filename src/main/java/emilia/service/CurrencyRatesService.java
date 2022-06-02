package emilia.service;

import emilia.api.OpenXchangeRatesClient;
import emilia.model.CurrencyRates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Service
public class CurrencyRatesService {

    private final OpenXchangeRatesClient xchangeRatesClient;
    private final SimpleDateFormat dateFormatter;
    private final SimpleDateFormat timeFormatter;
    @Value("${oxr.app.id}")
    private String appId;
    @Value("${oxr.base}")
    private String baseTicker;
    private long ratesRefreshTS = 0L;

    private CurrencyRates currentRates = null;
    private CurrencyRates yesterdaysRates = null;

    @Autowired
    public CurrencyRatesService(
            OpenXchangeRatesClient xchangeRatesClient,
            @Qualifier("date_formatter") SimpleDateFormat dateFormat,
            @Qualifier("time_formatter") SimpleDateFormat timeFormat) {
        this.xchangeRatesClient = xchangeRatesClient;
        this.dateFormatter = dateFormat;
        this.timeFormatter = timeFormat;
    }

    public void checkRates() {
        if(isRatesRefreshNeeded()){
            refreshRates();
        }
        processRates();
    }

    private void refreshRates(){
        currentRates = xchangeRatesClient.getCurrentRates(appId);
        String dateString = getDateStringForRatesRequest();
        yesterdaysRates = xchangeRatesClient.getRatesHistory(dateString, appId);
        ratesRefreshTS = System.currentTimeMillis();
    }

    private void processRates(){

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
