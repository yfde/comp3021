package hk.ust.comp3021.gui.scene.game;

import hk.ust.comp3021.game.GameState;
import hk.ust.comp3021.gui.component.board.GameBoard;
import hk.ust.comp3021.gui.component.control.ControlPanel;
import hk.ust.comp3021.gui.utils.Resource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Control logic for {@link GameScene}.
 */
public class GameSceneController implements Initializable {

    @FXML
    private GameBoard gameBoard;

    @FXML
    private ControlPanel controlPanel;

    @FXML
    private GridPane gamePane;

    private GUISokobanGame game;

    /**
     * Initialize the controller as you need.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO

    }

    /**
     * @param gameState Loads the game state.
     */
    public void setGameState(GameState gameState) {
        this.game = new GUISokobanGame(gameState, controlPanel.getController(), gameBoard.getController());
        // generate player controls
        gameState.getAllPlayers().forEach(player -> this.controlPanel.getController().addPlayer(
            player,
            Resource.getPlayerImageURL(player.getId())
        ));

        final var gameLoopThread = new Thread(game);
        gameLoopThread.start();
    }

    /**
     * Event handler when the exit button is clicked.
     * Fire {@link ExitEvent} so that {@link hk.ust.comp3021.gui.App} can handle it and switch to the start scene.
     *
     * @param event The event data
     */
    @FXML
    public void onExit(ActionEvent event) {
        // TODO

    }
}
