package com.Utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReportFile extends TestListenerAdapter {

    public ExtentHtmlReporter htmlReporter;
    public ExtentReports extent;
    public ExtentTest test;

    public void onStart(ITestContext testContext) {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        String repName = "Test-Report-" + timeStamp + ".html";
        htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "//HtmlReport//" + repName);
        htmlReporter.loadXMLConfig(System.getProperty("user.dir") + "/extent-config.xml");

        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);

        extent.setSystemInfo("OS", "Windows");
        extent.setSystemInfo("Hostname", "Selenium user");
        extent.setSystemInfo("Environment", "Browser");
        extent.setSystemInfo("Browser Name", "Chrome");
        extent.setSystemInfo("Author", "Muntasir");


        htmlReporter.config().setDocumentTitle("Automation Test Report");// title of report
        htmlReporter.config().setReportName("Functional Test ");// name of the report
        htmlReporter.config().setTheme(Theme.DARK);
    }

    public void onTestSuccess(ITestResult tr) {
        test = extent.createTest(tr.getName());
        test.log(Status.PASS, MarkupHelper.createLabel(tr.getName(), ExtentColor.GREEN));

        String pdfpath = System.getProperty("user.dir") + "\\PdfFolder\\" + tr.getName() + ".pdf";
        File f = new File(pdfpath);
        if (f.exists()) {
            try {
                test.fail("pdf is below: " + test.addScreenCaptureFromPath(pdfpath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void onTestFailure(ITestResult tr) {
        test = extent.createTest(tr.getName());
        test.log(Status.FAIL, MarkupHelper.createLabel(tr.getName(), ExtentColor.RED));

        String screenshotpath = System.getProperty("user.dir") + "\\FiledScreenshot\\" + tr.getName() + ".png";
        File f = new File(screenshotpath);
        if (f.exists()) {
            try {
                test.fail("screenshot is below: " + test.addScreenCaptureFromPath(screenshotpath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void onTestSkipped(ITestResult tr) {
        test = extent.createTest(tr.getName());
        test.log(Status.SKIP, MarkupHelper.createLabel(tr.getName(), ExtentColor.ORANGE));
    }

    public void onFinish(ITestContext testContext) {
        extent.flush();
    }
}

