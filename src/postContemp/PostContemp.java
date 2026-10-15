package postContemp;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
//import javax.media.opengl.GL2;

import jogamp.opengl.x11.glx.GLX;

import controlP5.*;
import sojamo.drop.*;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public class PostContemp extends PApplet {
	
	//seems i need this 
	public static void main(String[] args) {
		PApplet.main("postContemp.PostContemp");
	}
	
	// set up where most the globals are! i know, i know.
	// Notes: globals set on the control panels are in CF.java or CF2.java
	// GB.java has all the other globals (like picture and working buffers....
	// and sizes)
	GB myglobals;
	CF cf1;
	CF2 cf2;

	static Frame f;
	static Frame f2;

	static String pictureUrl;

	// the display is a screen view into GB.myCanvas, when GB.myCanvas is bigger
	// than theDisplay,
	// controls for pan and zoom are keyboard driven (see GB.externalInput())
	// the % of myCanvas shown in the Display is set with GB.screensizeFactor
	DisplayWindow theDisplay;

	// used to control the speed of stroke creation - and allow killstrokes to
	// do so gracefully
	int maxNewStrokes = 10;
	int animFrame = 0;
	int animFramel = 1;
	boolean flag = false;
	
	public void settings() {
		//I have to set the size before the pics get loaded!!! So the whole shebang is determined by the size of the photos 
		// If the size doesn't match the settings here it doesn't work 
		// so i've put in some standards
//		size (1872,1248); //use for camera landscape!
//		size (1000,1754); //use for camera portrait
		size (3000,3000); //phone pics
//		size (2048,2048);
		GB.screenWidth = width;
		GB.screenHeight = height;
	}

	public void setup() {
		// setup a timer in case you want to time stuff.
		int time = millis() / 1000;

		// setup all the GB global variables - well not quite all, they are in
		// GB, globals that are controlled by the interface are in CF and CF2
		myglobals = new GB(this);

		// set up the display window - which is probably smaller than the actual picture
		// start things off with a full view - where the screen shows the full
		// picture - at a lower res of course
		theDisplay = new DisplayWindow();
		DisplayWindow.fullview();

		// HSB is the default colorMode - but it does get changed here and
		// there. Some calcs are easier in RGB
		colorMode(HSB, 255, 255, 255);

		// setup the control panels and initialize all the CF and CF2 variables
		// **************************** the name of the window does not seem to
		// be working
		cf1 = new CF(this, 1240, 1700, "Pastiche");
		// seems that i need a bit of a delay ? before setting up the second
		// controlFrame
		delay(1000);
		cf2 = new CF2(this, 1240, 300, "P control");

	}// end setup

	public void draw() {
		 
		//first time through - move the main screen to 0,0  
		if (!flag) {
			    surface.setLocation(0, 0);
			    flag = true;
			  }
		
		
		// update the screen view - that is look into myCanvas in full half or
		// zoom modes (keyboard below - to change)
		theDisplay.update(this);
		
		// if the user pressed CF2 Stop New  -stop generating new strokes, wait for all the strokes to die and then turn drawPaused on
		if (CF2.killNewStrokes == true) {
			if (GB.theStrokes.size() == 0) {
				CF2.drawPaused = true;
				CF2.runRB.activate(1);// turn on the drawPaused button
				CF2.killNewStrokes = false;
			}
		}

		// if the user pressed CF2 Kill Strokes - remove all existing strokes an turn pause on
		if (CF2.killAllStrokes == true) {
			for (int i = GB.theStrokes.size() - 1; i > -1; i--) {
				GB.theStrokes.remove(i);
			}
			CF2.killAllStrokes = false;
			CF2.runRB.activate(1);// turn on the drawPaused button
			CF2.drawPaused = true;
		}

		// if the user selected a different kind of brush in CF  you have to remove all the existing strokes!!!
		if (CF.newBrush == true) {// 
			for (int i = GB.theStrokes.size() - 1; i > -1; i--) {
				GB.theStrokes.remove(i);
			}
			CF.newBrush = false;
		}

		
		//now assuming the drawing is not paused we process all the strokes - and create new ones if needed
		if (CF2.drawPaused == false) {
			
			// run several painting steps per screen refresh - the screen refresh is the slow part
			// (GB.stepsPerFrame, change with + and - keys)
			for (int step = 0; step < GB.stepsPerFrame && !CF2.drawPaused; step++) {
				// create strokes if you need them BUT Never more than MaxNewStrokes at a time  or things get slow
				int newStrokesCreated = 0;
				maxNewStrokes = 20;
				while ((GB.theStrokes.size() < CF.numOfStrokes) && (newStrokesCreated < maxNewStrokes)
						&& (CF2.killNewStrokes == false)) {
					switch (CF.strokeType) {
					case 0:
						GB.theStrokes.add(new Stroke(this));
						break;
					case 1:
						GB.theStrokes.add(new curveStroke(this));
						break;
					case 2:
						GB.theStrokes.add(new contAngStroke(this));
						break;
					}
					newStrokesCreated++;

				}
			
				if (GB.newStrokeFailed == true) {// newStrokeFailed is set to true in Stroke IFF the conditions for a new stroke can't be meet!!!!
					GB.newStrokeFailed = false; // reset the flag
					if (CF2.killNewStrokes = false)
						CF2.killNewStrokes = true;
					CF2.runRB.activate(2);// turn on the drawKilled button
				} else {
					// and update all the strokes that are running
					GB.outputCanvas.beginDraw();
					for (int i = 0; i < GB.theStrokes.size(); i++) {
						Stroke currentStroke = GB.theStrokes.get(i);
						if (currentStroke.strokeIsDead == true) {
							GB.theStrokes.remove(i);
						} else {
							currentStroke.updateStroke(this);
						}
					}
					GB.outputCanvas.endDraw();
				}

				//once in a while save a frame - counted in painting steps, so it doesn't depend on stepsPerFrame
				GB.paintSteps++;
				if (GB.paintSteps % GB.saveCount == 0) {
					GB.frame++;
					println("saving a frame after saveCount reached saving spotsave"+GB.frame);
					GB.outputCanvas.save(GB.saveDir + "spotsave" + GB.frame + ".jpg");
//					CF.strokeAngleAdjust = (float) (CF.strokeAngleAdjust + 0.025);
				}
			}// end steps
//			CF.strokeAngleAdjust = (float) (CF.strokeAngleAdjust + 0.00085);

		} // end if drawPaused

		if (GB.showFrameRate == true) { // display the frame rate in the Console
										// view - should put this in the control
										// panel******
			if (frameCount % 500 == 0) {
				println("frameRate " + frameRate + "    " + frameCount + "  frameCount " + frameCount);
			}
			// if (frameCount % 2000 == 0)
			// {GB.outputCanvas.save(GB.saveFileName+frameCount);}
		}

		if (CF.doCanvasCopy == true) {
			CF.doCanvasCopy = false;
			DisplayWindow.canvasCopy(this);
		}

		if (CF.doMeshDisplay == true) {
			CF.doMeshDisplay = false;
			MeshMap.displaySomeMesh(this);
		}

		if (CF2.meshRecalc == true) {
			// be sure all strokes are dead - because if any stroke referes to
			// the meshmap it will be effed up after a new mesh is created
			for (int i = GB.theStrokes.size() - 1; i > -1; i--) {
				GB.theStrokes.remove(i);
			}
			GB.triNum = 0;// start over with any display in order to avoid a out
							// of bounds error
			GB.theMeshMap = new MeshMap(this, CF2.meshMode, CF2.mThreshold, CF2.mResolution);
			MeshMap.displaySolidMesh(this); // draws the mesh onto the
													// screen - does not effect
													// myCanvas
		}

	}// end draw

	// function to deal with the mouse - it's a mess
	public void mousePressed() {
		// clicking on the screen will set the hsb start position restrictions
		// on CF....
		// first remap the mouse as the pixels of the display are probably not
		// the same as the actual picture
/*		int mValueX = (int) PApplet.map(mouseX, 0, 1920, DisplayWindow.screen_locx,
				DisplayWindow.screen_locx + DisplayWindow.displayW);
		int mValueY = (int) PApplet.map(mouseY, 0, 1280, DisplayWindow.screen_locy,
				DisplayWindow.screen_locy + DisplayWindow.displayH);
		// and just in case let's make sure we are still in bounds - print a
		// message just so you know why it crashed.
		if ((mValueX > GB.picWidth) || (mValueY > GB.picHeight)) {
			println("something is wrong in mousePressed ****** you should not be able to get here *******************");
		}

		// *************************************************************** TOTAL
		// HACK - need to think this through - later *****************

		if (CF.copyTrifromSource != -1) {
			PVector mpoint = new PVector(mValueX, mValueY);
			int trinum = MeshMap.findTriNum(this, mpoint);
			println(trinum);
			if (trinum == -1) {
				println("this should not happen - was this a tri that got added with the CF2 add boarder tri switch?");
			} else {
				// GB.workCanvas.beginDraw();
				MeshMap.fillaTri(this, trinum);
				MeshMap.copyTriChunk(this, trinum);
				// GB.workCanvas.endDraw();
			}
		}
		// ***************************************************

		// now get the color under the mouse from the screen that is displayed
		switch (CF.posResFrom) {// mouseSelectedColor used to set min max values
								// for hsb in CF.onMouseSetPosRestrict ()
								// that are then used for restrictions in
								// Stroke.isPosLegal
		case 0:
			GB.mouseSelectedColor = GB.thePicture.pixels[mValueY * GB.picWidth + mValueX];
			break;
		case 1:
			GB.mouseSelectedColor = GB.altPicture.pixels[mValueY * GB.picWidth + mValueX];
			break;
		case 2:
			GB.mouseSelectedColor = GB.workCanvas.pixels[mValueY * GB.picWidth + mValueX];
			break;
		case 3:
			GB.mouseSelectedColor = GB.controlCanvas.pixels[mValueY * GB.picWidth + mValueX];
			break;
		case 4:
			GB.mouseSelectedColor = GB.outputCanvas.pixels[mValueY * GB.picWidth + mValueX];
			break;
		}
		GB.mousePressed = true;

		// if we are in list mode (creating a list for StrokeStartPosition to
		// follow) get and store all the info
		if (CF2.createStartPositionList == true) {
			// if this is the first point don't do the "back fill"
			if (CF2.newlist == true) {
				CF2.newlist = false;
				if (GB.lineList.size() != 0) {
					for (int i = GB.lineList.size(); i == 0; i--) {
						GB.lineList.remove(i);
					}
				}
			}

			if (GB.lineList.size() == 0) {
				PVector aPoint = new PVector(mValueX, mValueY);
				GB.lineList.add(aPoint);
				GB.workCanvas.fill(0, 0, 255, 255);
				GB.workCanvas.ellipse(aPoint.x, aPoint.y, 10, 10);
				println("created the first point in the list");
			} else {
				// on all other points figure out the number of previous points
				// (based on CF.lineListInterval) and put them in the list
				int index = GB.lineList.size() - 1;
				PVector lastPoint = GB.lineList.get(index);
				PVector newPoint = new PVector(mValueX, mValueY);
				PVector dir = PVector.sub(newPoint, lastPoint);
				dir.div(CF2.lineListIntervalNum);
				for (int i = 0; i < CF2.lineListIntervalNum; i++) {
					PVector nextPoint = new PVector(lastPoint.x, lastPoint.y);
					lastPoint.add(dir);
					PVector wtf = new PVector(lastPoint.x, lastPoint.y);
					GB.lineList.add(wtf);
					GB.workCanvas.beginDraw();
					GB.workCanvas.fill(255, 0, 0, 255);
					GB.workCanvas.ellipse(wtf.x, wtf.y, 10, 10);
					GB.workCanvas.endDraw();
					println("added a point to the list at " + lastPoint.x + " " + lastPoint.y + " now there should be "
							+ GB.lineList.size());
				}
			}
			// do something about looping the list!
		}
*/	}

	// function to deal with the keyboard input
	public void keyPressed() { // look after any keyboard input
		println("a key was pressed");
		GB.externalInput(key, keyCode);
	}// end keypressed

	// functions to turn on and off the two control frames
	static void killCFs() {
		f2.setVisible(false);
		f.setVisible(false);
	}

	static void showCFs() {
		f.setVisible(true);
		f2.setVisible(true);
	}

	// functions to have user select files to be processed
	public void thePictureSelected(File selection) {
		if (selection == null) {
			println("Window was closed or the user hit cancel.");
		} else {
			println("User selected thePicture " + selection.getAbsolutePath());
			GB.thefile = selection.getAbsolutePath();
		}
	}

	public void altPictureSelected(File selection) {
		if (selection == null) {
			println("Window was closed or the user hit cancel.");
		} else {
			println("User selected altPicture " + selection.getAbsolutePath());
			GB.thealtfile = selection.getAbsolutePath();
		}
	}

}// end Pastiche-O-Matic
