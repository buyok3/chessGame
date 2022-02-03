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

    @Override
    protected Collection<Move> calculateKingCastles(Collection<Move> playerLegalMoves, Collection<Move> opponentsLegalMoves) {
        final List<Move> kingCastles = new ArrayList<>();

        if(this.playerKing.isFirstMove() && !this.isInCheck()) {
            if(!this.board.getTile(5).isTileOccupied() && !this.board.getTile(6).isTileOccupied()){
                final Tile rookTile = this.board.getTile(7);
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()){
                    if(Player.calculateAttackOnTile(5, opponentsLegalMoves).isEmpty() &&
                            Player.calculateAttackOnTile(6, opponentsLegalMoves).isEmpty() &&
                            rookTile.getPiece().getPieceType().isRook()) {
                        kingCastles.add(null);
                    }
                }
            }
        }

        if(!this.board.getTile(1).isTileOccupied() &&
                !this.board.getTile(2).isTileOccupied() &&
                !this.board.getTile(3).isTileOccupied()) {
            final Tile rookTile = this.board.getTile(0);
            if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()){
                kingCastles.add(null);
            }
        }

        return ImmutableList.copyOf(kingCastles);
    }
}
