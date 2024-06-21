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
import com.felixgear.petstores.person.Person;
import com.felixgear.petstores.pet.Pet;
import com.felixgear.petstores.store.Order;
import com.felixgear.petstores.utils.Assertions;
import com.github.javafaker.Faker;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class Base extends CommonFunctions {
    private static final Logger logger = LoggerFactory.getLogger(Base.class);
    
    private static String token;
    protected static Faker faker = new Faker();
    protected static Person person = new Person();
    protected static Pet pet = new Pet();
    protected static Order order = new Order();
    protected Assertions assertions = new Assertions();
    protected ExtentReports extent;
    protected ExtentTest test;
    protected static final Integer WRONG_ID = -10;
    
    @Parameters("baseUrl")
    @BeforeSuite(alwaysRun = true)
    public void initEnvironment(@Optional("https://petstore.swagger.io/v2") String baseUrl) {
	RestAssured.baseURI = baseUrl;
    }
    
    @BeforeSuite(alwaysRun = true, dependsOnMethods = "initEnvironment")
    public void loginHelper() {
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

    @BeforeClass(alwaysRun = true)
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
		logger.info("Running regression tests...");
	    } else if (group.equalsIgnoreCase("smoke")) {
		logger.info("Running smoke tests...");
	    }
	}
    }

    @BeforeMethod(alwaysRun = true)
    public void register(ITestContext context, Method method) {
	test = extent.createTest(method.getName());
	logger.info("Starting test: " + method.getName());

    }

    @AfterMethod(alwaysRun = true)
    public void logTestResults(ITestResult result) {
	if (result.getStatus() == ITestResult.FAILURE) {
	    test.fail(result.getThrowable());
	} else if (result.getStatus() == ITestResult.SUCCESS) {
	    test.pass("Test passed");
	} else {
	    test.skip(result.getThrowable());
	}
	extent.flush();
	logger.info("Finished test: " + result.getMethod().getMethodName());
    }

    @AfterClass(alwaysRun = true)
    public void teardown() {
	if (extent != null) {
	    extent.flush();
	}
    }

    @AfterSuite(alwaysRun = true)
    public void logout() {
	Response response = RestAssured.given().header("Authorization", "Bearer " + token).accept("application/json")
		.get("https://petstore.swagger.io/v2/user/logout");

	if (response.getStatusCode() == 200) {
	    logger.info("Logout successful");
	} else {
	    logger.info("Logout failed: " + response.getStatusLine());
	}
    }
}
