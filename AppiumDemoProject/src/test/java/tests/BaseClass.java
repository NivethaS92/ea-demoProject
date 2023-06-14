package tests;

import java.io.FileReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;


public class BaseClass {
	
	public AndroidDriver driver;
	public Map<String,String> map;
	public Map<String,String> jsonResponse;

	@BeforeTest	
	public void setup() throws Exception
	{

		DesiredCapabilities caps = new DesiredCapabilities();
		
		caps.setCapability(MobileCapabilityType.PLATFORM_NAME, "ANDROID");
		caps.setCapability(MobileCapabilityType.PLATFORM_VERSION, "10.0");
		caps.setCapability(MobileCapabilityType.DEVICE_NAME, "Pixel_5");
		caps.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "60");
		caps.setCapability(MobileCapabilityType.APP, "src/test/resources/apps/RecordLabelApp.apk");
                
		
		caps.setCapability("appPackage", "com.example.myapplication");
		caps.setCapability(MobileCapabilityType.NO_RESET, true);
		
		driver= new AndroidDriver (new URL("http://127.0.0.1:4723/wd/hub"), caps);
		
	}
	
	@Test (priority = 1)
	public void compare()
	{
		try 
		{
		List<WebElement> elements = driver.findElements(By.id("com.energyaustralia.codingtestsample:id/festivalTextView"));
		List<WebElement> bands = driver.findElements(By.id("com.energyaustralia.codingtestsample:id/titleTextView"));
		map = new HashMap<String,String>();
		for(WebElement element: elements)
		{
			for(WebElement ele: bands)
			{
			map.put(element.getText(), ele.getText());
			}
		}
		}
		catch(Exception e)
		{
			 System.out.println(e.getMessage());
		}
	}
	
	@Test (priority = 2)
	public void jsonResponse()
	{
		//JSON object to parse read file
       
	      try {

	    	   List<Object> o = (List<Object>) new JSONParser().parse(new FileReader("/Users/User/Desktop/course.json"));
	    	  
	    	   jsonResponse = new HashMap<String,String>();
	    	   
	    	   for(Object obj : o)
	    	   {
	    		   JSONObject jsonObject = (JSONObject)obj;
	    		   jsonResponse.put(jsonObject.get("name").toString(), jsonObject.get("bands").toString());
	    	   }
	    	   
	      }
	      catch(Exception e)
	      {
	    	  System.out.println(e.getMessage());
	      }
		
	}
	
	@Test (priority = 3)
	public void uIVerification()
	{
		 
		Assert.assertEquals(map.size() == jsonResponse.size(), true);
		
		MapDifference diff = Maps.difference(map, jsonResponse);
		
		Assert.assertFalse(diff.areEqual(), "The UI not as same as thew API response");
	}
	
	
	
	
	@AfterTest
	public void teardown()
	{
		driver.close();
	}
}
