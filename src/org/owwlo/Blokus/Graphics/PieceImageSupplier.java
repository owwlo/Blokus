
package org.owwlo.Blokus.Graphics;

import com.google.gwt.resources.client.ImageResource;

public class PieceImageSupplier {
  private final PieceImages pieceImages;

  public PieceImageSupplier(PieceImages pieceImages) {
    this.pieceImages = pieceImages;
  }

  public ImageResource getResource(int pieceIndex) {
    switch(pieceIndex) {
      case 1:
        return pieceImages.piece1();
      case 2:
        return pieceImages.piece2();
      case 3:
        return pieceImages.piece3();
      case 4:
        return pieceImages.piece4();
      case 5:
        return pieceImages.piece5();
      case 6:
        return pieceImages.piece6();
      case 7:
        return pieceImages.piece7();
      case 8:
        return pieceImages.piece8();
      case 9:
        return pieceImages.piece9();
      case 10:
        return pieceImages.piece10();
      case 11:
        return pieceImages.piece11();
      case 12:
        return pieceImages.piece12();
      case 13:
        return pieceImages.piece13();
      case 14:
        return pieceImages.piece14();
      case 15:
        return pieceImages.piece15();
      case 16:
        return pieceImages.piece16();
      case 17:
        return pieceImages.piece17();
      case 18:
        return pieceImages.piece18();
      case 19:
        return pieceImages.piece19();
      case 20:
        return pieceImages.piece20();
      case 21:
        return pieceImages.piece21();
    }
    return null;
  }
}
