package orangeRecognitionSystem;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class PrimaryPane extends Application{
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		BorderPane primaryBorderPane=new BorderPane();//最外面板
	        
	    FileMenu fileMenu=new FileMenu();//定义文件菜单实例	     
	     
	    primaryBorderPane.setTop(fileMenu.addMenu());
	    primaryBorderPane.setLeft(fileMenu.getImage());
	    primaryBorderPane.setRight(fileMenu.getDataAnalyzer());
	     
	    primaryStage.setScene(new Scene(primaryBorderPane,800,600));
	    primaryStage.setTitle("柑橘水果识别系统");
	    primaryStage.show();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}
}
