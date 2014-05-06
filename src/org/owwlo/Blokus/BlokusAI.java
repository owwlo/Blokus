
package org.owwlo.Blokus;

import java.util.List;

import org.owwlo.Blokus.Model.BlokusLogic.MovablePiece;
import org.owwlo.Blokus.Model.BlokusLogic;
import org.owwlo.Blokus.Model.BlokusState;
import org.owwlo.Blokus.Model.Piece;
import org.owwlo.Blokus.Model.Point;

public class BlokusAI {

    public MovablePiece getPossibleMove(BlokusState currentState, String yourPlayerId) {
        List<Integer> availablePiece = currentState.getPieceFromPlayer().get(yourPlayerId);
        if (availablePiece.isEmpty()) {
            return null;
        }
        int pieceIdx = availablePiece.get(0);
        Piece piece = Piece.getPiece(pieceIdx);
        int[][] boardBitmap = currentState.getBitmap();

        //piece.rotate((int) (Math.random() * (3 + 1)));

        int len = Constants.boardSizeMap.get(currentState.getPlayerList().size());
        int startPointIdx = (int) (Math.random() * (len * len + 1));
        for (int i = startPointIdx; i < len * len; i++) {
            int x = i % len;
            int y = i / len;
            boolean canFit = BlokusLogic.canFit(currentState, new MovablePiece(
                    yourPlayerId, piece, new Point(x, y)), BlokusLogic
                    .checkInitialMoveForPlayer(yourPlayerId, boardBitmap,
                            Constants.boardSizeMap.get(currentState
                                    .getPlayerList().size())));
            if (canFit) {
                return new MovablePiece(yourPlayerId, piece, new Point(x, y));
            }
        }
        for (int i = 0; i < startPointIdx; i++) {
            int x = i % len;
            int y = i / len;
            boolean canFit = BlokusLogic.canFit(currentState, new MovablePiece(
                    yourPlayerId, piece, new Point(x, y)), BlokusLogic
                    .checkInitialMoveForPlayer(yourPlayerId, boardBitmap,
                            Constants.boardSizeMap.get(currentState
                                    .getPlayerList().size())));
            if (canFit) {
                return new MovablePiece(yourPlayerId, piece, new Point(x, y));
            }
        }
        return null;
    }

}
