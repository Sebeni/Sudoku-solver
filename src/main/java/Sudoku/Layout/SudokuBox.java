package Sudoku.Layout;

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
    private final TextField[][] textFields = new TextField[9][9];
    private final String fontSize = "-fx-font-size: 20";


    public SudokuBox() {
        populateGrid();
        drawBorders();

        gridPane.setPadding(new Insets(20));
        BorderPane root = new BorderPane(gridPane);

        Button sudokuButton = new Button("Resolve");
        sudokuButton.setOnAction(event -> resolveButtonHandler());
        
        Button clear = new Button("Clear");
        clear.setOnAction(event -> clearButtonHandler());

        HBox buttonBox = new HBox(10, sudokuButton, clear);
        buttonBox.setPadding(new Insets(20));
        buttonBox.setAlignment(Pos.CENTER);

        root.setBottom(buttonBox);

        scene = new Scene(root);
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

                field.setStyle(fontSize);

                textFields[column][row] = field;
                gridPane.add(field, column, row);
            }
        }
    }

    private void drawBorders() {
        int firstBorder = 2;
        int secondBorder = 5;

        for (int column = 0; column < textFields.length; column++) {
            for (int row = 0; row < textFields[column].length; row++) {
                if (row == firstBorder || row == secondBorder) {
                    textFields[column][row].setStyle("-fx-border-width: 0 0 5 0; -fx-border-color: black;" + fontSize);
                }

                if (column == firstBorder || column == secondBorder) {
                    if (column == firstBorder && (row == firstBorder || row == secondBorder)) {
                        textFields[column][row].setStyle("-fx-border-width: 0 5 5 0; -fx-border-color: black;" + fontSize);
                    } else if (row == firstBorder || row == secondBorder) {
                        textFields[column][row].setStyle("-fx-border-width: 0 5 5 0; -fx-border-color: black;" + fontSize);
                    } else {
                        textFields[column][row].setStyle("-fx-border-width: 0 5 0 0; -fx-border-color: black;" + fontSize);
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
            }
        }
    }

    private void resolveButtonHandler() {
        
    }

}
