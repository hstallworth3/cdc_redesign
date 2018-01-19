package com.nbs.regressionSuite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.openqa.selenium.WebDriver;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import com.nbs.common.Login;
import com.rtts.utilities.DataFile;
import com.rtts.utilities.Log;

public class RegressionSuite1 {
	public Log log;
	DataFile df;
	public String Data_File_Path,Log_File_Path;
	public WebDriver driver;	

	@Before
	public void setUp() throws Exception {
		Login login = new Login();
		Data_File_Path = login.Data_File_Path+"regressionSuite\\RegressionSuite1.xls";
		Log_File_Path = login.Log_File_Path;		
	}

	@SuppressWarnings("rawtypes")
	@Test						
	public void testSuite() throws InstantiationException, IllegalAccessException, IOException{	
		log = new Log("RegressionSuite1", Log_File_Path,"");
		df = new DataFile(Data_File_Path, "Suite");		
		List<Class> testCases = new ArrayList<Class>();
		String Field_Value="";
		Class cname; 	
		String scriptName ="";		
		int rowNum = df.getRowCount();	
		System.out.println("rows are: "+ rowNum);		
		for (int row = 1; row < rowNum; row++) {	
			cname = null;
			scriptName = df.getDataFromColumn("TestScriptName", row);	
			System.out.println("scriptName: "+scriptName);							
			Field_Value = df.getDataFromColumn("Run", row);	
			if(Field_Value.equalsIgnoreCase("y")){		
				Set<String> packages = findAllPackagesStartingWith("com.nbs");			
				for (String pack : packages){
					String script_Name = pack + "." + scriptName;
					try {
						cname =  Class.forName(script_Name);
					} catch (final ClassNotFoundException e) {
						continue;
					}
					break;
				}
				if (cname != null)
					testCases.add(cname);
			}			
		}

		for (Class testCase : testCases)
		{				
			String fullName = testCase.toString();			
			String scriptname = fullName.substring(fullName.lastIndexOf(".") + 1);		
			runTestCase(testCase);
			String passed = Integer.toString(Log.summaryPassed);
			String failed = Integer.toString(Log.summaryFailed);
			System.out.println("Passed--: " + passed+Log.summaryPassed);			
			log.writeToSuite(scriptname, passed, failed, Log.FinalResult);
				}
		
	}

	@SuppressWarnings("rawtypes")
	private static void runTestCase(Class testCase)
	{
		Result result = JUnitCore.runClasses(testCase);
		for (Failure failure : result.getFailures())
		{
			System.out.println(failure.toString());
		}
	}

	public static Set<String> findAllPackagesStartingWith(String prefix) {
		List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
		classLoadersList.add(ClasspathHelper.contextClassLoader());
		classLoadersList.add(ClasspathHelper.staticClassLoader());
		Reflections reflections = new Reflections(new ConfigurationBuilder()
				.setScanners(new SubTypesScanner(false), new ResourcesScanner())
				.setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
				.filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(prefix))));
		Set<Class<? extends Object>> classes = reflections.getSubTypesOf(Object.class);

		Set<String> packageNameSet = new TreeSet<String>();
		for (Class<?> classInstance : classes) {
			String packageName = classInstance.getPackage().getName();
			if (packageName.startsWith(prefix)) {
				packageNameSet.add(packageName);
			}
		}
		return packageNameSet;
	}

	@After
	public void close() throws Exception {			
		log.close();
		df.close();		


	}
}