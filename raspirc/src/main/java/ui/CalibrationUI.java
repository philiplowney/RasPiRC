package ui;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import constants.commands.MaestroCommandType;
import lombok.Getter;
import lombok.Setter;
import model.calibration.VehicleCalibration;
import service.calibration.CalibrationService;
import service.maestro.MaestroHandler;
import service.maestro.exceptions.DeviceInUseException;
import service.maestro.exceptions.DeviceNotFoundException;
import service.maestro.exceptions.UnknownConnectionFailureException;

@ManagedBean
@ViewScoped
public class CalibrationUI
{
	private static final int PAUSE_PERIOD = 1000;

	private static final String ERROR_CONNECTING_TO_DEVICE = "Error connecting to device";

	private static final Logger LOGGER = Logger.getLogger(CalibrationUI.class.getCanonicalName());

	@Getter
	@Setter
	private VehicleCalibration calibration;

	private CalibrationService calibrationService = CalibrationService.getInstance();

	private MaestroHandler maestroHandler = MaestroHandler.getInstance();

	public void clickSetSteering()
	{
		maestroHandler.executeCommand((short)calibration.getSteeringServoIndex(), MaestroCommandType.SET_TARGET, (short)calibration.getSteeringCentre());
	}
	
	public void clickSetThrottle()
	{
		maestroHandler.executeCommand((short)calibration.getThrottleServoIndex(), MaestroCommandType.SET_TARGET, (short)calibration.getThrottleIdle());
	}
	
	public void clickTryLeft() throws InterruptedException
	{
		maestroHandler.executeCommand((short)calibration.getSteeringServoIndex(), MaestroCommandType.SET_TARGET, (short)calibration.getFullLeft());
		Thread.sleep(PAUSE_PERIOD);
		maestroHandler.executeCommand((short)calibration.getSteeringServoIndex(), MaestroCommandType.SET_TARGET, (short)calibration.getSteeringCentre());
	}
	public void clickTryRight() throws InterruptedException
	{
		maestroHandler.executeCommand((short)calibration.getSteeringServoIndex(), MaestroCommandType.SET_TARGET, (short)calibration.getFullRight());
		Thread.sleep(PAUSE_PERIOD);
		maestroHandler.executeCommand((short)calibration.getSteeringServoIndex(), MaestroCommandType.SET_TARGET, (short)calibration.getSteeringCentre());
	}
	
	public void clickTryForward() throws InterruptedException
	{
		maestroHandler.executeCommand((short)calibration.getThrottleServoIndex(), MaestroCommandType.SET_TARGET, (short)calibration.getFullForward());
		Thread.sleep(PAUSE_PERIOD);
		maestroHandler.executeCommand((short)calibration.getThrottleServoIndex(), MaestroCommandType.SET_TARGET, (short)calibration.getThrottleIdle());
	}
	
	public void clickTryReverse() throws InterruptedException
	{
		if(calibration.isUseDoubleBackForReverse())
		{
			maestroHandler.executeCommand((short)calibration.getThrottleServoIndex(), MaestroCommandType.SET_TARGET, (short)calibration.getFullReverse());
			Thread.sleep(50);
			maestroHandler.executeCommand((short)calibration.getThrottleServoIndex(), MaestroCommandType.SET_TARGET, (short)calibration.getThrottleIdle());
			Thread.sleep(50);
		}
		maestroHandler.executeCommand((short)calibration.getThrottleServoIndex(), MaestroCommandType.SET_TARGET, (short)calibration.getFullReverse());
		Thread.sleep(PAUSE_PERIOD);
		maestroHandler.executeCommand((short)calibration.getThrottleServoIndex(), MaestroCommandType.SET_TARGET, (short)calibration.getThrottleIdle());
	}
	
	@PostConstruct
	public void init()
	{
		try
		{
			maestroHandler.establishConnection();
		} catch (UnknownConnectionFailureException | DeviceNotFoundException | DeviceInUseException e)
		{
			LOGGER.log(Level.WARNING, ERROR_CONNECTING_TO_DEVICE, e);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Device not found", "Error: 0"+e.getMessage()));
		}
		LOGGER.info("Loading current vehicle calibrations...");
		calibration = calibrationService.fetchCurrentFromFileSystem();
		if(calibration == null)
		{
			LOGGER.info("No calibration found, loading default instead...");
			calibration = calibrationService.fetchDefaults();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No Calibration Found", "Using defaults instead"));
		}
		else{
			LOGGER.info("Calibration loaded");
		}
		clickSetSteering();
		clickSetThrottle();
	}
	
	@PreDestroy
	public void tidyUp()
	{
		maestroHandler.releaseConnection();
	}

	public void save()
	{
		if (!calibration.equals(calibrationService.fetchCurrentFromFileSystem()))
		{
			try
			{
				calibrationService.saveNewCalibration(calibration);
			} catch (IOException e)
			{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error saving calibration", null));
				LOGGER.log(Level.SEVERE, "Error saving calibration", e);
			}
			LOGGER.info("New Calibration Saved");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("New Calibration Saved", "Be careful when driving with these new settings initially!"));
		} else
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No Changes to Save", " "));
		}
	}

	public void restoreDefaults()
	{
		calibration = calibrationService.fetchDefaults();
		try
		{
			calibrationService.saveNewCalibration(calibration);
			LOGGER.info("Defaults restored: " + calibration.toString());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Defaults Restored", "Please check these calibrations"));
		} catch (IOException e)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error saving calibration", null));
			LOGGER.log(Level.SEVERE, "Error saving calibration", e);
		}
	}

	public void cancelChanges()
	{
		calibration = calibrationService.fetchCurrentFromFileSystem();
		LOGGER.info("Changes cancelled");
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Changes Cancelled"));
	}
	
}