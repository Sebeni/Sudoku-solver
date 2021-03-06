package pl.seb.czech.Sudoku.Algorithm;

import pl.seb.czech.Sudoku.Elements.Board;
import pl.seb.czech.Sudoku.Elements.Cell;

import java.util.LinkedList;

public class BoardState {
    private Board clonedBoard;

    private final int columnNumOfGuessedCell;
    private final int rowNumOfGuessedCell;
    private final LinkedList<Integer> possibleChoices;

    
    public BoardState(Board boardToClone, Cell guessedCell) {
        try {
            this.clonedBoard = boardToClone.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    
        this.columnNumOfGuessedCell = guessedCell.getColumnNum();
        this.rowNumOfGuessedCell = guessedCell.getRowNum();
        this.possibleChoices = new LinkedList<>(guessedCell.getPossibleChoices());
    }

    public Board getClonedBoard() {
        return clonedBoard;
    }

   

    public int getColumnNumOfGuessedCell() {
        return columnNumOfGuessedCell;
    }

    public int getRowNumOfGuessedCell() {
        return rowNumOfGuessedCell;
    }
    
    
    public Integer getGuessedValue(){
        Integer guessedValue = possibleChoices.poll();
        clonedBoard.getColumn(columnNumOfGuessedCell).getCell(rowNumOfGuessedCell).getPossibleChoices().remove(guessedValue);
        return guessedValue;
    }
    
    public LinkedList<Integer> getPossibleChoices() {
        return possibleChoices;
    }
}
