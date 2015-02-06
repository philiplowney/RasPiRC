package service.maestro;

import java.nio.ByteBuffer;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.usb4java.Context;
import org.usb4java.Device;
import org.usb4java.DeviceDescriptor;
import org.usb4java.DeviceHandle;
import org.usb4java.DeviceList;
import org.usb4java.LibUsb;

import service.maestro.exceptions.DeviceInUseException;
import service.maestro.exceptions.DeviceNotFoundException;
import service.maestro.exceptions.UnknownConnectionFailureException;
import constants.commands.MaestroCommandType;
import constants.hardware.MaestroHardwareSignature;

/**
 * <p>
 * Responsible for connecting to the Maestro Servo Controller, executing
 * hardware commands, and monitoring connectivity state to registered listeners.
 * </p>
 * <p>
 * This is a singleton class because there is only one physical hardware
 * resource. Please use getInstance() rather than trying to construct it. The
 * constructor is package-private(protected) on purpose ;)
 * </p>
 * 
 * @author Philip Lowney
 * 
 */
public class MaestroHandler
{
	protected MaestroHandler()
	{}

	private static MaestroHandler instance;

	public static MaestroHandler getInstance()
	{
		if (instance == null)
		{
			instance = new MaestroHandler();
			context = new Context();
			LibUsb.init(context);
		}
		return instance;
	}

	private static final Logger LOGGER = Logger.getLogger(MaestroHandler.class.getCanonicalName());

	private DeviceHandle deviceHandle;
	private static Context context;

	private List<DisconnectionListener> disconnectionListeners = new ArrayList<>();

	public void addDisconnectionListener(DisconnectionListener listener)
	{
		if (!disconnectionListeners.contains(listener))
		{
			LOGGER.info("Disconnection Listener Registered - " + listener.toString());
			disconnectionListeners.add(listener);
		}
	}

	private ScheduledExecutorService pollingService;

	private class DevicePoller implements Runnable
	{
		@Override
		public void run()
		{
			if (findDevice() == null)
			{
				deviceHandle=null;
				LOGGER.info(LocalTime.now().toString() + " Connection Lost. Notifying listeners.");
				for (DisconnectionListener listener : disconnectionListeners)
				{
					try
					{
						listener.notifyOfDisconnection();
					}
					catch (Exception e)
					{
						LOGGER.log(Level.WARNING, "Error notifying listeners ", e);
					}
				}
				LOGGER.info(LocalTime.now().toString() + " Listeners Notified.");
				releaseConnection();
			}
		}
	}

	/**
	 * <p>
	 * Should be used by all clients after calling getInstance(). If a
	 * connection has already been created and the device is still available,
	 * nothing changes.
	 * <p>
	 * <p>
	 * Once a connection is created, a period check is initiated to see if the
	 * device is still connected. If not:
	 * <ol>
	 * <li>All disconnectionListeners are notified</li>
	 * <li>The connection is released</li>
	 * <li>The period check is cancelled</li>
	 * </ol>
	 * </p>
	 * 
	 * @throws UnknownConnectionFailureException
	 *             If something unknown goes wrong
	 * @throws DeviceNotFoundException
	 * @throws DeviceInUseException
	 *             If another process is using the device <i>or</i> the
	 *             JVM/Server does not have access to the device (check your USB
	 *             permissions)
	 */
	public void establishConnection() throws UnknownConnectionFailureException, DeviceNotFoundException, DeviceInUseException
	{
		if (deviceHandle != null)
		{
			return;
		}
		else
		{
			verifyDeviceReady();
			try
			{
				deviceHandle = LibUsb.openDeviceWithVidPid(context, MaestroHardwareSignature.VENDOR_ID.getNumber(),
						MaestroHardwareSignature.PRODUCT_ID.getNumber());
				pollingService = Executors.newScheduledThreadPool(1);
				pollingService.scheduleAtFixedRate(new DevicePoller(), 0, 1, TimeUnit.SECONDS);
			}
			catch (Exception e) // Wanna catch run-time exceptions as a
								// UnknownConnectionFailureException, to
								// force compile-time checks
			{
				LOGGER.log(Level.SEVERE, e.getMessage());
				throw new UnknownConnectionFailureException();
			}
		}
	}

	private void verifyDeviceReady() throws DeviceNotFoundException, DeviceInUseException
	{
		Device device = findDevice();
		if (device == null)
		{
			throw new DeviceNotFoundException();
		}
		if (!isDeviceAvailable(device))
		{
			deviceHandle = null;
			throw new DeviceInUseException();
		}
	}

	private boolean isDeviceAvailable(Device d)
	{
		DeviceHandle dh = LibUsb.openDeviceWithVidPid(context, MaestroHardwareSignature.VENDOR_ID.getNumber(),
				MaestroHardwareSignature.PRODUCT_ID.getNumber());
		if (dh != null)
		{
			LibUsb.close(dh);
		}
		return dh != null;
	}

	private Device findDevice()
	{
		Device result = null;
		DeviceList deviceList = new DeviceList();
		LibUsb.getDeviceList(null, deviceList);

		for (Device d : deviceList)
		{
			DeviceDescriptor descriptor = new DeviceDescriptor();
			LibUsb.getDeviceDescriptor(d, descriptor);
			if (descriptor.idVendor() == MaestroHardwareSignature.VENDOR_ID.getNumber()
					&& descriptor.idProduct() == MaestroHardwareSignature.PRODUCT_ID.getNumber())
			{
				result = d;
				break;
			}
		}
		return result;
	}

	/**
	 * Release the USB device for other processes, and cancel periodic checking.
	 */
	public void releaseConnection()
	{
		// futurePoll.cancel(true);
		pollingService.shutdown();
		if (deviceHandle != null)
		{
			try
			{
				LibUsb.close(deviceHandle); 
			}
			catch (Exception e)
			{
				LOGGER.warning("Error calling LibUSB.close() : " + e.toString());
			}
			deviceHandle = null;
			LOGGER.info("Connection Released");
		}

	}

	public void executeCommand(short servoIndex, MaestroCommandType commandType, short value)
	{

		ByteBuffer buffer = ByteBuffer.allocateDirect(0);
		LibUsb.controlTransfer(deviceHandle, (byte) 0x40, commandType.getByteValue(), value, servoIndex, buffer, 5000l);
	}

	public List<String> getDeviceDescriptions()
	{
		List<String> result = new ArrayList<>();
		DeviceList deviceList = new DeviceList();
		LibUsb.getDeviceList(null, deviceList);

		for (Device d : deviceList)
		{
			DeviceDescriptor descriptor = new DeviceDescriptor();
			LibUsb.getDeviceDescriptor(d, descriptor);
			result.add(descriptor.toString());
		}
		return result;
	}
}
