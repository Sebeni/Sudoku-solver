package Sudoku.Mechanic;

import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Resolver {
    private final CellCreator creator;
    private final Board board;

    public Resolver(CellCreator creator) {
        this.creator = creator;
        board = creator.getBoard();
    }
    
    public Board resolve() {
        
        
        for(int column = 0; column < board.getColumns().size(); column++) {
            for(int row = 0; row < board.getColumn(column).getCells().size(); row++) {
                
                Cell cell = board.getColumn(column).getCell(row);
                if(cell.getNumInside() == Cell.EMPTY) {
                    if(cell.getPossibleChoices().size() > 1) {
                        //deleting from choices
                        
                        
                    } else {
                        int numToPlace = cell.getPossibleChoices().get(0);
                        cell.setNumInside(numToPlace);
                        

                    }
                    
                    
                }
            }
        }
        
        return board;
        
    }
    
    private List<Cell> getOtherCellsInRow (Cell cell) {
        
        List<Cell> result = new ArrayList<>();
        int row = cell.getRow();
        
        for(Column column : board.getColumns()) {
            if(!column.getCell(row).equals(cell)) {
                result.add(column.getCell(row));
            }
        }
        
        return result;
    }
    
    
    
    private void deleteRowOptions(Cell cell) {
        List<Integer> alreadyFilledCellsNumbers = CellCreator.getRowCellsFilled(cell.getColumn(), board).stream()
                .map(Cell::getNumInside)
                .collect(Collectors.toList());
        cell.getPossibleChoices().removeAll(alreadyFilledCellsNumbers);
        
        
        
        
        
        
        
    }
    
    private void fillTextFields(Cell cell, int numToPlace) {
        TextField[][] textFields = creator.getCurrentGame().getTextFields();
        textFields[cell.getColumn()][cell.getRow()].setText(String.valueOf(numToPlace));
    }
    
    

}
