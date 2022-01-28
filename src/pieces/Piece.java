package pieces;

import board.Board;
import board.Move;

import java.util.Collection;

public abstract class Piece {
    protected final int pieceCoordinate;
    protected final Alliance pieceAlliance;
    protected final boolean isFirstMove;

    protected Piece(final int pieceCoordinate, final Alliance pieceAlliance) {
        this.pieceCoordinate = pieceCoordinate;
        this.pieceAlliance = pieceAlliance;
        this.isFirstMove = false;
    }

    public boolean isFirstMove(){
        return this.isFirstMove;
    }

    public abstract Collection<Move> calculateLegalMoves(final Board board);

    public Alliance getPieceAlliance() {
        return this.pieceAlliance;
    }
}
