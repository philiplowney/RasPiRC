package service.maestro.exceptions;

public class UnknownConnectionFailureException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	@Override
	public String getMessage()
	{
		return "An unknown error was encountered when connecting to the device";
	}
}
