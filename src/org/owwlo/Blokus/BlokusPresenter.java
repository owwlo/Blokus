
package org.owwlo.Blokus;

import java.util.List;
import java.util.Map;

import org.game_api.GameApi.Container;
import org.game_api.GameApi.UpdateUI;
import org.owwlo.Blokus.Model.BlokusLogic;
import org.owwlo.Blokus.Model.BlokusLogic.MovablePiece;
import org.owwlo.Blokus.Model.BlokusState;
import org.owwlo.Blokus.Model.Piece;
import org.owwlo.Blokus.Model.Point;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

public class BlokusPresenter {
    public enum ViewState {
        VIEW_ONLY, MAKE_MOVE
    }

    public interface View {
        void pickFromValidPiece(List<Integer> pieces);

        /*
         * If the parameters are null, the position is not valid.
         */
        void updateTryBlock(Piece piece, Point pos);

        void setViewState(ViewState vs);

        void setPresenter(BlokusPresenter cheatPresenter);

        void setGameBoard(String yourPlayerId, List<String> playerList,
                String bitmap);

        /*
         * Show Lose scene.
         */
        void diaosi();

        /*
         * Show Win scene.
         */
        void gaofushuai();

        void cleanTryBlock();

        void updateMyStep(String player, Point pos, List<Point> pointList);

        void debug(String str);
    }

    private final View view;
    private final Container container;

    /*
     * Always keep the current valid game state.
     */
    private BlokusState currentState;
    private Piece chosePiece = null;
    private String yourPlayerId;
    private BlokusAI ai;

    public BlokusPresenter(View view, Container container) {
        this.view = view;
        this.container = container;
        ai = new BlokusAI();
        view.setPresenter(this);
    }

    public void updateUI(UpdateUI updateUI) {
        List<String> playerIds = updateUI.getPlayerIds();
        yourPlayerId = updateUI.getYourPlayerId();
        int yourPlayerIndex = updateUI.getPlayerIndex(yourPlayerId);

        // At this time, the state is empty so the game just begins. Game
        // needs to be initialized.
//        if (updateUI.getState().isEmpty()
//                && yourPlayerId == Ordering.<String> natural().min(
//                        updateUI.getPlayerIds())) {
//            sendInitialMove(updateUI.getPlayerIds());
//            return;
//        }

        if (updateUI.getState().isEmpty()) {
            if (yourPlayerIndex == 0) {
                sendInitialMove(playerIds);
            }
            return;
        }

        String currentPlayerId = updateUI.getPlayerIds().get(
                (updateUI.getPlayerIds()
                        .indexOf(updateUI.getLastMovePlayerId()) + 1)
                        % updateUI.getPlayerIds().size());
        currentState = BlokusState.getStateFromApiState(updateUI.getState(),
                currentPlayerId);

        // If there is no player id in player list, the player is a viewer.
        if (!updateUI.getPlayerIds().contains(yourPlayerId)) {
            disableUiAndWatch(yourPlayerId, currentState.getPieceFromPlayer());
            updateGameBoard(yourPlayerId, currentState.getPlayerList(),
                    currentState.getBitmapStr());
            return;
        }

        // At this situation, there is a winner. Game Over.
        if (currentState.getPassList().size() == currentState.getPlayerList()
                .size()) {
            int maxUsedPiece = -1;
            List<String> maxPlayers = Lists.newArrayList();
            for (String playerId : currentState.getPlayerList()) {
                int usedPiece = currentState.getEveryPlayerUsedPiece()
                        .get(playerId + "").size();
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
        updateGameBoard(yourPlayerId, currentState.getPlayerList(),
                currentState.getBitmapStr());
        if (isItMyTurn) {
            if (updateUI.isAiPlayer()) {
                //view.debug("ai");
                MovablePiece p = ai.getPossibleMove(currentState, yourPlayerId);
                disableUiAndWatch(yourPlayerId, currentState.getPieceFromPlayer());
                if (p != null) {
                    makeMove(p, p.getPosition());
                } else {
                    passMyTurn();
                }
            } else {
                showPickAndMakeMoveScene(yourPlayerId,
                        currentState.getPieceFromPlayer());
            }
        } else {
            disableUiAndWatch(yourPlayerId,
                    currentState.getPieceFromPlayer());
        }
    }

    private void showLoseScene() {
        view.diaosi();
    }

    private void showWinScene() {
        view.gaofushuai();
    }

    private void sendInitialMove(List<String> playerIds) {
        container.sendMakeMove(BlokusLogic.getMoveInitial(playerIds));
    }

    private void disableUiAndWatch(String yourPlayerId,
            Map<String, List<Integer>> pieceFromPlayer) {
        view.setViewState(ViewState.VIEW_ONLY);
        view.pickFromValidPiece(pieceFromPlayer.get(yourPlayerId));
    }

    private void showPickAndMakeMoveScene(String yourPlayerId,
            Map<String, List<Integer>> pieceFromPlayer) {
        view.setViewState(ViewState.MAKE_MOVE);
        view.pickFromValidPiece(pieceFromPlayer.get(yourPlayerId));
    }

    private void updateGameBoard(String yourPlayerId, List<String> playerList,
            String bitmapStr) {
        view.setGameBoard(yourPlayerId, playerList, bitmapStr);

    }

    public void chosePiece(int pieceId) {
        chosePiece = Piece.getPiece(pieceId);
    }

    /*
     * Because there is rotation in piece so it is false to pass just the id of
     * piece.
     */
    public void tryCurrentPieceWithPosition(Piece piece, Point pos) {
        boolean canFit = BlokusLogic.canFit(currentState, new MovablePiece(
                yourPlayerId, piece, pos), BlokusLogic
                .checkInitialMoveForPlayer(yourPlayerId, currentState
                        .getBitmap(), Constants.boardSizeMap.get(currentState
                        .getPlayerList().size())));
        view.cleanTryBlock();
        if (canFit) {
            view.updateTryBlock(piece, pos);
        }
    }

    public void makeMove(Piece piece, Point pos) {
        boolean canFit = BlokusLogic.canFit(currentState, new MovablePiece(
                yourPlayerId, piece, pos), BlokusLogic
                .checkInitialMoveForPlayer(yourPlayerId, currentState
                        .getBitmap(), Constants.boardSizeMap.get(currentState
                        .getPlayerList().size())));
        if (canFit) {
            view.setViewState(ViewState.VIEW_ONLY);

            container.sendMakeMove(BlokusLogic.getMakeMoveOperations(
                    currentState, piece.getId(), piece.getRotation(), pos));

            /**
             * Do I really need to do this? Won't container send the updated
             * state back to me?
             */
            updateView(yourPlayerId, piece.getId(), piece.getRotation(), pos);
        }
    }

    private void updateView(String player, int id, int rotation, Point pos) {
        Piece p = Piece.getPiece(id);
        p.rotate(rotation);
        view.updateMyStep(player, pos, p.getPointList());
    }

    public void passMyTurn() {
        container.sendMakeMove(BlokusLogic.getPassMeOperations(currentState));
    }

    public void rotateCurrentPiece() {
        if (chosePiece != null) {
            chosePiece.rotate();
        }
    }
}
