
package org.owwlo.Blokus.Graphics;

import java.util.List;

import org.adamtacy.client.ui.effects.events.EffectCompletedEvent;
import org.adamtacy.client.ui.effects.events.EffectCompletedHandler;
import org.adamtacy.client.ui.effects.impl.Resize;
import org.owwlo.Blokus.BlokusPresenter;
import org.owwlo.Blokus.BlokusPresenter.ViewState;
import org.owwlo.Blokus.Constants;
import org.owwlo.Blokus.Utils;
import org.owwlo.Blokus.Model.Piece;
import org.owwlo.Blokus.Model.Point;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.BoundaryDropController;
import com.allen_sauer.gwt.dnd.client.drop.SimpleDropController;
import com.allen_sauer.gwt.voices.client.Sound;
import com.allen_sauer.gwt.voices.client.SoundController;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class BlokusGraphics extends Composite implements BlokusPresenter.View {
  private final static int PLAYER_NUMBER = 2;
  private final static String TRY_BLOCK_STYLE = "color-try";
  private final static String PLAYER_A_BLOCK_STYLE = "color-icon";
  private final static String PLAYER_B_BLOCK_STYLE = "color-icon2";

  private BlokusPresenter blokusPresenter;
  private Piece currentPiece;
  private final PieceImageSupplier pieceImageSupplier;

  private List<Button> btns = Lists.newArrayList();
  private List<Button> currentPieceBtns = Lists.newArrayList();
  private List<Integer> tryPoint = Lists.newArrayList();
  private List<Image> pendingImages;
  private boolean canSelectPiece = true;

  private static BlokusGraphicsUiBinder uiBinder = GWT.create(BlokusGraphicsUiBinder.class);
  private SoundController soundController = new SoundController();
  private Sound sound = soundController.createSound(Sound.MIME_TYPE_AUDIO_WAV_PCM,
      "pieceDown.wav");
  private PickupDragController dragController;
  private SimpleDropController dropController;

  interface BlokusGraphicsUiBinder extends UiBinder<Widget, BlokusGraphics> {
  }

  @UiField
  VerticalPanel buttonContainer;
  @UiField
  VerticalPanel currentPieceContainer;
  @UiField
  FlowPanel pieceSelectContainer;
  @UiField
  Button passButton;
  @UiField
  Label drag_part;
  @UiField
  Label drop_part;

  @UiHandler("passButton")
  void onClickPassButton(ClickEvent e) {
    blokusPresenter.passMyTurn();
  }

  public BlokusGraphics() {
    PieceImages pieceImages = GWT.create(PieceImages.class);
    this.pieceImageSupplier = new PieceImageSupplier(pieceImages);
    initWidget(uiBinder.createAndBindUi(this));
    dragController = new PickupDragController(RootPanel.get(), false);
    dragController.makeDraggable(drag_part);
    dropController = new TestDropController(drop_part);
    dragController.registerDropController(dropController);
    initBoard();
    initPieceSelectArea();
    initCurrentPieceArea();
    initConfig();
  }

  class TestDropController extends SimpleDropController {
    public TestDropController(Widget dropTarget) {
      super(dropTarget);
    }
    @Override
    public void onDrop(DragContext context) {
      for (Widget w : context.selectedWidgets) {
        if(w instanceof Label) {
          w.setVisible(false);
          BlokusGraphics.this.drop_part.setText("You have dropped here!");
        }
      }
      super.onDrop(context);
    }
  }
  
  private void initConfig() {
    setViewState(ViewState.VIEW_ONLY);
  }

  private void initCurrentPieceArea() {
    int size = 5;
    for (int i = 0; i < size; i++) {
      HorizontalPanel hpInner = new HorizontalPanel();
      for (int j = 0; j < size; j++) {
        Button btn = new Button();
        btn.setSize("30px", "30px");
        btn.addClickHandler(new ClickHandler() {
          @Override
          public void onClick(ClickEvent event) {
            rotateCurrentPiece();
          }
        });
        currentPieceBtns.add(btn);
        hpInner.add(btn);
      }
      currentPieceContainer.add(hpInner);
    }
  }

  protected void rotateCurrentPiece() {
    if (currentPiece != null) {
      currentPiece.rotate();
      updateCurrentPiece();
    }
  }

  private void updateCurrentPiece() {
    if (currentPiece != null) {
      clearCurrentPieceShowCase();
      List<Point> pList = currentPiece.getPointList();

      int pieceXMax = currentPiece.getxMax();
      int pieceYMax = currentPiece.getyMax();

      int xOffset = (Constants.SELECT_PIECE_SHOW_SIZE - (pieceXMax)) / 2;
      int yOffset = (Constants.SELECT_PIECE_SHOW_SIZE - (pieceYMax)) / 2;

      int size = Constants.SELECT_PIECE_SHOW_SIZE;
      for (Point p : pList) {
        int index = (xOffset + p.x) * size + (yOffset + p.y);
        Button btn = currentPieceBtns.get(index);
        btn.setStyleName(TRY_BLOCK_STYLE);
      }
    }
  }

  private void clearCurrentPieceShowCase() {
    for (Button btn : currentPieceBtns) {
      btn.setStyleName("");
    }
  }

  private void initPieceSelectArea() {
    pendingImages = createImages(Utils.getIndicesInRange(1, 21), true);
    for (Image img : pendingImages) {
      img.setSize("50px", "50px");
      pieceSelectContainer.add(img);
    }
  }

  private List<Image> createImages(List<Integer> pieceIdx, boolean withClick) {
    List<Image> res = Lists.newArrayList();
    for (final int idx : pieceIdx) {
      final Image image = new Image(pieceImageSupplier.getResource(idx));
      if (withClick) {
        image.addClickHandler(new ClickHandler() {
          @Override
          public void onClick(ClickEvent event) {
            if (canSelectPiece) {
              sound.play();
              final int w = image.getWidth();
              final int h = image.getHeight();
              final Resize effect = new Resize(image.getElement());
              effect.setEndPercentage(150);
              effect.setDuration(0.2);
              effect.addEffectCompletedHandler(new EffectCompletedHandler() {
                @Override
                public void onEffectCompleted(EffectCompletedEvent event) {
                  if (effect.isInverted()) {
                    image.setSize(w + "px", h + "px");
                    return;
                  }
                  effect.invert();
                  effect.play();
                }
              });
              choseNewPiece(idx - 1);
              effect.play();
            }
          }
        });
      }
      res.add(image);
    }
    return res;
  }

  protected void choseNewPiece(int idx) {
    int pieceId = idx;
    currentPiece = Piece.getPiece(pieceId);
    blokusPresenter.chosePiece(pieceId);
    updateCurrentPiece();
  }

  private void initBoard() {
    final int size = Constants.boardSizeMap.get(PLAYER_NUMBER);
    for (int i = 0; i < size; i++) {
      HorizontalPanel hpInner = new HorizontalPanel();
      for (int j = 0; j < size; j++) {
        Button btn = new Button();
        btn.setSize("30px", "30px");
        btn.addClickHandler(new ClickHandler() {
          @Override
          public void onClick(ClickEvent event) {
            Button btn = (Button) event.getSource();
            int index = btns.indexOf(btn);
            int row = index / size;
            int col = index % size;
            blokusPresenter.makeMove(currentPiece, new Point(row, col));
          }
        });
        btn.addMouseOverHandler(new MouseOverHandler() {

          @Override
          public void onMouseOver(MouseOverEvent event) {
            Button btn = (Button) event.getSource();
            int index = btns.indexOf(btn);
            int row = index / size;
            int col = index % size;
            blokusPresenter.tryCurrentPieceWithPosition(currentPiece, new Point(row,
                col));
          }
        });
        btns.add(btn);
        hpInner.add(btn);
      }
      buttonContainer.add(hpInner);
    }
    BoundaryDropController dropController = new BoundaryDropController(RootPanel.get(), false);
    dragController.registerDropController(dropController);
  }

  @Override
  public void pickFromValidPiece(List<Integer> pieces) {
    for (Image img : pendingImages) {
      img.setVisible(false);
    }
    for (int idx : pieces) {
      pendingImages.get(idx).setVisible(true);
    }
    choseNewPiece(pieces.get(0));
  }

  @Override
  public void updateTryBlock(Piece piece, Point pos) {
    final int size = Constants.boardSizeMap.get(PLAYER_NUMBER);
    if (piece != null) {
      for (Point p : piece.getPointList()) {
        int index = (pos.x + p.x) * size + (pos.y + p.y);
        tryPoint.add(index);
        Button btn = btns.get(index);
        btn.setStyleName(TRY_BLOCK_STYLE);
      }
    }
  }

  @Override
  public void setViewState(ViewState vs) {
    if (vs == ViewState.MAKE_MOVE) {
      for (Button btn : btns) {
        btn.setEnabled(true);
        passButton.setEnabled(true);
      }
    } else if (vs == ViewState.VIEW_ONLY) {
      for (Button btn : btns) {
        btn.setEnabled(false);
        passButton.setEnabled(false);
      }
    }
  }

  @Override
  public void cleanTryBlock() {
    final int size = Constants.boardSizeMap.get(PLAYER_NUMBER);
    for (int index : tryPoint) {
      btns.get(index).removeStyleName(TRY_BLOCK_STYLE);
    }
    tryPoint.clear();
  }

  @Override
  public void setPresenter(BlokusPresenter blokusPresenter) {
    this.blokusPresenter = blokusPresenter;
  }

  @Override
  public void setGameBoard(String yourPlayerId, List<String> playerList, String bitmap) {
    int yourPlayerIdInt = Utils.getIntIdFromStringLongId(yourPlayerId);

    final int size = Constants.boardSizeMap.get(PLAYER_NUMBER);
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        int index = i * size + j;
        Button btn = btns.get(index);
        btn.setStyleName("");
      }
    }
    String[] bitmapStrSpl = bitmap.split(" ");
    for (String str : bitmapStrSpl) {
      if (str.length() <= 1) {
        continue;
      }
      String[] inner = str.split(",");
      int row = Integer.parseInt(inner[0]);
      int col = Integer.parseInt(inner[1]);
      int color = Integer.parseInt(inner[2]);
      int index = row * size + col;
      Button btn = btns.get(index);
      if (color == yourPlayerIdInt) {
        btn.setStyleName(PLAYER_B_BLOCK_STYLE);
      } else {
        btn.setStyleName(PLAYER_A_BLOCK_STYLE);
      }
    }
  }

  @Override
  public void diaosi() {
    Window.alert("Sorry, Lose...");
  }

  @Override
  public void gaofushuai() {
    Window.alert("You Win!");
  }

  @Override
  public void updateMyStep(String player, Point pos, List<Point> pointList) {
    final int size = Constants.boardSizeMap.get(PLAYER_NUMBER);
    for (Point p : pointList) {
      int index = (pos.x + p.x) * size + (pos.y + p.y);
      Button btn = btns.get(index);
      btn.setStyleName(PLAYER_B_BLOCK_STYLE);
    }
  }
}
