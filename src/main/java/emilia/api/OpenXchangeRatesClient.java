package emilia.api;

import emilia.model.CurrencyRates;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "OXRClient", url = "${oxr.url.base}")
public interface OpenXchangeRatesClient {

    @GetMapping("/latest.json")
    CurrencyRates getCurrentRates(@RequestParam("app_id") String appId);

    @GetMapping("/historical/{date}.json")
    CurrencyRates getRatesHistory(
            @PathVariable String date,
            @RequestParam("app_id") String appId
    );

}