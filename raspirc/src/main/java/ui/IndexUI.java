package ui;


import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@ViewScoped
public class IndexUI
{
	
	private static final Logger LOGGER = Logger.getLogger(IndexUI.class.getCanonicalName());
	public void clickButton()
	{
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "button clicked", null));
		LOGGER.info("Button clicked!");
	}
}
