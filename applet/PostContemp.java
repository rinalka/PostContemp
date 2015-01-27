package postContemp;

import java.awt.Color;
import java.awt.Frame;
import java.util.ArrayList;

import javax.media.opengl.GL2;

import jogamp.opengl.x11.glx.GLX;
import controlP5.ControlP5;
import controlP5.Textlabel;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;


public class PostContemp extends PApplet {
	
	//where all the globals are!  i know, i know. 
	//currently includes 	the original picture, 
	//					 	myCanvas work is done in, 
	//						the fmap that controls flows ect, 
	//						and brushes
	global testglobal;
	
	// saved file with - a frame number will be added - it ends out in /bin
   	String savefile = "saved";		 
    int frame = 0;    //frame counter appended to the for saved files
    
    
  	//the display is a screen view into myCanvas as it is constructed
	DisplayWindow theDisplay;  
	float screensize_factor = (float) 0.3;//% the display window is of the picture being drawn
	
	//the panel with all the controls and its various variables
	//the variables are not in the globals file - but there are static getters for each
	ControlFrame cf1;
	String control_panel_name = "PostContemporary";
   	int control_panel_width = 550;
   	int control_panel_height = 600;
   	int control_panel_x = 3200 - control_panel_width - 100;
   	int control_panel_y = 100;
   	
 
 	
   	//temporary variables used for testing until classes and controls are finalized

	

	
	public void setup() {
		// set the source picture that is to be "postContemporarized"		
		global.thefile = "ben4.jpg";
		// and for the time being name the brush file
		global.brush = "newtest32brush.png";

		//now setup all the global variables
		testglobal = new global(this);	
		
		// set up the display window - which is probably smaller than the actual picture
		theDisplay = new DisplayWindow(global.picWidth,global.picHeight, screensize_factor);

        size((int) (global.picWidth*screensize_factor), (int) (global.picHeight*screensize_factor)); 												//should be P2D but it crashes!
		colorMode (RGB,255,255,255);	
		
		//setup the control panel
		cf1 = addControlFrame(control_panel_name, control_panel_width, control_panel_height,control_panel_x,control_panel_y );
		
		// set up the strokes
		for(int i=0; i<global.theStrokes.length; i++) {
			global.theStrokes[i] = new Stroke(this,global.rawBrush);
		}
		println("there should be a total of "+global.theStrokes.length+" strokes");

	
		theDisplay.fullview();
		
	}//end setup

	
	public void draw() {
	
		//update the screen view into myCanvas so we can see at least part what is going on 
		theDisplay.update(this); 
		
		//update all of the current strokes, if one stroke is finished restart it
		for(int i=0; i<global.theStrokes.length; i++) {
			global.theStrokes[i].updateStroke(this);
			if(global.theStrokes[i].endStroke == true) {
				global.theStrokes[i] = new Stroke(this,global.rawBrush);
			}
		}
		
		//display the frame rate
//		if (frameCount % 10 == 0) {println(frameRate+"    "+frameCount);}
	   	   
	}//end draw
	



public void keyPressed() {  // used to move the screen display around the graphics buffer - or save a version to a file
	if (key == CODED) {
		theDisplay.zoomPosition(keyCode);
		}
	switch(key) {
	   case 's' : 
		   	frame++;
		   	global.myCanvas.save("saved"+frame);
		  break;	
	   case 'f' : // set the screen view window to cover the entire picture
		    theDisplay.fullview();
		  break;
	   case 'h' : // set the screen view window to cover the entire picture
		    theDisplay.halfview();
		  break;
	   case 'z' : // reset screen view window to the center of the picture
		    theDisplay.zoomview();	
		    break;
	   case 'w' :
		   global.myCanvas.background(255); // set to white
		   break;
	   case 'b' :
		   global.myCanvas.background(0); // set to black
           break;
	}//end switch
}//end keypressed	

//Control Frame setup and stuff
	ControlFrame addControlFrame(String theName, int theWidth, int theHeight, int thex, int they) {
			  Frame f = new Frame(theName);
			  ControlFrame p = new ControlFrame(this, theWidth, theHeight);
			  f.add(p);
			  p.init();
			  f.setTitle(theName);
			  f.setSize(p.w, p.h);
			  f.setLocation(thex, they);
			  f.setResizable(false);
			  f.setVisible(true);
			  return p;
		}
	
	
}//end PostContemporary
