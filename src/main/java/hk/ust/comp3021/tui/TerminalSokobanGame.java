package hk.ust.comp3021.tui;


import hk.ust.comp3021.actions.*;
import hk.ust.comp3021.game.AbstractSokobanGame;
import hk.ust.comp3021.game.GameState;
import hk.ust.comp3021.game.InputEngine;
import hk.ust.comp3021.game.RenderingEngine;
import hk.ust.comp3021.utils.NotImplementedException;

/**
 * A Sokoban game running in the terminal.
 */
public class TerminalSokobanGame extends AbstractSokobanGame {

    private final InputEngine inputEngine;

    private final RenderingEngine renderingEngine;

    /**
     * Create a new instance of TerminalSokobanGame.
     * Terminal-based game only support at most two players, although the hk.ust.comp3021.game package supports up to 26 players.
     * This is only because it is hard to control too many players in a terminal-based game.
     *
     * @param gameState       The game state.
     * @param inputEngine     the terminal input engin.
     * @param renderingEngine the terminal rendering engine.
     * @throws IllegalArgumentException when there are more than two players in the map.
     */
    public TerminalSokobanGame(GameState gameState, TerminalInputEngine inputEngine, TerminalRenderingEngine renderingEngine) {
        super(gameState);
        this.inputEngine = inputEngine;
        this.renderingEngine = renderingEngine;
        // TODO
        // Check the number of players
        if (super.state.getAllPlayerPositions().size() > 2) {
            throw new IllegalArgumentException();
        }
        this.renderingEngine.message("Sokoban game is ready");
        // throw new NotImplementedException();
    }

    @Override
    public void run() {
        // TODO
        boolean exit = false;
        while (!super.shouldStop() && !exit) {
            this.renderingEngine.render(super.state);
            if (super.state.getUndoQuota().isPresent()) {
                this.renderingEngine.message("Undo Quota: " + String.valueOf(this.state.getUndoQuota().get()));
            } else {
                this.renderingEngine.message("Unlimited");
            }

            var act = super.processAction(this.inputEngine.fetchAction());
            switch (act) {
                case ActionResult.Success a -> {
                    if (a.getAction() instanceof Exit) {
                        exit = true;
                    }
                }
                case ActionResult.Failed a -> this.renderingEngine.message(a.getReason());
            }
        }
        this.renderingEngine.render(super.state);
        this.renderingEngine.message("Game exits.");
        if (!exit) {
            this.renderingEngine.message("You win.");
        }
        // throw new NotImplementedException();
    }
}
