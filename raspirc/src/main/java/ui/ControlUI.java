package ui;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import service.vehicle.VehicleService;
import service.vehicle.VehicleServiceImpl;
import utils.UIUtils;
import constants.vehicle.SteeringDirection;
import constants.vehicle.ThrottleDirection;

@ManagedBean
@ViewScoped
public class ControlUI
{
	private static final Logger LOGGER = Logger.getLogger(ControlUI.class.getCanonicalName());
	private VehicleService vehicleService;
	
	public String getIPAddress()
	{
		HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		StringBuffer fullRequestURL = origRequest.getRequestURL();
		return UIUtils.determineBaseURLFromFullURL(fullRequestURL.toString());
	}
	public void setTestString(String testString)
	{
		LOGGER.info("Test string is being set to: "+testString);
	}
	
	//@PostConstruct
	public void init()
	{
		try
		{
			vehicleService = VehicleServiceImpl.getInstance();
		}
		catch (Exception e)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Vehicle is unavailable", "Could not access hardware"));
			LOGGER.log(Level.SEVERE, "Unable to access vehicle", e);
		}
	}
	public void clickLeft(int percent)
	{
		if(vehicleService==null)
		{
			return;
		}
		LOGGER.info("LEFT");
		vehicleService.steer(SteeringDirection.LEFT, percent);
	}
	public void clickRight(int percent)
	{
		if(vehicleService==null)
		{
			return;
		}
		LOGGER.info("RIGHT");
		vehicleService.steer(SteeringDirection.RIGHT, percent);
	}
	public void clickStraight()
	{
		if(vehicleService==null)
		{
			return;
		}
		LOGGER.info("STRAIGHT");
		vehicleService.steerStraightAhead();
	}
	public void clickForward(int percent)
	{
		if(vehicleService==null)
		{
			return;
		}
		LOGGER.info("FORWARD");
		vehicleService.applyThrottle(ThrottleDirection.FORWARD, percent);
	}
	public void clickReverse(int percent)
	{
		if(vehicleService==null)
		{
			return;
		}
		LOGGER.info("REVERSE");
		vehicleService.applyThrottle(ThrottleDirection.REVERSE, percent);
	}
	public void clickIdle()
	{
		if(vehicleService==null)
		{
			return;
		}
		LOGGER.info("IDLE");
		vehicleService.idleThrottle();
	}
}
