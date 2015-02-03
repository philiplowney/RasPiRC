package hardware;

import java.util.logging.Logger;

import org.junit.Test;
import org.usb4java.Device;

import service.maestro.MaestroHandler;

public class ConnectedTest
{
	private static MaestroHandler handler = MaestroHandler.getInstance();
	private final Logger LOGGER = Logger.getLogger(ConnectedTest.class.getCanonicalName());

//	@Test
//	public void printStatus()
//	{
//		LOGGER.info("Device connected? "+(handler.findDevice()!=null));
//	}
//	
//	@Test
//	public void printAvailability()
//	{
//		Device d = handler.findDevice();
//		LOGGER.info("Device available? "+handler.isDeviceAvailable(d));
//	}
//	
//	@Test
//	public void printAllDevices()
//	{
//		for(String s : handler.getDeviceDescriptions())
//		{
//			LOGGER.info("Device: "+s);
//		}
//	}
}
