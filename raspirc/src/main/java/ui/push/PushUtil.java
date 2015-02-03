package ui.push;

import javax.faces.application.FacesMessage;

import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

public class PushUtil
{
	public static void pushMessage(String channel, String messageHeading, String messageDetail)
	{
		EventBus eventBus = EventBusFactory.getDefault().eventBus();
        eventBus.publish(channel, new FacesMessage(messageHeading, messageDetail));
	}
}
