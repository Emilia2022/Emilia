package emilia.control;

import emilia.service.CurrencyRatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/r2g")
public class AppController {

    private final CurrencyRatesService currencyRatesService;

    @Autowired
    public AppController(CurrencyRatesService currencyRatesService) {
        this.currencyRatesService = currencyRatesService;
    }

    @GetMapping("/tickers")
    public List<String> getAllTickers() {
        return currencyRatesService.getAllTickers();
    }

}
