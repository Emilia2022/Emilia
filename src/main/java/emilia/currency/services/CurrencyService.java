package emilia.currency.services;

import emilia.currency.RateChange;

import java.util.List;

public interface CurrencyService {

    void checkRates();

    List<String> getAllTickers();

    RateChange evaluateAgainstBase(String ticker);

}
