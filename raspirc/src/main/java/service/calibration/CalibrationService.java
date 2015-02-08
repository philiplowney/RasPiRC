package service.calibration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;

import model.calibration.VehicleCalibration;

public class CalibrationService
{
	private static final String CALIBRATION_JSON_FILENAME = "calibration.json";

	private static final VehicleCalibration DEFAULT_CALIBRATION = new VehicleCalibration(6000, 5500, 6500, 6000, 4000, 8000, 1, 2, true);

	private static CalibrationService instance;
	
	private VehicleCalibration currentCalibration;

	protected CalibrationService()
	{
		currentCalibration = fetchCurrentFromFileSystem();
		if(currentCalibration == null)
		{
			currentCalibration = fetchDefaults();
		}
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
		return new VehicleCalibration(DEFAULT_CALIBRATION.getThrottleIdle(), DEFAULT_CALIBRATION.getFullReverse(), DEFAULT_CALIBRATION.getFullForward(),
				DEFAULT_CALIBRATION.getSteeringCentre(), DEFAULT_CALIBRATION.getFullLeft(), DEFAULT_CALIBRATION.getFullRight(), DEFAULT_CALIBRATION.getThrottleServoIndex(),
				DEFAULT_CALIBRATION.getSteeringServoIndex(), DEFAULT_CALIBRATION.isUseDoubleBackForReverse());
	}

	public VehicleCalibration getCurrent()
	{
		return currentCalibration;
	}
	
	/**
	 * 
	 * @return Null if none currently saved, otherwise the current calibration from the filesystem.
	 * @throws FileNotFoundException
	 */
	public VehicleCalibration fetchCurrentFromFileSystem()
	{
		File file = new File(CALIBRATION_JSON_FILENAME);
		if(!file.exists())
		{
			return null;
		}
		else
		{
			FileReader fr;
			try
			{
				fr = new FileReader(file);
				Gson gson = new Gson();
				VehicleCalibration result = gson.fromJson(fr, VehicleCalibration.class);
				currentCalibration = result;
				fr.close();
				return result;
			} catch (IOException e)
			{
				LOGGER.log(Level.SEVERE, "Cannont create file reader", e);
				return null;
			}
		}
	}

	/**
	 * Persist a new calibration object to the filesystem.
	 * @param newCalibration
	 * @throws IOException
	 */
	public void saveNewCalibration(VehicleCalibration newCalibration) throws IOException
	{
		File file = new File(CALIBRATION_JSON_FILENAME);
		if(file.exists())
		{
			file.delete();
		}
		Gson gson = new Gson();
		String fileContent = gson.toJson(newCalibration);
		FileWriter fw = new FileWriter(file);
		fw.write(fileContent);
		fw.close();
		currentCalibration = newCalibration;
		LOGGER.info("Calibration saved to: "+file.getAbsolutePath()+file.getName());
	}
}