package main.java;

import java.util.ArrayList;

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
	ArrayList<MatOfPoint> CONTOURS = new ArrayList<MatOfPoint>();
	//Image Converters
	CvSink GearSink = new CvSink("Gear Grabber");
	CvSink ShootSink = new CvSink("Shoot Grabber");
	CvSource GearSource = new CvSource("Gear Source",VideoMode.PixelFormat.kMJPEG,640,480,30);
	CvSource ShootSource = new CvSource("Shoot Source",VideoMode.PixelFormat.kMJPEG,640,480,30);
	//Image Constants
	Mat BINARY = new Mat();
	Mat	CLUSTERS = new Mat();
	Mat HEIRARCHY = new Mat();
	Mat	HSV = new Mat();
	Mat	SOURCE = new Mat();
	Mat	THRESH = new Mat();
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
	NetworkTable table = NetworkTable.getTable(null);
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
		ShootCam.setResolution(640, 480);
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
		
	});
	
}