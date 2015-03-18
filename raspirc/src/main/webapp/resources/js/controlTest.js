var COLOR_GREY_TEXT = "#f0f0f0";
var COLOR_GREEN_TEXT = "green";
var COLOR_RED_TEXT = "red";
var COLOR_BLUE_TEXT = "blue";

var lastKeyDown = 0;

var KEYCODE_LEFT = 37;
var KEYCODE_RIGHT = 39;
var KEYCODE_UP = 38;
var KEYCODE_DOWN = 40;

window.onload = function()
{
	colorThrottle(COLOR_GREY_TEXT, COLOR_GREEN_TEXT, COLOR_GREY_TEXT);
};
document.addEventListener('keydown',
		function(e)
		{
			if (e.keyCode != lastKeyDown)
			{
				switch (e.keyCode)
				{
					case KEYCODE_LEFT:
						goLeft();
						rotateWheel(0, -70);
						break;
					case KEYCODE_RIGHT:
						goRight();
						rotateWheel(0, 70);
						break;
					case KEYCODE_UP:
						goForward();
						colorThrottle(COLOR_RED_TEXT, COLOR_GREY_TEXT,
								COLOR_GREY_TEXT);
						break;
					case KEYCODE_DOWN:
						goReverse();
						colorThrottle(COLOR_GREY_TEXT, COLOR_GREY_TEXT,
								COLOR_BLUE_TEXT);
						break;
				}
			}
			lastKeyDown = e.keyCode;
		}, false);

function colorThrottle(forwardColor, idleColor, reverseColor)
{
	d3.select("#forward.throttleText").style("color", forwardColor);
	d3.select("#idle.throttleText").style("color", idleColor);
	d3.select("#reverse.throttleText").style("color", reverseColor);
}

document.addEventListener('keyup', function(e)
{
	if (e.keyCode === KEYCODE_LEFT || e.keyCode === KEYCODE_RIGHT)
	{
		goStraight();
		fromAngle = e.keyCode === KEYCODE_LEFT ? -70 : 70;
		rotateWheel(fromAngle, 0);
	}
	else if (e.keyCode === KEYCODE_UP || e.keyCode === KEYCODE_DOWN)
	{
		goIdle();
		colorThrottle(COLOR_GREY_TEXT, COLOR_GREEN_TEXT, COLOR_GREY_TEXT);
	}
	lastKeyDown = 0;
}, false);

function rotateWheel(currentAngle, targetAngle)
{
	d3.select("path").transition().attrTween(
			"transform",
			function()
			{
				return d3.interpolateString("rotate(" + currentAngle + ",  "
						+ (464 * 0.3) + "," + (500 * 0.3) + ") scale(0.3,0.3)",
						"rotate(" + targetAngle + ", " + (464 * 0.3) + ","
								+ (500 * 0.3) + ") scale(0.3,0.3)");
			});
}