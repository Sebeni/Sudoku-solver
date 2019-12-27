package Sudoku.Mechanic;

import java.util.ArrayList;
import java.util.List;

public class Square {
    private List<Cell> squareCells = new ArrayList<Cell>(9);

    public void addCells(Cell... cellsToAdd) {
        for(Cell c : cellsToAdd){
            squareCells.add(c);
        }
    }

    public List<Cell> getSquareCells() {
        return squareCells;
    }


    /*
   | 0 | 3 | 6 |
   | 1 | 4 | 7 |
   | 2 | 5 | 8 |
    */
    public static List<Square> groupCellsToSquare(List<List<Cell>> board) {
        List<Square> result = new ArrayList<>();
        
        for(int column = 0; column < 9; column += 3){
            Square firstUp = new Square();
            Square secondMiddle = new Square();
            Square thirdDown = new Square();
            
            List<Cell> firstColumn = board.get(column);
            List<Cell> secondColumn = board.get(column + 1);
            List<Cell> thirdColumn = board.get(column + 2);
            
            for(int row = 0; row < 9; row++) {
                if(row < 3) {
                    firstUp.addCells(firstColumn.get(row), secondColumn.get(row), thirdColumn.get(row));
                } else if (row < 6) {
                    secondMiddle.addCells(firstColumn.get(row), secondColumn.get(row), thirdColumn.get(row));
                } else {
                    thirdDown.addCells(firstColumn.get(row), secondColumn.get(row), thirdColumn.get(row));
                }
                
            }
            result.add(firstUp);
            result.add(secondMiddle);
            result.add(thirdDown);
        }
        
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        for(int i = 0; i < squareCells.size(); i++) {
            sb.append(squareCells.get(i));
            if(i % 3 == 2) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
