package org.owwlo.Blokus.Model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NoInitialContextException;

/**
 * A Piece item used in Blokus.
 * @author owwlo
 */

public class Piece {
	private List<Point> pointList = new ArrayList<Point>();
	private boolean pointBitmap[][] = null;
	private int xMax = 0, yMax = 0;

	public final boolean[][] getBitmap() throws NoInitialContextException {
		if(pointBitmap == null) {
			throw new NoInitialContextException();
		}
		return pointBitmap;
	}

	/**
	 * A Piece item contain no point used in Blokus.
	 */
	public Piece() {
	}

	/**
	 * A Piece item used in Blokus.
	 * @param s The string contain data for Piece.
	 */
	public Piece(String s) {
		setPointFromString(s);
	}

	/**
	 * Set Shape of the Piece from string.
	 * @param s The string contain data for Piece.
	 */
	public void setPointFromString(String s) {

	}

	/**
	 * Add new point to the current Piece.
	 * @param p Point to add.
	 */
	public void addPoint(Point p) {
		pointList.add(p);
		if(p.x > xMax || p.y > yMax) {
			generateBitmapFromPointList();
		} else {
			pointBitmap[p.y][p.x] = true;
		}
	}

	private void generateBitmapFromPointList(){
		for(Point p : pointList) {
			if(p.x > xMax) xMax = p.x;
			if(p.y > yMax) yMax = p.y;
		}
		pointBitmap = new boolean[yMax][xMax];
		for(Point p : pointList) {
			pointBitmap[p.y][p.x] = true;
		}
	}
}
