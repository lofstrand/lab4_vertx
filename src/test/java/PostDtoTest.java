import com.vertx.entity.BlogPostDto;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

public class PostDtoTest {

    public static final String REST_BLOGPOSTS_URI = "http://localhost:8081/api/blogposts";

    @Test
    public void PostDtoTest() {
        String graphUrl = "http://localhost:8090/#/graph/5c0f9b7975204a0500ee33fd";
        Long uid = 2L;
        BlogPostDto blogPostDto = new BlogPostDto(uid, graphUrl);

        RestTemplate restTemplate = new RestTemplate();
        try {
            URI uri = restTemplate.postForLocation(REST_BLOGPOSTS_URI + "/", blogPostDto, BlogPostDto.class);
            System.out.println("Location : " + uri.toASCIIString());
        } catch  (Exception e) {
            System.out.println("Something went wrong");
        }
    }

}
