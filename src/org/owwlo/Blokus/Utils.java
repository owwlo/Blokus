
package org.owwlo.Blokus;

import java.util.List;

import com.google.common.collect.Lists;

public class Utils {

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

  public static int[][] getBitmapFromPointStr(String str) {
    return getBitmapFromPointStr(str, 14);
  }

  public static int[][] getBitmapFromPointStr(String str, int size) {
    int[][] bitmap;
    bitmap = new int[size][size];
    Utils.fillBoard(bitmap, size, Constants.NO_OCCUPY_POINT_VALUE);
    String[] bitmapStrSpl = str.split(" ");
    for (String s : bitmapStrSpl) {
      if (s.length() <= 1) {
        continue;
      }
      String[] inner = s.split(",");
      int row = Integer.parseInt(inner[0]);
      int col = Integer.parseInt(inner[1]);
      int color = Integer.parseInt(inner[2]);
      bitmap[row][col] = color;
    }
    return bitmap;
  }

  public static int getIntIdFromStringLongId(String id) {
    Long l = Long.parseLong(id);
    return (int) (l%Integer.MAX_VALUE);
  }
}
