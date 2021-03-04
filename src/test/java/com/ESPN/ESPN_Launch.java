package com.ESPN;

import com.Base.TestBase;
import com.itextpdf.text.DocumentException;
import org.testng.annotations.Test;

import java.io.IOException;

public class ESPN_Launch extends TestBase {

    @Test()
    public void login() throws IOException, DocumentException, InterruptedException {
        Thread.sleep(10000);
        System.out.println("ESPN launched success.");

        //Email
//        typeById("com.chokuapp.dev:id/edt_email","mail@mail.com");
//        System.out.println("Email type success.");
//
//        typeById("com.chokuapp.dev:id/edt_password","user123456");
//        System.out.println("Password type success.");
//
//        clickById("com.chokuapp.dev:id/button");
//
//        sleep(3);

    }
}
