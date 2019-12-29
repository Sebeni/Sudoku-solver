package Sudoku.Mechanic;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResolverTestSuite {


    @Test
    public void testResolveEasySudoku() {

//        given
        Board board = createEmptyBoard();
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
        CellCreator mockCreator = Mockito.mock(CellCreator.class);
        Mockito.when(mockCreator.getBoard()).thenReturn(board);
        Resolver resolver = new Resolver(mockCreator);
        
//        when
        Board result = resolver.resolve();
        
//        then
        
        Assert.assertEquals(3, result.getColumn(0).getCell(0).getNumInside());
        Assert.assertEquals(7, result.getColumn(1).getCell(0).getNumInside());
        Assert.assertEquals(1, result.getColumn(8).getCell(0).getNumInside());
        


    }

    private Board createEmptyBoard() {
        Board board = new Board();

        for (int column = 0; column < 9; column++) {
            Column c = new Column(column);
            for (int row = 0; row < 9; row++) {
                c.getCells().add(new Cell(Cell.EMPTY, column, row));
            }
            board.getColumns().add(c);
        }
        return board;
    }


    private void fillBoard(Board board, String columnRowValue) {
        char[] numbers = columnRowValue.toCharArray();

        for (int i = 0; i < numbers.length; i += 3) {
            int column = Character.getNumericValue(numbers[i]);
            int row = Character.getNumericValue(numbers[i + 1]);
            int value = Character.getNumericValue(numbers[i + 2]);

            board.getColumn(column).getCell(row).setNumInside(value);
//                    set(row, new Cell(value, column, row));

        }

//        just for debug
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                System.out.print(board.getColumn(y).getCell(x));
            }
            System.out.println();
        }

    }


}
