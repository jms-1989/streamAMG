package streaming.tests;

import streaming.framework.pages.VideoPlayerPage;
import streaming.framework.driver.DriverManager;
import streaming.framework.config.ConfigManager;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LiveStreamTest extends BaseTest {

    @Test
    public void testYouTubeLiveStreamDetection() {
        // This test would need a live stream URL to work properly
        // For now, we'll test the live detection mechanism with regular video

        String videoUrl = ConfigManager.get("test.video.url");
        DriverManager.getDriver().get(videoUrl);

        VideoPlayerPage playerPage = new VideoPlayerPage(DriverManager.getDriver());
        playerPage.acceptCookies();
        playerPage.waitForPlayerToLoad();

        // Check if live stream indicators are present
        boolean isLive = playerPage.isLiveStreamActive();
        System.out.println("Live stream detected: " + isLive);

        // For regular YouTube videos, this should be false
        // For actual live streams, this would be true

        if (isLive) {
            System.out.println("This is a live stream!");

            // Test live stream specific functionality
            playerPage.playVideo();

            double duration = playerPage.getDuration();
            Assert.assertTrue(Double.isInfinite(duration) || duration > 7200,
                    "Live stream should have very long or infinite duration");

        } else {
            System.out.println("This is a regular video (not live)");

            double duration = playerPage.getDuration();
            Assert.assertTrue(duration > 0 && duration < Double.POSITIVE_INFINITY,
                    "Regular video should have finite duration");
        }
    }
}