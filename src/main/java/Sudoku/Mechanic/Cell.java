package Sudoku.Mechanic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Cell {
    private final int numInside;
    private int row;
    private int column;
    
    
    private List<Integer> possibleChoices = new ArrayList<>();
    public static int EMPTY = -1;

    public Cell(int numInside) {
        this.numInside = numInside;
        if(numInside == EMPTY){
            for(int i = 1; i < 11; i++) {
                possibleChoices.add(i);
            }
        }
    }


    public Cell(int numInside, int column, int row) {
        this.numInside = numInside;
        this.column = column;
        this.row = row;
    }

    @Override
    public String toString() {
        return numInside == EMPTY ? "| _ " : "| "+ numInside +" ";
    }

    public int getNumInside() {
        return numInside;
    }
    
    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
    
    
}
