package Sudoku.Algorithm;

import Sudoku.Elements.Board;
import Sudoku.Elements.Box;
import Sudoku.Elements.Cell;
import Sudoku.Elements.Column;
import Sudoku.Layout.AlertWindow;

import java.util.*;
import java.util.stream.Collectors;

public class Resolver {
    private Board board;
    private final LinkedList<BoardState> backTrack = new LinkedList<>();
    private boolean actionTaken;
   
    public boolean stopIteration = false;


    public Resolver(Board board) {
        this.board = board;
    }

    public Board resolve() throws CloneNotSupportedException {
        long emptyCounter = board.getColumns().stream()
                .flatMap(c -> c.getCells().stream())
                .filter(cll -> cll.getNumInside() == Cell.EMPTY)
                .count();
        
        int iterationCounter = 1;

        while (emptyCounter > 0 && !stopIteration) {
            actionTaken = false;

            for (int column = 0; column < board.getColumns().size(); column++) {
                for (int row = 0; row < board.getColumn(column).getCells().size(); row++) {
                    Cell cell = board.getColumn(column).getCell(row);

                    if (cell.getNumInside() != Cell.EMPTY) {
                        continue;
                    }

                    if (cell.getPossibleChoices().size() > 1) {
                        deleteFromPossibleChoices(cell);
                    } else if (cell.getPossibleChoices().size() == 1) {
                        if (isLastChoiceUnique(cell)) {
                            cell.setNumInside(cell.getPossibleChoices().get(0));
                            actionTaken = true;
                        } else {
                            startBackTrack();
                        }
                    } else {
                        startBackTrack();
                    }
                }
                emptyCounter = board.getColumns().stream()
                        .flatMap(c -> c.getCells().stream())
                        .filter(cll -> cll.getNumInside() == Cell.EMPTY)
                        .count();
            }
            
            if (!actionTaken && emptyCounter > 0) {
                startGuessing();
            }

            iterationCounter++;
        }

        return board;
    }

    private void startBackTrack() throws CloneNotSupportedException {
        if (backTrack.size() > 1) {
            if (backTrack.getLast().getPossibleChoices().size() > 0) {
                getAnotherGuessedValue();
            } else {
                getBackOneBacktrack();
            }
        } else if (backTrack.size() == 1 && backTrack.getLast().getPossibleChoices().size() > 0) {
            getAnotherGuessedValue();
        } else {
            AlertWindow.display("Impossible", "Sudoku cannot be resolved. Check input numbers!");
            stopIteration = true;
        }
    }

    private void deleteFromPossibleChoices(Cell cell) {
        int sizeBefore = cell.getPossibleChoices().size();
        deleteRowChoices(cell);
        deleteColumnChoices(cell);
        deleteSquareChoices(cell);
        int sizeAfter = cell.getPossibleChoices().size();

        if (sizeBefore > sizeAfter) {
            actionTaken = true;
        }

    }


    private void getBackOneBacktrack() throws CloneNotSupportedException {
        backTrack.pollLast();
        BoardState lastGoodBackTrack = backTrack.getLast();
        board = lastGoodBackTrack.getClonedBoard().clone();
        startBackTrack();
    }

    private void getAnotherGuessedValue() throws CloneNotSupportedException {
        BoardState lastBoardState = backTrack.getLast();
        board = lastBoardState.getClonedBoard().clone();
        continueGuessing(lastBoardState);
    }


    private void deleteRowChoices(Cell cell) {
        List<Integer> alreadyFilledCellsInRowNumsInside = BoardCreator.getRowCellsFilled(cell.getRowNum(), board).stream()
                .map(Cell::getNumInside)
                .collect(Collectors.toList());

        cell.getPossibleChoices().removeAll(alreadyFilledCellsInRowNumsInside);

        Set<Integer> allPossibleChoicesOtherCells = getOtherCellsInRow(cell).stream()
                .flatMap(cell1 -> cell1.getPossibleChoices().stream())
                .collect(Collectors.toSet());

        trySetNumInside(cell, alreadyFilledCellsInRowNumsInside, allPossibleChoicesOtherCells);
    }

