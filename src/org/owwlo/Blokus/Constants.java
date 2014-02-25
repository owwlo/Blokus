
package org.owwlo.Blokus;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class Constants {
    public static final boolean DEBUG = false;

    public static final int NO_OCCUPY_POINT_VALUE = 9/* Integer.MIN_VALUE */;

    public static final String JSON_TURN = "turn";
    public static final String JSON_ROTATION = "totation";
    public static final String JSON_USER_LIST = "user-list";
    public static final String JSON_USER_ID = "playerId";
    public static final String JSON_USER_USED_PIECES = "user-used-pieces";
    public static final String JSON_BITMAP = "bitmap";
    public static final String JSON_PASS = "pass";
    public static final String JSON_USE_PIECE = "use-piece";
    public static final String JSON_POINT = "point";
    public static final String JSON_PASS_LIST = "pass-list";

    public static final Map<Integer, Integer> boardSizeMap = ImmutableMap
            .<Integer, Integer> of(2, 14, 4, 21);

    public static final String getPieceForUserString(int userId) {
        return "pieceFor" + userId;
    }

    private static final List<Color> assignColorList = ImmutableList.of(Color.BLACK, Color.WHITE);

    public static final Color getColorForPlayerIndex(int index) {
        return assignColorList.get(index);
    }

    public static class BlokusPiece {
        public static final List<String> pieceStringList = new ArrayList<String>() {
            {
                this.add("0,0");
                this.add("0,0;0,1");
                this.add("0,0;0,1;1,1");
                this.add("0,0;0,1;0,2");
                this.add("0,0;0,1;1,0;1,1");
                this.add("0,1;1,0;1,1;1,2");
                this.add("0,0;0,1;0,2;0,3");
                this.add("0,2;1,0;1,1;1,2");
                this.add("0,1;0,2;1,0;1,1");
                this.add("0,0;1,0;1,1;1,2;1,3");
                this.add("0,1;1,1;2,0;2,1;2,2");
                this.add("0,0;1,0;2,0;2,1;2,2");
                this.add("0,1;0,2;0,3;1,0;1,1");
                this.add("0,2;1,0;1,1;1,2;2,0");
                this.add("0,0;1,0;2,0;3,0;4,0");
                this.add("0,0;1,0;1,1;2,0;2,1");
                this.add("0,1;0,2;1,0;1,1;2,0");
                this.add("0,0;0,1;1,0;2,0;2,1");
                this.add("0,1;0,2;1,0;1,1;2,1");
                this.add("0,1;1,0;1,1;1,2;2,1");
                this.add("0,1;1,0;1,1;1,2;1,3");
            }
        };
    }
}
