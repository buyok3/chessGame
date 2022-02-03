package board;

import pieces.Pawn;
import pieces.Piece;
import pieces.Rook;

public abstract class Move {
    final Board board;
    final Piece movedPiece;
    final int destinationCoordinate;

    public static final Move NULL_MOVE = new NullMove();

    private Move(final Board board,
                 final Piece piece,
                 final int destinationCoordinate) {
        this.board = board;
        this.movedPiece = piece;
        this.destinationCoordinate = destinationCoordinate;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.destinationCoordinate;
        result = prime * result + this.movedPiece.hashCode();
        return result;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other)
            return true;

        if (!(other instanceof Move))
            return false;

        final Move otherMove = (Move) other;
        return super.equals(otherMove) && getAttackedPiece().equals(otherMove.getAttackedPiece());

    }

    public int getCurrentCoordinate() {
        return this.getMovedPiece().getPiecePosition();
    }

    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }

    public Piece getMovedPiece() {
        return this.movedPiece;
    }

    public boolean isAttack() {
        return false;
    }

    public boolean isCastlingMove() {
        return false;
    }

    public Piece getAttackedPiece() {
        return null;
    }

    public Board execute() {
        final Board.Builder builder = new Board.Builder();

        for (final Piece piece : this.board.currentPlayer().getAlivePieces()) {
            if (!this.movedPiece.equals(piece)) {
                builder.setPiece(piece);
            }
        }

        for (final Piece piece : this.board.currentPlayer().getOpponent().getAlivePieces()) {
            builder.setPiece(piece);
        }

        builder.setPiece(this.movedPiece.movePiece(this));
        builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());

        return builder.build();
    }

    public static final class RegularMove extends Move {
        public RegularMove(final Board board,
                           final Piece piece,
                           final int destinationCoordinate) {
            super(board, piece, destinationCoordinate);
        }
    }

    public static class AttackMove extends Move {
        final Piece attackedPiece;

        public AttackMove(final Board board,
                          final Piece piece,
                          final int destinationCoordinate,
                          final Piece attackedPiece) {
            super(board, piece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public int hashCode() {
            return this.attackedPiece.hashCode() + super.hashCode();
        }

        @Override
        public boolean equals(final Object other) {
            if (this == other)
                return true;

            if (!(other instanceof AttackMove))
                return false;

            final AttackMove otherAttackMove = (AttackMove) other;
            return getCurrentCoordinate() == otherAttackMove.getCurrentCoordinate() &&
                    getDestinationCoordinate() == otherAttackMove.getDestinationCoordinate() &&
                    getMovedPiece() == otherAttackMove.getMovedPiece() &&
                    getAttackedPiece() == otherAttackMove.getAttackedPiece();

        }

        @Override
        public Board execute() {
            return null;
        }

        @Override
        public boolean isAttack() {
            return true;
        }

        @Override
        public Piece getAttackedPiece() {
            return this.attackedPiece;
        }
    }

    public static final class PawnMove extends Move {
        public PawnMove(final Board board,
                        final Piece piece,
                        final int destinationCoordinate) {
            super(board, piece, destinationCoordinate);
        }
    }

    public static class PawnAttackMove extends AttackMove {
        public PawnAttackMove(final Board board,
                              final Piece piece,
                              final int destinationCoordinate,
                              final Piece attackedPiece) {
            super(board, piece, destinationCoordinate, attackedPiece);
        }
    }

    public static final class PawnAttackEnPassantMove extends PawnAttackMove {
        public PawnAttackEnPassantMove(final Board board,
                                       final Piece piece,
                                       final int destinationCoordinate,
                                       final Piece attackedPiece) {
            super(board, piece, destinationCoordinate, attackedPiece);
        }
    }

    public static final class PawnJumpMove extends Move {
        public PawnJumpMove(final Board board,
                            final Piece piece,
                            final int destinationCoordinate) {
            super(board, piece, destinationCoordinate);
        }

        @Override
        public Board execute() {
            final Board.Builder builder = new Board.Builder();
            for (final Piece piece : this.board.currentPlayer().getAlivePieces()) {
                if (!this.movedPiece.equals(piece)) {
                    builder.setPiece(piece);
                }
            }

            for (final Piece piece : this.board.currentPlayer().getOpponent().getAlivePieces()) {
                builder.setPiece(piece);
            }

            final Pawn movedPawn = (Pawn) this.movedPiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassantPawn(movedPawn);
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());

            return builder.build();
        }
    }

    static class CastleMove extends Move {
        protected final Rook castleRook;
        protected final int castleRookPosition;
        protected final int castleRookDestination;

        public CastleMove(final Board board,
                          final Piece piece,
                          final int destinationCoordinate,
                          final Rook castleRook,
                          final int castleRookPosition,
                          final int castleRookDestination) {
            super(board, piece, destinationCoordinate);
            this.castleRook = castleRook;
            this.castleRookPosition = castleRookPosition;
            this.castleRookDestination = castleRookDestination;
        }

        public Rook getCastleRook() {
            return this.castleRook;
        }

        @Override
        public boolean isCastlingMove(){
            return true;
        }

        @Override
        public Board execute(){
            final Board.Builder builder = new Board.Builder();
            for (final Piece piece : this.board.currentPlayer().getAlivePieces()) {
                if (!this.movedPiece.equals(piece) && !this.castleRook.equals(piece)) {
                    builder.setPiece(piece);
                }
            }

            for (final Piece piece : this.board.currentPlayer().getOpponent().getAlivePieces()) {
                builder.setPiece(piece);
            }

            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setPiece(new Rook(this.castleRookDestination, this.castleRook.getPieceAlliance()));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());

            return builder.build();
        }
    }

    public static final class KingSideCastleMove extends CastleMove {
        public KingSideCastleMove(final Board board,
                                  final Piece piece,
                                  final int destinationCoordinate,
                                  final Rook castleRook,
                                  final int castleRookPosition,
                                  final int castleRookDestination) {
            super(board, piece, destinationCoordinate, castleRook, castleRookPosition, castleRookDestination);
        }
    }

    public static final class QueenSideCastleMove extends CastleMove {
        public QueenSideCastleMove(final Board board,
                                   final Piece piece,
                                   final int destinationCoordinate,
                                   final Rook castleRook,
                                   final int castleRookPosition,
                                   final int castleRookDestination) {
            super(board, piece, destinationCoordinate, castleRook, castleRookPosition, castleRookDestination);
        }
    }

    public static final class NullMove extends Move {
        public NullMove() {
            super(null, null, -1);
        }

        @Override
        public Board execute() {
            throw new RuntimeException("Can't execute null move!");
        }
    }

    public static class MoveFactory {
        private MoveFactory() {
            throw new RuntimeException("Not supposed to create instance!");
        }

        public static Move createMove(final Board board,
                                      final int currentCoordinate,
                                      final int destinationCoordinate) {
            for (final Move move : board.getAllLegalMoves()) {
                if (move.getCurrentCoordinate() == currentCoordinate &&
                        move.getDestinationCoordinate() == destinationCoordinate) {
                    return move;
                }
            }

            return NULL_MOVE;
        }

    }
}

