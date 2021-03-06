
package org.owwlo.Blokus.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.owwlo.Blokus.Constants;
import org.owwlo.Blokus.Utils;

import com.google.common.collect.Maps;

public class BlokusState {
    /*
     * One thing important to know is that if the turn is 0 that means there is
     * no last player in the game.
     */
    private final String turn;

    private List<String> playerList;
    private int[][] bitmap;
    private Map<String, List<Integer>> everyPlayerUsedPiece;
    private String bitmapString;
    private List<String> passList;

    public BlokusState(String turn,
            List<String> playerList, int[][] bitmap,
            Map<String, List<Integer>> everyPlayerUsedPiece, String bitmapString,
            List<String> passList) {
        super();
        this.turn = turn;
        this.playerList = playerList;
        this.bitmap = bitmap;
        this.everyPlayerUsedPiece = everyPlayerUsedPiece;
        this.bitmapString = bitmapString;
        this.passList = passList;
    }

    public void addPassId(String id) {
        passList.add(id);
    }

    public final List<String> getPassList() {
        return passList;
    }

    public final String getTurn() {
        return turn;
    }

    public final String getOppositeId() {
        String id = "";
        for (String i : playerList) {
            if (!i.equals(turn)) {
                id = i;
                break;
            }
        }
        return id;
    }

    public final Map<String, List<Integer>> getPieceFromPlayer() {
        Map<String, List<Integer>> rtn = Maps.newHashMap();
        for (String playerId : playerList) {
            rtn.put(playerId, Utils.getIndicesInRange(0, 20)); // There are 20
                                                               // pieces in
                                                               // total.
            List<Integer> pieceUsed = everyPlayerUsedPiece.get(playerId + "");
            rtn.get(playerId).removeAll(pieceUsed);
        }
        return rtn;
    }

    public final List<String> getPlayerList() {
        return playerList;
    }

    public final int[][] getBitmap() {
        return bitmap;
    }

    public final Map<String, List<Integer>> getEveryPlayerUsedPiece() {
        return everyPlayerUsedPiece;
    }

    public final String getBitmapString() {
        return bitmapString;
    }

    @SuppressWarnings("unchecked")
    public static BlokusState getStateFromApiState(
            Map<String, Object> gameApiState) {
        return getStateFromApiState(
                gameApiState, (String) gameApiState.get(Constants.JSON_TURN));
    }

    @SuppressWarnings("unchecked")
    public static BlokusState getStateFromApiState(
            Map<String, Object> gameApiState, String turn) {
        List<String> userList = (List<String>) gameApiState
                .get(Constants.JSON_USER_LIST);
        List<String> passList = new ArrayList<>((List<String>) gameApiState
                .get(Constants.JSON_PASS_LIST));
        Map<String, List<Integer>> usedUsedPieceAll = (Map<String, List<Integer>>) gameApiState
                .get(Constants.JSON_USER_USED_PIECES);
        int[][] bitmap;

        int boardsize = Constants.boardSizeMap.get(userList.size());
        bitmap = new int[boardsize][boardsize];
        Utils.fillBoard(bitmap, boardsize, Constants.NO_OCCUPY_POINT_VALUE);
        String bitmapStr = (String) gameApiState.get(Constants.JSON_BITMAP);
        String[] bitmapStrSpl = bitmapStr.split(" ");
        for (String str : bitmapStrSpl) {
            if (str.length() <= 1) {
                continue;
            }
            String[] inner = str.split(",");
            int row = Integer.parseInt(inner[0]);
            int col = Integer.parseInt(inner[1]);
            int color = Integer.parseInt(inner[2]);
            bitmap[row][col] = color;
        }
        return new BlokusState(turn, userList, bitmap,
                usedUsedPieceAll, bitmapStr, passList);
    }

    public static boolean equal(BlokusState a, BlokusState b) {
        if (a.getTurn() != b.getTurn()) {
            return false;
        }
        if (a.getPlayerList().size() != b.getPlayerList().size()) {
            return false;
        } else {
            for (String i : a.getPlayerList()) {
                if (!b.getPlayerList().contains(i)) {
                    return false;
                }
            }
        }
        if (a.getEveryPlayerUsedPiece().size() != b.getEveryPlayerUsedPiece().size()) {
            return false;
        } else {
            if (!a.getEveryPlayerUsedPiece().keySet()
                    .equals(b.getEveryPlayerUsedPiece().keySet())) {
                return false;
            } else {
                for (String key : a.getEveryPlayerUsedPiece().keySet()) {
                    List<Integer> l1 = new ArrayList<>(a.getEveryPlayerUsedPiece().get(key));
                    List<Integer> l2 = new ArrayList<>(a.getEveryPlayerUsedPiece().get(key));
                    Collections.sort(l1);
                    Collections.sort(l2);
                    if (!l1.equals(l2)) {
                        return false;
                    }
                }
            }
        }
        int boardLen = Constants.boardSizeMap.get(a.getPlayerList().size());
        for (int i = 0; i < boardLen; i++) {
            for (int j = 0; j < boardLen; j++) {
                if (a.getBitmap()[i][j] != b.getBitmap()[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public String getBitmapStr() {
        StringBuilder sb = new StringBuilder();
        int boardsize = Constants.boardSizeMap.get(getPlayerList().size());
        for (int y = 0; y < boardsize; y++) {
            for (int x = 0; x < boardsize; x++) {
                if (bitmap[y][x] != Constants.NO_OCCUPY_POINT_VALUE) {
                    sb.append(y + "," + x + "," + bitmap[y][x] + " ");
                }
            }
        }
        return sb.toString().trim();
    }
}
