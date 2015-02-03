package ui;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import lombok.Getter;
import service.maestro.MaestroHandler;

@ManagedBean
@ViewScoped
public class DeviceLogUI
{
	private final MaestroHandler maestroHandler = MaestroHandler.getInstance();
	
	@Getter
	private List<String> deviceDescriptionList;
	
	@PostConstruct
	public void init()
	{
		deviceDescriptionList = maestroHandler.getDeviceDescriptions();
	}
}
