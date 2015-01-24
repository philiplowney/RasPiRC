package musingsofabuilder.raspirc.ui;


import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class IndexUI
{
	private static final Logger LOGGER = Logger.getLogger(IndexUI.class.getCanonicalName());
	public void clickButton()
	{
		LOGGER.info("Button clicked!");
	}
}
