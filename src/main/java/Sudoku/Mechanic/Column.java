package Sudoku.Mechanic;

import java.util.ArrayList;
import java.util.List;

public class Column {
    private int columnNumber;
    private List<Cell> cells = new ArrayList<>(9);

    public Column(int columnNumber) {
        this.columnNumber = columnNumber;
    }

    public List<Cell> getCells() {
        return cells;
    }
    
    public Cell getCell(int index) {
        return cells.get(index);
    }
}
