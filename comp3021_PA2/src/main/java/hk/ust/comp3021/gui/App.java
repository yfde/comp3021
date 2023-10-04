package hk.ust.comp3021.gui;

import hk.ust.comp3021.game.GameState;
import hk.ust.comp3021.gui.component.maplist.MapEvent;
import hk.ust.comp3021.gui.scene.game.ExitEvent;
import hk.ust.comp3021.gui.scene.game.GameScene;
import hk.ust.comp3021.gui.scene.start.StartScene;
import hk.ust.comp3021.gui.utils.Message;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The JavaFX application that launches the game.
 */
public class App extends Application {
    private Stage primaryStage;

    private Scene startScene;

    /**
     * Set up the primary stage and show the {@link StartScene}.
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages.
     * @throws Exception if something goes wrong.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Sokoban Game - COMP3021 2022Fall");
        // TODO
        this.primaryStage.setScene(new StartScene());
        this.startScene = this.primaryStage.getScene();
        this.primaryStage.show();
        this.primaryStage.addEventHandler(MapEvent.OPEN_MAP_EVENT_TYPE, this::onOpenMap);
        this.primaryStage.addEventHandler(ExitEvent.EVENT_TYPE, this::onExitGame);
    }

    /**
     * Event handler for opening a map.
     * Switch to the {@link GameScene} in the {@link this#primaryStage}.
     *
     * @param event The event data related to the map being opened.
     */
    public void onOpenMap(MapEvent event) {
        // TODO
        var gameState = new GameState(event.getModel().gameMap());
        if (gameState.getAllPlayers().size() > 4) {
            Message.error("Too many players", "The maximum number of players is 4.");
            return;
        }
        try {
            this.primaryStage.setScene(new GameScene(gameState));
        } catch (IOException e) {
            Message.error("Failed to create the GameScene", e.getMessage());
        }

    }

    /**
     * Event handler for exiting the game.
     * Switch to the {@link StartScene} in the {@link this#primaryStage}.
     *
     * @param event The event data related to exiting the game.
     */
    public void onExitGame(ExitEvent event) {
        // TODO
        this.primaryStage.setScene(this.startScene);
    }
}
