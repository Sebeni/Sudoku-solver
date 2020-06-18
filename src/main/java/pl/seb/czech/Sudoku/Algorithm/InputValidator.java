package pl.seb.czech.Sudoku.Algorithm;

import pl.seb.czech.Sudoku.Elements.Box;
import pl.seb.czech.Sudoku.Elements.Cell;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class InputValidator {
    private final BoardCreator boardCreator;

    public InputValidator(BoardCreator boardCreator) {
        this.boardCreator = boardCreator;
    }

    boolean checkInputData() {
        return isColumnCorrect() && isRowCorrect() && isSquareCorrect();
    }

    boolean isColumnCorrect() {
        for (int column = 0; column < 9; column++) {
            List<Cell> allPresentValuesInColumn = BoardCreator.getColumnCellsFilled(column, boardCreator.getBoard());

            Set<Integer> allIndividualValues = new HashSet<>();
            allPresentValuesInColumn.forEach(cell -> allIndividualValues.add(cell.getNumInside()));

            if (allIndividualValues.size() != allPresentValuesInColumn.size()) {
                boardCreator.highlightDuplicate(allPresentValuesInColumn);
                return false;
            }
        }
        return true;
    }

    boolean isRowCorrect() {
        for (int column = 0; column < 9; column++) {
            List<Cell> allPresentValuesInRow = BoardCreator.getRowCellsFilled(column, boardCreator.getBoard());
            Set<Integer> allIndividualValues = new HashSet<>();
            allPresentValuesInRow.forEach(cell -> allIndividualValues.add(cell.getNumInside()));

            if (allIndividualValues.size() != allPresentValuesInRow.size()) {
                boardCreator.highlightDuplicate(allPresentValuesInRow);
                return false;
            }
        }
        return true;
    }

    boolean isSquareCorrect() {
        for (Box s : boardCreator.getBoard().getBoxes()) {
            List<Cell> allPresentValuesInSquare = s.getCellsInBox();
            allPresentValuesInSquare = allPresentValuesInSquare.stream().filter(cell -> cell.getNumInside() != -1).collect(Collectors.toList());

            Set<Integer> allIndividualValues = new HashSet<>();
            allPresentValuesInSquare.forEach(cell -> allIndividualValues.add(cell.getNumInside()));

            if (allIndividualValues.size() != allPresentValuesInSquare.size()) {
                boardCreator.highlightDuplicate(allPresentValuesInSquare);
                return false;
            }
        }
        return true;
    }
}