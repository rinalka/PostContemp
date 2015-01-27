package postContemp;


import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

public class DisplayWindow {
	
	int hold_displayW ;
	int hold_displayH ;
	int displayW;
	int displayH;
	int screen_locx;              // coords and size of the screen display
	int screen_locy;
	int picW;
	int picH;
	PImage temp;     // temp storage for the part of the graphics buffer that will be displayed on the view panel

	
	DisplayWindow (int _picW, int _picH, float screensizefactor) {
		picW = _picW;
		picH = _picH;
		displayW = (int) (picW * screensizefactor);
		displayH = (int) (picH * screensizefactor);
		hold_displayW = displayW;
		hold_displayH = displayH;

	
		screen_locx = (picW /2) - (displayW); //start the screen view window in the center of the picture
		screen_locy = (picH /2) - (displayH);

	}
	
	 void update(PApplet p) {
		// copy a chunk from myCanvas and display it on the screen
		   temp = global.myCanvas.get(screen_locx,screen_locy,displayW,displayH);
		   p.g.image(temp,0,0,hold_displayW,hold_displayH);
		   p.g.removeCache(temp);
	}
	 
	 void zoomPosition( int keyCode) {
		 if (keyCode == PApplet.UP) {screen_locy = screen_locy - 10;}
		    if (keyCode == PApplet.DOWN) {screen_locy = screen_locy + 10;}
		    if (keyCode == PApplet.LEFT) {screen_locx = screen_locx - 10;}
		    if (keyCode == PApplet.RIGHT) {screen_locx = screen_locx + 10;}
		    if (screen_locx < 0) {screen_locx = 0;}
		    if (screen_locy <0) {screen_locy = 0;}
		    if (screen_locx+displayW > picW)  {screen_locx = picW - displayW; }
		    if (screen_locy+displayH > picH)  {screen_locy = picH - displayH;}
	 }
	 
	 void fullview() {
		 screen_locx = 0;
		 screen_locy = 0;
		 displayW = picW;//view_panel_width;
		 displayH = picH;//view_panel_height;	 
	 
	 }
	 
	 void halfview() {
		 screen_locx = ((picW /4) - (hold_displayW/4)); //start the screen view window in the center of the picture
		 screen_locy = ((picH /4)-  (hold_displayH/4));
		 displayW = picW/2;
		 displayH = picH/2;	 
	 
	 }
	 
	 void zoomview() {
		 screen_locx = ((picW /2) - (hold_displayW/2)); //start the screen view window in the center of the picture
		 screen_locy = ((picH /2)-  (hold_displayH/2));
		 displayW = hold_displayW;
		 displayH = hold_displayH;
	 }

}
	 



