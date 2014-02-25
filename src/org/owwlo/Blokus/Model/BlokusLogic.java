
package org.owwlo.Blokus.Model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.cheat.client.GameApi.EndGame;
import org.cheat.client.GameApi.Operation;
import org.cheat.client.GameApi.Set;
import org.cheat.client.GameApi.SetTurn;
import org.cheat.client.GameApi.VerifyMove;
import org.cheat.client.GameApi.VerifyMoveDone;
import org.owwlo.Blokus.Constants;
import org.owwlo.Blokus.Constants.BlokusPiece;
import org.owwlo.Blokus.Utils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

/**
 * A Board item used in Blokus.
 * 
 * @author owwlo
 */

public class BlokusLogic {
    private List<MovablePiece> pieceList = new ArrayList<MovablePiece>();
    private List<BoardListener> boardLinsterList = new LinkedList<BoardListener>();
    private List<Piece> pieceBowl = new ArrayList<Piece>() {
        {
            for (String str : BlokusPiece.pieceStringList) {
                this.add(new Piece(str));
            }
            if (Constants.DEBUG) {
                System.out.println("Count of Pieces loaded: " + this.size());
            }
        }
    };

    private int xLen = 0, yLen = 0;

    /*
     * Because we need to know which point belongs to who, the bitmap need to be
     * form with INT other than BOOLEAN.
     */
    private int[][] boardBitmp = null;

    /**
     * Create a 0x0 GameBoard.
     */
    public BlokusLogic() {
    }

    /**
     * Attention: This method cost much because it will generate a new bitmap
     * for the board.
     */
    public void resetBoardSize(int x, int y) {
    }

