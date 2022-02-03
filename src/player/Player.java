package player;

import board.Board;
import board.Move;
import board.MoveStatus;
import board.MoveTransition;
import com.google.common.collect.ImmutableList;
import pieces.Alliance;
import pieces.King;
import pieces.Piece;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Player {

    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;
    private boolean isInCheck;

    Player(final Board board, final Collection<Move> legalMoves, final Collection<Move> opponentMoves) {
        this.board = board;
        this.playerKing = findKing();
        this.legalMoves = ImmutableList.copyOf(legalMoves);
        this.isInCheck = !Player.calculateAttackOnTile(this.playerKing.getPiecePosition(), opponentMoves).isEmpty();
    }

    protected static Collection<Move> calculateAttackOnTile(final int piecePosition, Collection<Move> opponentMoves) {
        final List<Move> attackMoves = new ArrayList<>();
        for (final Move move : opponentMoves) {
            if (piecePosition == move.getDestinationCoordinate()) {
                attackMoves.add(move);
            }
        }

        return ImmutableList.copyOf(attackMoves);
    }

    private King findKing() {
        for (final Piece piece : getAlivePieces()) {
            if (piece.getPieceType().isKing()) {
                return (King) piece;
            }
        }
        throw new RuntimeException("King is absent! Shouldn't reach here!");
    }

    public King getPlayerKing() {
        return this.playerKing;
    }

    public Collection<Move> getLegalMoves(){
        return this.legalMoves;
    }

    public boolean isMoveLegal(final Move move) {
        return this.legalMoves.contains(move);
    }

    public boolean isInCheck() {
        return this.isInCheck;
    }

    public boolean isInCheckmate() {
        return this.isInCheck && !hasEscapeMoves();
    }

    public boolean isInStalemate() {
        return !this.isInCheck() && !hasEscapeMoves();
    }

    public boolean isCastled() {
        return false;
    }

    protected boolean hasEscapeMoves() {
        for (final Move move : this.legalMoves) {
            final MoveTransition transition = makeMove(move);
            if (transition.getMoveStatus().isDone()) {
                return true;
            }
        }

        return false;
    }

    public MoveTransition makeMove(final Move move) {
        if (!isMoveLegal(move)) {
            return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
        }

        final Board transitionBoard = move.execute();

        final Collection<Move> kingAttacks = Player.calculateAttackOnTile(transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
                transitionBoard.currentPlayer().getLegalMoves());

        if(!kingAttacks.isEmpty()) {
            return new MoveTransition(this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }

        return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
    }

    public abstract Collection<Piece> getAlivePieces();

    public abstract Alliance getAlliance();

    public abstract Player getOpponent();

    protected abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegalMoves, Collection<Move> opponentsLegalMoves);
}
