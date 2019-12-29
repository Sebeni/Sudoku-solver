package Sudoku.Mechanic;

import Sudoku.Layout.SudokuBox;
import javafx.scene.control.TextField;

import java.util.*;
import java.util.stream.Collectors;

public class CellCreator {
    private SudokuBox currentGame;
    private Board board = new Board();
    private List<Square> squares = new ArrayList<>();

    public CellCreator(SudokuBox currentGame) {
        this.currentGame = currentGame;
    }

    public boolean createCells() {
        TextField[][] fields = currentGame.getTextFields();

        for (int column = 0; column < 9; column++) {
            Column sudokuColumn = new Column(column);
            for (int row = 0; row < 9; row++) {
                String input = fields[column][row].getText();
                if (input.isEmpty()) {
                    sudokuColumn.getCells().add(new Cell(Cell.EMPTY, column, row));
                } else {
                    sudokuColumn.getCells().add(new Cell(Integer.parseInt(input), column, row));
                }
            }
            board.getColumns().add(sudokuColumn);
        }
        squares = Square.groupCellsToSquare(board);


        printInDebug();
        return checkInputData();
    }

    private void printInDebug() {
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                System.out.print(board.getColumn(y).getCell(x));
            }
            System.out.println();
        }

        System.out.println("SQUARES");
        for (int i = 0; i < squares.size(); i++) {
            System.out.println("square numero: " + i);
            System.out.println(squares.get(i));
        }
    }

    private boolean checkInputData() {
        return isColumnCorrect() && isRowCorrect() && isSquareCorrect();
    }


    private boolean isColumnCorrect() {
        for (int column = 0; column < 9; column++) {
            List<Cell> allPresentValuesInColumn = getColumnCellsFilled(column, board);

            Set<Integer> allIndividualValues = new HashSet<>();
            allPresentValuesInColumn.forEach(cell -> allIndividualValues.add(cell.getNumInside()));

            if (allIndividualValues.size() != allPresentValuesInColumn.size()) {
                highlightDuplicate(allPresentValuesInColumn);
                return false;
            }
        }
        return true;
    }

    public static List<Cell> getColumnCellsFilled(int columnNum, Board board) {
        return board.getColumn(columnNum).getCells().stream()
                        .filter(cell -> cell.getNumInside() != Cell.EMPTY)
                        .collect(Collectors.toList());
    }

    private boolean isRowCorrect() {
        for (int column = 0; column < 9; column++) {
            List<Cell> allPresentValuesInRow = getRowCellsFilled(column, board);
            Set<Integer> allIndividualValues = new HashSet<>();
            allPresentValuesInRow.forEach(cell -> allIndividualValues.add(cell.getNumInside()));

            if (allIndividualValues.size() != allPresentValuesInRow.size()) {
                highlightDuplicate(allPresentValuesInRow);
                return false;
            }
        }
        return true;
    }

    public static List<Cell> getRowCellsFilled(int columnNum, Board board) {
        List<Cell> allPresentValuesInRow = new ArrayList<>();
        for (int row = 0; row < 9; row++) {
            Cell cell = board.getColumn(row).getCell(columnNum);
            if (cell.getNumInside() != Cell.EMPTY) {
                allPresentValuesInRow.add(cell);
            }
        }
        return allPresentValuesInRow;
    }


    private boolean isSquareCorrect() {
        for (Square s : squares) {
            List<Cell> allPresentValuesInSquare = s.getSquareCells();
            allPresentValuesInSquare = allPresentValuesInSquare.stream().filter(cell -> cell.getNumInside() != -1).collect(Collectors.toList());

            Set<Integer> allIndividualValues = new HashSet<>();
            allPresentValuesInSquare.forEach(cell -> allIndividualValues.add(cell.getNumInside()));

            if (allIndividualValues.size() != allPresentValuesInSquare.size()) {
                highlightDuplicate(allPresentValuesInSquare);
                return false;
            }
        }
        return true;
    }

    private void highlightDuplicate(List<Cell> allPresentValues) {
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
                    currentGame.getTextFields()[cell.getColumn()][cell.getRow()].setStyle("-fx-background-color: red");
                });
    }


    public SudokuBox getCurrentGame() {
        return currentGame;
    }

    public Board getBoard() {
        return board;
    }

    public List<Square> getSquares() {
        return squares;
    }
}
