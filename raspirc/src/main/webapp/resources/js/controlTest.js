
	var lastKeyDown = 0;
	var KEYCODE_LEFT = 37;
	var KEYCODE_RIGHT = 39;
	
	document.addEventListener('keydown', function(e) {
		if (e.keyCode == lastKeyDown) {
			return;
		} else if (e.keyCode === KEYCODE_LEFT) {
			rotateWheel(0,-70);
			goLeft();
		} else if (e.keyCode === KEYCODE_RIGHT) {
			rotateWheel(0,70);
			goRight();
		} else if (e.keyCode === 38) {
			goForward();
		} else if (e.keyCode === 40) {
			goReverse();
		}
		lastKeyDown = e.keyCode;
	}, false);

	document.addEventListener('keyup', function(e) {
		if (e.keyCode === KEYCODE_LEFT || e.keyCode === KEYCODE_RIGHT) {
			switch(e.keyCode)
			{
				case KEYCODE_LEFT:
					rotateWheel(-70, 0);
					break;
				case KEYCODE_RIGHT:
					rotateWheel(70, 0);
					break;
			}
			goStraight();
		} else if (e.keyCode === 38 || e.keyCode === 40) {
			goIdle();
		}
		lastKeyDown = 0;
	}, false);
	
	function rotateWheel(currentAngle, targetAngle)
	{
		d3.select("path").transition().attrTween("transform", function() {
		      return d3.interpolateString("rotate("+currentAngle+")", "rotate("+targetAngle+", 464,500)");
	    });
	}