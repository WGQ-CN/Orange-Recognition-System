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
	private final MenuItem miOpenImage = new MenuItem("打开图片"),miSaveImage = new MenuItem("保存图片");
	
	private final MenuItem miColorFilter = new MenuItem("颜色筛选"), miKaiYunSuan = new MenuItem("开运算"), 
			miGray = new MenuItem("灰度化"), miMedianBlur = new MenuItem("平均模糊"), miGetCanny = new MenuItem("边缘检测"), 
			miBiYunSuan = new MenuItem("闭运算"), miFindContours = new MenuItem("轮廓跟踪");
	
	private final MenuItem miAbout = new MenuItem("关于");
	
	private final Menu menu1 = new Menu("文件");
	private final Menu menu2 = new Menu("操作");
	private final Menu menu3 = new Menu("关于");
	
	public static BorderPane getImage = new BorderPane();
	
	//将图片索引信息传送到DataAnalyzer中，供“开始”按钮使用。
	public static String transport;
	
	//返回橘子数量
    private final DataAnalyzer dataAnalyzer = new DataAnalyzer();//定义数据分析实例
   
    //返回DataAnayzer的实例
    public final DataAnalyzer getDataAnalyzer() {
        return dataAnalyzer;
    }
    
    //返回图像实例
    public final BorderPane getImage() {
    	return getImage;
    }
    
    //文件选项
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
    
    //从文件中加载图片
    public void openImage() {
    	
    	FileChooser fileChooser = new FileChooser();//选择文件控件
    	
    	fileChooser.getExtensionFilters().addAll(//添加过滤器
          new ExtensionFilter("图片(*.jpg)", "*.jpg"),
          new ExtensionFilter("所有文件", "*.*")
    			);
    	
        File file = fileChooser.showOpenDialog(null);
        if (file != null && file.getName().endsWith(".jpg")) {
   			    transport = file.toString().replace('\\', '/');
   			    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
   	        	ImageProcessing.initialization(FileMenu.transport);
   	        	FileMenu.showSrcImage();
        }else {
           System.out.println("打开文件错误");
        }
    }
    
    //保存图片
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
    
    //显示初始图像
    public static void showSrcImage() {
    	image = toBufferedImage(ImageProcessing.srcImage);
    	showImage(image);
    }
    
    //显示颜色筛选图像
    public static void showColorFilter() {
    	image = toBufferedImage(ImageProcessing.orangeImage);
    	showImage(image);
    }
    
    //显示开运算图像
    public static void showKaiYunSuan() {
    	image = toBufferedImage(ImageProcessing.kysImage);
    	showImage(image);
    }
    
    //显示灰度化图像
    public static void showGray() {
    	image = toBufferedImage(ImageProcessing.grayImage);
    	showImage(image);
    }
    
    //显示平均模糊图像
    public static void showMedianBlur() {
    	image = toBufferedImage(ImageProcessing.blurImage);
    	showImage(image);
    }
    
    //显示边缘检测图像
    public static void showGetCanny() {
    	image = toBufferedImage(ImageProcessing.edgeImage);
    	showImage(image);
    }
    
    //显示闭运算图像
    public static void showBiYunSuan() {
    	image = toBufferedImage(ImageProcessing.bysImage);
    	showImage(image);
    }
    
    //显示轮廓跟踪图像
    public static void showFindContours() {
    	image = toBufferedImage(ImageProcessing.contoursImage);
    	showImage(image);
    }
    
    //关于
    public void about() {
    	    Stage secondStage = new Stage();
    	    secondStage.setTitle("关于");
    	    Label label = new Label("开发人员：吴耿潜 林琪 谢佳发 周子枫\n系统版本：1.0");
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
		// 获取所有的像素点
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
