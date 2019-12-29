package Sudoku.Layout;

import Sudoku.Mechanic.CellCreator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;

public class SudokuBox {
    private final Scene scene;
    private final GridPane gridPane = new GridPane();
    
    
//    [column][row]
    private final TextField[][] textFields = new TextField[9][9];
    private final Pane[][] rootPanes = new Pane[9][9];


    public SudokuBox() {
        populateGrid();
        drawBorders();

        gridPane.setPadding(new Insets(20));
        BorderPane root = new BorderPane(gridPane);

        Button resolve = new Button("Resolve");
        resolve.setOnAction(event -> resolveButtonHandler());
        resolve.setId("buttons");

        Button clear = new Button("Clear");
        clear.setOnAction(event -> clearButtonHandler());
        clear.setId("buttons");

        HBox buttonBox = new HBox(10, resolve, clear);
        buttonBox.setPadding(new Insets(20));
        buttonBox.setAlignment(Pos.CENTER);

        root.setBottom(buttonBox);

        scene = new Scene(root);
        scene.getStylesheets().add("SudokuCells.css");
    }

    private void populateGrid() {
        for (int column = 0; column < 9; column++) {
            for (int row = 0; row < 9; row++) {
                TextField field = new TextField();

                field.setAlignment(Pos.CENTER);
                field.setPrefSize(50, 50);

                field.setOnKeyTyped(event -> {
                    if (field.getText().length() >= 1) event.consume();
                });

                field.setOnKeyReleased(event -> {
                    if (!field.getText().matches("[1-9]")) field.clear();
                });

                field.setOnMouseClicked(event -> field.clear());

                textFields[column][row] = field;

                Pane pane = new Pane(field);
                rootPanes[column][row] = pane;

                gridPane.add(pane, column, row);
            }
        }
    }

    private void drawBorders() {
        int firstBorder = 2;
        int secondBorder = 5;

        for (int column = 0; column < rootPanes.length; column++) {
            for (int row = 0; row < rootPanes[column].length; row++) {
                if (row == firstBorder || row == secondBorder) {
                    rootPanes[column][row].setStyle("-fx-border-width: 0 0 5 0; -fx-border-color: black;");
                }

                if (column == firstBorder || column == secondBorder) {
                    if (column == firstBorder && (row == firstBorder || row == secondBorder)) {
                        rootPanes[column][row].setStyle("-fx-border-width: 0 5 5 0; -fx-border-color: black;");
                    } else if (row == firstBorder || row == secondBorder) {
                        rootPanes[column][row].setStyle("-fx-border-width: 0 5 5 0; -fx-border-color: black;");
                    } else {
                        rootPanes[column][row].setStyle("-fx-border-width: 0 5 0 0; -fx-border-color: black;");
                    }
                }
            }
        }
    }

    public Scene getScene() {
        return scene;
    }

    private void clearButtonHandler() {
        for (TextField[] textField : textFields) {
            for (TextField field : textField) {
                field.clear();
                field.setStyle(null);
            }
        }
    }

    private void resolveButtonHandler() {
        clearStyles();
        CellCreator cc = new CellCreator(this);
        if (cc.createCells()) {
            System.out.println("CORRECT");
            
        } else {
            AlertBox.display("Duplicated values", "There are duplicated values in highlighted cells!");
        }
    }

    public TextField[][] getTextFields() {
        return textFields;
    }
    
    private void clearStyles(){
        for(TextField[] outer : textFields) {
            for(TextField field : outer){
                field.setStyle(null);
                if(!field.getText().isEmpty()) {
                    field.setStyle("-fx-background-color: grey");
                }
            }
        }
    }
}
