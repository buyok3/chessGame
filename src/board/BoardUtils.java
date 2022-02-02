package board;

public class BoardUtils {
    public static final boolean[] FIRST_COLUMN = initColumn(0);
    public static final boolean[] SECOND_COLUMN = initColumn(1);
    public static final boolean[] SEVENTH_COLUMN = initColumn(6);
    public static final boolean[] EIGHTH_COLUMN = initColumn(7);

    public static final boolean[] SECOND_ROW = initRow(8);
    public static final boolean[] SEVENTH_ROW = initRow(48);

    public static final int TILES_NUMBER = 64;
    public static final int TILES_NUMBER_PER_ROW = 8;

    private BoardUtils(){
        throw new RuntimeException("You can't do that!");
    }

    private static boolean[] initColumn(int columnNumber){
        final boolean[] column = new boolean[TILES_NUMBER];

        do{
            column[columnNumber] = true;
            columnNumber += TILES_NUMBER_PER_ROW;
        } while (columnNumber < TILES_NUMBER);

        return column;
    }

    private static boolean[] initRow(int rowNumber){
        final boolean[] row = new boolean[TILES_NUMBER];
        do{
            row[rowNumber] = true;
            rowNumber++;
        } while(rowNumber % TILES_NUMBER_PER_ROW != 0);
        return row;
    }

    public static boolean isPossibleCoordinate(final int coordinate) {
        return coordinate >= 0 && coordinate < TILES_NUMBER;
    }
}