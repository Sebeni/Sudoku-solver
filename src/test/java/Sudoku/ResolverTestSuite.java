package Sudoku;

import Sudoku.Algorithm.BoardCreator;
import Sudoku.Elements.*;
import Sudoku.Algorithm.Resolver;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.*;
import java.util.stream.Collectors;

public class ResolverTestSuite {


    @Test
    public void testResolveEasySudoku() {

//        given
        Board board = new Board();
        String values =
                "015" +
                        "022" +
                        "047" +
                        "068" +
                        "074" +

                        "111" +
                        "148" +
                        "152" +
                        "185" +

                        "206" +
                        "219" +
                        "228" +
                        "277" +
                        "281" +

                        "313" +
                        "344" +
                        "365" +

                        "416" +
                        "442" +
                        "471" +

                        "527" +
                        "541" +
                        "578" +

                        "609" +
                        "612" +
                        "664" +
                        "673" +
                        "688" +

                        "704" +
                        "733" +
                        "749" +
                        "775" +

                        "818" +
                        "823" +
                        "845" +
                        "869" +
                        "876";

        fillBoard(board, values);

        BoardCreator mockCreator = Mockito.mock(BoardCreator.class);

        Mockito.when(mockCreator.getBoard()).thenReturn(board);
        Resolver resolver = new Resolver(mockCreator.getBoard());

//        when
        Board result = resolver.resolve();

        printBoard(result);


//        then

        Assert.assertEquals(3, result.getColumn(0).getCell(0).getNumInside());
        Assert.assertEquals(7, result.getColumn(1).getCell(0).getNumInside());
        Assert.assertEquals(1, result.getColumn(8).getCell(0).getNumInside());
        Assert.assertTrue(onlyUniqueNumbers(result));
    }

    @Test
    public void testResolveSudokuHard() {
        Board board = new Board();
        String values =
                "031" +
                        "042" +
                        "069" +
                        "078" +
                        "137" +
                        "245" +
                        "258" +
                        "267" +
                        "308" +
                        "325" +
                        "343" +
                        "361" +
                        "384" +
                        "411" +
                        "422" +
                        "446" +
                        "468" +
                        "477" +
                        "503" +
                        "527" +
                        "544" +
                        "565" +
                        "586" +
                        "621" +
                        "636" +
                        "647" +
                        "755" +
                        "814" +
                        "826" +
                        "841" +
                        "852";

        fillBoard(board, values);

        BoardCreator mockCreator = Mockito.mock(BoardCreator.class);

        Mockito.when(mockCreator.getBoard()).thenReturn(board);
        Resolver resolver = new Resolver(mockCreator.getBoard());

//        when
        Board result = resolver.resolve();

        printBoard(result);

//        then

        Assert.assertTrue(onlyUniqueNumbers(result));
//        Assert.assertEquals(4, result.getColumn(0).getCell(0).getNumInside());
//        Assert.assertEquals(8, result.getColumn(0).getCell(0).getNumInside());
//        Assert.assertEquals(2, result.getColumn(0).getCell(0).getNumInside());
    }


    private void fillBoard(Board board, String columnRowValue) {
        char[] numbers = columnRowValue.toCharArray();

        for (int i = 0; i < numbers.length; i += 3) {
            int column = Character.getNumericValue(numbers[i]);
            int row = Character.getNumericValue(numbers[i + 1]);
            int value = Character.getNumericValue(numbers[i + 2]);

            board.getColumn(column).getCell(row).setNumInside(value);

        }
    }

    private void printBoard(Board board) {
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                System.out.print(board.getColumn(y).getCell(x));

            }
            System.out.println();

        }
    }
    
    @Test
    public void testCloning() throws Exception {
        Board original = new Board();
        Cell cellOriginal = original.getColumn(0).getCell(0);
        
        Board clone = original.clone();
        Cell cellCopy = clone.getColumn(0).getCell(0);
        
        Assert.assertEquals(cellOriginal, cellCopy);
        
        cellCopy.setNumInside(2);
        Assert.assertNotEquals(cellOriginal, cellCopy);
        
    }
    
    private boolean onlyUniqueNumbers(Board filledBoard) {
        Map<Column, Set<Integer>> allValuesInColumns = new HashMap<>();
        
        for(Column c : filledBoard.getColumns()){
            Set<Integer> valuesInColumn = c.getCells().stream()
                    .map(cell -> cell.getNumInside())
                    .collect(Collectors.toSet());

            allValuesInColumns.put(c, valuesInColumn);
        }
        
        boolean areColumnsIncorrect = allValuesInColumns.entrySet().stream()
                .anyMatch(columnSetEntry -> columnSetEntry.getValue().size() != 9);
        
        
        Map<Box, Set<Integer>> allValuesInBoxes = new HashMap<>();
        
        for(Box b : filledBoard.getBoxes()){
            Set<Integer> valuesInBox = b.getCellsInBox().stream()
                    .map(cell -> cell.getNumInside())
                    .collect(Collectors.toSet());
            
            allValuesInBoxes.put(b, valuesInBox);
        }

        boolean areBoxesIncorrect = allValuesInBoxes.entrySet().stream()
                .anyMatch(columnSetEntry -> columnSetEntry.getValue().size() != 9);
        
        
        
        Map<List<Cell>, Set<Integer>> allValuesInRows = new HashMap<>();
        
        
        for(int row = 0; row < 9; row++) {
            List<Cell> cellsInRow = new ArrayList<>();
            for (int columnNum = 0; columnNum < filledBoard.getColumns().size(); columnNum++) {
                cellsInRow.add(filledBoard.getColumn(columnNum).getCell(row));
            }
            
            Set<Integer> valuesInRow = cellsInRow.stream()
                    .map(cell -> cell.getNumInside())
                    .collect(Collectors.toSet());
            
            allValuesInRows.put(cellsInRow, valuesInRow);
        }
        
        boolean areRowsIncorrect = allValuesInRows.entrySet().stream()
                .anyMatch(columnSetEntry -> columnSetEntry.getValue().size() != 9);
        
        
        return !areColumnsIncorrect && !areBoxesIncorrect && !areRowsIncorrect;
        
    }

    


}
