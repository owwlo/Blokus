
package org.owwlo.Blokus.Model;

import java.util.ArrayList;
import java.util.List;

import org.owwlo.Blokus.Constants;

import com.google.common.collect.Lists;

/**
 * A Piece item used in Blokus.
 * 
 * @author owwlo
 */

public class Piece {
  private List<Point> pointList = new ArrayList<Point>();
  private boolean[][] pointBitmap = null;
  private int xMax = 0, yMax = 0;
  private int rotation = 0;

  /*
   * If the piece is not create by data string, the id for this piece will be
   * -1.
   */
  private int pieceId = -1;

  public final List<Point> getPointList() {
    return pointList;
  }

  public final boolean[][] getBitmap() {
    return pointBitmap;
  }

  public final int getWidth() {
    return xMax;
  }

  public final int getHeight() {
    return yMax;
  }

  /**
   * A Piece item contain no point used in Blokus.
   */
  public Piece() {
  }

  /**
   * A Piece item used in Blokus.
   * 
   * @param s The string contain data for Piece.
   */
  public Piece(String s) {
    setPointFromString(s);
  }

  /**
   * Set Shape of the Piece from string.
   * 
   * @param s The string contain data for Piece.
   */
  public void setPointFromString(String s) {
    pieceId = Constants.BlokusPiece.pieceStringList.indexOf(s);
    String[] strs = s.split(";");
    for (String str : strs) {
      String[] pos = str.split(",");
      Point point = new Point(Integer.parseInt(pos[0]), Integer.parseInt(pos[1]));
      addPoint(point);
    }
  }

  public Piece(Piece p) {
    for (Point point : p.getPointList()) {
      addPoint(point);
    }
    this.pieceId = p.getId();
    //this.rotate(p.getRotation());
  }

  /**
   * Add new point to the current Piece.
   * 
   * @param p Point to add.
   */
  public void addPoint(Point p) {
    pointList.add(p);
    if (p.x >= xMax || p.y >= yMax) {
      generateBitmapFromPointList();
    } else {
      pointBitmap[p.x][p.y] = true;
    }
    if (Constants.DEBUG) {
      printPiece();
    }
  }

  private void generateBitmapFromPointList() {
    for (Point p : pointList) {
      if (p.x >= xMax) {
        xMax = p.x + 1;
      }
      if (p.y >= yMax) {
        yMax = p.y + 1;
      }
    }
    pointBitmap = new boolean[xMax][yMax];
    for (Point p : pointList) {
      pointBitmap[p.x][p.y] = true;
    }
  }

  public void printPiece() {
    System.out.println("****************************");
    for (int i = 0; i < yMax; i++) {
      for (int j = 0; j < xMax; j++) {
        System.out.print((pointBitmap[i][j] ? 1 : 0) + " ");
      }
      System.out.println();
    }
    System.out.println("****************************");
    System.out.println();
  }

  public static Piece getPiece(int id) {
    return new Piece(Constants.BlokusPiece.pieceStringList.get(id));
  }

  public void rotate(int time) {
    while (time > 0) {
      rotate();
      time--;
    }
  }

  public void rotate() {
    rotation = (rotation + 1) % 4;
    List<Point> newPointList = Lists.newArrayList();
    int x_min = Integer.MAX_VALUE, x_max = Integer.MIN_VALUE;
    int y_min = Integer.MAX_VALUE, y_max = Integer.MIN_VALUE;
    for (Point p : pointList) {
      Point newP = new Point(p.y, -p.x);
      if (newP.x < x_min) {
        x_min = newP.x;
      }
      if (newP.x > x_max) {
        x_max = newP.x;
      }
      if (newP.y < y_min) {
        y_min = newP.y;
      }
      if (newP.y > y_max) {
        y_max = newP.y;
      }
      newPointList.add(newP);
    }
    int offset_x = -x_min;
    int offset_y = -y_min;
    for (Point p : newPointList) {
      p.x += offset_x;
      p.y += offset_y;
    }

    xMax = 0;
    yMax = 0;
    pointList.clear();

    for (Point p : newPointList) {
      addPoint(p);
    }
  }

  public final int getId() {
    return pieceId;
  }

  public int getRotation() {
    return rotation;
  }

  public int getxMax() {
    return xMax;
  }

  public int getyMax() {
    return yMax;
  }
}
