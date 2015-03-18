package hardware;

import junit.framework.Assert;

import org.junit.Test;

import utils.UIUtils;

public class UIUtilsTest
{
	@Test
	public void splitProperlyWithHTTP()
	{
		String sampleAddress = "http://localhost:8080/drive.xhtml";
		String result = UIUtils.determineBaseURLFromFullURL(sampleAddress);
		Assert.assertEquals("http://localhost", result);
	}
	@Test
	public void splitProperlyWithoutHTTP()
	{
		String sampleAddress = "localhost:8080/drive.xhtml";
		String result = UIUtils.determineBaseURLFromFullURL(sampleAddress);
		Assert.assertEquals("localhost", result);
	}
}