    /**
     * Create a x*y GameBoard.
     */
    public BlokusLogic(int x, int y) {
        xLen = x;
        yLen = y;
        boardBitmp = new int[y][x];
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                boardBitmp[i][j] = Constants.NO_OCCUPY_POINT_VALUE;
            }
        }
        notifyOnBitmapChanged(boardBitmp);
    }

    public static boolean canFit(BlokusState state, MovablePiece mp,
            boolean isInitial) {
        Piece piece = mp;
        Point point = mp.getPosition();
        int xLen = Constants.boardSizeMap.get(state.getPlayerList().size());
        int yLen = xLen;
        int[][] boardBitmap = state.getBitmap();
        int owner = mp.getOwnerId();

        List<Point> piecePointList = piece.getPointList();

        boolean isCornerOccupy = false;
        if (point.x < 0 || point.y < 0) {
            return false;
        }
        for (Point p : piecePointList) {
            if (point.x + p.x >= xLen) {
                return false;
            }
            if (point.y + p.y >= yLen) {
                return false;
            }
            if (boardBitmap[point.y + p.y][point.x + p.y] != Constants.NO_OCCUPY_POINT_VALUE) {
                return false;
            }
            if (check4DirectionOccupy(point.x + p.x, point.y + p.y, owner,
                    boardBitmap, xLen, yLen)) {
                return false;
            }
            if (!isInitial) {
                if (check4CornerOccupy(point.x + p.x, point.y + p.y, owner,
                        boardBitmap, xLen, yLen)) {
                    isCornerOccupy = true;
                }
            }
        }
        if (!isInitial) {
            if (!isCornerOccupy) {
                return false;
            }
        }
        return true;
    }

    /**
     * Judge whether the Piece can fit into the board.
     * 
     * @param p The piece to be test.
     * @param isInitial Default false.
     * @return If it is fit.
     */
    public boolean canFit(Piece piece, Point point, int owner, boolean isInitial) {
        List<Point> piecePointList = piece.getPointList();
        boolean isCornerOccupy = false;
        if (point.x < 0 || point.y < 0) {
            return false;
        }
        for (Point p : piecePointList) {
            if (point.x + p.x >= xLen) {
                return false;
            }
            if (point.y + p.y >= yLen) {
                return false;
            }
            if (boardBitmp[point.y + p.y][point.x + p.y] != Constants.NO_OCCUPY_POINT_VALUE) {
                return false;
            }
            if (check4DirectionOccupy(point.x + p.x, point.y + p.y, owner)) {
                return false;
            }
            if (!isInitial) {
                if (check4CornerOccupy(point.x + p.x, point.y + p.y, owner)) {
                    isCornerOccupy = true;
                }
            }
        }
        if (!isInitial) {
            if (!isCornerOccupy) {
                return false;
            }
        }
        return true;
    }

    public final int[][] getBitmap() {
        return boardBitmp;
    }

    private static boolean check4CornerOccupy(int x, int y, int color,
            int[][] boardBitmap, int xLen, int yLen) {
        if (Utils.Range(x + 1, 0, xLen)) {
            if (Utils.Range(y + 1, 0, yLen)) {
                if (boardBitmap[y + 1][x + 1] == color) {
                    return true;
                }
            }
            if (Utils.Range(y - 1, 0, yLen)) {
                if (boardBitmap[y - 1][x + 1] == color) {
                    return true;
                }
            }
        }
        if (Utils.Range(x - 1, 0, xLen)) {
            if (Utils.Range(y + 1, 0, yLen)) {
                if (boardBitmap[y + 1][x - 1] == color) {
                    return true;
                }
            }
            if (Utils.Range(y - 1, 0, yLen)) {
                if (boardBitmap[y - 1][x - 1] == color) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean check4CornerOccupy(int x, int y, int color) {
        if (Utils.Range(x + 1, 0, xLen)) {
            if (Utils.Range(y + 1, 0, yLen)) {
                if (boardBitmp[y + 1][x + 1] == color) {
                    return true;
                }
            }
            if (Utils.Range(y - 1, 0, yLen)) {
                if (boardBitmp[y - 1][x + 1] == color) {
                    return true;
                }
            }
        }
        if (Utils.Range(x - 1, 0, xLen)) {
            if (Utils.Range(y + 1, 0, yLen)) {
                if (boardBitmp[y + 1][x - 1] == color) {
                    return true;
                }
            }
            if (Utils.Range(y - 1, 0, yLen)) {
                if (boardBitmp[y - 1][x - 1] == color) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean check4DirectionOccupy(int x, int y, int color,
            int[][] boardBitmap, int xLen, int yLen) {
        if (Utils.Range(x + 1, 0, xLen)) {
            if (boardBitmap[y][x + 1] == color) {
                return true;
            }
        }
        if (Utils.Range(x - 1, 0, xLen)) {
            if (boardBitmap[y][x - 1] == color) {
                return true;
            }
        }
        if (Utils.Range(y + 1, 0, yLen)) {
            if (boardBitmap[y + 1][x] == color) {
                return true;
            }
        }
        if (Utils.Range(y - 1, 0, yLen)) {
            if (boardBitmap[y - 1][x] == color) {
                return true;
            }
        }
        return false;
    }

    private boolean check4DirectionOccupy(int x, int y, int color) {
        if (Utils.Range(x + 1, 0, xLen)) {
            if (boardBitmp[y][x + 1] == color) {
                return true;
            }
        }
        if (Utils.Range(x - 1, 0, xLen)) {
            if (boardBitmp[y][x - 1] == color) {
                return true;
            }
        }
        if (Utils.Range(y + 1, 0, yLen)) {
            if (boardBitmp[y + 1][x] == color) {
                return true;
            }
        }
        if (Utils.Range(y - 1, 0, yLen)) {
            if (boardBitmp[y - 1][x] == color) {
                return true;
            }
        }
        return false;
    }

    public boolean canFit(MovablePiece mp) {
        return canFit(mp, mp.getPosition(), mp.getOwnerId(), false);
    }

    public boolean canFit(MovablePiece mp, boolean isInitial) {
        return canFit(mp, mp.getPosition(), mp.getOwnerId(), isInitial);
    }

    public boolean addPiece(MovablePiece mp) {
        return addPiece(mp, false);
    }

    public boolean addPiece(MovablePiece mp, boolean isInitial) {
        if (canFit(mp, isInitial)) {
            pieceList.add(mp);
            List<Point> pointList = mp.getPointList();
            Point piecePosition = mp.getPosition();
            for (Point p : pointList) {
                boardBitmp[piecePosition.y + p.y][piecePosition.x + p.x] = mp
                        .getOwnerId();
            }
            notifyOnBitmapChanged(boardBitmp);
            if (Constants.DEBUG) {
                printBoard();
            }
            return true;
        }
        return false;
    }

    public static class MovablePiece extends Piece {
        private Point position;
        private int ownerId;

        /**
         * "ownid" is a must because we need to know this piece belongs to whom.
         * 
         * @param ownid
         */
        public MovablePiece(int ownid) {
            ownerId = ownid;
            position = new Point();
        }

        public MovablePiece(int ownid, Piece piece, Point point) {
            super(piece);
            ownerId = ownid;
            position = point;
        }

        public MovablePiece(int ownid, String data, Point point) {
            super(data);
            ownerId = ownid;
            position = point;
        }

        public void setPosition(Point p) {
            position = p;
        }

        public final Point getPosition() {
            return position;
        }

        public final int getOwnerId() {
            return ownerId;
        }

        @Override
        public void printPiece() {
            System.out.println("PieceBitMapForOwner: " + ownerId);
            super.printPiece();
        }
    }

    public void printBoard() {
        System.out.println("********BoardBitMap*********");
        for (int i = 0; i < yLen; i++) {
            for (int j = 0; j < xLen; j++) {
                System.out.print(boardBitmp[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("****************************");
        System.out.println();
    }

    /**
     * Listener for observing if anyone has made a move.
     * 
     * @author owwlo
     */
    public static interface BoardListener {
        void onBoardBitmapChanged(final int[][] bmp);
    }

    public void addBoardListener(BoardListener listener) {
        boardLinsterList.add(listener);
    }

    public void removeBoardListener(BoardListener listener) {
        boardLinsterList.remove(listener);
    }

    public void notifyOnBitmapChanged(final int[][] bmp) {
        for (BoardListener listener : boardLinsterList) {
            listener.onBoardBitmapChanged(bmp);
        }
    }

    public final List<Piece> getAllPiece() {
        return pieceBowl;
    }

    public static VerifyMoveDone verify(VerifyMove verifyMove) {
        try {
            checkMoveIsLegal(verifyMove);
            return new VerifyMoveDone();
        } catch (Exception e) {
            if (Constants.DEBUG) {
                e.printStackTrace();
            }
            return new VerifyMoveDone(verifyMove.getLastMovePlayerId(),
                    e.getMessage());
        }
    }

    public static boolean isPieceSelectionLegal(int playerId,
            int pieceSelected, Map<String, List<Integer>> everyPlayerUsedPiece) {
        return !everyPlayerUsedPiece.get(playerId + "").contains(pieceSelected);
    }

    private static void checkMoveIsLegal(VerifyMove verifyMove)
            throws Exception {
        List<Operation> lastMove = verifyMove.getLastMove();

        // Create a copy of the original Map for the type of the map is
        // Immutable.
        Map<String, Object> lastStateMap = new HashMap<>(
                verifyMove.getLastState());
        Map<String, Object> currentStateMap = verifyMove.getState();
        int lastPlayerId = verifyMove.getLastMovePlayerId();

        BlokusState lastState = getStateFromApiState(lastStateMap);
        BlokusState currentState = getStateFromApiState(currentStateMap);
        Map<String, Operation> operationSetMap = new HashMap<>();

        boolean hasPassOperation = false;

        // Pre-check operations
        for (Operation op : lastMove) {
            if (op instanceof Set) {
                String key = ((Set) op).getKey();
                if (key.equals(Constants.JSON_PASS)) {
                    hasPassOperation = true;
                }
                operationSetMap.put(key, op);
            }
        }

        if (!hasPassOperation) {
            throw new Exception("Hacker! No valid operation");
        }

        for (Operation op : lastMove) {
            if (op instanceof Set) {
                Set set = ((Set) op);
                String key = set.getKey();
                if (key.equals(Constants.JSON_TURN)) {
                    lastStateMap.put(Constants.JSON_TURN, (int) set.getValue());
                } else if (key.equals(Constants.JSON_PASS)) {
                    if (!(Boolean) set.getValue()) {
                        Set usePieceAction = (Set) operationSetMap
                                .get(Constants.JSON_USE_PIECE);
                        Set usePointAction = (Set) operationSetMap
                                .get(Constants.JSON_POINT);

                        int pieceIndex = (int) usePieceAction.getValue();
                        String[] pointOrd = ((String) (usePointAction
                                .getValue())).split(",");
                        Point usePoint = new Point(
                                Integer.parseInt(pointOrd[1]),
                                Integer.parseInt(pointOrd[0]));

                        MovablePiece mp = new MovablePiece(lastPlayerId,
                                Constants.BlokusPiece.pieceStringList
                                        .get(pieceIndex), usePoint);

                        boolean isFit = BlokusLogic.canFit(
                                lastState,
                                mp,
                                checkInitialMoveForPlayer(lastPlayerId,
                                        lastState.getBitmap(),
                                        Constants.boardSizeMap.get(lastState
                                                .getPlayerList().size())));
                        if (isFit) {
                            String appendedBmpStr = lastState.getBitmapString()
                                    + " " + StringUtils.join(pointOrd, ",") + "," + lastPlayerId;

                            // Update Bitmap.
                            lastStateMap.put(Constants.JSON_BITMAP, appendedBmpStr);

                            // Update used piece list.
                            Map<String, List<Integer>> usedPieces = new HashMap<>(
                                    lastState.getEveryPlayerUsedPiece());
                            List<Integer> usedListForPlayer = new ArrayList<>(usedPieces.get(""
                                    + lastPlayerId));

                            if (usedListForPlayer.contains(pieceIndex)) {
                                throw new Exception("piece reused.");
                            }

                            usedListForPlayer.add(pieceIndex);
                            usedPieces.put("" + lastPlayerId, usedListForPlayer);
                            lastStateMap.put(Constants.JSON_USER_USED_PIECES, usedPieces);

                            BlokusState newState = BlokusState.getStateFromApiState(lastStateMap);
                            if (!BlokusState.equal(newState, currentState)) {
                                throw new Exception("states not unified");
                            }
                        } else {
                            throw new Exception("No fit");
                        }
                    } else {
                        List<Integer> passList = lastState.getPassList();
                        if (passList.contains(lastPlayerId)) {
                            /**
                             * TODO Finish the game.
                             */
                        } else {
                            lastState.addPassId(lastPlayerId);
                            lastStateMap.put(Constants.JSON_PASS_LIST, lastState.getPassList());
                            BlokusState newState = BlokusState.getStateFromApiState(lastStateMap);
                            if (!BlokusState.equal(newState, currentState)) {
                                throw new Exception("states not unified in pass");
                            }
                        }
                    }
                }
            } else if (op instanceof EndGame) {
                // TODO when will receive EndGame message??
            } else {
                // No other operation is needed. So, receiving other operation
                // means there being a hacker.
                throw new Exception("Hacker!");
            }
        }
    }

    private static BlokusState getStateFromApiState(
            Map<String, Object> lastStateMap) {
        return BlokusState.getStateFromApiState(lastStateMap);
    }

    public static boolean checkInitialMoveForPlayer(int playerId,
            int[][] boardBitmap, int len) {
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                if (boardBitmap[i][j] == playerId) {
                    return false;
                }
            }
        }
        return true;
    }

    public static VerifyMove createVerifyMove(int yourPlayerId,
            List<Map<String, Object>> playersInfo, Map<String, Object> state,
            Map<String, Object> lastState, List<Operation> lastMove,
            int lastMovePlayerId) {
        return new VerifyMove(playersInfo, state, lastState, lastMove, lastMovePlayerId,
                ImmutableMap.<Integer, Integer> of());
    }

    public static void main(String[] args) {
        List<Map<String, Object>> playersInfo = ImmutableList.of(
                (Map<String, Object>) (ImmutableMap.<String, Object> of(
                        Constants.JSON_USER_ID, 1)),
                (Map<String, Object>) (ImmutableMap.<String, Object> of(
                        Constants.JSON_USER_ID, 2)));

        Map<String, Object> lastState = ImmutableMap
                .<String, Object> builder()
                .put(Constants.JSON_TURN, 2)
                .put(Constants.JSON_USER_LIST, ImmutableList.<Integer> of(1, 2))
                .put(Constants.JSON_PASS_LIST, ImmutableList.<Integer> of())
                .put(Constants.JSON_BITMAP, "0,0,1 0,5,2 0,6,2")
                .put(Constants.JSON_USER_USED_PIECES,
                        ImmutableMap.<String, List<Integer>> builder()
                                .put("1", ImmutableList.<Integer> of(0))
                                .put("2", ImmutableList.<Integer> of(1))
                                .build()).build();

        Map<String, Object> currentState = ImmutableMap
                .<String, Object> builder()
                .put(Constants.JSON_TURN, 1)
                .put(Constants.JSON_USER_LIST, ImmutableList.<Integer> of(1, 2))
                .put(Constants.JSON_PASS_LIST, ImmutableList.<Integer> of())
                .put(Constants.JSON_BITMAP, "0,0,1 0,5,2 0,6,2 1,4,2")
                .put(Constants.JSON_USER_USED_PIECES,
                        ImmutableMap.<String, List<Integer>> of("1",
                                ImmutableList.<Integer> of(0), "2",
                                ImmutableList.<Integer> of(0, 2))).build();

        List<Operation> operationsFor2 = ImmutableList.<Operation> of(new Set(
                Constants.JSON_TURN, 1), new Set(Constants.JSON_PASS, false),
                new Set(Constants.JSON_USE_PIECE, 0), new Set(
                        Constants.JSON_POINT, "1,4"));
        System.out.println(BlokusLogic.verify(createVerifyMove(1, playersInfo,
                currentState, lastState, operationsFor2, 2)));
    }

    public static List<Operation> getPassMeOperations(BlokusState currentState) {
        List<Operation> ops = new ArrayList<>();
        ops.add(new SetTurn(currentState.getTurn()));
        ops.add(new Set(Constants.JSON_PASS, true));
        return ops;
    }

    public static List<Operation> getMakeMoveOperations(BlokusState currentState, int pieceId,
            int rotation, Point pos) {
        List<Operation> ops = ImmutableList.<Operation> of(new SetTurn(
                currentState.getTurn()), new Set(Constants.JSON_PASS, false),
                new Set(Constants.JSON_ROTATION, rotation),
                new Set(Constants.JSON_USE_PIECE, pieceId), new Set(
                        Constants.JSON_POINT, pos.y + "," + pos.x));
        return ops;
    }

    public static List<Operation> getMoveInitial(List<Integer> playerIds) {
        int playerAId = playerIds.get(0);
        int playerBId = playerIds.get(1);
        List<Operation> operations = Lists.newArrayList();
        operations.add(new SetTurn(playerAId));
        return operations;
    }
}
