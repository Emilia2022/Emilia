package emilia.currency.services;

import emilia.currency.CurrencyCalculator;
import emilia.currency.CurrencyRates;
import emilia.currency.OpenXchangeRatesClient;
import emilia.currency.RateChange;
import org.assertj.core.api.WithAssertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class CurrencyServiceImplTest implements WithAssertions {

    private final String APP_ID = "1234";
    private final OpenXchangeRatesClient client = Mockito.mock(OpenXchangeRatesClient.class);
    private final SimpleDateFormat dateFormat = Mockito.mock(SimpleDateFormat.class);
    private final CurrencyCalculator currencyCalculator = Mockito.mock(CurrencyCalculator.class);

    private final CurrencyRates currentRates = new CurrencyRates();
    private final CurrencyRates oldRates = new CurrencyRates();

    private final CurrencyServiceImpl tested = new CurrencyServiceImpl(client, currencyCalculator, dateFormat, APP_ID);

    @Before
    public void setUp() {
        // SimpleDateFormat uses this one under the hood
        Mockito.when(dateFormat.format(any(Date.class),
                        any(StringBuffer.class),
                        any(FieldPosition.class)))
                .thenReturn(new StringBuffer("01.01.2022"));
        Mockito.when(client.getCurrentRates(any())).thenReturn(currentRates);
        Mockito.when(client.getRatesHistory(any(), any())).thenReturn(oldRates);
    }

    @Test
    public void whenNoRatesAreAvailableThenRefresh() {
        // when
        tested.checkRates();
        // then
        Mockito.verify(client).getCurrentRates(any());
        Mockito.verify(client).getRatesHistory(any(), any());
    }

    @Test
    public void whenRatesAreExpiredThenRefresh() {

        // given
        long twoHoursAgo = System.currentTimeMillis() - 2 * 3600;
        currentRates.setTimestamp(twoHoursAgo);
        oldRates.setTimestamp(twoHoursAgo);

        // when
        tested.checkRates();

        // then
        Mockito.verify(client).getCurrentRates(any());
        Mockito.verify(client).getRatesHistory(any(), any());
    }

    @Test
    public void whenRatesAreUpToDateThenNotRefresh() {

        // given
        long now = System.currentTimeMillis();
        currentRates.setTimestamp(now);
        oldRates.setTimestamp(now);

        // when
        tested.checkRates(); // <- otherwise "refresh needed" will always eval as true
        tested.checkRates();

        // then
        Mockito.verify(client, times(1)).getCurrentRates(any());
        Mockito.verify(client, times(1)).getRatesHistory(any(), any());
    }

    @Test
    public void shouldReturnTickersForAllCurrencies() {

        // given
        Map<String, Double> rates = new HashMap<>();
        rates.put("USD", 0d);
        rates.put("EUR", 0d);
        currentRates.setRates(rates);

        List<String> expected = Arrays.asList("EUR", "USD");

        // when
        List<String> result = tested.getAllTickers();

        // then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void whenAskedToEvaluateAgainstBaseThenDelegateToCalculator() {

        // given
        when(currencyCalculator.determineChange(eq("USD"), any(), any())).thenReturn(RateChange.UNCHANGED);

        // when
        RateChange result = tested.evaluateAgainstBase("USD");

        // then
        assertThat(result).isEqualTo(RateChange.UNCHANGED);

    }


}