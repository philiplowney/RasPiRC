package service.vehicle;

import java.util.logging.Level;
import java.util.logging.Logger;

import model.calibration.VehicleCalibration;
import constants.commands.MaestroCommandType;
import constants.vehicle.SteeringDirection;
import constants.vehicle.ThrottleDirection;
import service.calibration.CalibrationService;
import service.maestro.MaestroHandler;
import service.maestro.exceptions.DeviceInUseException;
import service.maestro.exceptions.DeviceNotFoundException;
import service.maestro.exceptions.UnknownConnectionFailureException;
import service.vehicle.exceptions.VehicleUnavailableException;

public class VehicleServiceImpl implements VehicleService
{
	private static final Logger LOGGER = Logger.getLogger(VehicleServiceImpl.class.getCanonicalName());

	private static VehicleServiceImpl instance;
	private ThrottleDirection lastThrottleDirection = null;

	public static VehicleServiceImpl getInstance() throws VehicleUnavailableException
	{
		if (instance == null)
		{
			instance = new VehicleServiceImpl();
		}
		return instance;
	}

	private CalibrationService calibrationService;
	private MaestroHandler maestroHandler;

	protected VehicleServiceImpl() throws VehicleUnavailableException
	{
		calibrationService = CalibrationService.getInstance();
		maestroHandler = MaestroHandler.getInstance();
		try
		{
			maestroHandler.establishConnection();
		}
		catch (UnknownConnectionFailureException | DeviceNotFoundException | DeviceInUseException e)
		{
			LOGGER.log(Level.WARNING, "Error connecting", e);
			throw new VehicleUnavailableException();
		}
	}

	@Override
	public void steer(SteeringDirection direction, int percent)
	{
		VehicleCalibration calibration = calibrationService.getCurrent();
		short targetValue = determineTargetValue(percent, calibration.getFullValue(direction), calibration.getSteeringCentre());
		maestroHandler.executeCommand((short) calibration.getSteeringServoIndex(), MaestroCommandType.SET_TARGET, (short) targetValue);
	}

	private short determineTargetValue(int percent, long fullValue, long centreValue)
	{
		return (short) (((fullValue - centreValue) * ((float) percent / 100)) + centreValue);
	}

	@Override
	public void steerStraightAhead()
	{
		VehicleCalibration calibration = calibrationService.getCurrent();
		int servoIndex = calibration.getSteeringServoIndex();
		maestroHandler.executeCommand((short) servoIndex, MaestroCommandType.SET_TARGET, (short) calibration.getSteeringCentre());
	}

	@Override
	public void applyThrottle(ThrottleDirection direction, int percent)
	{
		VehicleCalibration calibration = calibrationService.getCurrent();
		short targetValue = determineTargetValue(percent, calibration.getFullValue(direction), calibration.getThrottleIdle());
		if (direction == ThrottleDirection.REVERSE && calibration.isUseDoubleBackForReverse() && lastThrottleDirection!=null && lastThrottleDirection==ThrottleDirection.FORWARD)
		{
			flickReverse(calibration, targetValue);
		}
		maestroHandler.executeCommand((short) calibration.getThrottleServoIndex(), MaestroCommandType.SET_TARGET, (short) targetValue);
		lastThrottleDirection = direction;
	}

	private void flickReverse(VehicleCalibration calibration, short targetValue)
	{
		int throttleServoIndex = calibration.getThrottleServoIndex();
		maestroHandler.executeCommand((short) throttleServoIndex, MaestroCommandType.SET_TARGET, (short) targetValue);
		try
		{
			Thread.sleep(50);
		}
		catch (InterruptedException e)
		{
			LOGGER.log(Level.SEVERE, "Unable to pause for reverse", e);
		}
		maestroHandler.executeCommand((short) throttleServoIndex, MaestroCommandType.SET_TARGET,
				(short) calibration.getThrottleIdle());
		try
		{
			Thread.sleep(50);
		}
		catch (InterruptedException e)
		{
			LOGGER.log(Level.SEVERE, "Unable to pause for reverse", e);
		}
	}

	@Override
	public void idleThrottle()
	{
		VehicleCalibration calibration = calibrationService.getCurrent();
		maestroHandler.executeCommand((short) calibration.getThrottleServoIndex(), MaestroCommandType.SET_TARGET,
				(short) calibration.getThrottleIdle());
	}
}
