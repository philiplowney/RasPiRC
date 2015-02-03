package constants.hardware;

public enum MaestroHardwareSignature
{
	PRODUCT_ID((short)137),
	VENDOR_ID((short)8187);
	
	private short num;
	
	MaestroHardwareSignature(short number)
	{
		num = number;
	}
	
	public short getNumber()
	{
		return num;
	}
}
