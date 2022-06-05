package emilia.control;

import emilia.currency.CurrencyRatesService;
import emilia.currency.RateChange;
import emilia.gifs.GifService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/r2g")
public class AppController {

    private final CurrencyRatesService currencyRatesService;
    private final GifService gifService;

    @Value("${giphy.broke}")
    private String broke;
    @Value("${giphy.rich}")
    private String rich;
    @Value("${giphy.uncertain}")
    private String uncertain;

    @Autowired
    public AppController(
            CurrencyRatesService currencyRatesService,
            GifService gifService) {
        this.currencyRatesService = currencyRatesService;
        this.gifService = gifService;
    }

    @GetMapping("/tickers")
    public List<String> getAllTickers() {
        return currencyRatesService.getAllTickers();
    }

    @GetMapping("/gif")
    public ResponseEntity<String> getGif(
            @RequestParam String ticker,
            @RequestParam int offset) {
        String tag = getTagForGifSearch(ticker);
        return gifService.search(tag, offset);
    }

    private String getTagForGifSearch(String ticker) {
        RateChange rateChange = currencyRatesService.compareBaseAgainst(ticker);
        switch (rateChange) {
            case GROWN:
                return rich;
            case FALLEN:
                return broke;
            case UNCHANGED:
                return uncertain;
        }
        return null;
    }

}
