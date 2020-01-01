package Sudoku.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Box  {
    private final List<Cell> cellsInBox = new ArrayList<>(9);

    public void addCells(Cell... cellsToAdd) {
        cellsInBox.addAll(Arrays.asList(cellsToAdd));
    }

    public List<Cell> getCellsInBox() {
        return cellsInBox;
    }
    

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        for(int i = 0; i < cellsInBox.size(); i++) {
            sb.append(cellsInBox.get(i));
            if(i % 3 == 2) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Box)) return false;
        Box box = (Box) o;
        return cellsInBox.equals(box.cellsInBox);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cellsInBox);
    }
}
