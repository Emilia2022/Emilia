package emilia.control;

import emilia.currency.CurrencyRatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Init {

    private final CurrencyRatesService service;

    @Autowired
    public Init(CurrencyRatesService service) {
        this.service = service;
    }

    @PostConstruct
    public void onStart() {
        service.checkRates();
    }

}
