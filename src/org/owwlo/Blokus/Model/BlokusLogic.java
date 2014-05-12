
package org.owwlo.Blokus.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.game_api.GameApi.Operation;
import org.game_api.GameApi.Set;
import org.game_api.GameApi.SetTurn;
import org.game_api.GameApi.VerifyMove;
import org.game_api.GameApi.VerifyMoveDone;
import org.owwlo.Blokus.Constants;
import org.owwlo.Blokus.Utils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;

/**
 * A Board item used in Blokus.
 * 
 * @author owwlo
 */

public class BlokusLogic {
    public static boolean canFit(BlokusState state, MovablePiece mp,
            boolean isInitial) {
        Piece piece = mp;
        Point point = mp.getPosition();
        int xLen = Constants.boardSizeMap.get(state.getPlayerList().size());
        int yLen = xLen;
        int[][] boardBitmap = state.getBitmap();
        String owner = mp.getOwnerId();

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
            if (boardBitmap[point.x + p.x][point.y + p.y] != Constants.NO_OCCUPY_POINT_VALUE) {
                return false;
            }
            if (check4DirectionOccupy(point.x + p.x, point.y + p.y, Utils.getIntIdFromStringLongId(owner),
                    boardBitmap, xLen, yLen)) {
                return false;
            }
            if (!isInitial) {
                if (check4CornerOccupy(point.x + p.x, point.y + p.y, Utils.getIntIdFromStringLongId(owner),
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

    private static boolean check4CornerOccupy(int x, int y, int color,
            int[][] boardBitmap, int xLen, int yLen) {
        if (Utils.Range(x + 1, 0, xLen)) {
            if (Utils.Range(x + 1, 0, yLen)) {
                if (boardBitmap[x + 1][y + 1] == color) {
                    return true;
                }
            }
            if (Utils.Range(x - 1, 0, yLen)) {
                if (boardBitmap[x - 1][y + 1] == color) {
                    return true;
                }
            }
        }
        if (Utils.Range(y - 1, 0, xLen)) {
            if (Utils.Range(x + 1, 0, yLen)) {
                if (boardBitmap[x + 1][y - 1] == color) {
                    return true;
                }
            }
            if (Utils.Range(x - 1, 0, yLen)) {
                if (boardBitmap[x - 1][y - 1] == color) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean check4DirectionOccupy(int x, int y, int color,
            int[][] boardBitmap, int xLen, int yLen) {
        if (Utils.Range(y + 1, 0, xLen)) {
            if (boardBitmap[x][y + 1] == color) {
                return true;
            }
        }
        if (Utils.Range(y - 1, 0, xLen)) {
            if (boardBitmap[x][y - 1] == color) {
                return true;
            }
        }
        if (Utils.Range(x + 1, 0, yLen)) {
            if (boardBitmap[x + 1][y] == color) {
                return true;
            }
        }
        if (Utils.Range(x - 1, 0, yLen)) {
            if (boardBitmap[x - 1][y] == color) {
                return true;
            }
        }
        return false;
    }

    public static class MovablePiece extends Piece {
        private Point position;
        private String ownerId;

        /**
         * "ownid" is a must because we need to know this piece belongs to whom.
         * 
         * @param ownid
         */
        public MovablePiece(String ownid) {
            ownerId = ownid;
            position = new Point();
        }

        public MovablePiece(String yourPlayerId, Piece piece, Point point) {
            super(piece);
            ownerId = yourPlayerId;
            position = point;
        }

        public MovablePiece(String ownid, String data, Point point) {
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

        public final String getOwnerId() {
            return ownerId;
        }

        @Override
        public void printPiece() {
            System.out.println("PieceBitMapForOwner: " + ownerId);
            super.printPiece();
        }
    }

    public static VerifyMoveDone verify(VerifyMove verifyMove) {
        try {
            checkMoveIsLegal(verifyMove);
            return new VerifyMoveDone();
        } catch (Exception e) {
            e.printStackTrace();
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
//        List<Operation> lastMove = verifyMove.getLastMove();
//
//        // Create a copy of the original Map for the type of the map is
//        // Immutable.
//        Map<String, Object> lastStateMap = new HashMap<>(
//                verifyMove.getLastState());
//        Map<String, Object> currentStateMap = verifyMove.getState();
//        String lastPlayerId = verifyMove.getLastMovePlayerId();
//        String otherPlayerId = null;
//        for (String id : verifyMove.getPlayerIds()) {
//            if (!id.equals(lastPlayerId)) {
//                otherPlayerId = id;
//                break;
//            }
//        }
//
//        // This is the initial move. No need to verify.
//        if (lastStateMap.isEmpty() && currentStateMap.isEmpty()) {
//            return;
//        }
//
//        System.out.println("lastState:");
//        System.out.println(lastStateMap);
//        System.out.println("currentState:");
//        System.out.println(currentStateMap);
//
//        System.out.println("lastPlayerId:" + lastPlayerId);
//
//        // The player in this side will make a move. Ignore verification.
//        if (lastStateMap.isEmpty() && !currentStateMap.isEmpty()) {
//            return;
//        }
//
//        BlokusState lastState = getStateFromApiState(lastStateMap, lastPlayerId);
//        BlokusState currentState = getStateFromApiState(currentStateMap, otherPlayerId);
//        Map<String, Operation> operationSetMap = new HashMap<>();
//
//        boolean hasPassOperation = false;
//
//        // Pre-check operations
//        for (Operation op : lastMove) {
//            if (op instanceof Set) {
//                String key = ((Set) op).getKey();
//                if (key.equals(Constants.JSON_PASS)) {
//                    hasPassOperation = true;
//                }
//                operationSetMap.put(key, op);
//            }
//        }
//
//        if (!hasPassOperation) {
//            throw new Exception("Hacker! No valid operation");
//        }
//
//        for (Operation op : lastMove) {
//            if (op instanceof SetTurn) {
//                lastStateMap.put(Constants.JSON_TURN, ((SetTurn) op).getPlayerId());
//            } else if (op instanceof Set) {
//                Set set = ((Set) op);
//                String key = set.getKey();
//                if (key.equals(Constants.JSON_TURN)) {
//                    lastStateMap.put(Constants.JSON_TURN, (Integer) set.getValue());
//                } else if (key.equals(Constants.JSON_PASS)) {
//                    if (!(Boolean) set.getValue()) {
//                        Set usePieceAction = (Set) operationSetMap
//                                .get(Constants.JSON_USE_PIECE);
//                        Set usePointAction = (Set) operationSetMap
//                                .get(Constants.JSON_POINT);
//
//                        int pieceIndex = (Integer) usePieceAction.getValue();
//                        String[] pointOrd = ((String) (usePointAction
//                                .getValue())).split(",");
//                        Point usePoint = new Point(
//                                Integer.parseInt(pointOrd[1]),
//                                Integer.parseInt(pointOrd[0]));
//
//                        MovablePiece mp = new MovablePiece(lastPlayerId,
//                                Constants.BlokusPiece.pieceStringList
//                                        .get(pieceIndex), usePoint);
//
//                        boolean isFit = BlokusLogic.canFit(
//                                lastState,
//                                mp,
//                                checkInitialMoveForPlayer(lastPlayerId,
//                                        lastState.getBitmap(),
//                                        Constants.boardSizeMap.get(lastState
//                                                .getPlayerList().size())));
//                        if (isFit) {
//                            // String appendedBmpStr =
//                            // lastState.getBitmapString()
//                            // + " " + Joiner.on(",").join(pointOrd) + "," +
//                            // lastPlayerId;
//                            //
//                            // // Update Bitmap.
//                            // lastStateMap.put(Constants.JSON_BITMAP,
//                            // appendedBmpStr);
//                            //
//                            // // Update used piece list.
//                            // Map<String, List<Integer>> usedPieces = new
//                            // HashMap<>(
//                            // lastState.getEveryPlayerUsedPiece());
//                            // List<Integer> usedListForPlayer = new
//                            // ArrayList<>(usedPieces.get(""
//                            // + lastPlayerId));
//                            //
//                            // if (usedListForPlayer.contains(pieceIndex)) {
//                            // throw new Exception("piece reused.");
//                            // }
//                            //
//                            // usedListForPlayer.add(pieceIndex);
//                            // usedPieces.put("" + lastPlayerId,
//                            // usedListForPlayer);
//                            // lastStateMap.put(Constants.JSON_USER_USED_PIECES,
//                            // usedPieces);
//                            //
//                            // BlokusState newState =
//                            // BlokusState.getStateFromApiState(lastStateMap,
//                            // lastPlayerId);
//                            // if (!BlokusState.equal(newState, currentState)) {
//                            // throw new Exception("states not unified");
//                            // }
//                        } else {
//                            throw new Exception("No fit");
//                        }
//                    } else {
//                        List<String> passList = lastState.getPassList();
//                        if (passList.contains(lastPlayerId)) {
//                            /**
//                             * TODO Finish the game.
//                             */
//                        } else {
//                            lastState.addPassId(lastPlayerId);
//                            lastStateMap.put(Constants.JSON_PASS_LIST, lastState.getPassList());
//                            BlokusState newState = BlokusState.getStateFromApiState(lastStateMap,
//                                    lastPlayerId);
//                            if (!BlokusState.equal(newState, currentState)) {
//                                throw new Exception("states not unified in pass");
//                            }
//                        }
//                    }
//                }
//            } else if (op instanceof EndGame) {
//                // TODO when will receive EndGame message??
//            } else {
//                // No other operation is needed. So, receiving other operation
//                // means there being a hacker.
//                throw new Exception("Hacker!");
//            }
//        }
    }

    private static BlokusState getStateFromApiState(
            Map<String, Object> lastStateMap, String playerId) {
        return BlokusState.getStateFromApiState(lastStateMap, playerId);
    }

    public static boolean checkInitialMoveForPlayer(String playerId,
            int[][] boardBitmap, int len) {
      int playerIdInt = Utils.getIntIdFromStringLongId(playerId);
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                if (boardBitmap[i][j] == playerIdInt) {
                    return false;
                }
            }
        }
        return true;
    }

    public static VerifyMove createVerifyMove(String yourPlayerId,
            List<Map<String, Object>> playersInfo, Map<String, Object> state,
            Map<String, Object> lastState, List<Operation> lastMove,
            String lastMovePlayerId) {
        return new VerifyMove(playersInfo, state, lastState, lastMove, lastMovePlayerId,
                ImmutableMap.<String, Integer> of());
    }

    public static List<Operation> getPassMeOperations(BlokusState currentState) {
        List<Operation> ops = new ArrayList<>();
        List<String> newPassList = Lists.newArrayList(currentState.getPassList());
        newPassList.add(currentState.getTurn());
        ops.add(new SetTurn(currentState.getOppositeId()));
        ops.add(new Set(Constants.JSON_PASS, true));
        ops.add(new Set(Constants.JSON_PASS_LIST, newPassList));
        return ops;
    }

    public static List<Operation> getMakeMoveOperations(BlokusState currentState, int pieceId,
            int rotation, Point pos) {
        String bitmap = currentState.getBitmapString();

        // Generate bitmap for new state.
        Piece piece = Piece.getPiece(pieceId);
        piece.rotate(rotation);
        List<Point> pointList = piece.getPointList();
        StringBuilder sb = new StringBuilder();
        for (Point p : pointList) {
            sb.append((pos.x + p.x) + "," + (pos.y + p.y) + "," + currentState.getTurn() + " ");
        }
        String newAppendBitmap = sb.toString().trim();

        String newBitmap = bitmap + " " + newAppendBitmap;

        // Modify old used piece list.
        List<Integer> newUsedList = Lists.newArrayList(currentState.getEveryPlayerUsedPiece().get(
                "" + currentState.getTurn()));
        newUsedList.add(pieceId);
        Map<String, List<Integer>> newUsedMap = Maps.newHashMap(currentState
                .getEveryPlayerUsedPiece());
        newUsedMap.put("" + currentState.getTurn(), newUsedList);

        List<Operation> ops = ImmutableList.<Operation> of(new SetTurn(
                currentState.getOppositeId()), new Set(Constants.JSON_PASS, false),
                new Set(Constants.JSON_ROTATION, rotation),
                new Set(Constants.JSON_USE_PIECE, pieceId), new Set(
                        Constants.JSON_POINT, pos.x + "," + pos.y),
                new Set(Constants.JSON_BITMAP, newBitmap),
                new Set(Constants.JSON_USER_USED_PIECES, newUsedMap));
        return ops;
    }

    public static List<Operation> getMoveInitial(List<String> playerIds) {
        List<Operation> operations = Lists.newArrayList();
        operations.add(new SetTurn(playerIds.get(1)));
        operations.add(new Set(Constants.JSON_USER_LIST, playerIds));
        operations.add(new Set(Constants.JSON_PASS_LIST, ImmutableList.<Integer> of()));
        operations.add(new Set(Constants.JSON_BITMAP, ""));
        Map<String, List<Integer>> usedMap = Maps.newHashMap();
        for (String id : playerIds) {
            usedMap.put("" + id, ImmutableList.<Integer> of());
        }
        operations.add(new Set(Constants.JSON_USER_USED_PIECES, usedMap));
        return operations;
    }
}
