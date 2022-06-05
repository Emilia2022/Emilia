package emilia.control;

import emilia.currency.services.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class AppInit {

    private final CurrencyService service;

    @Autowired
    public AppInit(CurrencyService service) {
        this.service = service;
    }

    @PostConstruct
    public void onStart() {
        service.checkRates();
    }

}
