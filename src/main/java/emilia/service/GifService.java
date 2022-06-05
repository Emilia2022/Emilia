package emilia.service;

import emilia.api.GiphyClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class GifService {

    @Value("${giphy.api.key}")
    private String apiKey;

    private final GiphyClient giphyClient;

    public GifService(GiphyClient giphyClient) {
        this.giphyClient = giphyClient;
    }

    public ResponseEntity<String> getRandomGif(String tag) {
        return giphyClient.getGif(apiKey, tag);
    }

}
