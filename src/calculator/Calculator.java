package calculator;

import java.math.BigDecimal;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Calculator extends Application {
    
    double width;
    double height;
    TextField tf = new TextField();
    Button btn = new Button();
    Double var1;
    Double var2;
    Double answer; 
    String op;
    String display;
    Button eq = new Button("=");
    Text txt = new Text();
    boolean opClicked = false;
    BorderPane bp = new BorderPane();
    
    @Override
    public void start(Stage primaryStage) {
        
        primaryStage.setTitle("Calculator");
        
        Scene scene = new Scene(bp, 400, 300);
        width = scene.getWidth();
        height = scene.getHeight();
        
        
        setup();

        primaryStage.setTitle("Calculator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public void setup(){
        txt.setFont(new Font(20));

        bp.setPadding(new Insets(5));

        VBox top = new VBox();
        top.setSpacing(5);
               
        top.getChildren().addAll(txt, tf);
        
        String numArray[][] = new String[][]{
         {"7", "8", "9"},
         {"4", "5", "6"},
         {"1", "2", "3"},
         {"0", null, "."},
        };
        
        GridPane gpNum = setupGridPane(numArray, 4, 3);
        
        String charArray[][] = new String[][]{
        {"X", "%", "Clear"},
        {"-", "X!", "X\u00b2"},
        {"+", "\u221A", "X\u00b3"},
        {"\u00f7", "\u221B", "X\u207F"}
        };
        
        GridPane gpChars = setupGridPane(charArray, 4, 3);

   
        HBox hb = new HBox(20, gpNum, gpChars);
        hb.setPadding(new Insets(10,0,10,0));
        
        setEquals();
        

        bp.setTop(top);
        bp.setCenter(hb);
        bp.setBottom(eq);
        
    }
 
    
    public GridPane setupGridPane(String[][] grid, int rows, int cols){
        
        GridPane gp = new GridPane();

        for(int i = 0; i < cols; i++){
            ColumnConstraints cConst = new ColumnConstraints();
            cConst.setPercentWidth(100/cols);
            gp.getColumnConstraints().add(cConst);
        }
        
        for(int i = 0; i < rows; i++){
            RowConstraints rConst = new RowConstraints();
            rConst.setPercentHeight(100/rows);
            gp.getRowConstraints().add(rConst);
        }
  

        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                Button button = new Button();
                button.setText(grid[i][j]);
                
                    if(i == 3 & j == 0 & grid[3][1] == null){
                        gp.add(button, 0, 3, 2, 1); 
                        j = 1;
                        button.setPrefSize((width/ cols) * 2, height/rows);
                    }
                    else{
                        button.setPrefSize(width/ cols, height/rows);
                        gp.add(button, j, i);
                        GridPane.setHalignment(button, HPos.CENTER);  
                        
                        if(grid[3][1] != null){
                            button.setStyle(("-fx-background-color: lightcoral"));                            
                        }
                    }              
                    buttonClick(button);
            }
        }
        gp.setHgap(20);
        gp.setVgap(20);
 
        return(gp);        
    }

    
    public void buttonClick(Button btn){
        
        btn.setOnAction((ActionEvent e) -> {
            
            String btnText = btn.getText();
            
            if(btnText.equals("Clear")){
                txt.setText("");
                clear();
            }
            
            else if(opClicked == false){
                if (isNumber(btnText) || ".".equals(btnText)){

                    if (".".equals(btnText) && !tf.getText().contains(".")) {
                        tf.appendText(btnText);
                    } else if (isNumber(btnText) && (!tf.getText().contains(".") ||
                               tf.getText().contains("."))) {
                        tf.appendText(btnText);
                    }          
                }       

                else{                  
                    String field1 = tf.getText();
                    var1 = Double.parseDouble(field1);                   
                    tf.clear(); 
                    opClicked = true;
                    op = btnText;
                    
                    if(isSingleVarOp()){
                        singleVarFunc();
                        String var = setFormat(var1, false);
                        String ans = setFormat(answer, true);
                        
                        set1VarText(var, ans);
                                                                 
                        clear();                                          
                    }                 
                }                
            }
            else{
                if (isNumber(btnText) || ".".equals(btnText)){
                    if (".".equals(btnText) && !tf.getText().contains(".")) {
                        tf.appendText(btnText);
                    } else if (isNumber(btnText) && (!tf.getText().contains(".") ||
                               tf.getText().contains("."))) {
                        tf.appendText(btnText);
                    }                    
                }                
            }                   
        });    
    }
    
    public void set1VarText(String v1, String a){
        switch (op) {
            case "\u221A":  
            case "\u221B":
                display = op + v1 + " = " + a;
                break;
            case "X!":
                display = v1 + "!" + " = " + a;
                break;
            case "X\u00b2":
                display = v1 + "\u00b2" + " = " + a;
                break;
            default:
                display = v1 + "\u00b3" + " = " + a;
                break;
        }
        
        
        txt.setText(display);
        double textWidth = txt.getLayoutBounds().getWidth(); 
        if (textWidth > 390){
            setNewStage();
        }

               
    }
    
    public void setNewStage(){
        Text t = new Text(display);
        StackPane sp = new StackPane(t);
        Scene secondScene = new Scene(sp, 350, 250);
        
        StackPane.setAlignment(t, Pos.TOP_LEFT); // align to top left
        t.wrappingWidthProperty().bind(secondScene.widthProperty()); // text wrapping
            
        Stage s = new Stage();
        s.setTitle("Big Result");
        s.setScene(secondScene);
        s.show();
            
        txt.setText("");      
    }
    
    public boolean isSingleVarOp(){
        return op.equals("X!") || op.equals("\u221A") || op.equals("\u221B") || op.equals("X\u00b2") || op.equals("X\u00b3");    
    }
    
    
    public void setEquals(){
        
        eq.setMaxWidth(Double.MAX_VALUE);
        eq.setStyle(("-fx-background-color: lightcoral"));
        
        eq.setOnAction((ActionEvent e) ->{
            String field2 = tf.getText();
            var2 = Double.parseDouble(field2);

            
            doubleVarFunc();
            
            String vari1 = setFormat(var1, false);
            String vari2 = setFormat(var2, false);
            String ans = setFormat(answer, true);
            
            set2VarText(vari1, vari2, ans);
          
            
            clear();
            
        });      
    }
    
    public void set2VarText(String v1, String v2, String a){
        if(op.equals("X\u207F")){
            display = v1 + " pow " + v2 + " = " + a;
        }
        else{
            display = v1 + " "  + op + " " + v2 + " = " + a;           
        }
        
        txt.setText(display);
        double textWidth = txt.getLayoutBounds().getWidth(); 
        if (textWidth > 390){
            setNewStage();
        }
    }
    
    public String setFormat(double a, boolean res){
        
        
        BigDecimal number = new BigDecimal(a);
        String sNumber = number.toPlainString();
        
        if (!Double.toString(a).contains("E[/d]*")) {
            if (Double.toString(a).length() < number.toPlainString().length()) {
                if (res == true && isSingleVarOp()) {
                    sNumber = number.toPlainString();
                } else {
                    sNumber = Double.toString(a);
                }
            } else {
                sNumber = number.toPlainString();
            }
        } else {
            sNumber = number.toPlainString();
        }     
        return(sNumber); 
    }
    
    
    public void doubleVarFunc(){
        
        switch (op) {
            case "X":
                answer = var1 * var2;
                break;
            case "-":
                answer = var1 - var2;                       
                break;
            case "+":
                answer = var1 + var2;       
                break;
            case "\u00f7":
                answer = var1 / var2;             
                break;
            case "%":
                answer = var1 % var2;                              
                break;
            default:
                answer = Math.pow(var1, var2);
                break;
        }
    }
    
    public void singleVarFunc(){
            
        switch (op) {
            case "X!":
                double k = 1;
                for(int i = 1; i <= var1; i++){
                    k = k * i;
                }   answer = k;
                break;
            case "\u221A":
                answer = Math.sqrt(var1);
                break;
            case "\u221B":
                answer = Math.cbrt(var1);
                break;
            case "X\u00b2":
                answer = Math.pow(var1, 2);
                break;
            default:
                answer = Math.pow(var1, 3);
                break;
        }     
    }
    
    public void clear(){
            tf.clear();
            var1 = null;
            var2 = null;
            opClicked = false;
    }

 
    public boolean isNumber(String str){
        try{
            Double num = Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e) {
            
        }
        return false;       
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}

