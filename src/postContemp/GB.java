package postContemp;

import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;
import processing.opengl.PShader;

public class GB {
	public static PImage thePicture,altPicture;  
	public static PGraphics outputCanvas,workCanvas,controlCanvas; 
	
	public static String thefile,thealtfile;
	public static int picWidth,picHeight;
	public static int displayWidth,displayHeight;
	public static float mouseCorrectionX,mouseCorrectionY;
	public static float ratio;
	public static int scrollAmount;

	public static PImage rawBrush;
	public static String brush1,brush2,brush3,brush4,brush5,brush6,brush7,brush8,brush9,brush10,brush11,brush12,brush13,brush14,brush15,brush16;
	public static int whichBrush;
	public static int bW,bH;
	
	public static MeshMap theMeshMap;
	public static int triNum =0;
	public static int verticeNum = 0;
	
	public static ArrayList <Stroke> theStrokes;
	public static PVector holdStrokePos; 
 
	public static ArrayList <PVector> lineList;
	public static int listNum = 0;
	
	public static String saveFileName;
	public static int saveCount = 0;
	public static int frame = 0;
	
	public static boolean showFrameRate;	
	public static boolean mousePressed = false;
	public static boolean displayTemp = false;
	public static boolean displayCanny = false;
	
	public static int mouseSelectedColor ;
	public static boolean newStrokeFailed = false;
	
//	public static float strokeAngle;
   	
//   	public static int fulldisplayWidth;
//   	public static int fulldisplayHeight;
	
   	public static boolean waitingforsomething = false;
	public static int screenWidth;
	public static int screenHeight;
	public static float strokeAngle;
   	


	//GB sets up all the Globals  - including loading all the files
	GB(PApplet p){
		PApplet.println("in GB - looking to load the two files - the file and alt file");
		JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		chooser.setFileFilter(chooser.getAcceptAllFileFilter());
		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) 
		{
		 PApplet. println("You chose to open this file: " + chooser.getSelectedFile().getAbsolutePath());//was .getName
		}
		thefile = chooser.getSelectedFile().getAbsolutePath(); //was .getName
		thePicture = p.loadImage(thefile);
		p.image(thePicture,0,0,screenWidth,screenHeight);

//		p.delay(2000);// why do i need this???
		
		JFileChooser chooser2 = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		//above line was     = new JFileChooser();
		chooser.setFileFilter(chooser.getAcceptAllFileFilter());
		int returnVal2 = chooser.showOpenDialog(null);
		if (returnVal2 == JFileChooser.APPROVE_OPTION) 
		{
		 p. println("You chose to open this file: " + chooser.getSelectedFile().getAbsolutePath());//was .getName
		}
		thealtfile = chooser.getSelectedFile().getAbsolutePath(); //was .getName
//		p.delay(10000);
		altPicture = p.loadImage(thealtfile);

		picWidth = thePicture.width;
		picHeight = thePicture.height;
		
		theStrokes = new ArrayList <Stroke>();		//theStrokes is a list of current strokes - each of which will have it's own brush...	
		lineList = new ArrayList <PVector>();		//a list that the user creates with mousePressed in PostContemp, to be used as StartPositions
		
		// set a name for the saved file  - a frame number is concatenated when saved, saveCount is number of frames between saves
		saveFileName = "guit";
		saveCount = 1000;
		//frame counter appended to saved files - file is saved from the keyboard with 's' 
		
		// setup variables used to control the screen display zoom ect....
	    setDisplayControl(p);

	    //set up the canvas's for work, display and control
		setBuffers(p, thefile, thealtfile);
		
 
       	showFrameRate = false;
       	
		// names of the brush files available to be selected from the control frame (CF.java)
		p.println("loading the brush files");
		brush1= "whitedot.png";
		brush2= "b80.png";
		brush3= "b80.jpg";		
		//brush4 = "bigblob.png";
		brush5= "blobcross_32.png";
		brush6= "b120.png";
		brush7= "grass_32.png";
		brush8= "hardstar_32.png";
		brush9= "kapow_32.png";
		brush10= "leaf_32.png";
		brush11= "lightnin_32.png";
		brush12= "puffball_32.png";
		brush13= "square_32.png";
		
		//set up a default brush
		rawBrush = p.loadImage(brush1);
		bW = rawBrush.width;
		bH = rawBrush.height;	
		//temp fix to brush size
		bW=256;
		bH = 256;
		
