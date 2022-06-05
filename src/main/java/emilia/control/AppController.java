package emilia.control;

import emilia.currency.services.CurrencyService;
import emilia.currency.services.CurrencyServiceImpl;
import emilia.gifs.GifService;
import emilia.gifs.SearchQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static emilia.di.AppConfig.SEARCH_QUERY_PROVIDER;

@RestController
@RequestMapping("/r2g")
public class AppController {

    private final CurrencyService currencyService;
    private final GifService gifService;
    private final SearchQueryProvider searchQueryProvider;

    @Autowired
    public AppController(
            CurrencyServiceImpl currencyRatesService,
            @Qualifier(SEARCH_QUERY_PROVIDER) SearchQueryProvider searchQueryProvider,
            GifService gifService) {
        this.currencyService = currencyRatesService;
        this.gifService = gifService;
        this.searchQueryProvider = searchQueryProvider;
    }

    @GetMapping("/tickers")
    public List<String> getAllTickers() {
        return currencyService.getAllTickers();
    }

    @GetMapping("/gif")
    public ResponseEntity<String> getGif(
            @RequestParam String ticker,
            @RequestParam int offset) {
        String query = searchQueryProvider.get(ticker);
        return gifService.search(query, offset);
    }

}
