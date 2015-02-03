package constants.commands;

public enum MaestroCommandType
{
	SET_TARGET((byte)0x85);
	
	byte byteValue;
	
	MaestroCommandType(byte val)
	{
		byteValue = val;
	}
	
	public byte getByteValue()
	{
		return byteValue;
	}
}
