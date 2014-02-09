package org.owwlo.Blokus;

import java.util.ArrayList;
import java.util.List;

public class Constants {
	public final static boolean DEBUG = true;

	public final static int NO_OCCUPY_POINT_VALUE	= 9/*Integer.MIN_VALUE*/;

	public static class BlokusPiece {
		public static final List<String> pieceStringList = new ArrayList<String>(){{
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
		}};
	}
}
