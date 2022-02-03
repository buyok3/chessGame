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

public class Knight extends Piece {
    private final int[] POSSIBLE_MOVES = {-17, -15, -10, -6, 6, 10, 15, 17};

    public Knight(final int pieceCoordinate, final Alliance pieceAlliance) {
        super(PieceType.KNIGHT, pieceCoordinate, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final int currentPossibleOffset : POSSIBLE_MOVES) {
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

    @Override
    public String toString(){
        return PieceType.KNIGHT.toString();
    }

    @Override
    public Piece movePiece(Move move) {
        return new Knight(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }

    private static boolean isFirstColumn(final int currentPosition, final int nextPosition){
        return BoardUtils.FIRST_COLUMN[currentPosition] && (nextPosition == -17 || nextPosition == -10 ||
                nextPosition == 6 || nextPosition == 15);
    }

    private static boolean isSecondColumn(final int currentPosition, final int nextPosition){
        return BoardUtils.SECOND_COLUMN[currentPosition] && (nextPosition == 6 || nextPosition == -10);
    }

    private static boolean isSeventhColumn(final int currentPosition, final int nextPosition){
        return BoardUtils.SEVENTH_COLUMN[currentPosition] && (nextPosition == -6 || nextPosition == 10);
    }

    private static boolean isEighthColumn(final int currentPosition, final int nextPosition){
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (nextPosition == 17 || nextPosition == 10 ||
                nextPosition == -6 || nextPosition == -15);
    }

    private static boolean isEdgeSituation(final int currentPosition, final int nextPosition){
        return isFirstColumn(currentPosition, nextPosition) || isSecondColumn(currentPosition, nextPosition) ||
                isSeventhColumn(currentPosition, nextPosition) || isEighthColumn(currentPosition, nextPosition);
    }
}
