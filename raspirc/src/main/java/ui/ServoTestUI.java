package ui;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import lombok.Getter;
import lombok.Setter;

import org.primefaces.event.SlideEndEvent;

import service.maestro.DisconnectionListener;
import service.maestro.MaestroHandler;
import constants.commands.MaestroCommandType;

@ManagedBean
@ViewScoped
public class ServoTestUI implements DisconnectionListener
{
	private Logger LOGGER = Logger.getLogger(ServoTestUI.class.getCanonicalName());
	private final MaestroHandler maestroHandler = MaestroHandler.getInstance();

	@Getter
	private int[] servoIndices =
	{ 1, 2, 3, 4, 5, 6 };

	@Getter
	@Setter
	private int chosenServo = servoIndices[0];

	@Getter
	public int chosenValue;

	public void onSlideEnd(SlideEndEvent event)
	{
		chosenValue=(int)event.getValue();
		LOGGER.info("New servo value chosen. Servo: "+chosenServo+", value: "+chosenValue);
		if(connected == true)
		{
			maestroHandler.executeCommand((short)chosenServo, MaestroCommandType.SET_TARGET, (short)chosenValue);
		}
		else
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to execute command", "Connection not active - please check device"));
		}
	}

	@PostConstruct
	public void init()
	{
		try
		{ 
			maestroHandler.establishConnection();
			maestroHandler.addDisconnectionListener(this);
			String msg = "Connection successful";
			LOGGER.info(msg);
			connected = true;
		}
		catch (Exception e)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Device Unavailable", "Connection not active - please check device"));
			LOGGER.warning(e.getMessage());
		}
	}
	
	private boolean connected = false;
	
	@PreDestroy
	public void release()
	{
		maestroHandler.releaseConnection();
		connected = false;
	}

	@Override
	public void notifyOfDisconnection()
	{
		String errorMessage = "The connection to the device was lost.";
		LOGGER.warning(errorMessage);
		connected = false;
	}
}
