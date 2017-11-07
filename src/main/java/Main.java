package main.java;

import java.util.ArrayList;
import java.util.Iterator;

import edu.wpi.first.wpilibj.networktables.*;
import edu.wpi.first.wpilibj.tables.*;
import edu.wpi.cscore.*;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class Main {
	//Defined Variables
	ArrayList<MatOfPoint> G_Contours = new ArrayList<MatOfPoint>();
	ArrayList<MatOfPoint> S_Contours = new ArrayList<MatOfPoint>();
	//Image Converters
	CvSink GearSink = new CvSink("Gear Grabber");
	CvSink ShootSink = new CvSink("Shoot Grabber");
	CvSource GearSource = new CvSource("Gear Source",VideoMode.PixelFormat.kMJPEG,640,480,30);
	CvSource ShootSource = new CvSource("Shoot Source",VideoMode.PixelFormat.kMJPEG,640,480,30);
	//Image Constants
	Mat S_BINARY = new Mat();
	Mat	S_CLUSTERS = new Mat();
	Mat S_HEIRARCHY = new Mat();
	Mat	S_HSV = new Mat();
	Mat	S_SOURCE = new Mat();
	Mat	S_THRESH = new Mat();
	Mat G_BINARY = new Mat();
	Mat	G_CLUSTERS = new Mat();
	Mat G_HEIRARCHY = new Mat();
	Mat	G_HSV = new Mat();
	Mat	G_SOURCE = new Mat();
	Mat	G_THRESH = new Mat();
	//Stream Variables
	MjpegServer GearStream = new MjpegServer("MJPEG Server", 1184);
	MjpegServer GearStreamAlt = new MjpegServer("MJPEG Server", 1185);
	MjpegServer ShootStream = new MjpegServer("MJPEG Server", 1186);
	MjpegServer ShootStreamAlt = new MjpegServer("MJPEG Server", 1187);
	//Color Constants
	Scalar Black = new Scalar(0,0,0);
	Scalar Blue = new Scalar(255,0,0);
	Scalar Green = new Scalar(0,255,0);
	Scalar Red = new Scalar(0,0,255);
	Scalar Yellow = new Scalar(255,255,0);
	NetworkTable table = NetworkTable.getTable("vision");
	//Camera Variable
	UsbCamera GearCam = setUsbCamera(0, GearStream);
	UsbCamera ShootCam = setUsbCamera(1,ShootStream);
	//Undefined Variables
	Double TargetSpeed;
	Double Distance;
	Rect GearTarget;
	Rect ShootTarget;
	
	public void main(String[] args) {
		// Loads our OpenCV library. This MUST be included
		System.loadLibrary("opencv_java310");;
		//Start Code
		NetworkTable.setClientMode();
	    NetworkTable.setTeam(2509);
	    NetworkTable.initialize();
		GearCam.setResolution(640, 480);
		GearCam.setBrightness(0);
		ShootCam.setResolution(640, 480);
		ShootCam.setBrightness(0);
		GearSink.setSource(GearCam);
		ShootSink.setSource(ShootCam);
		GearStream.setSource(GearSource);
		ShootStream.setSource(ShootSource);
		ProcessGear.start();
		ProcessShooter.start();
	}

	private UsbCamera setUsbCamera(int cameraId, MjpegServer server) {
		UsbCamera camera = new UsbCamera("CoprocessorCamera", cameraId);
		server.setSource(camera);
		return camera;
	}
	private Thread ProcessGear = new Thread(()->{
		
	});
	private Thread ProcessShooter = new Thread(()->{
		while(true){
			S_Contours.clear();
			ShootSink.grabFrame(S_SOURCE);
			Imgproc.cvtColor(S_SOURCE, S_HSV, Imgproc.COLOR_BGR2RGB);
			Imgproc.threshold(S_HSV, S_BINARY, 180, 190, Imgproc.THRESH_BINARY_INV);	
			Imgproc.cvtColor(S_BINARY, S_THRESH, Imgproc.COLOR_HSV2BGR);
			Imgproc.cvtColor(S_THRESH, S_CLUSTERS, Imgproc.COLOR_BGR2GRAY);
			Mat S_GRAY = S_CLUSTERS;
			Imgproc.Canny(S_GRAY, S_HEIRARCHY, 2, 4);
			Imgproc.findContours(S_HEIRARCHY, S_Contours, new Mat(),Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
	        for(MatOfPoint mop :S_Contours){
	        	Rect rec = Imgproc.boundingRect(mop);
	        	Imgproc.rectangle(S_SOURCE, rec.br(), rec.tl(), Red);
	        }
	        for(Iterator<MatOfPoint> iterator = S_Contours.iterator();iterator.hasNext();){
				MatOfPoint matOfPoint = (MatOfPoint) iterator.next();
				Rect rec = Imgproc.boundingRect(matOfPoint);
				//float aspect = (float)rec.width/(float)rec.height;
				if( rec.height < 20||rec.height>100){
					iterator.remove();
					continue;
				}
		        
			}
	        table.putNumber("Shoot Height", ShootTarget.height);
	        table.putNumber("Shoot Width", ShootTarget.width);
	        table.putNumber("Shoot X", ShootTarget.x);
	        table.putNumber("Shoot Y", ShootTarget.y);
		}
	});
	
}