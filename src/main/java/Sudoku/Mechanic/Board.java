package Sudoku.Mechanic;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private List<Column> columns = new ArrayList<>(9);

    public List<Column> getColumns() {
        return columns;
    }
    
    public Column getColumn(int index) {
        return columns.get(index);
    }
}
