package player;

import board.Board;
import board.Move;
import pieces.Alliance;
import pieces.Piece;

import java.util.Collection;

public class WhitePlayer extends Player{
    public WhitePlayer(final Board board,
                       final Collection<Move> whiteLegalMoves,
                       final Collection<Move> blackLegalMoves) {
        super(board, whiteLegalMoves, blackLegalMoves);
    }

    @Override
    public Collection<Piece> getAlivePieces() {
        return this.board.getWhitePieces();
    }

    @Override
    public Alliance getAlliance(){
        return Alliance.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.board.blackPlayer();
    }
}
