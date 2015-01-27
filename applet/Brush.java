package postContemp;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public class Brush {

	PImage rawBrush;		//the original brush loaded in setup - unaltered
	PImage currentBrush;    // the current state of the brush - depending on.....
	PImage tempBrush;
	PImage storedBrush;
	
	PGraphics brush;

	

	int brushW;
	int brushH;
	int brushS;
	int locX;
	int locY;
	int scaleh;
	int scalew;
	float brushAngle;
	int redTint,greTint,bluTint,alpTint;
	int once = 0;
	
	Brush (PApplet p, PImage _rawBrush, PVector startLoc) {
		
		rawBrush = _rawBrush;
		currentBrush = rawBrush;
	 	brushW = rawBrush.width;
	 	brushH = rawBrush.height;
	 	brushS = brushW*brushH;
	 	moveBrush(p,startLoc);	//move brush for the first time
	}
	
	

	
	void drawBrush (PApplet p){

		global.myCanvas.imageMode(p.CENTER);
		global.myCanvas.pushMatrix();
			global.myCanvas.translate(locX,locY);
			global.myCanvas.rotate (brushAngle);
			global.myCanvas.tint(redTint,greTint,bluTint,alpTint);
			global.myCanvas.image(currentBrush,0,0,scalew,scaleh);//
			global.myCanvas.removeCache(currentBrush); //****** delete from memory manually or CRASH because of Java heap
		global.myCanvas.popMatrix();
		global.myCanvas.imageMode(p.CORNER);
			

	}
	
	void moveBrush(PApplet p, PVector pos) {
		locX = (int) pos.x; 
		locY = (int) pos.y; 
	}
	
	void sizeBrush() {
		scalew =	(int) ControlFrame.getScaleX() * brushW;
		scaleh =    (int) ControlFrame.getScaleY() * brushH;
	}
	
	void tintBrush() {
		redTint = (int) ControlFrame.getTintR();//
		greTint = (int) ControlFrame.getTintG();
		bluTint = (int) ControlFrame.getTintB();
		alpTint= (int) ControlFrame.getAlph();

	}
	
	void rotateBrush() {
//		brushAngle = (float) (brushAngle+.02);
//		brushAngle = ControlFrame.getBrushAngle();
	}
	

	void loadBrush(PApplet p) {

		tempBrush = global.fmap.get(locX,locY,brushW,brushH);

		tempBrush.mask(rawBrush);
		currentBrush = tempBrush;
	}

	void colorBrush(PApplet p) {
//		p.println(tempBrush.pixels.length+"  "+global.bW*global.bH);
		for (int i=0; i< global.bW*global.bH; i++) {
	
		int red = (int) p.red(tempBrush.pixels[i]);
//		int gre = (int) p.green(tempBrush.pixels[i]);
//		int blu = (int) p.blue(tempBrush.pixels[i]);
//		int alpha = (int) p.alpha(global.rawBrush.pixels[i]);
//		int color = p.color(red,gre,blu,alpha);
//		tempBrush.pixels[i]=color;
	}
//		tempBrush.blend(rawBrush, 0, 0, brushW,brushH, 0, 0,brushW,brushH, p.BLEND); 
//    	tempBrush.mask(rawBrush);
		currentBrush = tempBrush;

	}
}
