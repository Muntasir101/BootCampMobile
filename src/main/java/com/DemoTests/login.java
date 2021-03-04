package com.DemoTests;

import com.Base.TestBase;
import com.itextpdf.text.DocumentException;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.io.IOException;

public class login extends TestBase {

    @Test()
    public void testDemo2() throws IOException, DocumentException, InterruptedException {

        //Login
        MobileElement Email=(MobileElement) driver.findElement(By.id("com.chokuapp.dev:id/edt_email"));
        MobileElement Password=(MobileElement) driver.findElement(By.id("com.chokuapp.dev:id/edt_password"));
        MobileElement LoginBtn=(MobileElement) driver.findElement(By.id("com.chokuapp.dev:id/button"));

        Email.sendKeys("mail@mail.com");
        Thread.sleep(3000);
        System.out.println("Email Type success");

        Password.sendKeys("12333444");
        Thread.sleep(3000);
        System.out.println("Password Type success");

        LoginBtn.click();
        System.out.println("Login click success");

        captureScreenshot(driver,"Google Search");



    }
}
