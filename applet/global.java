package postContemp;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;


public class global {

	public static	PImage picture;  	// the picture that is to be modified
	public static   String thefile;
	public static	PGraphics myCanvas; // the graphics buffer - where the new picture is created
	public static   PGraphics fmap;     // a flow control map of some sort to guide the production of the new graphics
	public static   int picWidth;
	public static   int picHeight;

	public static PImage rawBrush;
	public static String brush;
	public static int bW;
	public static int bH;

	public static Stroke[] theStrokes;

	global(PApplet p){
		picture = p.loadImage(thefile);
		picWidth = picture.width;
		picHeight = picture.height;
		
		myCanvas = p.createGraphics(picWidth, picHeight); // the graphics buffer starts as white or a copy of the picture
	    myCanvas.beginDraw();
		myCanvas.background(255); // set to white
//		myCanvas.image(picture, 0, 0, picture.width, picture.height);  //or copy in the picure
		myCanvas.endDraw();
		
      	fmap = p.createGraphics(picWidth, picHeight); // fmap is (for now) just a copy of the picture
    	fmap.beginDraw();
     	fmap.image(picture, 0, 0, picWidth, picHeight);
     	fmap.endDraw();
     	
		rawBrush = p.loadImage(brush);
		bW = rawBrush.width;
		bH = rawBrush.height;	

		theStrokes = new Stroke[100];

	}
	
}
