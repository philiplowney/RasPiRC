package hardware;

import java.util.logging.Logger;

import model.calibration.VehicleCalibration;

import org.junit.Test;
import org.usb4java.Device;

import constants.commands.MaestroCommandType;
import service.calibration.CalibrationService;
import service.maestro.MaestroHandler;
import service.maestro.exceptions.DeviceInUseException;
import service.maestro.exceptions.DeviceNotFoundException;
import service.maestro.exceptions.UnknownConnectionFailureException;

public class ConnectedTest
{
	private static MaestroHandler handler = MaestroHandler.getInstance();
	private final Logger LOGGER = Logger.getLogger(ConnectedTest.class.getCanonicalName());

	@Test
	public void goForward() throws InterruptedException, UnknownConnectionFailureException, DeviceNotFoundException, DeviceInUseException
	{
		handler.establishConnection();
		VehicleCalibration calibration = CalibrationService.getInstance().fetchDefaults();
		
		// Go forward for 7 tenths of a second
		handler.executeCommand((short) calibration.getThrottleServoIndex(), MaestroCommandType.SET_TARGET, (short) calibration.getFullForward());
		Thread.sleep(700);
		
		// Quick-back
		handler.executeCommand((short) calibration.getThrottleServoIndex(), MaestroCommandType.SET_TARGET, (short) calibration.getFullReverse());
		Thread.sleep(100);
		handler.executeCommand((short) calibration.getThrottleServoIndex(), MaestroCommandType.SET_TARGET, (short) calibration.getThrottleIdle());
		Thread.sleep(100);

		// Actual back
		handler.executeCommand((short) calibration.getThrottleServoIndex(), MaestroCommandType.SET_TARGET, (short) calibration.getFullReverse());
		Thread.sleep(1000);
		handler.executeCommand((short) calibration.getThrottleServoIndex(), MaestroCommandType.SET_TARGET, (short) calibration.getThrottleIdle());
	}
	// @Test
	// public void printStatus()
	// {
	// LOGGER.info("Device connected? "+(handler.findDevice()!=null));
	// }
	//
	// @Test
	// public void printAvailability()
	// {
	// Device d = handler.findDevice();
	// LOGGER.info("Device available? "+handler.isDeviceAvailable(d));
	// }
	//
	// @Test
	// public void printAllDevices()
	// {
	// for(String s : handler.getDeviceDescriptions())
	// {
	// LOGGER.info("Device: "+s);
	// }
	// }
}
