package Sudoku.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Column {
    private int columnNumber;
    private List<Cell> cells = new ArrayList<>(9);

    public Column(int columnNumber) {
        this.columnNumber = columnNumber;
    }

    public List<Cell> getCells() {
        return cells;
    }
    
    public Cell getCell(int rowNum) {
        return cells.get(rowNum);
    }

    public int getColumnNumber() {
        return columnNumber;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Column)) return false;
        Column column = (Column) o;
        return columnNumber == column.columnNumber &&
                cells.equals(column.cells);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnNumber, cells);
    }
}
