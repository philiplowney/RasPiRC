package service.calibration;

import java.util.logging.Logger;

import model.calibration.VehicleCalibration;

public class CalibrationService
{
	private static final VehicleCalibration DEFAULT_CALIBRATION = new VehicleCalibration(6000, 5500, 6500, 6000, 4000, 8000, 1, 2, true);

	private static CalibrationService instance;

	protected CalibrationService()
	{
	}

	public static CalibrationService getInstance()
	{
		if (instance == null)
		{
			instance = new CalibrationService();
		}
		return instance;
	}

	private static final Logger LOGGER = Logger.getLogger(CalibrationService.class.getCanonicalName());

	public VehicleCalibration fetchDefaults()
	{
		return DEFAULT_CALIBRATION;
	}

	public VehicleCalibration fetchCurrent()
	{
		LOGGER.warning("FetchCurrent isnt implemented yet, returning default instead");
		return fetchDefaults();
	}
}