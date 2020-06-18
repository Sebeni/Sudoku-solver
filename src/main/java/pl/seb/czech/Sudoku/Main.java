package pl.seb.czech.Sudoku;

import javafx.application.Application;
import javafx.stage.Stage;
import pl.seb.czech.Sudoku.Layout.SudokuWindow;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Sudoku solver");
        primaryStage.setScene(new SudokuWindow().getScene());
        primaryStage.show();
    }
}
