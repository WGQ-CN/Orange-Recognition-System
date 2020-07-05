package orangeRecognitionSystem;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.*;
import org.opencv.imgcodecs.*;
import org.opencv.imgproc.Imgproc;

public class ImageProcessing {
	public static final int rgb = 0;
	public static final int hsv = 1;
	
	public static Mat srcImage = new Mat();//³õÊ¼Í¼Ïñ
	public static Mat grayImage = new Mat();//»Ò¶ÈÍ¼Ïñ
	public static Mat orangeImage = new Mat();//ÑÕÉ«É¸Ñ¡Í¼Ïñ
	public static Mat kysImage = new Mat();//¿ªÔËËãÍ¼Ïñ
	public static Mat blurImage = new Mat();//Ä£ºýÍ¼Ïñ
	public static Mat edgeImage = new Mat();//±ßÔµ¼ì²âÍ¼Ïñ
	public static Mat bysImage = new Mat();//±ÕÔËËãÍ¼Ïñ
	public static Mat contoursImage = new Mat();//ÂÖÀªÍ¼Ïñ
	
	ImageProcessing(){
		
	}
	
	//Í¼Ïñ´¦ÀíÄ£¿é³õÊ¼»¯
	public static void initialization(String src) {
		ImageProcessing.srcImage = Imgcodecs.imread(src);
		ImageProcessing.grayImage = srcImage.clone();
		ImageProcessing.orangeImage = srcImage.clone();
		ImageProcessing.kysImage = srcImage.clone();
		ImageProcessing.blurImage = srcImage.clone();
		ImageProcessing.edgeImage = srcImage.clone();
		ImageProcessing.bysImage = srcImage.clone();
		ImageProcessing.contoursImage = srcImage.clone();
	}
	
	//Í¼Ïñ»Ò¶È»¯
	public static void gray() {
		Imgproc.cvtColor(ImageProcessing.srcImage, ImageProcessing.grayImage, Imgproc.COLOR_BGR2GRAY);
	}
	
	//ÑÕÉ«É¸Ñ¡
	public static void colorFilter(int colorSpace) {
		if(colorSpace == ImageProcessing.hsv) {
			Mat tempImage = new Mat();
			Imgproc.cvtColor(ImageProcessing.srcImage, tempImage, Imgproc.COLOR_RGB2HSV, 3);
			Core.inRange(tempImage, new Scalar(95, 130, 160), new Scalar(156, 255, 255), ImageProcessing.orangeImage);
		}else {
			Core.inRange(ImageProcessing.srcImage, new Scalar(20, 190, 0), new Scalar(255, 255, 255), ImageProcessing.orangeImage);
		}
	}
	
	//¿ªÔËËã
	public static void kaiYunSuan() {
		Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, (new Size(15,5)));
		
		Imgproc.erode(ImageProcessing.orangeImage, ImageProcessing.kysImage, element);
		Imgproc.dilate(ImageProcessing.kysImage, ImageProcessing.kysImage, element);
	}
	
	//Æ½¾ùÄ£ºý
	public static void MedianBlur() {
		Imgproc.medianBlur(ImageProcessing.kysImage, ImageProcessing.blurImage, 55);
	}
	
	//±ßÔµ¼ì²â
	public static void getCanny() {
		Imgproc.Canny(ImageProcessing.blurImage, ImageProcessing.edgeImage, 3, 9, 3, false);
	}
	
	//±ÕÔËËã
	public static void biYunSuan() {
		Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(1, 1), new Point(-1, -1));
	
		Imgproc.morphologyEx(ImageProcessing.edgeImage, ImageProcessing.bysImage, Imgproc.MORPH_CLOSE, kernel);
	}
	
	//ÂÖÀª¸ú×Ù
	public static int findContours() {
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat hierarchy=new Mat();
	    Imgproc.findContours(ImageProcessing.bysImage, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
	   
	    MatOfPoint2f contoursPoly = new MatOfPoint2f();
	    Point center = new Point();
	    float[] radius = new float[1];
	    
	    int count = 0;
	    
	    for(int i=0; i < contours.size(); i++) {
	    	MatOfPoint2f contours2f = new MatOfPoint2f(contours.get(i).toArray());
	    	Imgproc.approxPolyDP(contours2f, contoursPoly, 3, true);
	    	Imgproc.minEnclosingCircle(contoursPoly, center, radius);
	    	if(radius[0] > 50) {
	    		Imgproc.circle(ImageProcessing.contoursImage, center, (int)radius[0], new Scalar(0, 0, 255), 2);
	    		count ++;
	    	}
	    }
	    return count;
	}
}
