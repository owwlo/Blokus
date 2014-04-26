package org.owwlo.Blokus.Graphics;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;

public interface I18n extends Messages {
	public static I18n LANG = GWT.create(I18n.class);

	@Key("nextPiece")
	String nextPiece();

	@Key("dropHere")
	String dropHere();

	@Key("passThisRound")
	String passThisRound();

	@Key("availablePieces")
	String availablePieces();
}
