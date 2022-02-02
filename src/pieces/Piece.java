package pieces;

import board.Board;
import board.Move;

import java.util.Collection;

public abstract class Piece {
    protected final PieceType pieceType;
    protected final int pieceCoordinate;
    protected final Alliance pieceAlliance;
    protected final boolean isFirstMove;

    protected Piece(final PieceType pieceType,
                    final int pieceCoordinate,
                    final Alliance pieceAlliance) {
        this.pieceType = pieceType;
        this.pieceCoordinate = pieceCoordinate;
        this.pieceAlliance = pieceAlliance;
        this.isFirstMove = false;
    }

    public boolean isFirstMove(){
        return this.isFirstMove;
    }

    public int getPiecePosition() {
        return this.pieceCoordinate;
    }

    public abstract Collection<Move> calculateLegalMoves(final Board board);

    public Alliance getPieceAlliance() {
        return this.pieceAlliance;
    }

    public PieceType getPieceType() {
        return this.pieceType;
    }

    public enum PieceType{
        PAWN("P"){
            @Override
            public boolean isKing(){
                return false;
            }
        },
        KNIGHT("N"){
            @Override
            public boolean isKing(){
                return false;
            }
        },
        BISHOP("B"){
            @Override
            public boolean isKing(){
                return false;
            }
        },
        ROOK("R"){
            @Override
            public boolean isKing(){
                return false;
            }
        },
        QUEEN("Q"){
            @Override
            public boolean isKing(){
                return false;
            }
        },
        KING("K"){
            @Override
            public boolean isKing(){
                return true;
            }
        };

        private final String pieceName;

        PieceType(final String pieceName){
            this.pieceName = pieceName;
        }

        @Override
        public String toString(){
            return this.pieceName;
        }

        public abstract boolean isKing();
    }
}
