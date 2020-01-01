package Sudoku.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Board implements Cloneable  {
    private final List<Column> columns = new ArrayList<>(9);
    private final List<Box> boxes = new ArrayList<>(9);

    
    public Board() {
        for (int column = 0; column < 9; column++) {
            Column c = new Column(column);
            for (int row = 0; row < 9; row++) {
                c.getCells().add(new Cell(Cell.EMPTY, column, row));
            }
            columns.add(c);
        }
        groupCellsToBoxes();
    }
    
    
    public List<Column> getColumns() {
        return columns;
    }

    public Column getColumn(int columnNum) {
        return columns.get(columnNum);
    }

    public List<Box> getBoxes() {
        return boxes;
    }


    /*
    | 0 | 3 | 6 |
    | 1 | 4 | 7 |
    | 2 | 5 | 8 |
    */
    private void groupCellsToBoxes() {

        for (int column = 0; column < 9; column += 3) {
            Box firstUp = new Box();
            Box secondMiddle = new Box();
            Box thirdDown = new Box();

            Column firstColumn = columns.get(column);
            Column secondColumn = columns.get(column + 1);
            Column thirdColumn = columns.get(column + 2);

            for (int row = 0; row < 9; row++) {
                if (row < 3) {
                    firstUp.addCells(firstColumn.getCell(row), secondColumn.getCell(row), thirdColumn.getCell(row));
                } else if (row < 6) {
                    secondMiddle.addCells(firstColumn.getCell(row), secondColumn.getCell(row), thirdColumn.getCell(row));
                } else {
                    thirdDown.addCells(firstColumn.getCell(row), secondColumn.getCell(row), thirdColumn.getCell(row));
                }
            }
            boxes.add(firstUp);
            boxes.add(secondMiddle);
            boxes.add(thirdDown);
        }
    }
    
    
    public Box getBox(Cell cellInside) {
        
        for (Box b : boxes) {
            if(b.getCellsInBox().contains(cellInside)) {
                return b;
            }
        }
        return null;
    }


    @Override
    public Board clone() throws CloneNotSupportedException {
//        Board boardClone = (Board) super.clone(); // doesn't work properly on hardest sudoku
        Board original = this;

        Board boardClone = new Board();
        
        
        for(Column column : original.getColumns()) {
            for(Cell c : column.getCells()) {
                Cell cellToClone = boardClone.getColumn(column.getColumnNumber()).getCell(c.getRowNum());
                cellToClone.setNumInsideClone(c.getNumInside());
                cellToClone.setPossibleChoices(c.getPossibleChoices());
            }
        }
        return boardClone; 
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Board)) return false;
        Board board = (Board) o;
        return columns.equals(board.columns) &&
                boxes.equals(board.boxes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columns, boxes);
    }
}
