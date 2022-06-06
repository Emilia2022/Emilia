package emilia.gifs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class GifServiceImpl implements GifService {

    private final String apiKey;
    private final GiphyClient giphyClient;

    @Autowired
    public GifServiceImpl(
            GiphyClient giphyClient,
            @Value("${giphy.api.key}") String apiKey) {
        this.giphyClient = giphyClient;
        this.apiKey = apiKey;
    }

    @Override
    public ResponseEntity<String> search(
            String query,
            int offset) {
        return giphyClient.getGif(apiKey, query, offset);
    }

}
