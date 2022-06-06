package emilia.currency;

import java.util.HashMap;
import java.util.Map;

public class CurrencyCalculator {

    private final String baseTicker;

    public CurrencyCalculator(String baseTicker) {
        this.baseTicker = baseTicker;
    }

    CurrencyRates recalculateAgainstCustomBase(CurrencyRates original) {
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

    public RateChange determineChange(
            String ticker,
            CurrencyRates currentRates,
            CurrencyRates historicalRates) {
        currentRates = recalculateAgainstCustomBase(currentRates);
        historicalRates = recalculateAgainstCustomBase(historicalRates);
        Double current = currentRates.getRates().get(ticker);
        Double old = historicalRates.getRates().get(ticker);
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


}
