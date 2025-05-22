package streaming.tests;

import streaming.framework.config.ConfigManager;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class APITest {

    @Test
    public void testYouTubeEmbedEndpoint() {
        String videoId = ConfigManager.get("test.video.id");
        String embedUrl = "https://www.youtube.com/embed/" + videoId;

        Response response = given()
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .when()
                .get(embedUrl)
                .then()
                .extract().response();

        Assert.assertEquals(response.getStatusCode(), 200, "YouTube embed should load successfully");

        String content = response.getBody().asString();
        Assert.assertTrue(content.contains("youtube") || content.contains("player"),
                "Embed should contain YouTube player content");
    }
}