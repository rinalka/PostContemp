package postContemp;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;


public class Stroke {

	Brush thisStrokesBrush;
	
	PVector startStrokePos;
	PVector currentStrokePos;
	PVector strokeVelocity;
	PVector strokeAcceleration;
	
	int tarketStrokeLength;
	int currentStrokeLength;
	Boolean endStroke;


	Stroke(PApplet p, PImage thisStrokesBrushFile){
		setStartStrokePosition(p);
		currentStrokePos = startStrokePos;
		endStroke = false;
		tarketStrokeLength = 0;
		strokeVelocity = new PVector (p.random(-1,1),p.random(-1,1));
		strokeVelocity.mult(global.bW/2);
		thisStrokesBrush = new Brush(p,thisStrokesBrushFile,startStrokePos);	
		thisStrokesBrush.loadBrush(p);
	}
	
	void setStartStrokePosition(PApplet p) {
		startStrokePos = new PVector (p.random(global.picWidth-global.bW),p.random(global.picHeight-global.bH));
	}
	
	void updateStroke(PApplet p){
		
		processBrush(p);//process the brush at the current location of the stroke
		moveStroke(); //move the stroke to it's next location
		endStroke(); //check to see if the stroke is to be ended
		
	}
	
	void processBrush(PApplet p) {
		thisStrokesBrush.rotateBrush();
		thisStrokesBrush.sizeBrush();
		thisStrokesBrush.tintBrush();
		thisStrokesBrush.moveBrush (p,currentStrokePos);
//		thisStrokesBrush.loadBrush(p);
		thisStrokesBrush.colorBrush(p);
		thisStrokesBrush.drawBrush (p);
	}
	
	void moveStroke() {
		currentStrokePos.add(strokeVelocity);
		endStroke(); //check to see if the stroke is to be ended
	}
		
	void endStroke () {
		tarketStrokeLength = (int) ControlFrame.getStrokeLength(); //get the tarket stroke length from the control panel
		strokeOnScreen();
		strokeAtTarketLenth();
	}

	void strokeOnScreen() {
		//check if stroke is still on the screen
		if ((currentStrokePos.x > global.picWidth) || (currentStrokePos.x < 0) ||
		   (currentStrokePos.y > global.picHeight) || (currentStrokePos.y < 0)) {
			endStroke = true;
		}
	}
		
	void strokeAtTarketLenth() {
		//check if stroke has met it's tarketed length
		currentStrokeLength = currentStrokeLength + global.rawBrush.width/2;
		if (currentStrokeLength >tarketStrokeLength ) 
			endStroke = true;
	}
		
}//end Stroke
	
	
