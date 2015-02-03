# RasPiRC
An Internet Remote-Control Car based on a Raspberry Pi &amp; 2-Channel R/C car &amp; A Pololu Maestro

![Overview Diagram](http://2.bp.blogspot.com/-butaei7-tR4/VMFlYPp8RfI/AAAAAAAAAKI/le_0sJZN-sc/s1600/big%2Bidea.png)
### Work in Progress
This is an evolving little creation, but I'm blogging most steps here: <http://musingsofabuilder.blogspot.ie/>

## Materials Needed
* A 2-Channel Remote Control Car, using Servos and/or ESC
* A Raspberry Pi
* A Servo Controller from Pololu - I got mine here: <https://www.pololu.com/product/1350>
* Some way to supply a steady five volts to the Pi - I'm using a buck-boost regulator, which I got here: <https://www.pololu.com/product/2574>

## Set up
Assuming you've it wired up, you need to install an app server on the Pi (I recommend Jetty 9), and place the war-file from this project in the webapps directory. Restart jetty and navigate to [Pi address]:8080/raspirc/

## Wiring Diagram
Here's one way to do it:
![](http://3.bp.blogspot.com/-1vmKdRUHSwc/VL__f6OqoCI/AAAAAAAAAJ4/g-nPGeaQzsc/s1600/pi%2Bconnected.png)
