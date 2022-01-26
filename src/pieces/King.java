package pieces;

import board.Board;
import board.BoardUtils;
import board.Move;
import board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static board.BoardUtils.isPossibleCoordinate;

public class King extends Piece {
    final private int[] POSSIBLE_COORDINATES = {-9, -8, -7, -1, 1, 7, 8, 9};

    protected King(final int pieceCoordinate, final Alliance pieceAlliance) {
        super(pieceCoordinate, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final int currentPossibleOffset : POSSIBLE_COORDINATES) {
            final int possibleDestination;

            possibleDestination = this.pieceCoordinate + currentPossibleOffset;

            if (isPossibleCoordinate(possibleDestination)) {
                if (isEdgeSituation(this.pieceCoordinate, currentPossibleOffset)) {
                    continue;
                }

                final Tile possibleDestinationTile = board.getTile(possibleDestination);

                if (!possibleDestinationTile.isTileOccupied()) {
                    legalMoves.add(new Move.RegularMove(board, this, possibleDestination));
                } else {
                    final Piece pieceAtDestination = possibleDestinationTile.getPiece();

                    if (this.getPieceAlliance() != pieceAtDestination.getPieceAlliance()) {
                        legalMoves.add(new Move.AttackMove(board, this, possibleDestination, pieceAtDestination));
                    }
                }
            }
        }

        return ImmutableList.copyOf(legalMoves);
    }

    private static boolean isFirstColumn(final int currentPosition, final int possiblePosition) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (possiblePosition == -9 || possiblePosition == 7 || possiblePosition == -1);
    }

    private static boolean isEightColumn(final int currentPosition, final int possiblePosition) {
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (possiblePosition == -7 || possiblePosition == 9 || possiblePosition == 1);
    }

    private static boolean isEdgeSituation(final int currentPosition, final int possiblePosition) {
        return isFirstColumn(currentPosition, possiblePosition) || isEightColumn(currentPosition, possiblePosition);
    }
}
