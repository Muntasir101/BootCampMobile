package com.Base;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import report.ExtentManager;
import report.ExtentTestManager;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TestBase {

    //Extent Report Setup
    public static ExtentReports extent;
    public static AppiumDriver appiumDriver = null;
    public static String platform = null;
    public static WebDriver driver;
    private static WebDriverWait driverWait;

    static File app=new File("F:\\Android Test Automation\\carcon-20200513-dev.apk");

    //screenshot
    public static void captureScreenshot(WebDriver driver, String screenshotName) {

        DateFormat df = new SimpleDateFormat("(MM.dd.yyyy-HH:mma)");
        Date date = new Date();
        df.format(date);

        File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(file, new File(System.getProperty("user.dir") + "//screenshots//" + screenshotName + " " + df.format(date) + ".png"));
            System.out.println("Screenshot captured");
        } catch (Exception e) {
            System.out.println("Exception while taking screenshot " + e.getMessage());
        }
    }

    /*************** Reporting *****************/


    @Parameters({"OS", "deviceName", "version", "appiumPort"})
    @BeforeMethod
    public static AppiumDriver getDriver(String OS, String deviceName, String version,
                                         String appiumPort) throws IOException {
        platform = OS;
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability(MobileCapabilityType.APP,app.getAbsolutePath());
        cap.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
        if (OS.equalsIgnoreCase("android")) {
            cap.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.ANDROID);
            cap.setCapability(MobileCapabilityType.PLATFORM_VERSION, version);
            cap.setCapability("appPackage", "com.chokuapp.dev");
            cap.setCapability("appActivity", "com.chokuapp.ui.splash.SplashActivity");
            if (appiumDriver == null) {
                appiumDriver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), cap);
            }
        } else {
            cap.setCapability(MobileCapabilityType.PLATFORM_VERSION, version);
            cap.setCapability("bundleId", "com.example.apple-samplecode.UICatalog");
            cap.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
            cap.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.IOS);
            cap.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone");
            cap.setCapability(MobileCapabilityType.UDID, "00008030-0016453A3ED8802E");
            if (appiumDriver == null) {
                appiumDriver = new IOSDriver(new URL("http://127.0.0.1:4723/wd/hub"), cap);
            }
        }
        appiumDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        return appiumDriver;
    }


    /**
     * This method will swipe either up, Down, left or Right according to the
     * direction specified. This method takes the size of the screen and uses
     * the swipe function present in the Appium driver to swipe on the screen
     * with a particular timeout. There is one more method to implement swipe
     * using touch actions, which is not put up here.
     *
     *
     */


    public static void swipeFromOneToAnother(MobileElement element1, MobileElement element2) {
        try {
            TouchActions actions = new TouchActions(appiumDriver);
            appiumDriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
            Point location1 = element1.getLocation();
            int X1 = location1.getX();
            int Y1 = location1.getY();
            Point location2 = element2.getLocation();
            int X2 = location2.getX();
            int Y2 = location2.getY();

            actions.scroll(X1, Y1).move(X2, Y2).release().perform();
        } catch (Exception e) {

        }
    }

    public static void sleepFor(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @BeforeSuite
    public void extentSetup(ITestContext context) {
        ExtentManager.setOutputDirectory(context);
        extent = ExtentManager.getInstance();
    }

    @BeforeMethod
    public void startExtent(Method method) {
        String className = method.getDeclaringClass().getSimpleName();
        String methodName = method.getName().toLowerCase();
        ExtentTestManager.startTest(method.getName());
        ExtentTestManager.getTest().assignCategory(className);
    }

    @AfterMethod
    public void afterEachTestMethod(ITestResult result) {
        ExtentTestManager.getTest().getTest().setStartedTime(getTime(result.getStartMillis()));
        ExtentTestManager.getTest().getTest().setEndedTime(getTime(result.getEndMillis()));
        for (String group : result.getMethod().getGroups()) {
            ExtentTestManager.getTest().assignCategory(group);
        }

        if (result.getStatus() == 1) {
            ExtentTestManager.getTest().log(LogStatus.PASS, "test  Passed");
        } else if (result.getStatus() == 2) {
            ExtentTestManager.getTest().log(LogStatus.FAIL, getStackTrace(result.getThrowable()));
        } else if (result.getStatus() == 3) {
            ExtentTestManager.getTest().log(LogStatus.SKIP, "test  Skipped");
        }

        ExtentTestManager.endTest();
        extent.flush();

        if (result.getStatus() == ITestResult.FAILURE) {
            if (platform.equalsIgnoreCase("android") || platform.equalsIgnoreCase("ios")) {
                captureScreenshot(appiumDriver, result.getName());
            } else captureScreenshot(driver, result.getName());
        }
    }

    @AfterSuite
    public void generateReport() {
        extent.close();
    }

    private Date getTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }

    protected String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }

    @AfterMethod
    public void cleanUpApp() {
        appiumDriver.quit();
    }

    public void clickByXpath(String locator) {
        appiumDriver.findElement(By.xpath(locator)).click();
    }

    public void clickByXpath(MobileElement locator) {
        locator.click();
    }

    public void clickByXpathWebElement(WebElement locator) {
        locator.click();
    }

    public void sleep(int sec) throws InterruptedException {
        Thread.sleep(1000 * sec);
    }

    public void typeByXpath(String locator, String value) {
        appiumDriver.findElement(By.xpath(locator)).sendKeys(value);
    }

    public List<String> getTexts(List<WebElement> elements) {
        List<String> text = new ArrayList<String>();

        for (WebElement element : elements) {
            text.add(element.getText());
        }

        return text;
    }


    public void alertAccept(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert().accept();
        } catch (Exception e) {
            System.err.println("No alert visible in 5 seconds");
        }
    }


}