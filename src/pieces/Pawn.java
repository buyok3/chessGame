package pieces;

import board.Board;
import board.BoardUtils;
import board.Move;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Pawn extends Piece {

    private final int[] POSSIBLE_COORDINATES = {8, 16};

    protected Pawn(final int pieceCoordinate, final Alliance pieceAlliance) {
        super(pieceCoordinate, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        List<Move> legalMoves = new ArrayList<>();

        for (final int currentPossibleOffset : POSSIBLE_COORDINATES) {

            final int possibleDestination = this.pieceCoordinate + (currentPossibleOffset * this.getPieceAlliance().getDirection());

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
                    (BoardUtils.EIGHTH_COLUMN[this.pieceCoordinate] && this.pieceAlliance.isWhite()) || ) {

            } else if (currentPossibleOffset == 9) {

            }
        }

        return ImmutableList.copyOf(legalMoves);
    }
}
