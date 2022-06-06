package emilia.gifs;

import emilia.currency.RateChange;
import emilia.currency.services.CurrencyService;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;
import org.mockito.Mockito;

public class SearchQueryProviderTest implements WithAssertions {


    private final CurrencyService service = Mockito.mock(CurrencyService.class);
    private final SearchQueryProvider tested = new SearchQueryProvider(service, "broke", "rich", "uncertain");


    @Test
    public void whenRatesHaveGrownThenShouldReturnRich() {
        // given
        Mockito.when(service.evaluateAgainstBase("USD")).thenReturn(RateChange.GROWN);
        // when
        String result = tested.get("USD");
        // then
        assertThat(result).isEqualTo("rich");
    }

    @Test
    public void whenRatesHaveFallenThenShouldReturnBroke() {
        // given
        Mockito.when(service.evaluateAgainstBase("USD")).thenReturn(RateChange.FALLEN);
        // when
        String result = tested.get("USD");
        // then
        assertThat(result).isEqualTo("broke");
    }

    @Test
    public void whenRatesHaveNotChangedThenShouldReturnUncertain() {
        // given
        Mockito.when(service.evaluateAgainstBase("USD")).thenReturn(RateChange.UNCHANGED);
        // when
        String result = tested.get("USD");
        // then
        assertThat(result).isEqualTo("uncertain");

    }


}