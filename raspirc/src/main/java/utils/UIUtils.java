package utils;

public class UIUtils
{
	public static String determineBaseURLFromFullURL(String fullURL)
	{
		String[] sections = fullURL.split(":");
		if(sections[0].equalsIgnoreCase("http"))
		{
			return sections[0]+":"+sections[1];
		}
		else
		{
			return sections[0];
		}
	}
}
