package pieces;

import board.Board;
import board.Move;

import java.util.Collection;

public abstract class Piece {
    protected final PieceType pieceType;
    protected final int pieceCoordinate;
    protected final Alliance pieceAlliance;
    protected final boolean isFirstMove;
    private final int cachedHashCode;

    protected Piece(final PieceType pieceType,
                    final int pieceCoordinate,
                    final Alliance pieceAlliance) {
        this.pieceType = pieceType;
        this.pieceCoordinate = pieceCoordinate;
        this.pieceAlliance = pieceAlliance;
        this.isFirstMove = false;
        this.cachedHashCode = computeHashCode();
    }

    @Override
    public boolean equals(final Object other){
        if(this == other)
            return true;
        if(!(other instanceof Piece))
            return false;
        final Piece otherPiece = (Piece) other;

        return pieceAlliance == otherPiece.getPieceAlliance() && pieceCoordinate == otherPiece.getPiecePosition() &&
               pieceType == otherPiece.getPieceType() && isFirstMove == otherPiece.isFirstMove();
    }

    @Override
    public int hashCode(){
        return this.cachedHashCode;
    }

    public int computeHashCode(){
        int result = pieceType.hashCode();
        result = 31 * result + pieceAlliance.hashCode();
        result = 31 * result + pieceCoordinate;
        result = 31 * result + (isFirstMove ? 1 : 0);
        return result;
    }

    public boolean isFirstMove() {
        return this.isFirstMove;
    }

    public int getPiecePosition() {
        return this.pieceCoordinate;
    }

    public abstract Collection<Move> calculateLegalMoves(final Board board);

    public abstract Piece movePiece(Move move);

    public Alliance getPieceAlliance() {
        return this.pieceAlliance;
    }

    public PieceType getPieceType() {
        return this.pieceType;
    }

    public enum PieceType {
        PAWN("P") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KNIGHT("N") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        BISHOP("B") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        ROOK("R") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return true;
            }
        },
        QUEEN("Q") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KING("K") {
            @Override
            public boolean isKing() {
                return true;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        };

        private final String pieceName;

        PieceType(final String pieceName) {
            this.pieceName = pieceName;
        }

        @Override
        public String toString() {
            return this.pieceName;
        }

        public abstract boolean isKing();
        public abstract boolean isRook();
    }
}
