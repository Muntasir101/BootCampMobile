package com.Utilities;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class ReadConfigFile {
    Properties pro;

    public ReadConfigFile() {
        File src = new File("./src/main/resources/config.properties");
        try {
            FileInputStream fis = new FileInputStream(src);
            pro = new Properties();
            pro.load(fis);
        } catch (Exception e) {
            System.out.println("Exception is " + e.getMessage());
        }
    }

    public String getUrl() {
        String baseUrl = pro.getProperty("baseUrl");
        return baseUrl;
    }

    public String getLoginUrl() {
        String baseUrl = pro.getProperty("loginUrl");
        return baseUrl;
    }

    public String getUsername() {
        String username = pro.getProperty("username");
        return username;
    }

    public String getPassword() {
        String password = pro.getProperty("password");
        return password;
    }

    public String getFirefoxpath() {
        String firefoxpath = pro.getProperty("driverpath");
        return firefoxpath;
    }

    public String getChromepath() {
        String chromepath = pro.getProperty("chropath");
        return chromepath;
    }

    public String invalidUsername() {
        String Invalidusername = pro.getProperty("Invalidusername");
        return Invalidusername;
    }
}

