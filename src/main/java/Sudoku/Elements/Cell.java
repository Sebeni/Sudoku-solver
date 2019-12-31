package Sudoku.Elements;

import Sudoku.Algorithm.Resolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Cell  {
    private int numInside;
    private int rowNum;
    private int columnNum;
    
    
    private List<Integer> possibleChoices = new ArrayList<>();
    public static int EMPTY = -1;
    

    public Cell(int numInside, int columnNum, int rowNum) {
        this.numInside = numInside;
        this.columnNum = columnNum;
        this.rowNum = rowNum;

        if(numInside == EMPTY){
            for(int i = 1; i < 10; i++) {
                possibleChoices.add(i);
            }
        }
    }

    @Override
    public String toString() {
        return numInside == EMPTY ? "| _ " : "| "+ numInside +" ";
    }
    
    

    public int getNumInside() {
        return numInside;
    }
    
    public int getRowNum() {
        return rowNum;
    }

    public int getColumnNum() {
        return columnNum;
    }

    public List<Integer> getPossibleChoices() {
        return possibleChoices;
    }

    public void setNumInside(int numInside) {
        this.numInside = numInside;
    }
    
    public void setNumInsideClone(int numInside){
        this.numInside = numInside;
    }

    public void setPossibleChoices(List<Integer> possibleChoices) {
        this.possibleChoices = new ArrayList<>(possibleChoices);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cell)) return false;
        Cell cell = (Cell) o;
        return numInside == cell.numInside &&
                rowNum == cell.rowNum &&
                columnNum == cell.columnNum;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numInside, rowNum, columnNum);
    }
}
