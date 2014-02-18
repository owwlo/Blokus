
package org.owwlo.Blokus;

import java.util.List;

import com.google.common.collect.Lists;

public class Utils {

    /**
     * Range from [from, end).
     * 
     * @param k
     * @param from
     * @param end
     * @return
     */
    public static boolean Range(int k, int from, int end) {
        if (k < end && k >= from) {
            return true;
        }
        return false;
    }

    public static void printBoard(int[][] boardBitmap, int len) {
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                System.out.print(boardBitmap[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void fillBoard(int[][] boardBitmap, int len, int val) {
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                boardBitmap[i][j] = val;
            }
        }
    }

    public static List<Integer> getIndicesInRange(int fromInclusive,
            int toInclusive) {
        List<Integer> keys = Lists.newArrayList();
        for (int i = fromInclusive; i <= toInclusive; i++) {
            keys.add(i);
        }
        return keys;
    }
}
