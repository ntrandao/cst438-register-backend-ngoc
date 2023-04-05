package com.cst438;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.StudentRepository;
import com.cst438.domain.Student;

/*
 * This example shows how to use selenium testing using the web driver 
 * with Chrome browser.
 * 
 *  - Buttons, input, and anchor elements are located using XPATH expression.
 *  - onClick( ) method is used with buttons and anchor tags.
 *  - Input fields are located and sendKeys( ) method is used to enter test data.
 *  - Spring Boot JPA is used to initialize, verify and reset the database before
 *      and after testing.
 *      
 *    Make sure that TEST_COURSE_ID is a valid course for TEST_SEMESTER.
 *    
 *    URL is the server on which Node.js is running.
 */

@SpringBootTest
public class EndToEndRegistrationTest {

	public static final String CHROME_DRIVER_FILE_LOCATION = "C:/chromedriver.exe";

	public static final String URL = "http://localhost:3000";

	public static final String TEST_STUDENT_EMAIL = "test@csumb.edu";
	
	public static final String TEST_STUDENT_NAME = "GREATONE";

	public static final int TEST_COURSE_ID = 40443; 

	public static final String TEST_SEMESTER = "2021 Fall";

	public static final int SLEEP_DURATION = 1000; // 1 second.

	/*
	 * When running in @SpringBootTest environment, database repositories can be used
	 * with the actual database.
	 */
	
	@Autowired
	StudentRepository studentRepository;
	
	@Autowired
	EnrollmentRepository enrollmentRepository;

	@Autowired
	CourseRepository courseRepository;

	/*
	 * Student add course TEST_COURSE_ID to schedule for 2021 Fall semester.
	 */
	
	@Test
	public void addStudentTest() throws Exception {

		/*
		 * if student is already registered, then delete the registration.
		 */
		
		Student x = null;
		do {
			x = studentRepository.findByEmail(TEST_STUDENT_EMAIL);
			if (x != null)
				studentRepository.delete(x);
		} while (x != null);

		// set the driver location and start driver
		//@formatter:off
		// browser	property name 				Java Driver Class
		// edge 	webdriver.edge.driver 		EdgeDriver
		// FireFox 	webdriver.firefox.driver 	FirefoxDriver
		// IE 		webdriver.ie.driver 		InternetExplorerDriver
		//@formatter:on

		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
		WebDriver driver = new ChromeDriver();
		// Puts an Implicit wait for 10 seconds before throwing exception
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		try {

			driver.get(URL);
			Thread.sleep(SLEEP_DURATION);

			// select the last of the radio buttons on the list of semesters page.
			
//			WebElement we = driver.findElement(By.xpath("(//input[@type='radio'])[last()]"));
//			we.click();
//
//			// Locate and click "Get Schedule" button
//			
//			driver.findElement(By.xpath("//a")).click();
//			Thread.sleep(SLEEP_DURATION);

			// Locate and click "Add Student" button which is the last button on the page.
			driver.findElement(By.xpath("//a [last()]")).click();
			Thread.sleep(SLEEP_DURATION);

			// click Add Student Button again which is the only button on the page
			
//			driver.findElement(By.xpath("//input[@name='course_id']")).sendKeys(Integer.toString(TEST_COURSE_ID));
			driver.findElement(By.xpath("//button")).click();
			Thread.sleep(SLEEP_DURATION);

			/*
			* verify that new course shows in schedule.
			* get the title of all courses listed in schedule
			*/ 
		
			driver.findElement(By.xpath("//input[@name='email']")).sendKeys(TEST_STUDENT_EMAIL);
			driver.findElement(By.xpath("//input[@name='name']")).sendKeys(TEST_STUDENT_NAME);
			
			driver.findElement(By.xpath("//button [@id='Add']")).click();
			Thread.sleep(SLEEP_DURATION);
//			Course course = courseRepository.findById(TEST_COURSE_ID).get();
			
			
//			List<WebElement> elements  = driver.findElements(By.xpath("//div[@data-field='title']/div[@class='MuiDataGrid-cellContent']"));
//			boolean found = false;
//			for (WebElement e : elements) {
//				System.out.println(e.getText()); // for debug
//				if (e.getText().equals(course.getTitle())) {
//					found=true;
//					break;
//				}
//			}
			WebElement emailDiv = driver.findElement(By.xpath("//div[@id='emailDiv']"));
			assertEquals( emailDiv.getText(), "EMAIL: " + TEST_STUDENT_EMAIL);
			WebElement nameDiv = driver.findElement(By.xpath("//div[@id='nameDiv']"));
			assertEquals( nameDiv.getText(), "NAME: " + TEST_STUDENT_NAME);
			
			// verify that enrollment row has been inserted to database.
			
//			Enrollment e = enrollmentRepository.findByEmailAndCourseId(TEST_USER_EMAIL, TEST_COURSE_ID);
//			assertNotNull(e, "Course enrollment not found in database.");

		} catch (Exception ex) {
			throw ex;
		} finally {

			// clean up database.
			
//			Enrollment e = enrollmentRepository.findByEmailAndCourseId(TEST_USER_EMAIL, TEST_COURSE_ID);
//			if (e != null)
//				enrollmentRepository.delete(e);

			x = studentRepository.findByEmail(TEST_STUDENT_EMAIL);
			if (x != null)
				studentRepository.delete(x);

			driver.quit();
		}

	}
}
