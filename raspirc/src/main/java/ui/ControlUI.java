package ui;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.mockito.Mockito;

import service.vehicle.VehicleService;
import constants.vehicle.SteeringDirection;
import constants.vehicle.ThrottleDirection;

@ManagedBean
@ViewScoped
public class ControlUI
{
	private static final Logger LOGGER = Logger.getLogger(ControlUI.class.getCanonicalName());
	private VehicleService vehicleService;
	

	public void setTestString(String testString)
	{
		LOGGER.info("Test string is being set to: "+testString);
	}
	
	@PostConstruct
	public void init()
	{
		vehicleService = Mockito.mock(VehicleService.class);
		//vehicleService = VehicleService.getInstance();
	}
	public void clickLeft(int percent)
	{
		LOGGER.info("LEFT");
		vehicleService.steer(SteeringDirection.LEFT, percent);
	}
	public void clickRight(int percent)
	{
		LOGGER.info("RIGHT");
		vehicleService.steer(SteeringDirection.RIGHT, percent);
	}
	public void clickStraight()
	{
		LOGGER.info("STRAIGHT");
		vehicleService.steerStraightAhead();
	}
	public void clickForward(int percent)
	{
		LOGGER.info("FORWARD");
		vehicleService.applyThrottle(ThrottleDirection.FORWARD, percent);
	}
	public void clickReverse(int percent)
	{
		LOGGER.info("REVERSE");
		vehicleService.applyThrottle(ThrottleDirection.REVERSE, percent);
	}
	public void clickIdle()
	{
		LOGGER.info("IDLE");
		vehicleService.idleThrottle();
	}
}
