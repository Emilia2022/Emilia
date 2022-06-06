package emilia.gifs;

import org.springframework.http.ResponseEntity;

public interface GifService {

    ResponseEntity<String> search(
            String query,
            int offset);

}
