package emilia.currency;

import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;


public class CurrencyCalculatorTest implements WithAssertions {


    private final String CUSTOM_BASE = "RUB";

    private final CurrencyCalculator tested = new CurrencyCalculator(CUSTOM_BASE);

    @Test
    public void shouldRecalculateRatesAgainstCustomBase() {
        // given
        final double USDvsEUR = 0.8;
        final double USDvsRUB = 75.0;

        CurrencyRates currencyRates = setupCurrencyRates(USDvsEUR, USDvsRUB);

        // when
        CurrencyRates result = tested.recalculateAgainstCustomBase(currencyRates);

        // then
        Map<String, Double> map = result.getRates();
        assertThat(result.getBase()).isEqualTo("RUB");
        assertThat(map.get("RUB")).isEqualTo(1.0);
        assertThat(map.get("USD")).isEqualTo(75.0);
        assertThat(map.get("EUR")).isEqualTo(93.75);
    }

    @Test
    public void givenThatCurrencyGainedAgainstBaseShouldReturnGROWN() {
        // given
        final double USDvsEUR = 0.9;
        final double USDvsRUB = 90.0;
        final double USDvsEUR_OLD = 0.85;
        final double USDvsRUB_OLD = 80.0;

        CurrencyRates current = setupCurrencyRates(USDvsEUR, USDvsRUB);
        CurrencyRates old = setupCurrencyRates(USDvsEUR_OLD, USDvsRUB_OLD);

        // when
        RateChange result = tested.determineChange("USD", current, old);

        // then
        assertThat(result).isEqualTo(RateChange.GROWN);
    }

    @Test
    public void givenThatCurrencyFellAgainstBaseShouldReturnFALLLEN() {
        // given
        final double USDvsEUR = 0.9;
        final double USDvsRUB = 75.0;
        final double USDvsEUR_OLD = 0.85;
        final double USDvsRUB_OLD = 80.0;

        CurrencyRates current = setupCurrencyRates(USDvsEUR, USDvsRUB);
        CurrencyRates old = setupCurrencyRates(USDvsEUR_OLD, USDvsRUB_OLD);

        // when
        RateChange result = tested.determineChange("USD", current, old);

        // then
        assertThat(result).isEqualTo(RateChange.FALLEN);
    }

    @Test
    public void givenThatCurrencyRemainedStableAgainstBaseShouldReturnUNCHANGED() {
        // given
        final double USDvsEUR = 0.9;
        final double USDvsRUB = 90.0;
        final double USDvsEUR_OLD = 0.9;
        final double USDvsRUB_OLD = 90.0;

        CurrencyRates current = setupCurrencyRates(USDvsEUR, USDvsRUB);
        CurrencyRates old = setupCurrencyRates(USDvsEUR_OLD, USDvsRUB_OLD);

        // when
        RateChange result = tested.determineChange("USD", current, old);

        // then
        assertThat(result).isEqualTo(RateChange.UNCHANGED);
    }

    private CurrencyRates setupCurrencyRates(
            double usdVsEUR,
            double usdVsRUB) {
        CurrencyRates currencyRates = new CurrencyRates();
        currencyRates.setBase("USD");
        Map<String, Double> rates = new HashMap<>();
        rates.put("USD", 1.0);
        rates.put("EUR", usdVsEUR);
        rates.put("RUB", usdVsRUB);
        currencyRates.setRates(rates);
        return currencyRates;
    }

}