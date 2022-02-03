package pieces;

import board.Board;
import board.BoardUtils;
import board.Move;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Pawn extends Piece {

    private final int[] POSSIBLE_COORDINATES = {7, 8, 9, 16};

    public Pawn(final int pieceCoordinate, final Alliance pieceAlliance) {
        super(PieceType.PAWN, pieceCoordinate, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        List<Move> legalMoves = new ArrayList<>();

        for (final int currentPossibleOffset : POSSIBLE_COORDINATES) {

            final int possibleDestination = this.pieceCoordinate + (currentPossibleOffset * this.pieceAlliance.getDirection());

            if (!BoardUtils.isPossibleCoordinate(possibleDestination)) {
                continue;
            }

            if (currentPossibleOffset == 8 && !board.getTile(possibleDestination).isTileOccupied()) {
                legalMoves.add(new Move.RegularMove(board, this, possibleDestination));
            } else if (currentPossibleOffset == 16 && this.isFirstMove() &&
                    (BoardUtils.SECOND_ROW[this.pieceCoordinate] && this.getPieceAlliance().isBlack()) ||
                    (BoardUtils.SEVENTH_ROW[this.pieceCoordinate] && this.getPieceAlliance().isWhite())) {
                final int behindPossibleDestination = this.pieceCoordinate + (this.pieceAlliance.getDirection() * 8);
                if (board.getTile(behindPossibleDestination).isTileOccupied() ||
                        board.getTile(possibleDestination).isTileOccupied()) {
                    legalMoves.add(new Move.RegularMove(board, this, possibleDestination));
                }
            } else if (currentPossibleOffset == 7 &&
                    !((BoardUtils.EIGHTH_COLUMN[this.pieceCoordinate] && this.pieceAlliance.isWhite()) ||
                            (BoardUtils.FIRST_COLUMN[this.pieceCoordinate] && this.pieceAlliance.isBlack()))) {
                if (board.getTile(possibleDestination).isTileOccupied()) {
                    final Piece pieceOnDestination = board.getTile(possibleDestination).getPiece();
                    if (this.pieceAlliance != pieceOnDestination.getPieceAlliance()) {
                        legalMoves.add(new Move.RegularMove(board, this, possibleDestination));
                    }
                }
            } else if (currentPossibleOffset == 9 &&
                    !((BoardUtils.EIGHTH_COLUMN[this.pieceCoordinate] && this.pieceAlliance.isWhite()) ||
                            (BoardUtils.FIRST_COLUMN[this.pieceCoordinate] && this.pieceAlliance.isBlack()))) {
                if (board.getTile(possibleDestination).isTileOccupied()) {
                    final Piece pieceOnDestination = board.getTile(possibleDestination).getPiece();
                    if (this.pieceAlliance != pieceOnDestination.getPieceAlliance()) {
                        legalMoves.add(new Move.RegularMove(board, this, possibleDestination));
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public String toString(){
        return PieceType.PAWN.toString();
    }

    @Override
    public Piece movePiece(Move move) {
        return new Pawn(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }
}
