package org.owwlo.Blokus.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;

public class BlokusState {
	/**
	 * TODO why the turn should be declared as final?
	 */
	private final int turn;

	private Map<Integer, List<Piece>> pieceFromPlayer;

	public BlokusState(int turn, Map<Integer, List<Piece>> pieceFromPlayer) {
		super();
		this.turn = turn;
		this.pieceFromPlayer = pieceFromPlayer;
	}

	public int getTurn() {
		return turn;
	}

	public Map<Integer, List<Piece>> getPieceFromPlayer() {
		return pieceFromPlayer;
	}

	@SuppressWarnings("unchecked")
	public static BlokusState getStateFromApiState(
			Map<String, Object> gameApiState) {
		int _turn = (int) gameApiState.get("turn");
		Map<Integer, List<Piece>> pieceForUsersMap = new HashMap<Integer, List<Piece>>();
		ImmutableList<Integer> userIds = (ImmutableList<Integer>) gameApiState
				.get("userIds");
		for (Integer i : userIds) {
			ImmutableList<Integer> pieceForI = (ImmutableList<Integer>) gameApiState
					.get("pieceFor" + i);
			pieceForUsersMap.put(i, getPieceListFromIndexList(pieceForI));
		}
		return new BlokusState(_turn, pieceForUsersMap);
	}

	private static List<Piece> getPieceListFromIndexList(
			ImmutableList<Integer> pieceForI) {
		List<Piece> pieces = new ArrayList<>();
		for (Integer i : pieceForI) {
			pieces.add(Piece.getPiece(i));
		}
		return pieces;
	}
}
