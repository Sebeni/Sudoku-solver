package Sudoku.Algorithm;

import Sudoku.Elements.Board;
import Sudoku.Elements.Box;
import Sudoku.Elements.Cell;
import Sudoku.Elements.Column;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Resolver {
    private final Board board;

    public Resolver(Board board) {
        this.board = board;
    }

    public Board resolve() {
        long emptyCounter = 1;
        int iterationCounter = 0;

        do {
            for (int column = 0; column < board.getColumns().size(); column++) {
                for (int row = 0; row < board.getColumn(column).getCells().size(); row++) {

                    Cell cell = board.getColumn(column).getCell(row);
                    if (cell.getNumInside() == Cell.EMPTY) {
                        if (cell.getPossibleChoices().size() > 1) {
                            //deleting from choices
                            deleteRowOptions(cell);
                            deleteColumnOptions(cell);
                            deleteSquareOptions(cell);

                        } else {
                            int numToPlace = cell.getPossibleChoices().get(0);
                            cell.setNumInside(numToPlace);
                            System.out.println("addding value: " + numToPlace + " column: " + cell.getColumnNum() + " row: " + cell.getRowNum());
                        }
                    }
                }
            }
            emptyCounter = board.getColumns().stream()
                    .flatMap(column -> column.getCells().stream())
                    .filter(cell -> cell.getNumInside() == Cell.EMPTY)
                    .count();
            
            System.out.println(++iterationCounter);

        } while (emptyCounter > 0);
        
        return board;

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
        List<Integer> alreadyFilledCellsRowNumbers = BoardCreator.getRowCellsFilled(cell.getRowNum(), board).stream()
                .map(Cell::getNumInside)
                .collect(Collectors.toList());

        cell.getPossibleChoices().removeAll(alreadyFilledCellsRowNumbers);

        Set<Integer> allPossibleChoicesOtherCells = getOtherCellsInRow(cell).stream()
                .flatMap(cell1 -> cell1.getPossibleChoices().stream())
                .collect(Collectors.toSet());

        for (Integer i : cell.getPossibleChoices()) {
            if (!allPossibleChoicesOtherCells.contains(i) && !alreadyFilledCellsRowNumbers.contains(i)) {
                cell.setNumInside(i);
            }
        }
    }

    private void deleteColumnOptions(Cell cell) {
        List<Integer> alreadyFilledCellsColumnNumbers = BoardCreator.getColumnCellsFilled(cell.getColumnNum(), board).stream()
                .map(Cell::getNumInside)
                .collect(Collectors.toList());

        cell.getPossibleChoices().removeAll(alreadyFilledCellsColumnNumbers);

        Set<Integer> allPossibleChoicesOtherCells = board.getColumn(cell.getColumnNum()).getCells().stream()
                .filter(cell1 -> !cell1.equals(cell))
                .flatMap(cell1 -> cell1.getPossibleChoices().stream())
                .collect(Collectors.toSet());

        for (Integer i : cell.getPossibleChoices()) {
            if (!allPossibleChoicesOtherCells.contains(i) && !alreadyFilledCellsColumnNumbers.contains(i)) {
                cell.setNumInside(i);
            }
        }
    }

    private void deleteSquareOptions(Cell cell) {
        Box boxWithCell = board.getBox(cell);

        List<Integer> alreadyFilledCellsBoxNumbers = boxWithCell.getCellsInBox().stream()
                .map(Cell::getNumInside)
                .collect(Collectors.toList());

        cell.getPossibleChoices().removeAll(alreadyFilledCellsBoxNumbers);

        Set<Integer> allPossibleChoicesOtherCells = board.getColumn(cell.getColumnNum()).getCells().stream()
                .filter(cell1 -> !cell1.equals(cell))
                .flatMap(cell1 -> cell1.getPossibleChoices().stream())
                .collect(Collectors.toSet());

        for (Integer i : cell.getPossibleChoices()) {
            if (!allPossibleChoicesOtherCells.contains(i) && !alreadyFilledCellsBoxNumbers.contains(i)) {
                cell.setNumInside(i);
            }
        }

    }


}
