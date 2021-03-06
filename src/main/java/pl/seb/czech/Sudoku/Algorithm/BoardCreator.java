package pl.seb.czech.Sudoku.Algorithm;

import pl.seb.czech.Sudoku.Elements.Board;
import pl.seb.czech.Sudoku.Elements.Cell;
import javafx.scene.control.TextField;

import java.util.*;
import java.util.stream.Collectors;

public class BoardCreator {
    private final TextField[][] textFields;
    private final Board board = new Board();


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
        
        InputValidator validator = new InputValidator(this);
        return validator.checkInputData();
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
                .forEach(cell -> textFields[cell.getColumnNum()][cell.getRowNum()].setStyle("-fx-background-color: red"));
    }

    

    public Board getBoard() {
        return board;
    }
}
