package service.vehicle;

import constants.vehicle.SteeringDirection;
import constants.vehicle.ThrottleDirection;

public interface VehicleService
{

	void steer(SteeringDirection direction, int percent);

	void steerStraightAhead();

	void idleThrottle();

	void applyThrottle(ThrottleDirection direction, int percent);

}
