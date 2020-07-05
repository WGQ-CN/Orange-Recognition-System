package orangeRecognitionSystem;

import javafx.scene.control.Label;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import javax.imageio.ImageIO;
import java.io.IOException;

public class FileMenu {
	static BufferedImage image;
	private final MenuItem miOpenImage = new MenuItem("��ͼƬ"),miSaveImage = new MenuItem("����ͼƬ");
	
	private final MenuItem miColorFilter = new MenuItem("��ɫɸѡ"), miKaiYunSuan = new MenuItem("������"), 
			miGray = new MenuItem("�ҶȻ�"), miMedianBlur = new MenuItem("ƽ��ģ��"), miGetCanny = new MenuItem("��Ե���"), 
			miBiYunSuan = new MenuItem("������"), miFindContours = new MenuItem("��������");
	
	private final MenuItem miAbout = new MenuItem("����");
	
	private final Menu menu1 = new Menu("�ļ�");
	private final Menu menu2 = new Menu("����");
	private final Menu menu3 = new Menu("����");
	
	public static BorderPane getImage = new BorderPane();
	
	//��ͼƬ������Ϣ���͵�DataAnalyzer�У�������ʼ����ťʹ�á�
	public static String transport;
	
	//������������
    private final DataAnalyzer dataAnalyzer = new DataAnalyzer();//�������ݷ���ʵ��
   
    //����DataAnayzer��ʵ��
    public final DataAnalyzer getDataAnalyzer() {
        return dataAnalyzer;
    }
    
    //����ͼ��ʵ��
    public final BorderPane getImage() {
    	return getImage;
    }
    
    //�ļ�ѡ��
    public final MenuBar addMenu() {
    	MenuBar mb = new MenuBar();
    	
    	mb.getMenus().addAll(menu1,menu2,menu3);
        menu1.getItems().addAll(miOpenImage,miSaveImage);
        menu2.getItems().addAll(miColorFilter,miKaiYunSuan,miGray,miMedianBlur,miGetCanny,miBiYunSuan,miFindContours);
        menu3.getItems().addAll(miAbout);
        
        miOpenImage.setOnAction(e -> openImage());
        miSaveImage.setOnAction(e -> saveImage());
         
        miColorFilter.setOnAction(e -> showColorFilter());
        miKaiYunSuan.setOnAction(e -> showKaiYunSuan());
        miGray.setOnAction(e -> showGray());
        miMedianBlur.setOnAction(e -> showMedianBlur());
        miGetCanny.setOnAction(e -> showGetCanny());
        miBiYunSuan.setOnAction(e -> showBiYunSuan());
        miFindContours.setOnAction(e -> showFindContours());
         
        miAbout.setOnAction(e -> about());
         
    	return mb;
    }
    
    //���ļ��м���ͼƬ
    public void openImage() {
    	
    	FileChooser fileChooser = new FileChooser();//ѡ���ļ��ؼ�
    	
    	fileChooser.getExtensionFilters().addAll(//��ӹ�����
          new ExtensionFilter("ͼƬ(*.jpg)", "*.jpg"),
          new ExtensionFilter("�����ļ�", "*.*")
    			);
    	
        File file = fileChooser.showOpenDialog(null);
        if (file != null && file.getName().endsWith(".jpg")) {
   			    transport = file.toString().replace('\\', '/');
   			    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
   	        	ImageProcessing.initialization(FileMenu.transport);
   	        	FileMenu.showSrcImage();
        }else {
           System.out.println("���ļ�����");
        }
    }
    
    //����ͼƬ
    public static void saveImage() {
    	 FileChooser fileChooser = new FileChooser();
         ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                 "JPG files (*.jpg)", "*.jpg");
         fileChooser.getExtensionFilters().add(extFilter);
         
         File file = fileChooser.showSaveDialog(null);

         if (file != null) {
             if (!file.getPath().endsWith(".jpg")) {
                 file = new File(file.getPath() + ".jpg");
             }
             try {
               ImageIO.write(image, "jpg", file);
             } catch (IOException e) {
               throw new RuntimeException(e);
             }
         }
    }
    
    //��ʾ��ʼͼ��
    public static void showSrcImage() {
    	image = toBufferedImage(ImageProcessing.srcImage);
    	showImage(image);
    }
    
    //��ʾ��ɫɸѡͼ��
    public static void showColorFilter() {
    	image = toBufferedImage(ImageProcessing.orangeImage);
    	showImage(image);
    }
    
    //��ʾ������ͼ��
    public static void showKaiYunSuan() {
    	image = toBufferedImage(ImageProcessing.kysImage);
    	showImage(image);
    }
    
    //��ʾ�ҶȻ�ͼ��
    public static void showGray() {
    	image = toBufferedImage(ImageProcessing.grayImage);
    	showImage(image);
    }
    
    //��ʾƽ��ģ��ͼ��
    public static void showMedianBlur() {
    	image = toBufferedImage(ImageProcessing.blurImage);
    	showImage(image);
    }
    
    //��ʾ��Ե���ͼ��
    public static void showGetCanny() {
    	image = toBufferedImage(ImageProcessing.edgeImage);
    	showImage(image);
    }
    
    //��ʾ������ͼ��
    public static void showBiYunSuan() {
    	image = toBufferedImage(ImageProcessing.bysImage);
    	showImage(image);
    }
    
    //��ʾ��������ͼ��
    public static void showFindContours() {
    	image = toBufferedImage(ImageProcessing.contoursImage);
    	showImage(image);
    }
    
    //����
    public void about() {
    	    Stage secondStage = new Stage();
    	    secondStage.setTitle("����");
    	    Label label = new Label("������Ա���⹢Ǳ ���� л�ѷ� ���ӷ�\nϵͳ�汾��1.0");
    	    StackPane secondPane = new StackPane(label);
            Scene secondScene = new Scene(secondPane, 400, 300);
            secondStage.setScene(secondScene);
            secondStage.show();
    }
   
    private static BufferedImage toBufferedImage(Mat matrix) { 
		int type = BufferedImage.TYPE_BYTE_GRAY;
		if (matrix.channels() > 1) { 
			type = BufferedImage.TYPE_3BYTE_BGR;
			} 
		int bufferSize = matrix.channels() * matrix.cols() * matrix.rows(); 
		byte[] buffer = new byte[bufferSize];
		matrix.get(0, 0, buffer); 
		// ��ȡ���е����ص�
		BufferedImage image = new BufferedImage(matrix.cols(), matrix.rows(), type);
		final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData(); 
		System.arraycopy(buffer, 0, targetPixels, 0, buffer.length);
		return image; 
	}
    
    public static void showImage(BufferedImage image) {
    	WritableImage wr = null;
    	if (image != null) {
    		wr = new WritableImage(image.getWidth(), image.getHeight());
    		PixelWriter pw = wr.getPixelWriter();
    		for (int x = 0; x < image.getWidth(); x++) {
    			for (int y = 0; y < image.getHeight(); y++) {
    				pw.setArgb(x, y, image.getRGB(x, y));
    			}
    		}
    	}
	    ImageView imageView = new ImageView(wr);
	    imageView.setFitHeight(700);
	    imageView.setFitWidth(600);
	    imageView.setPreserveRatio(true);
	    getImage.setCenter(imageView); 
    }
}
