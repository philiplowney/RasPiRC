package service.maestro.exceptions;

@SuppressWarnings("serial")
public class DeviceInUseException extends Exception
{
	@Override
	public String getMessage()
	{
		return "The device is in use by another process";
	}
}
