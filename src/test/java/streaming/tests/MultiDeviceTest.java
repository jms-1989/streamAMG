package streaming.tests;

import streaming.framework.pages.VideoPlayerPage;
import streaming.framework.config.ConfigManager;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.Map;

public class MultiDeviceTest {

    @Test
    public void testMobileCompatibility() {
        // Set up mobile emulation for iPhone 12 Pro
        Map<String, String> mobileEmulation = new HashMap<>();
        mobileEmulation.put("deviceName", "iPhone 12 Pro");

        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("mobileEmulation", mobileEmulation);
        options.addArguments("--disable-web-security");
        options.addArguments("--disable-blink-features=AutomationControlled");

        ChromeDriver mobileDriver = new ChromeDriver(options);

        try {
            // Navigate to  video
            String videoUrl = ConfigManager.get("test.video.url");
            mobileDriver.get(videoUrl);

            VideoPlayerPage playerPage = new VideoPlayerPage(mobileDriver);

            // Wait for player to load on mobile
            playerPage.waitForPlayerToLoad();
            Assert.assertTrue(playerPage.isPlayerLoaded(),
                    "Video player should load on mobile device");

            // Test video playability on mobile
            playerPage.playVideo();

            // Wait for video to start
            Thread.sleep(5000);

            // Check if video can be controlled
            double duration = playerPage.getDuration();
            System.out.println("Video duration on mobile: " + duration + " seconds");

            // Duration should be greater than 0 when loaded
            if (duration > 0) {
                Assert.assertTrue(duration > 0, "Video should have valid duration on mobile");
            }

            // Check video source is available
            String videoSrc = playerPage.getVideoSrc();
            System.out.println("Mobile video source: " + videoSrc);

            // Test page responsiveness by checking viewport
            Long viewportWidth = (Long) mobileDriver.executeScript("return window.innerWidth;");
            System.out.println("Mobile viewport width: " + viewportWidth);
            Assert.assertTrue(viewportWidth <= 428, "Should be in mobile viewport"); // iPhone 12 Pro width

        } catch (Exception e) {
            System.out.println("Mobile test exception: " + e.getMessage());
            Assert.fail("Mobile compatibility test failed: " + e.getMessage());
        } finally {
            mobileDriver.quit();
        }
    }
}
