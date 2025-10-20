package com.utilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ThreadGuard;

import java.time.Duration;


public class Driver {

    /*
    - This Driver is following SINGLETON PATTERN => creating single driver instance and using it for all tests
    - This driver is currently useful to run Sequential tests
    - This cannot be used for parallel testing
    - We will make it more dynamic later
     */

    // private static WebDriver driver;

    // For Parallel Testing
    // 1. We won't use private static WebDriver driver; anymore
    // 2. Create a private constructor (private constructor + ThreadLocal class  = Best practice for Parallel Testing
    // 3. We will use ThreadLocal WebDriver for Parallel Testing
    // 4. Driver.getDriver() will still be used in all test classes. But after this configuration, this method will
    //    return copy of a thread safe driver each time
    // 5. Initialise the WebDriver instance through a different method
    // 6. Close the driver


    // Create a private constructor
    private Driver(){

    }

    // Use ThreadLocal WebDriver for Parallel Testing
    // This will return a copy of the driver instance for each thread (browser)
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();


    // Driver.getDriver() will still be used in all test classes.
    public static WebDriver getDriver() {
        if (driver.get() == null) {
            System.out.println("Instantiating WebDriver... ");
            // But now we are initialising the driver through a method call
            initialiseDriver();
        }

        return driver.get();
    }

    // Initialise the WebDriver instance through a different method
    public static void initialiseDriver() {
        switch (ConfigReader.getProperty("browser")){
            case "chrome":
                WebDriverManager.chromedriver().setup();
                // driver = new ChromeDriver();
                driver.set(ThreadGuard.protect(new ChromeDriver()));
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                driver.set(ThreadGuard.protect(new EdgeDriver()));
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver.set(ThreadGuard.protect(new FirefoxDriver()));
                break;
            case "safari":
                WebDriverManager.safaridriver().setup();
                driver.set(ThreadGuard.protect(new SafariDriver()));
                break;
            case "chrome-headless":
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--headless" , "new");
                driver.set(ThreadGuard.protect(new ChromeDriver(options)));
                break;
            default:
                throw new RuntimeException();

        }
        driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        driver.get().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));
        driver.get().manage().window().maximize();

    }

    public static void closeDriver(){
        if (driver != null){
            driver.get().quit();
            driver.remove();
        }
    }

}
