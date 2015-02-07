package ui;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import service.calibration.CalibrationService;
import service.maestro.MaestroHandler;
import lombok.Getter;
import lombok.Setter;
import model.calibration.VehicleCalibration;

@ManagedBean
@ViewScoped
public class CalibrationUI
{
	private static final Logger LOGGER = Logger.getLogger(CalibrationUI.class.getCanonicalName());

	@Getter
	@Setter
	private VehicleCalibration calibration;
	
	private CalibrationService calibrationService = CalibrationService.getInstance();
	private MaestroHandler maestroHandler = MaestroHandler.getInstance();
	
	@PostConstruct
	public void init()
	{
		LOGGER.fine("Loading current vehicle calibrations...");
		calibration = calibrationService.fetchCurrent();
	}
	
	public void trySteeringCentre()
	{
		
	}
}