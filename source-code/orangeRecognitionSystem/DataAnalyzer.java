package orangeRecognitionSystem;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class DataAnalyzer extends VBox{

	private TextField tfNumber = new TextField();
	private GridPane gridPane = new GridPane();
    private Button Action = new Button("开始");
    
    public DataAnalyzer() {
    	
    	 //设置不可输入
         tfNumber.setEditable(false);
         //设置宽度
         tfNumber.setMaxWidth(40);
         
         gridPane.setVgap(20);
         gridPane.setHgap(10);
    	 gridPane.addRow(8, new Label("橘子个数："));
    	 gridPane.addRow(9, tfNumber, new Label("个		"));	 
         gridPane.addRow(15, Action);
         
         getChildren().add(gridPane);
         setSpacing(150);//设间距
         
         Action.setOnAction(e ->{
     		ImageProcessing.gray();
     		ImageProcessing.colorFilter(ImageProcessing.hsv);
     		ImageProcessing.kaiYunSuan();
     		ImageProcessing.MedianBlur();
     		ImageProcessing.getCanny();
     		ImageProcessing.biYunSuan();
     		tfNumber.setText(String.valueOf(ImageProcessing.findContours()));
     		FileMenu.showFindContours();
         });
    }
}
