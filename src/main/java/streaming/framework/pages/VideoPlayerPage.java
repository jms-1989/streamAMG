package streaming.framework.pages;

import streaming.framework.config.ConfigManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.interactions.Actions;
import java.time.Duration;
import java.util.List;

public class VideoPlayerPage {
    private WebDriver driver;
    private WebDriverWait wait;
    private Actions actions;

    public VideoPlayerPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.actions = new Actions(driver);
        PageFactory.initElements(driver, this);
    }

    public void waitForPlayerToLoad() {
        // Wait for YouTube player to load
        wait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector("#movie_player")),
                ExpectedConditions.presenceOfElementLocated(By.cssSelector("video")),
                ExpectedConditions.presenceOfElementLocated(By.cssSelector(".html5-video-player"))
        ));

        // Wait a bit more for player to fully initialize
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void playVideo() {
        waitForPlayerToLoad();
        dismissCookieConsent();

        try {
            // First try clicking the large play button overlay
            List<WebElement> largePlayButtons = driver.findElements(
                    By.cssSelector(".ytp-large-play-button, .ytp-cued-thumbnail-overlay-image"));
            if (!largePlayButtons.isEmpty() && largePlayButtons.get(0).isDisplayed()) {
                largePlayButtons.get(0).click();
                Thread.sleep(1000);
                return;
            }

            // Try the regular play button
            List<WebElement> playButtons = driver.findElements(
                    By.cssSelector(".ytp-play-button, button[aria-label*='Play']"));
            if (!playButtons.isEmpty()) {
                playButtons.get(0).click();
                return;
            }

            // Fallback: click on video player area to start playback
            WebElement playerArea = driver.findElement(By.cssSelector("#movie_player, .html5-video-player"));
            playerArea.click();

        } catch (Exception e) {
            // Final fallback: use JavaScript to play video
            try {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("document.querySelector('video').play()");
            } catch (Exception ex) {
                System.out.println("Could not play video: " + ex.getMessage());
            }
        }
    }

    public void pauseVideo() {
        try {
            // Try pause button first
            List<WebElement> pauseButtons = driver.findElements(
                    By.cssSelector(".ytp-pause-button, button[aria-label*='Pause']"));
            if (!pauseButtons.isEmpty() && pauseButtons.get(0).isDisplayed()) {
                pauseButtons.get(0).click();
                return;
            }

            // Fallback: click on video to pause
            WebElement playerArea = driver.findElement(By.cssSelector("#movie_player"));
            playerArea.click();

        } catch (Exception e) {
            // JavaScript fallback
            try {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("document.querySelector('video').pause()");
            } catch (Exception ex) {
                System.out.println("Could not pause video: " + ex.getMessage());
            }
        }
    }

    public boolean isVideoPlaying() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebElement video = driver.findElement(By.tagName("video"));
            return (Boolean) js.executeScript("return !arguments[0].paused", video);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isLiveStreamActive() {
        try {
            // Check for live badge on YouTube
            List<WebElement> liveBadges = driver.findElements(
                    By.cssSelector(".ytp-live-badge, .badge-style-type-live-now"));
            return !liveBadges.isEmpty() && liveBadges.get(0).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public double getCurrentTime() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebElement video = driver.findElement(By.tagName("video"));
            Object time = js.executeScript("return arguments[0].currentTime", video);
            return time != null ? ((Number) time).doubleValue() : 0.0;
        } catch (Exception e) {
            return 0.0;
        }
    }

    public double getDuration() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebElement video = driver.findElement(By.tagName("video"));
            Object duration = js.executeScript("return arguments[0].duration", video);
            return duration != null ? ((Number) duration).doubleValue() : 0.0;
        } catch (Exception e) {
            return 0.0;
        }
    }

    public String getVideoSrc() {
        try {
            WebElement video = driver.findElement(By.tagName("video"));
            return video.getAttribute("src");
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isPlayerLoaded() {
        try {
            return driver.findElements(By.cssSelector("#movie_player, .html5-video-player, video")).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public void acceptCookies() {
        try {
            // Handle YouTube cookie consent
            List<WebElement> acceptButtons = driver.findElements(
                    By.cssSelector("button[aria-label*='Accept'], button[aria-label*='I agree'], .VfPpkd-LgbsSe[aria-label*='Accept']"));
            if (!acceptButtons.isEmpty()) {
                acceptButtons.get(0).click();
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            // Ignore if no cookie consent found
        }
    }

    public void dismissCookieConsent() {
        try {
            // Look for and dismiss any cookie consent dialogs
            List<WebElement> cookieButtons = driver.findElements(
                    By.cssSelector("button[aria-label*='Accept all'], button[aria-label*='I agree'], .eom-buttons button"));
            if (!cookieButtons.isEmpty()) {
                cookieButtons.get(0).click();
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            // Ignore if no consent dialog
        }
    }

    public void skipAds() {
        try {
            // Wait and skip ads if present
            Thread.sleep(3000);
            List<WebElement> skipButtons = driver.findElements(
                    By.cssSelector(".ytp-ad-skip-button, .ytp-skip-ad-button"));
            if (!skipButtons.isEmpty() && skipButtons.get(0).isDisplayed()) {
                skipButtons.get(0).click();
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            // Ignore if no ads
        }
    }

    public String getVideoTitle() {
        try {
            WebElement titleElement = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("h1.ytd-video-primary-info-renderer, #container h1")));
            return titleElement.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isVideoAvailable() {
        try {
            // Check if video is available (not removed/private)
            List<WebElement> errorElements = driver.findElements(
                    By.cssSelector(".reason, .yt-alert-message"));
            return errorElements.isEmpty();
        } catch (Exception e) {
            return true;
        }
    }
}