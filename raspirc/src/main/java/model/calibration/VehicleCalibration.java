package model.calibration;

import constants.vehicle.SteeringDirection;
import constants.vehicle.ThrottleDirection;
import lombok.Data;

@Data
public class VehicleCalibration
{
	public VehicleCalibration(long throttleIdle, long fullReverse, long fullForward, long steeringCentre, long fullLeft, long fullRight, int throttleServoIndex,
			int steeringServoIndex, boolean useDoubleBackForReverse)
	{
		super();
		this.throttleIdle = throttleIdle;
		this.fullReverse = fullReverse;
		this.fullForward = fullForward;
		this.steeringCentre = steeringCentre;
		this.fullLeft = fullLeft;
		this.fullRight = fullRight;
		this.throttleServoIndex = throttleServoIndex;
		this.steeringServoIndex = steeringServoIndex;
		this.useDoubleBackForReverse = useDoubleBackForReverse;
	}
	private long throttleIdle, fullReverse, fullForward, steeringCentre, fullLeft, fullRight;
	private int throttleServoIndex, steeringServoIndex;
	private boolean useDoubleBackForReverse;
	public long getFullValue(SteeringDirection direction)
	{
		long result = 0;
		switch(direction)
		{
			case LEFT:
				result = getFullLeft();
				break;
			case RIGHT:
				result = getFullRight();
				break;
		}
		return result;
	}
	public long getFullValue(ThrottleDirection direction)
	{
		long result = 0;
		switch(direction)
		{
			case FORWARD:
				result = getFullForward();
				break;
			case REVERSE:
				result = getFullReverse();
				break;		
		}
		return result;
	}
}