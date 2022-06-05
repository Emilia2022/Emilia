package emilia.control;

import emilia.service.CurrencyRatesService;
import emilia.service.GifService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<String> getGif(@RequestParam String ticker) {
        String tag = determineTagForTicker(ticker);
        return gifService.getRandomGif(tag);
    }

    private String determineTagForTicker(String ticker){
        return ""; //todo
    }

}
