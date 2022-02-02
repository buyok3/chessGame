package board;

import pieces.Piece;

public abstract class Move {
    final Board board;
    final Piece piece;
    final int destinationCoordinate;

    private Move(final Board board,
                 final Piece piece,
                 final int destinationCoordinate) {
        this.board = board;
        this.piece = piece;
        this.destinationCoordinate = destinationCoordinate;
    }

    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }

    public abstract Board execute();

    public static final class RegularMove extends Move {
        public RegularMove(final Board board,
                           final Piece piece,
                           final int destinationCoordinate) {
            super(board, piece, destinationCoordinate);
        }

        @Override
        public Board execute() {
            return null;
        }
    }

    public static final class AttackMove extends Move {
        final Piece attackedPiece;

        public AttackMove(final Board board,
                          final Piece piece,
                          final int destinationCoordinate,
                          final Piece attackedPiece) {
            super(board, piece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public Board execute() {
            return null;
        }
    }
}