package Sudoku.Algorithm;

import Sudoku.Elements.Board;
import Sudoku.Layout.SudokuWindow;
import Sudoku.Elements.Box;
import Sudoku.Elements.Cell;
import Sudoku.Elements.Column;
import javafx.scene.control.TextField;

import java.util.*;
import java.util.stream.Collectors;

public class BoardCreator {
    private TextField[][] textFields;
    private Board board = new Board();


    public BoardCreator(TextField[][] textFields) {
        this.textFields = textFields;
    }

    public boolean setCells() {
        for (int column = 0; column < 9; column++) {
            for (int row = 0; row < 9; row++) {
                String input = textFields[column][row].getText();
                if (!input.isEmpty()) {
                    Cell cellToChange =  board.getColumn(column).getCell(row);
                    cellToChange.setNumInside(Integer.parseInt(input));
                    cellToChange.getPossibleChoices().clear();
                }
            }
        }

        printInDebug();
        InputValidator validator = new InputValidator(this);
        return validator.checkInputData();
    }

    private void printInDebug() {
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                System.out.print(board.getColumn(y).getCell(x));
            }
            System.out.println();
        }

        System.out.println("SQUARES");
        List<Box> boxes = board.getBoxes();
        
        for (int i = 0; i < boxes.size(); i++) {
            System.out.println("square numero: " + i);
            System.out.println(boxes.get(i));
        }
    }


    public static List<Cell> getColumnCellsFilled(int columnNum, Board board) {
        return board.getColumn(columnNum).getCells().stream()
                        .filter(cell -> cell.getNumInside() != Cell.EMPTY)
                        .collect(Collectors.toList());
    }
    
    public static List<Cell> getRowCellsFilled(int row, Board board) {
        List<Cell> allPresentValuesInRow = new ArrayList<>();
        for (int column = 0; column < 9; column++) {
            Cell cell = board.getColumn(column).getCell(row);
            if (cell.getNumInside() != Cell.EMPTY) {
                allPresentValuesInRow.add(cell);
            }
        }
        return allPresentValuesInRow;
    }
    

    public void highlightDuplicate(List<Cell> allPresentValues) {
        Map<Integer, Integer> countOccurrences = new HashMap<>();

        for (int i = 1; i < 10; i++) {
            countOccurrences.put(i, 0);
        }

        allPresentValues.forEach(cell -> {
            int num = cell.getNumInside();
            int updatedValue = countOccurrences.get(num) + 1;
            countOccurrences.put(num, updatedValue);
        });

        List<Integer> valuesToHighlight = countOccurrences.entrySet().stream()
                .filter(integerIntegerEntry -> integerIntegerEntry.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        allPresentValues.stream()
                .filter(cell -> valuesToHighlight.contains(cell.getNumInside()))
                .forEach(cell -> {
                    textFields[cell.getColumnNum()][cell.getRowNum()].setStyle("-fx-background-color: red");
                });
    }

    

    public Board getBoard() {
        return board;
    }
}
