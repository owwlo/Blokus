package org.owwlo.Blokus;

public class Utils {

	/**
	 * Range from [from, end).
	 * @param k
	 * @param from
	 * @param end
	 * @return
	 */
	public static boolean Range(int k, int from, int end) {
		if(k<end && k>=from) return true;
		return false;
	}
}
