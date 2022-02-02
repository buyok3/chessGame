package player;

import board.Board;
import board.Move;
import pieces.Alliance;
import pieces.Piece;

import java.util.Collection;

public class BlackPlayer extends Player{
    public BlackPlayer(final Board board,
                       final Collection<Move> whiteLegalMoves,
                       final Collection<Move> blackLegalMoves) {
        super(board, blackLegalMoves, whiteLegalMoves);
    }

    @Override
    public Collection<Piece> getAlivePieces() {
        return this.board.getBlackPieces();
    }

    @Override
    public Alliance getAlliance(){
        return Alliance.BLACK;
    }

    @Override
    public Player getOpponent() {
        return this.board.whitePlayer();
    }
}
