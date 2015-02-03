package ui.push;

import javax.faces.application.FacesMessage;
import org.primefaces.push.annotation.OnMessage;
import org.primefaces.push.annotation.PushEndpoint;
import org.primefaces.push.impl.JSONEncoder;

/**
 * 
 * @author Primefaces copy+paste from
 *         http://www.primefaces.org/showcase/push/notify.xhtml
 * 
 */
@PushEndpoint(NotifyResource.CHANNEL)
public class NotifyResource
{
	public static final String CHANNEL = "/servoTest";
	@OnMessage(encoders =
	{ JSONEncoder.class })
	public FacesMessage onMessage(FacesMessage message)
	{
		return message;
	}

}