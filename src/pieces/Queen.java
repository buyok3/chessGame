package pieces;

import board.Board;
import board.BoardUtils;
import board.Move;
import board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Queen extends Piece {

    final private int[] POSSIBLE_COORDINATE_VECTOR = {-9, -8, -7, -1, 1, 7, 8, 9};

    protected Queen(final int pieceCoordinate, final Alliance pieceAlliance) {
        super(pieceCoordinate, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final int possibleCoordinateMove : POSSIBLE_COORDINATE_VECTOR) {
            int possibleDestinationCoordinate = this.pieceCoordinate;

            while (BoardUtils.isPossibleCoordinate(possibleDestinationCoordinate)) {
                if (isEdgeSituation(this.pieceCoordinate, possibleDestinationCoordinate)) {
                    break;
                }

                possibleDestinationCoordinate += possibleCoordinateMove;
                if (BoardUtils.isPossibleCoordinate(possibleDestinationCoordinate)) {
                    final Tile possibleDestinationTile = board.getTile(possibleDestinationCoordinate);

                    if (!possibleDestinationTile.isTileOccupied()) {
                        legalMoves.add(new Move.RegularMove(board, this, possibleDestinationCoordinate));
                    } else {
                        final Piece pieceOnDestination = possibleDestinationTile.getPiece();
                        final Alliance pieceAllianceOnDestination = pieceOnDestination.getPieceAlliance();

                        if (this.getPieceAlliance() != pieceAllianceOnDestination)
                            legalMoves.add(new Move.AttackMove(board, this, possibleDestinationCoordinate, pieceOnDestination));
                    }
                }
            }
        }

        return ImmutableList.copyOf(legalMoves);
    }

    private static boolean isFirstColumn(final int currentPosition, final int possiblePosition) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (possiblePosition == 7 || possiblePosition == -9 ||
                possiblePosition == -1);
    }

    private static boolean isEightColumn(final int currentPosition, final int possiblePosition) {
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (possiblePosition == -7 || possiblePosition == 9 ||
                possiblePosition == 1);
    }

    private static boolean isEdgeSituation(final int currentPosition, final int possiblePosition) {
        return isFirstColumn(currentPosition, possiblePosition) || isEightColumn(currentPosition, possiblePosition);
    }
}
