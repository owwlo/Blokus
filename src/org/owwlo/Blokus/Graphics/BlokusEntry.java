
package org.owwlo.Blokus.Graphics;

import org.game_api.GameApi;
import org.game_api.GameApi.Container;
import org.game_api.GameApi.Game;
import org.game_api.GameApi.UpdateUI;
import org.game_api.GameApi.VerifyMove;
import org.owwlo.Blokus.BlokusPresenter;
import org.owwlo.Blokus.Model.BlokusLogic;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class BlokusEntry implements EntryPoint {
  Container container;
  BlokusPresenter blokusPresenter;

  @Override
  public void onModuleLoad() {
    Game game = new Game() {
      @Override
      public void sendVerifyMove(VerifyMove verifyMove) {
        container.sendVerifyMoveDone(BlokusLogic.verify(verifyMove));
      }

      @Override
      public void sendUpdateUI(UpdateUI updateUI) {
        blokusPresenter.updateUI(updateUI);
      }
    };
    container = new GameApi.ContainerConnector(game);
    BlokusGraphics blokusGraphics = new BlokusGraphics();
    blokusPresenter =
        new BlokusPresenter(blokusGraphics, container);
    RootPanel.get("mainDiv").add(blokusGraphics);
    container.sendGameReady();
  }
}
