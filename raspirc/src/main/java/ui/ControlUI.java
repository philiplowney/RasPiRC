package ui;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import constants.vehicle.SteeringDirection;
import constants.vehicle.ThrottleDirection;
import service.vehicle.VehicleService;

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
		vehicleService = VehicleService.getInstance();
	}
	public void clickLeft(int percent)
	{
		vehicleService.steer(SteeringDirection.LEFT, percent);
	}
	public void clickRight(int percent)
	{
		vehicleService.steer(SteeringDirection.RIGHT, percent);
	}
	public void clickStraight()
	{
		vehicleService.steerStraightAhead();
	}
	public void clickForward(int percent)
	{
		vehicleService.applyThrottle(ThrottleDirection.FORWARD, percent);
	}
	public void cickReverse(int percent)
	{
		vehicleService.applyThrottle(ThrottleDirection.REVERSE, percent);
	}
}
