package pieces;

import board.Board;
import board.Move;

import java.util.Collection;

public abstract class Piece {
    protected final int pieceCoordinate;
    protected final Alliance pieceAlliance;

    protected Piece(final int pieceCoordinate, final Alliance pieceAlliance) {
        this.pieceCoordinate = pieceCoordinate;
        this.pieceAlliance = pieceAlliance;
    }

    public abstract Collection<Move> calculateLegalMoves(final Board board);

    public Alliance getPieceAlliance() {
        return this.pieceAlliance;
    }
}
