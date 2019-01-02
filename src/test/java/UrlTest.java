import com.vertx.entity.BlogPostDto;
import org.junit.Test;

public class UrlTest {

    @Test
    public void UrlTest() {
        String str = "http://localhost:8090/?uid=2#/viewgraph/5c0f9b7975204a0500ee33fd";
        BlogPostDto blogPostDto = convertCtxToBlogPostDto(str);
        System.out.println(blogPostDto.getGraph_url());
        System.out.println(blogPostDto.getOwner());
    }

    private BlogPostDto convertCtxToBlogPostDto(String url) {
        String newUrl = url.replace("viewgraph", "graph");
        int startUID = newUrl.indexOf("?uid=");
        int endUID = newUrl.indexOf("#/");
        String replacement = "";
        String toBeReplaced = newUrl.substring(startUID, endUID);
        String graphURL = newUrl.replace(toBeReplaced, replacement);
        String uid = toBeReplaced.replace("?uid=", "");

        // Create the blogpost with uid and graphURL
        BlogPostDto blogPost = new BlogPostDto(Long.valueOf(uid), graphURL);
        return blogPost;
    }
}
