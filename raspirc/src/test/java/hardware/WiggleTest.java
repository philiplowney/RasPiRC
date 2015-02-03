package hardware;

import org.junit.AfterClass;

import org.junit.BeforeClass;
import org.junit.Test;

import constants.commands.MaestroCommandType;

import service.maestro.MaestroHandler;

public class WiggleTest
{
	private static MaestroHandler handler = MaestroHandler.getInstance();
	
//	@BeforeClass
//	public static void open() throws Exception
//	{
//		handler.establishConnection();
//	}
//	
//	@Test
//	public void wiggle() throws Exception
//	{
//		handler.executeCommand((short)5, MaestroCommandType.SET_TARGET, (short) 6000);
//		int sleepTime = 250;
//		Thread.sleep(sleepTime);
//		handler.executeCommand((short)5, MaestroCommandType.SET_TARGET, (short) 4000);
//		Thread.sleep(sleepTime);
//		handler.executeCommand((short)5, MaestroCommandType.SET_TARGET, (short) 8000);
//		Thread.sleep(sleepTime);
//		handler.executeCommand((short)5, MaestroCommandType.SET_TARGET, (short) 4000);
//		Thread.sleep(sleepTime);
//		handler.executeCommand((short)5, MaestroCommandType.SET_TARGET, (short) 6000);
//	}
//	
//	@AfterClass
//	public static void closeOff()
//	{
//		handler.releaseConnection();
//	}
}