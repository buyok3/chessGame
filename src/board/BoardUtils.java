package board;

public class BoardUtils {
    public static final int TILES_NUMBER = 64;
    public static final int TILES_NUMBER_PER_ROW = 8;

    private BoardUtils(){
        throw new RuntimeException("You can't do that!");
    }

    public static boolean isPossibleCoordinate(final int coordinate) {
        return coordinate >= 0 && coordinate < TILES_NUMBER;
    }
}
