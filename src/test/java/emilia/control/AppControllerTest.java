package emilia.control;

import com.fasterxml.jackson.databind.ObjectMapper;
import emilia.currency.services.CurrencyServiceImpl;
import emilia.gifs.GifServiceImpl;
import emilia.gifs.SearchQueryProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@WebMvcTest(AppController.class)
@ComponentScan("emilia")
public class AppControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyServiceImpl currencyService;

    @MockBean
    private GifServiceImpl gifService;

    @MockBean
    private SearchQueryProvider searchQueryProvider;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void whenCurrencyTickersAreAvailableThenReturnTickers() throws Exception {
        // given
        List<String> tickers = new ArrayList<>();
        tickers.add("USD");
        Mockito.when(currencyService.getAllTickers()).thenReturn(tickers);
        // when
        mockMvc.perform(get("/r2g/tickers")
                        .content(mapper.writeValueAsString(tickers))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                // then
                .andExpect(jsonPath("$[0]")
                        .value("USD"));
    }

    @Test
    public void whenNoTickersAreAvailableThenReturnNoData() throws Exception {
        // given
        Mockito.when(currencyService.getAllTickers()).thenReturn(null);
        // when
        mockMvc.perform(get("/r2g/tickers")
                        .content(mapper.writeValueAsString(null))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                // then
                .andExpect(jsonPath("$[0]")
                        .doesNotExist());
    }

    @Test
    public void whenGifRequestSucceededThenReturnDataANdQuery() throws Exception {
        // given
        String giphyData = "{\"foo\" : \"bar\"}";
        String query = "query";
        ResponseEntity<String> response = new ResponseEntity<>(giphyData, HttpStatus.OK);
        Mockito.when(gifService.search(eq(query), eq(0))).thenReturn(response);
        Mockito.when(searchQueryProvider.get("USD")).thenReturn(query);
        // when
        mockMvc.perform(get("/r2g/gif?ticker=USD&offset=0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                // then
                .andExpect(jsonPath("$.query")
                        .value(query))
                .andExpect(jsonPath("$.foo")
                        .value("bar"));
    }

    @Test
    public void whenGifRequestFailedThenReturnNoData() throws Exception {
        ResponseEntity<String> response = new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        Mockito.when(gifService.search(anyString(), anyInt())).thenReturn(response);
        Mockito.when(searchQueryProvider.get("USD")).thenReturn("query");

        mockMvc.perform(get("/r2g/gif?ticker=USD&offset=0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(403))
                .andExpect(content().bytes(new byte[0]));
    }
}