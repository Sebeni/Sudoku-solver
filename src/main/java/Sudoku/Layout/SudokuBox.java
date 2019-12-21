package Sudoku.Layout;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class SudokuBox {
    private final Scene scene;
    private final GridPane gridPane = new GridPane();
    private final List<TextField> textFields = new ArrayList<>();


    public SudokuBox() {
        populateGrid();
        gridPane.setPadding(new Insets(20));
        Button sudokuButton = new Button("Resolve");
        
        VBox vbox = new VBox(10, gridPane, sudokuButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        BorderPane root = new BorderPane(vbox);
        
        
        
        scene = new Scene(root);
    }

    private void populateGrid() {
        for (int column = 0; column < 9; column++) {
            for (int row = 0; row < 9; row++) {
                TextField field = new TextField();
                
                field.setAlignment(Pos.CENTER);
                field.setPrefSize(50, 50);

                field.setOnKeyTyped(event -> {
                    if(field.getText().length() >= 1) event.consume();
                });
                
                field.setStyle("-fx-font-size: 20");


                textFields.add(field);
                
                gridPane.add(field, column, row);
            }
        }
    }
    
    
    
    
    

    public Scene getScene() {
        return scene;
    }
    

}
