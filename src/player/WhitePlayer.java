package player;

import board.Board;
import board.Move;
import board.Tile;
import com.google.common.collect.ImmutableList;
import pieces.Alliance;
import pieces.Piece;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    @Override
    protected Collection<Move> calculateKingCastles(Collection<Move> playerLegalMoves, Collection<Move> opponentsLegalMoves) {
        final List<Move> kingCastles = new ArrayList<>();

        if(this.playerKing.isFirstMove() && !this.isInCheck()) {
            if(!this.board.getTile(61).isTileOccupied() && !this.board.getTile(62).isTileOccupied()){
                final Tile rookTile = this.board.getTile(63);
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()){
                    if(Player.calculateAttackOnTile(61, opponentsLegalMoves).isEmpty() &&
                       Player.calculateAttackOnTile(62, opponentsLegalMoves).isEmpty() &&
                       rookTile.getPiece().getPieceType().isRook()) {
                        kingCastles.add(null);
                    }
                }
            }
        }

        if(!this.board.getTile(59).isTileOccupied() &&
           !this.board.getTile(58).isTileOccupied() &&
           !this.board.getTile(57).isTileOccupied()) {
            final Tile rookTile = this.board.getTile(56);
            if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()){
                kingCastles.add(null);
            }
        }

        return ImmutableList.copyOf(kingCastles);
    }
}
