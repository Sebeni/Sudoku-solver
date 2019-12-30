package Sudoku.Algorithm;

import Sudoku.Elements.Board;
import Sudoku.Elements.Box;
import Sudoku.Elements.Cell;
import Sudoku.Elements.Column;
import Sudoku.Layout.AlertWindow;
import Sudoku.Layout.SudokuWindow;

import java.util.*;
import java.util.stream.Collectors;

public class Resolver {
    private Board board;
    private LinkedList<BoardState> backTrack = new LinkedList<>();
    private boolean actionTaken;
    
    private SudokuWindow sd;


    public Resolver(Board board, SudokuWindow sd) {
        this.board = board;
        this.sd = sd;
    }

    public Board resolve() {
        long emptyCounter = 1;
        int iterationCounter = 1;
        
        while (emptyCounter > 0) {
            actionTaken = false;

            for (int column = 0; column < board.getColumns().size(); column++) {
                for (int row = 0; row < board.getColumn(column).getCells().size(); row++) {

                    Cell cell = board.getColumn(column).getCell(row);
                    if (cell.getNumInside() == Cell.EMPTY) {
                        if (cell.getPossibleChoices().size() > 1) {
                            //deleting from choices
                            int sizeBefore = cell.getPossibleChoices().size();
                            deleteRowOptions(cell);
                            deleteColumnOptions(cell);
                            deleteSquareOptions(cell);
                            int sizeAfter = cell.getPossibleChoices().size();
                            if (sizeBefore > sizeAfter) {
                                actionTaken = true;
                            }
                        } else {
                            if(cell.getPossibleChoices().size() == 0) {
                                int numToPlace = cell.getPossibleChoices().get(0);
                                cell.setNumInside(numToPlace);
                                System.out.println("addding value: " + numToPlace + " column: " + cell.getColumnNum() + " row: " + cell.getRowNum());

                                actionTaken = true;
                            } else {
                                actionTaken = false;
                            }
                            
                        }
                    }
                    emptyCounter = board.getColumns().stream()
                            .flatMap(c -> c.getCells().stream())
                            .filter(cll -> cll.getNumInside() == Cell.EMPTY)
                            .count();
                }
            }

            try {
                resolveNoActionTaken(emptyCounter, iterationCounter);
            } catch (CloneNotSupportedException e){
                e.printStackTrace();
            }
            
            iterationCounter++;
            System.out.println(iterationCounter);
            
            sd.fillTextFields(board);
        }
        return board;
    }

    private void resolveNoActionTaken (long emptyCounter, int iterationCounter) throws CloneNotSupportedException {
        if (!actionTaken && emptyCounter > 0) {
            System.out.println("BACKTRACK");
            if (isMoreThanTwoOptionsPresent()) {
                startGuessing(iterationCounter);
                System.out.println("START GUESSING");
            } else {
                if (backTrack.getLast().getPossibleChoices().size() > 0) {
                    BoardState lastBoardState = backTrack.getLast();
                    board = lastBoardState.getClonedBoard().clone();
                    continueGuessing(lastBoardState);
                    System.out.println("CONTINUE GUESSING");
                   
                } else if (backTrack.size() > 1) {
                    backTrack.pollLast();
                    board = backTrack.getLast().getClonedBoard().clone();
                    startGuessing(iterationCounter);
                    System.out.println("GET BACK");

                } else {
                    AlertWindow.display("Impossible", "Sudoku cannot be resolved. Check input numbers!");
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


    private void deleteRowOptions(Cell cell) {
        List<Integer> alreadyFilledCellsInRowNumsInside = BoardCreator.getRowCellsFilled(cell.getRowNum(), board).stream()
                .map(Cell::getNumInside)
                .collect(Collectors.toList());

        cell.getPossibleChoices().removeAll(alreadyFilledCellsInRowNumsInside);

        Set<Integer> allPossibleChoicesOtherCells = getOtherCellsInRow(cell).stream()
                .flatMap(cell1 -> cell1.getPossibleChoices().stream())
                .collect(Collectors.toSet());

        trySetNumInside(cell, alreadyFilledCellsInRowNumsInside, allPossibleChoicesOtherCells);
    }

    private void deleteColumnOptions(Cell cell) {
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

    private void deleteSquareOptions(Cell cell) {
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

    private void trySetNumInside(Cell cell, List<Integer> alreadyFilledCellsInRowNumsInside, Set<Integer> allPossibleChoicesOtherCells) {
        for (Integer i : cell.getPossibleChoices()) {
            if (!allPossibleChoicesOtherCells.contains(i) && !alreadyFilledCellsInRowNumsInside.contains(i)) {
                cell.setNumInside(i);
                actionTaken = true;
            }
        }
    }

    private void startGuessing(int iterationNumber) {
        List<Cell> allEmptyCells = board.getColumns().stream()
                .flatMap(column -> column.getCells().stream())
                .filter(cell -> cell.getNumInside() == Cell.EMPTY)
                
                .collect(Collectors.toList());
        
        int fewestPossibilities = allEmptyCells.stream()
                .map(cell -> cell.getPossibleChoices().size())
                .mapToInt(value -> value)
                .min().getAsInt();

        Cell guessedCell = allEmptyCells.stream().filter(cell -> cell.getPossibleChoices().size() == fewestPossibilities).findAny().get();

        BoardState boardState = new BoardState(board, iterationNumber, guessedCell);
        backTrack.add(boardState);

        guessedCell.setNumInside(boardState.getGuessedValue());
        
        actionTaken = true;
    }
    
    private void continueGuessing(BoardState lastBoardState) {
        Cell guessedCell = board.getColumn(lastBoardState.getColumnNumOfGuessedCell()).getCell(lastBoardState.getRowNumOfGuessedCell());
        
        guessedCell.setNumInside(lastBoardState.getGuessedValue());
    }

    private boolean isMoreThanTwoOptionsPresent() {
        return board.getColumns().stream()
                .flatMap(column -> column.getCells().stream())
                .anyMatch(cell -> cell.getPossibleChoices().size() > 1);

    }
    


}