		//setup a default stroke position
		holdStrokePos = new PVector(0,0);
	}

	static void setDisplayControl(PApplet p) {
		
		picWidth = thePicture.width;
		picHeight = thePicture.height;

		if (picHeight > picWidth) {
			//we are in portrait mode
			ratio = (float) (picHeight/screenHeight);
		    displayHeight = screenHeight;
		    displayWidth = (int) (picWidth/ratio);
		}else {
			ratio = (float) (picWidth/screenWidth);
			displayWidth = screenWidth;
			displayHeight = (int) (picHeight/ratio);
		}
	
		    
//check the size of the window set in PostContemp......		
//		fulldisplayWidth =  1872/picWidth; // 1872 and 1248 because they are a 1/3 of 5616 and 3744 - should probably be 1920 by 1280
//		fulldisplayWidth = DisplayWindow.displayW/picWidth;
//		fulldisplayWidth =  1678/picWidth;
//		fulldisplayHeight = DisplayWindow.displayH/picHeight;		
//		fulldisplayHeight = 1248/picHeight;

		//when the mouse is used to select a color its position needs to be corrected for screen coordinates to pic coords
		mouseCorrectionX = (float) picWidth/displayWidth;  
		mouseCorrectionY = (float) picHeight/displayHeight; 
		
		
//		if ((fulldisplayWidth < 1) || (fulldisplayHeight < 1)) {
//			if (fulldisplayWidth < fulldisplayHeight ) screensizeFactor = fulldisplayWidth;
//			else { screensizeFactor = fulldisplayHeight;}
//		}
	}

	static void setBuffers(PApplet p, String aPicture, String bPicture) {
		outputCanvas = p.createGraphics(picWidth, picHeight); 
	    outputCanvas.beginDraw();
	    	outputCanvas.background(255); // set to white
		outputCanvas.endDraw();
		p.println("out "+p.millis()/1000);
		
		workCanvas = p.createGraphics(picWidth, picHeight); 
	    workCanvas.beginDraw();
			workCanvas.background(255,0,0); // set to red
		workCanvas.endDraw();
		p.println("work "+p.millis()/1000);
		
	  	controlCanvas = p.createGraphics(picWidth, picHeight);
		controlCanvas.beginDraw();
			controlCanvas.image(thePicture, 0, 0, picWidth, picHeight); //set to the picture - so that something happens if you just press play
	 	controlCanvas.endDraw();
	 	p.println("control "+p.millis()/1000);
	}
	
	
	static void setPic(PApplet p, String aPicture) {
		if (CF.dropTo == 0) {
			thePicture = p.loadImage(aPicture);
		}else {
			altPicture = p.loadImage(aPicture);
		}
		if (thePicture.width != altPicture.width   || thePicture.height != altPicture.height) {
			p.println("warning ************** Dropped picture is NOT the same size as the pictures loaded at RUN time - who knows what will happen******");
			p.println(thePicture.width);
		}
	}
	
	static void externalInput(char k, int kc) {
		
		if (  kc == 37 || kc == 38 || kc == 39 || kc ==40) {
			 DisplayWindow.zoomPosition(kc);
			}
		switch(k) {
		   case PApplet.DELETE:               //kill all strokes and copy a color (set in CF) to CF.whichCanvas ...(work,control,output)
			    CF2.runRB.activate(3);	
				CF.doBackground = true;
				break;
		   case 's' : //save the current frame
			   	GB.frame++;
//				println("saving a frame after s key pressed");
				GB.outputCanvas.save("C:\\Users\\baaan\\desktop\\saved\\" + "saveme"+ GB.frame +".jpg");
			  break;	
		   case 'f' : // set the screen view window to cover the entire picture
			   DisplayWindow.fullview();
			  break;
		   case 'h' : // set the screen view window to cover the entire picture
			   DisplayWindow.halfview();
			  break;
		   case 'z' : // reset screen view window to the center of the picture
			   DisplayWindow.zoomview();	
			    break;
		   case 'r' : // toggle frame rate display
			    if (GB.showFrameRate == true) GB.showFrameRate = false;
			    else GB.showFrameRate = true;
			    break;
		   case 'k' : // turn off both CF and CF2
			   PostContemp.killCFs();
			   break;
		   case 'd' : // turn them back on
			   PostContemp.showCFs();
			   break;
		   case ' ':
			   if (CF2.drawPaused == true) { CF2.drawPaused = false; CF2.runRB.activate(0);}
			   else { CF2.drawPaused = true; CF2.runRB.activate(1);}
			   break;
		   case 'x':
			   	DisplayWindow.zoomin();
			   break;
		   case 'c':
			    DisplayWindow.zoomout();	
			   break;
			   
		}//end switch

	}
}
