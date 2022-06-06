package emilia.gifs;

import org.assertj.core.api.WithAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.eq;

@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan("emilia")
public class GifServiceImplTest implements WithAssertions {

    @Autowired
    private GifServiceImpl tested;

    @MockBean
    private GiphyClient giphyClient;

    @Value("${giphy.api.key}")
    private String apiKey;

    @Test
    public void whenCallingGiphyApiThenReturnExpectedResponse() {

        // given
        final int OFFSET = 1;
        final String QUERY = "some query";
        ResponseEntity<String> response = new ResponseEntity<>("test", HttpStatus.OK);
        Mockito.when(giphyClient.getGif(eq(apiKey), eq(QUERY), eq(OFFSET))).thenReturn(response);

        // when
        ResponseEntity<String> result = tested.search(QUERY, OFFSET);

        // then
        assertThat(result.getBody()).isEqualTo("test");
    }

}