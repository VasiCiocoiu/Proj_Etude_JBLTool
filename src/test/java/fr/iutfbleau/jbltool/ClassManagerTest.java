package fr.iutfbleau.jbltool;

import java.io.File;
import fr.iutfbleau.jbltool.managers.ClassManager;
import java.io.IOException;
import java.io.FileNotFoundException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertTrue;

public class ClassManagerTest {
	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();

	@Test
	public void throw_filenotfoundexception_getClassNode(){
		ClassManager cm = new ClassManager();
		try {
			cm.readFromInternalFile("/Doesn't exist");
		} catch(IOException e) {
			assertTrue(e instanceof FileNotFoundException);
			return ;
		}
		Assert.fail("No exception thrown.");
	}

	@Test
	public void throw_ioexception_getClassNode(){
		ClassManager cm = new ClassManager();
		try {
			cm.readFromInternalFile("/documentation.xml");
		} catch(IOException e) {
			assertTrue(e instanceof IOException);
			return ;
		}
		Assert.fail("No exception thrown.");
	}

	@Test
	public void good_getClassNode(){
		ClassManager cm = new ClassManager();
		try {
			cm.readFromInternalFile("/Test.class");
		} catch(IOException e) {
			Assert.fail("Exception thrown: " + e.getMessage());
			return ;
		}
		assertTrue(true);
	}

	@Test
	public void correct_source_code_name(){
		ClassManager cm = new ClassManager();
		try {
			cm.readFromInternalFile("/Test.class");
		} catch(IOException e) {
			Assert.fail("Exception thrown: " + e.getMessage());
			return ;
		}
		assertTrue(cm.getClassNode().sourceFile.equals("Test.java"));
	}

	@Test
	public void null_file_getSourceCodeFile(){
		ClassManager cm = new ClassManager();
		Assert.assertNull(cm.getSourceCodeFile());
	}

	@Test
	public void not_null_file_getSourceCodeFile(){
		ClassManager cm = new ClassManager();
		try {
			cm.readFromInternalFile("/Test.class");
		} catch(IOException e) {
			Assert.fail("Exception thrown: " + e.getMessage());
			return ;
		}
		Assert.assertNotNull(cm.getSourceCodeFile());
	}

	@Test
	public void null_file_getClassNode(){
		ClassManager cm = new ClassManager();
		Assert.assertNull(cm.getClassNode());
	}

	@Test
	public void not_null_file_getClassNode(){
		ClassManager cm = new ClassManager();
		try {
			cm.readFromInternalFile("/Test.class");
		} catch(IOException e) {
			Assert.fail("Exception thrown: " + e.getMessage());
			return ;
		}
		Assert.assertNotNull(cm.getClassNode());
	}

	@Test
	public void throw_illegalstateexception_writeToFile(){
	exceptionRule.expect(IllegalStateException.class);

		ClassManager cm = new ClassManager();
		try {
			cm.writeToFile(new File("/Test2.class"));
		} catch(IOException e) {
			Assert.fail("Wrong exception thrown.");
		}
		Assert.fail("No exception thrown.");
	}

	@Test
	public void throw_illegalstateexception_overwriteToFile(){
		exceptionRule.expect(IllegalStateException.class);

		ClassManager cm = new ClassManager();
		try {
			cm.overwriteToFile();
		} catch(IOException e) {
			Assert.fail("Wrong exception thrown.");
		}
		Assert.fail("No exception thrown.");
	}
}