    private void startGuessing() {
        List<Cell> allEmptyCells = board.getColumns().stream()
                .flatMap(column -> column.getCells().stream())
                .filter(cell -> cell.getNumInside() == Cell.EMPTY)
                .collect(Collectors.toList());

        Cell guessedCell = allEmptyCells.get(0);

        BoardState boardState = new BoardState(board, guessedCell);
        backTrack.add(boardState);
        guessedCell.setNumInside(boardState.getGuessedValue());
        actionTaken = true;
    }

    private void continueGuessing(BoardState lastBoardState) {
        Cell guessedCell = board.getColumn(lastBoardState.getColumnNumOfGuessedCell()).getCell(lastBoardState.getRowNumOfGuessedCell());
        guessedCell.setNumInside(lastBoardState.getGuessedValue());
    }

    private boolean isLastChoiceUnique(Cell cellToCheck) {
        List<Integer> allValuesInColumnRowBox = new ArrayList<>();

        List<Integer> alreadyFilledCellsInColumnNumsInside = BoardCreator.getColumnCellsFilled(cellToCheck.getColumnNum(), board).stream()
                .map(Cell::getNumInside)
                .collect(Collectors.toList());

        List<Integer> alreadyFilledCellsInBoxNumsInside = board.getBox(cellToCheck).getCellsInBox().stream()
                .map(Cell::getNumInside)
                .collect(Collectors.toList());

        List<Integer> alreadyFilledCellsInRowNumsInside = BoardCreator.getRowCellsFilled(cellToCheck.getRowNum(), board).stream()
                .map(Cell::getNumInside)
                .collect(Collectors.toList());

        allValuesInColumnRowBox.addAll(alreadyFilledCellsInColumnNumsInside);
        allValuesInColumnRowBox.addAll(alreadyFilledCellsInBoxNumsInside);
        allValuesInColumnRowBox.addAll(alreadyFilledCellsInRowNumsInside);

        return !allValuesInColumnRowBox.contains(cellToCheck.getPossibleChoices().get(0));
    }


    private void deleteColumnChoices(Cell cell) {
        List<Integer> alreadyFilledCellsInColumnNumsInside = BoardCreator.getColumnCellsFilled(cell.getColumnNum(), board).stream()
                .map(Cell::getNumInside)
                .collect(Collectors.toList());

        cell.getPossibleChoices().removeAll(alreadyFilledCellsInColumnNumsInside);

        Set<Integer> allPossibleChoicesOtherCells = board.getColumn(cell.getColumnNum()).getCells().stream()
                .filter(cell1 -> !cell1.equals(cell))
                .flatMap(cell1 -> cell1.getPossibleChoices().stream())
                .collect(Collectors.toSet());

        trySetNumInside(cell, alreadyFilledCellsInColumnNumsInside, allPossibleChoicesOtherCells);
    }

    private void deleteSquareChoices(Cell cell) {
        Box boxWithCell = board.getBox(cell);

        List<Integer> alreadyFilledCellsInBoxNumsInside = boxWithCell.getCellsInBox().stream()
                .map(Cell::getNumInside)
                .collect(Collectors.toList());

        cell.getPossibleChoices().removeAll(alreadyFilledCellsInBoxNumsInside);

        Set<Integer> allPossibleChoicesOtherCells = board.getColumn(cell.getColumnNum()).getCells().stream()
                .filter(cell1 -> !cell1.equals(cell))
                .flatMap(cell1 -> cell1.getPossibleChoices().stream())
                .collect(Collectors.toSet());

        trySetNumInside(cell, alreadyFilledCellsInBoxNumsInside, allPossibleChoicesOtherCells);
    }

    private void trySetNumInside(Cell cell, List<Integer> alreadyFilledCellsInNumsInside, Set<Integer> allPossibleChoicesOtherCells) {
        if (cell.getPossibleChoices().size() > 0) {
            for (Integer i : cell.getPossibleChoices()) {
                if (!allPossibleChoicesOtherCells.contains(i) && !alreadyFilledCellsInNumsInside.contains(i) && isLastChoiceUnique(cell)) {
                    cell.setNumInside(i);
                    cell.getPossibleChoices().clear();
                    actionTaken = true;
                    break;
                }
            }
        }
    }

    private List<Cell> getOtherCellsInRow(Cell cell) {

        List<Cell> result = new ArrayList<>();
        int row = cell.getRowNum();

        for (Column column : board.getColumns()) {
            if (!column.getCell(row).equals(cell)) {
                result.add(column.getCell(row));
            }
        }

        return result;
    }
}
