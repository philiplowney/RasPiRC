package service.maestro.exceptions;

@SuppressWarnings("serial")
public class DeviceNotFoundException extends Exception
{
	@Override
	public String getMessage()
	{
		return "Unable to locate device";
	}
}
