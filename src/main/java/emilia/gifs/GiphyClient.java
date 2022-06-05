package emilia.gifs;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "giphyClient", url = "${giphy.url.base}")
public interface GiphyClient {

    @GetMapping("search")
    ResponseEntity<String> getGif(
            @RequestParam("api_key") String apiKey,
            @RequestParam("q") String query,
            @RequestParam("offset") int offset
    );

}