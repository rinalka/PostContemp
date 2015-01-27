package postContemp;

import processing.core.PApplet;
import processing.core.PFont;
import controlP5.ControlFont;
import controlP5.ControlP5;
import controlP5.ControllerInterface;


public class ControlFrame extends PApplet {
	  int w, h;
	  static int ScaleX = 2;
	  static int ScaleY = 2;
	  static int TintR = 255;
	  static int TintG = 255;
	  static int TintB = 255;
	  static int Alph = 127;
	  static float brushAngle = (float) 0.01;
	  static int strokeLength = 100;

	  ControlP5 cp5;
   	  Object parent;
   	  
     public ControlFrame(Object theParent, int theWidth, int theHeight) {
		    parent = theParent;
		    w = theWidth;
		    h = theHeight;
	  }
     	
	  
	  
	  public void setup() {
		  size(w, h);
		  frameRate(25);
		  cp5 = new ControlP5(this);
		  
	      // change the original colors or whatever of the layout here
//	      cp5.setColorForeground(0xffaa0000);
//	      cp5.setColorBackground(0xff660000);
//	      cp5.setColorActive(0xffff0000);
		    
		  //create all of the controllers you want
		  cp5.addSlider("ScaleX")	.plugTo(parent,"ScaleX")	.setRange(1, 50)	.setValue(ScaleX)	.setPosition(10,10).setSize(255,20);
		  cp5.addSlider("ScaleY")	.plugTo(parent,"ScaleY")	.setRange(1, 50)	.setValue(ScaleY)	.setPosition(10,40).setSize(255,20);
		  cp5.addSlider("TintR")	.plugTo(parent,"TintR")		.setRange(0, 255)	.setValue(TintR)	.setPosition(10,90).setSize(255,20);
		  cp5.addSlider("TintG")	.plugTo(parent,"TintB")		.setRange(0, 255)	.setValue(TintB)	.setPosition(10,120).setSize(255,20);
		  cp5.addSlider("TintB")	.plugTo(parent,"TintG")		.setRange(0, 255)	.setValue(TintG)	.setPosition(10,150).setSize(255,20);
		  cp5.addSlider("Alph")		.plugTo(parent,"Alph")		.setRange(0, 255)	.setValue(Alph)		.setPosition(10,180).setSize(255,20);
		
		  cp5.addSlider("brushAngle")		.plugTo(parent,"brushAngle")	.setRange(0,(float) TWO_PI)	.setValue(brushAngle)	.setPosition(10,220).setSize(255,20);
		  cp5.addSlider("strokeLength")		.plugTo(parent,"strokeLength")	.setRange(0,300)	.setValue(strokeLength)	.setPosition(10,250).setSize(255,20);
		  
		  //custimize the sliders or knobs
		  PFont pfont = createFont("Arial",20,true); // use true/false for smooth/no-smooth
		  ControlFont font = new ControlFont(pfont,241);
		  cp5.getController("ScaleX")    .getCaptionLabel()    .setFont(font)   .setSize(18) 	.setColor(0xffffffff);
		  cp5.getController("ScaleY")    .getCaptionLabel()    .setFont(font)   .setSize(18)	.setColor(0xffffffff);
		  cp5.getController("TintR")    .getCaptionLabel()     .setFont(font)   .setSize(18)	.setColor(0xffffffff);
		  cp5.getController("TintG")    .getCaptionLabel()     .setFont(font)   .setSize(18)	.setColor(0xffffffff);
		  cp5.getController("TintB")    .getCaptionLabel()     .setFont(font)   .setSize(18)	.setColor(0xffffffff);
		  cp5.getController("Alph")     .getCaptionLabel()     .setFont(font)   .setSize(18)	.setColor(0xffffffff);
		  cp5.getController("strokeLength")     .getCaptionLabel()     .setFont(font)   .setSize(18)	.setColor(0xffffffff);
		  
		  cp5.getController("brushAngle")   .getCaptionLabel()     .setFont(font)   .setSize(18)	.setColor(0xffffffff);
		  cp5.getController("brushAngle")   .getColor() .setBackground(0xffff0000);

		  }
	  
	  public void draw() {
		  //draw things to the controll planel other than sliders and knobs

	  }

	  	public static float getScaleX() {return ScaleX;	  	}
	  	public static float getScaleY() {return ScaleY;	  	}

	  	public static float getTintR() {return TintR;	  	}
	  	public static float getTintG() {return TintG;	  	}
	  	public static float getTintB() {return TintB;	  	}
	  	public static float getAlph()  {return Alph;	  	}
	  	
	  	public static float getBrushAngle()  {return brushAngle;	  	}
	  	public static float getStrokeLength()  {return strokeLength;	  	}

}
