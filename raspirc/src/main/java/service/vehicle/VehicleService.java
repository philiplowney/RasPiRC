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

public class VehicleService
{
	private static final Logger LOGGER = Logger.getLogger(VehicleService.class.getCanonicalName());

	private static VehicleService instance;

	public static VehicleService getInstance()
	{
		if (instance == null)
		{
			instance = new VehicleService();
		}
		return instance;
	}

	private CalibrationService calibrationService;
	private MaestroHandler maestroHandler;

	protected VehicleService()
	{
		calibrationService = CalibrationService.getInstance();
		maestroHandler = MaestroHandler.getInstance();
		try
		{
			maestroHandler.establishConnection();
		} catch (UnknownConnectionFailureException | DeviceNotFoundException | DeviceInUseException e)
		{
			LOGGER.log(Level.WARNING, "Error connecting", e);
		}
	}

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

	public void steerStraightAhead()
	{
		VehicleCalibration calibration = calibrationService.getCurrent();
		int servoIndex = calibration.getSteeringServoIndex();
		maestroHandler.executeCommand((short) servoIndex, MaestroCommandType.SET_TARGET, (short) calibration.getSteeringCentre());
	}

	public void applyThrottle(ThrottleDirection direction, int percent)
	{
		VehicleCalibration calibration = calibrationService.getCurrent();
		short targetValue = determineTargetValue(percent, calibration.getFullValue(direction), calibration.getThrottleIdle());
		if(direction==ThrottleDirection.REVERSE && calibration.isUseDoubleBackForReverse())
		{
			maestroHandler.executeCommand((short) calibration.getThrottleServoIndex(), MaestroCommandType.SET_TARGET, (short) targetValue);
			try
			{
				Thread.sleep(50);
			}
			catch (InterruptedException e)
			{
				LOGGER.log(Level.SEVERE, "Unable to pause for reverse", e);
			}
			maestroHandler.executeCommand((short) calibration.getThrottleServoIndex(), MaestroCommandType.SET_TARGET, (short) calibration.getThrottleIdle());
			try
			{
				Thread.sleep(50);
			}
			catch (InterruptedException e)
			{
				LOGGER.log(Level.SEVERE, "Unable to pause for reverse", e);
			}
		}
		maestroHandler.executeCommand((short) calibration.getThrottleServoIndex(), MaestroCommandType.SET_TARGET, (short) targetValue);
	}

	public void idleThrottle()
	{
		VehicleCalibration calibration = calibrationService.getCurrent();
		maestroHandler.executeCommand((short) calibration.getThrottleServoIndex(), MaestroCommandType.SET_TARGET, (short) calibration.getThrottleIdle());
	}
}
