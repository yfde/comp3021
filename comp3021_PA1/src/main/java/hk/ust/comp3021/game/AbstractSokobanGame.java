package hk.ust.comp3021.game;

import hk.ust.comp3021.actions.*;
import hk.ust.comp3021.entities.*;
import org.jetbrains.annotations.NotNull;

/**
 * A base implementation of Sokoban Game.
 */
public abstract class AbstractSokobanGame implements SokobanGame {
    @NotNull
    protected final GameState state;
    private boolean exit = false;

    protected AbstractSokobanGame(@NotNull GameState gameState) {
        this.state = gameState;
    }

    /**
     * @return True is the game should stop running.
     * For example when the user specified to exit the game or the user won the game.
     */
    protected boolean shouldStop() {
        // TODO
        if (this.state.isWin() || exit) {
            return true;
        }
        return false;
        // throw new NotImplementedException();
    }

    /**
     * @param action The action received from the user.
     * @return The result of the action.
     */
    protected ActionResult processAction(@NotNull Action action) {
        // TODO
        return switch (action) {
            case Move act-> {
                var p = this.state.getPlayerPositionById(act.getInitiator());
                var e = this.state.getEntity(act.nextPosition(p));
                String error = "";
                var result = switch (e) {
                    case Wall ignored -> {
                        error = "You hit a wall.";
                        yield false;
                    }
                    case Box b -> {
                        if (b.getPlayerId() == act.getInitiator()) {
                            if (this.state.getEntity(act.nextPosition(act.nextPosition(p))) instanceof Empty) {
                                this.state.move(act.nextPosition(p), act.nextPosition(act.nextPosition(p)));
                                this.state.move(p, act.nextPosition(p));
                                this.state.checkpoint();
                                yield true;
                            } else {
                                error = "Failed to push the box.";
                                yield false;
                            }
                        } else {
                            error = "You cannot move other players' boxes.";
                            yield false;
                        }
                    }
                    case Player ignored -> {
                        error = "You hit another player.";
                        yield false;
                    }
                    case Empty ignored -> {
                        this.state.move(p, act.nextPosition(p));
                        yield true;
                    }
                    default -> false;
                };
                if (result) {
                    yield new ActionResult.Success(act);
                } else {
                    yield new ActionResult.Failed(act, error);
                }
            }
            case Undo act -> {
                var quota = this.state.getUndoQuota();
                if (quota.isPresent()){
                    if (quota.get() != 0) {
                        this.state.undo();
                        yield new ActionResult.Success(act);
                    } else {
                        yield new ActionResult.Failed(act, "You have run out of your undo quota.");
                    }
                } else {
                    this.state.undo();
                    yield new ActionResult.Success(act);
                }
            }
            case InvalidInput act -> new ActionResult.Failed(act, "Invalid Input");
            case Exit act -> {
                this.exit = true;
                yield new ActionResult.Success(act);
            }
        };
        // throw new NotImplementedException();
    }
}
