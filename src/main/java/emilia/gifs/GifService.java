package emilia.gifs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class GifService {

    @Value("${giphy.api.key}")
    private String apiKey;

    private final GiphyClient giphyClient;

    @Autowired
    public GifService(GiphyClient giphyClient) {
        this.giphyClient = giphyClient;
    }

    public ResponseEntity<String> search(
            String query,
            int offset) {
        return giphyClient.getGif(apiKey, query, offset);
    }

}
