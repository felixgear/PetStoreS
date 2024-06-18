package com.felixgear.petstores;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.felixgear.petstores.utils.Assertions;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class Base extends CommonFunctions {
    private static final Logger logger = LoggerFactory.getLogger(Base.class);

    private static String token;
    protected Assertions assertions = new Assertions();
    protected ExtentReports extent;
    protected ExtentTest test;
    protected static final Integer WRONG_ID = -10;

    @Parameters("baseUrl")
    @BeforeSuite
    public void loginHelper(@Optional("https://petstore.swagger.io/v2") String baseUrl) {
	RestAssured.baseURI = baseUrl;
	Map<String, String> login = new HashMap<>();
	login.put("username", "test");
	login.put("password", "abc123");
	Response response = getUrl(LOGIN_USER, login);

	if (response.getStatusCode() == 200) {
	    String message = response.getBody().jsonPath().getString("message");
	    String[] messageParts = message.split(":");
	    token = messageParts[1];
	    logger.info("Access Token: " + token);
	} else {
	    throw new RuntimeException("Login failed: " + response.getStatusLine());
	}
    }

    @BeforeClass
    public void setup(ITestContext context) {
	ExtentSparkReporter htmlReporter = new ExtentSparkReporter("extent.html");
	htmlReporter.config().setDocumentTitle("Automation Report");
	htmlReporter.config().setReportName(" Tests");
	htmlReporter.config().setTheme(Theme.STANDARD);

	extent = new ExtentReports();
	extent.attachReporter(htmlReporter);

	extent.setSystemInfo("Host Name", "PetStoreS host");
	extent.setSystemInfo("Environment", "QA");
	extent.setSystemInfo("User Name", "Test User");

	// Set groups for regression and smoke tests based on Beanshell script
	String[] groups = context.getIncludedGroups();
	for (String group : groups) {
	    if (group.equalsIgnoreCase("regression")) {
		System.out.println("Running regression tests...");
	    } else if (group.equalsIgnoreCase("smoke")) {
		System.out.println("Running smoke tests...");
	    }
	}
    }

    @BeforeMethod
    public void register(ITestContext context, Method method) {
	test = extent.createTest(method.getName());
	System.out.println("Starting test: " + method.getName());

    }

    @AfterMethod
    public void logTestResults(ITestResult result) {
	if (result.getStatus() == ITestResult.FAILURE) {
	    test.fail(result.getThrowable());
	} else if (result.getStatus() == ITestResult.SUCCESS) {
	    test.pass("Test passed");
	} else {
	    test.skip(result.getThrowable());
	}
	extent.flush();
	System.out.println("Finished test: " + result.getMethod().getMethodName());
    }

    @AfterClass
    public void teardown() {
	if (extent != null) {
	    extent.flush();
	}
    }

    @AfterSuite
    public void logout() {
	Response response = RestAssured.given().header("Authorization", "Bearer " + token).accept("application/json")
		.get("https://petstore.swagger.io/v2/user/logout");

	if (response.getStatusCode() == 200) {
	    System.out.println("Logout successful");
	} else {
	    System.out.println("Logout failed: " + response.getStatusLine());
	}
    }
}
