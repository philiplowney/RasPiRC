package service.application;

import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import service.vehicle.VehicleService;
import service.vehicle.VehicleServiceImpl;
import service.vehicle.exceptions.VehicleUnavailableException;
import constants.vehicle.SteeringDirection;

@WebListener
public class ApplicationStarter implements ServletContextListener
{
	private static final Logger LOGGER = Logger.getLogger(ApplicationStarter.class.getCanonicalName());

	@Override
	public void contextDestroyed(ServletContextEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0)
	{
		LOGGER.info("Application has started");
		LOGGER.info("Will attempt to start vehicle...");
		try
		{
			VehicleService vehicleService = VehicleServiceImpl.getInstance();
			LOGGER.info("Vehicle ready, will calibrate");
			vehicleService.idleThrottle();
			vehicleService.steerStraightAhead();
			LOGGER.info("Calibrated throttle & steering. Will now wiggle wheels..");
			try
			{
				Thread.sleep(300);// give the speed control a chance to know the throttle's idled and turn on
				vehicleService.steer(SteeringDirection.LEFT, 50);
				Thread.sleep(300);
				vehicleService.steer(SteeringDirection.RIGHT, 50);
				Thread.sleep(300);
				vehicleService.steer(SteeringDirection.LEFT, 50);
				Thread.sleep(300);
				vehicleService.steer(SteeringDirection.RIGHT, 50);
				Thread.sleep(300);
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			vehicleService.steerStraightAhead();
		}
		catch (VehicleUnavailableException e)
		{
			LOGGER.severe("Vehicle unavailable");
		}
	}
}
