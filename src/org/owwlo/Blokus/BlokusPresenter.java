
package org.owwlo.Blokus;

import java.awt.Point;
import java.util.List;
import java.util.Map;

import org.cheat.client.GameApi.Container;
import org.cheat.client.GameApi.UpdateUI;
import org.owwlo.Blokus.Model.BlokusLogic;
import org.owwlo.Blokus.Model.BlokusLogic.MovablePiece;
import org.owwlo.Blokus.Model.BlokusState;
import org.owwlo.Blokus.Model.Piece;

import com.google.common.collect.Lists;

public class BlokusPresenter {
    enum ViewState {
        VIEW_ONLY, MAKE_MOVE
    }

    interface View {
        void pickFromValidPiece(List<Integer> pieces);

        /*
         * If the parameters are null, the position is not valid.
         */
        void updateTryBlock(Piece piece, Point pos);

        void setViewState(ViewState vs);

        void setPresenter(BlokusPresenter cheatPresenter);

        void setGameBoard(int yourPlayerId, List<Integer> playerList, String bitmap);

        /*
         * Show Lose scene.
         */
        void diaosi();

        /*
         * Show Win scene.
         */
        void gaofushuai();
    }

    private final View view;
    private final Container container;

    /*
     * Always keep the current valid game state.
     */
    private BlokusState currentState;
    private Piece chosePiece = null;
    private int yourPlayerId;

    public BlokusPresenter(View view, Container container) {
        this.view = view;
        this.container = container;
        view.setPresenter(this);
    }

    public void updateUI(UpdateUI updateUI) {
        yourPlayerId = updateUI.getYourPlayerId();

        // At this time, the state is empty so the game just begins. Game
        // needs to be initialized.
        if (updateUI.getState().isEmpty()) {
            sendInitialMove(updateUI.getPlayerIds());
            return;
        }

        currentState = BlokusState.getStateFromApiState(updateUI.getState());

        // If there is no player id in player list, the player is a viewer.
        if (!updateUI.getPlayerIds().contains(yourPlayerId)) {
            disableUiAndWatch();
            updateGameBoard(yourPlayerId
                    , currentState.getPlayerList(), currentState.getBitmapStr());
            return;
        }

        // At this situation, there is a winner. Game Over.
        if (currentState.getPassList().size() == currentState.getPlayerList().size()) {
            int maxUsedPiece = -1;
            List<Integer> maxPlayers = Lists.newArrayList();
            for (int playerId : currentState.getPlayerList()) {
                int usedPiece = currentState.getEveryPlayerUsedPiece().get(playerId + "").size();
                if (usedPiece > maxUsedPiece) {
                    maxUsedPiece = usedPiece;
                    maxPlayers.clear();
                    maxPlayers.add(playerId);
                } else if (usedPiece == maxUsedPiece) {
                    maxPlayers.add(playerId);
                }
            }
            if (maxPlayers.contains(yourPlayerId)) {
                showWinScene();
            } else {
                showLoseScene();
            }
            return;
        }

        boolean isItMyTurn = (yourPlayerId == currentState.getTurn());

        // No matter whose turn, Update the game board.
        updateGameBoard(yourPlayerId, currentState.getPlayerList(), currentState.getBitmapStr());

        if (isItMyTurn) {
            showPickAndMakeMoveScene(yourPlayerId, currentState.getPieceFromPlayer());
        } else {
            disableUiAndWatch();
        }
    }

    private void showLoseScene() {
        view.diaosi();
    }

    private void showWinScene() {
        view.gaofushuai();
    }

    private void sendInitialMove(List<Integer> playerIds) {
        container.sendMakeMove(BlokusLogic.getMoveInitial(playerIds));
    }

    private void disableUiAndWatch() {
        view.setViewState(ViewState.VIEW_ONLY);
    }

    private void showPickAndMakeMoveScene(int yourPlayerId,
            Map<Integer, List<Integer>> pieceFromPlayer) {
        view.setViewState(ViewState.MAKE_MOVE);
        view.pickFromValidPiece(pieceFromPlayer.get(yourPlayerId));

    }

    private void updateGameBoard(int yourPlayerId, List<Integer> playerList, String bitmapStr) {
        view.setGameBoard(yourPlayerId, playerList, bitmapStr);

    }

    void chosePiece(int pieceId) {
        chosePiece = Piece.getPiece(pieceId);
    }

    /*
     * Because there is rotation in piece so it is false to pass just the id of
     * piece.
     */
    void tryCurrentPieceWithPosition(Piece piece, Point pos) {
        boolean canFit = BlokusLogic.canFit(currentState,
                new MovablePiece(yourPlayerId, piece, pos), BlokusLogic
                        .checkInitialMoveForPlayer(yourPlayerId, currentState.getBitmap(),
                                Constants.boardSizeMap.get(currentState.getPlayerList().size())));
        if (canFit) {
            view.updateTryBlock(piece, pos);
        } else {
            view.updateTryBlock(null, null);
        }
    }

    void makeMove(Piece piece, Point pos) {
        boolean canFit = BlokusLogic.canFit(currentState,
                new MovablePiece(yourPlayerId, piece, pos), BlokusLogic
                        .checkInitialMoveForPlayer(yourPlayerId, currentState.getBitmap(),
                                Constants.boardSizeMap.get(currentState.getPlayerList().size())));
        if (canFit) {
            container.sendMakeMove(BlokusLogic.getMakeMoveOperations(currentState, piece.getId(),
                    piece.getRotation(), pos));
        }
    }

    void passMyTurn() {
        container.sendMakeMove(BlokusLogic.getPassMeOperations(currentState));
    }

    void rotateCurrentPiece() {
        if (chosePiece != null) {
            chosePiece.rotate();
        }
    }
}
