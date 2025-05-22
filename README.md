# README

## Prerequisites
|Tool|Version|Purpose|
|---|---|---|
|Java|11+|Runtime environment|
|Maven|3.6+|Dependency management & build|
|ChromeDriver|Latest|Browser automation
|Git|2.0+|Version control|

## Installation Commands

### Verify Java installation
```java -version```

### Verify Maven installation
```mvn -version```

### Install ChromeDriver (macOS with Homebrew)
```brew install chromedriver```

### Installation

Clone Repository:

```git clone https://github.com/jms-1989/streamAMG.git```

```cd auto-stream-test```

```mvn clean compile```

### Running Tests

Run all tests with: 

```mvn clean test```

## Test Strategy

### Testing Approach
Here's the general approach for testing a video streaming service that I'd take based on my framework.

The framework takes a layered testing strategy - it wouldn't just be checking if videos play, but covering the whole experience from multiple angles.

The four main testing areas:
- API Testing: This would be checking the backend services that feed information to the video player.  Making sure the video metadata, stream URLs, and all that foundational data is fine (admittedly, I didn't implement this in the framework as I really wasn't sure about it and I'd taken more than the recommended time already)
- Performance Testing: Here I'd be looking at how well the streams actually perform, measuring things like buffering, dropped frames, and load times. 
- Multi-Device Compatibility: Testing that the service works properly across different devices and browsers. Using browser emulation to simulate mobile devices without needing actual hardware.
- Live Streaming Events - Testing the live content, which behaves differently from on-demand videos (infinite duration, real-time progression, etc.).

### Automation Framework
The framework itself is built using Java and follows standard industry patterns:
- Page Object Model: Separated the test logic from the page interactions. So rather than having tests that directly click buttons and find elements, we have a VideoPlayerPage class that handles all the video player interactions. This means if the UI changes, you only need to update one place.
- Configuration Management: All the URLs, settings, and test data are stored in a properties file. This makes it dead easy to run the same tests against different environments (dev, staging, production) without changing any code.
- Driver Management: Each test gets its own browser session and they don't interfere with each other. Crucial for running tests in parallel.

This setup is modular and extensible. Each test area is independent, so you can run just the API tests if you're working on backend changes etc. The BaseTest class handles all the common setup and teardown, so individual tests can focus on what they are made to do.

It follows Maven conventions, so it works nicely with standard Java tooling. TestNG provides the test execution framework with reporting and parallel executions.

The whole thing is supposed to be maintainable and scalable so you can easily add new test scenarios without restructuring the entire framework. It's also configuration-driven, so the same tests can be run against different environments or with different parameters without code changes.

I used YouTube because the live videos are easily accessible, as are the elements for capturing with Selenium. 

## Assumptions

1. I assumed there would be some sort of login page for the test environments which is why the 'base test' exists.
2. I assumed that it would need to be run from the command line, not just the IDE, hence the maven and testNG implementation
3. I assumed you'd want a working model that runs which is why there's not much that runs

## AI usage

1. I did use Claude to assist with the API test. I hadn't done API testing outside of Postman so it was new to me and I needed to know about using restassured.
2. I did use SonarQube for assistance with issues within IntelliJ as I'm not a perfect coder. Lots of syntax errors, help with missing assertions, and with exception handling

## Final thoughts

I did probably spend too much time on this as I became a little too focused on making sure there was something that would run because I'm just not good at leaving things unfinished.

Due to some issues with cookie messages, it may need manual intervention for the mobile view test as scrolling is required - I just spent too much time to justify fixing it, I also only really managed 1 test per area because of time.

Honestly, I did find this challenging but I enjoyed it overall. Most of my automation experience has been finding objects on a web page and asserting if it was there or not, so this was very different.
