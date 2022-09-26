package hk.ust.comp3021.game;

import hk.ust.comp3021.entities.*;
import hk.ust.comp3021.utils.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;

/**
 * The state of the Sokoban Game.
 * Each game state represents an ongoing game.
 * As the game goes, the game state changes while players are moving while the original game map stays the unmodified.
 * <b>The game state should not modify the original game map.</b>
 * <p>
 * GameState consists of things changing as the game goes, such as:
 * <li>Current locations of all crates.</li>
 * <li>A move history.</li>
 * <li>Current location of player.</li>
 * <li>Undo quota left.</li>
 */
public class GameState {

    private int maxWidth;
    private int maxHeight;
    private Optional<Integer> undoLimit;
    private Set<Position> destinations = new HashSet<>();
    private Set<Integer> playerIds = new HashSet<>();
    private HashMap<Position, Entity> allEntity = new HashMap<>();
    private ArrayList<HashMap<Position, Entity>> checkpoints = new ArrayList<>();

    /**
     * Create a running game state from a game map.
     *
     * @param map the game map from which to create this game state.
     */
    public GameState(@NotNull GameMap map) {
        // TODO
        this.maxWidth = map.getMaxWidth();
        this.maxHeight = map.getMaxHeight();
        this.undoLimit = map.getUndoLimit();
        this.destinations = map.getDestinations();
        this.playerIds = map.getPlayerIds();
        for (int i = 0; i < this.maxHeight; i++) {
            for (int j = 0; j < this.maxWidth; j++) {
                Entity e = map.getEntity(new Position(j, i));
                this.allEntity.put(new Position(j, i), e);
            }
        }
        checkpoint();
        // throw new NotImplementedException();
    }

    /**
     * Get the current position of the player with the given id.
     *
     * @param id player id.
     * @return the current position of the player.
     */
    public @Nullable Position getPlayerPositionById(int id) {
        // TODO
        for (int i = 0; i < this.maxHeight; i++) {
            for (int j = 0; j < this.maxWidth; j++) {
                Entity e = this.allEntity.get(new Position(j, i));
                if (e instanceof Player) {
                    if (((Player) e).getId() == id) {
                        return new Position(j, i);
                    }
                }
            }
        }
        return null;
        // throw new NotImplementedException();
    }

    /**
     * Get current positions of all players in the game map.
     *
     * @return a set of positions of all players.
     */
    public @NotNull Set<Position> getAllPlayerPositions() {
        // TODO
        Set<Position> result = new HashSet<>();
        for (int i = 0; i < this.maxHeight; i++) {
            for (int j = 0; j < this.maxWidth; j++) {
                Entity e = this.allEntity.get(new Position(j, i));
                if (e instanceof Player) {
                    result.add(new Position(j, i));
                }
            }
        }
        return result;
        // throw new NotImplementedException();
    }

    /**
     * Get the entity that is currently at the given position.
     *
     * @param position the position of the entity.
     * @return the entity object.
     */
    public @Nullable Entity getEntity(@NotNull Position position) {
        // TODO
        return this.allEntity.get(position);
        // throw new NotImplementedException();
    }

    /**
     * Get all box destination positions as a set in the game map.
     * This should be the same as that in {@link GameMap} class.
     *
     * @return a set of positions.
     */
    public @NotNull @Unmodifiable Set<Position> getDestinations() {
        // TODO
        return this.destinations;
        // throw new NotImplementedException();
    }

    /**
     * Get the undo quota currently left, i.e., the maximum number of undo actions that can be performed from now on.
     * If undo is unlimited,
     *
     * @return the undo quota left (using {@link Optional#of(Object)}) if the game has an undo limit;
     * {@link Optional#empty()} if the game has unlimited undo.
     */
    public Optional<Integer> getUndoQuota() {
        // TODO
        return this.undoLimit;
        // throw new NotImplementedException();
    }

    /**
     * Check whether the game wins or not.
     * The game wins only when all box destinations have been occupied by boxes.
     *
     * @return true is the game wins.
     */
    public boolean isWin() {
        // TODO
        for (Position p: this.destinations) {
            if (!(this.allEntity.get(p) instanceof Box)) {
                return false;
            }
        }
        return true;
        // throw new NotImplementedException();
    }

    /**
     * Move the entity from one position to another.
     * This method assumes the validity of this move is ensured.
     * <b>The validity of the move of the entity in one position to another need not to check.</b>
     *
     * @param from The current position of the entity to move.
     * @param to   The position to move the entity to.
     */
    public void move(Position from, Position to) {
        // TODO
        this.allEntity.put(to, this.allEntity.get(from));
        this.allEntity.put(from, new Empty());
        // throw new NotImplementedException();
    }

    /**
     * Record a checkpoint of the game state, including:
     * <li>All current positions of entities in the game map.</li>
     * <li>Current undo quota</li>
     * <p>
     * Checkpoint is used in {@link GameState#undo()}.
     * Every undo actions reverts the game state to the last checkpoint.
     */
    public void checkpoint() {
        // TODO
        HashMap<Position, Entity> temp = new HashMap<>();
        for (int i = 0; i < this.maxHeight; i++) {
            for (int j = 0; j < this.maxWidth; j++) {
                Entity e = this.allEntity.get(new Position(j, i));
                temp.put(new Position(j, i), e);
            }
        }
        this.checkpoints.add(temp);
        // throw new NotImplementedException();
    }

    /**
     * Revert the game state to the last checkpoint in history.
     * This method assumes there is still undo quota left, and decreases the undo quota by one.
     * <p>
     * If there is no checkpoint recorded, i.e., before moving any box when the game starts,
     * revert to the initial game state.
     */
    public void undo() {
        // TODO
        int size = this.checkpoints.size();
        if (size > 1) {
            this.allEntity = this.checkpoints.get(size - 2);
            this.checkpoints.remove(size - 1);
            if (this.undoLimit.isPresent()) {
                this.undoLimit = Optional.of(this.undoLimit.get() - 1);
            }
        } else {
            this.allEntity = this.checkpoints.get(0);
        }
        // throw new NotImplementedException();
    }

    /**
     * Get the maximum width of the game map.
     * This should be the same as that in {@link GameMap} class.
     *
     * @return maximum width.
     */
    public int getMapMaxWidth() {
        // TODO
        return this.maxWidth;
        // throw new NotImplementedException();
    }

    /**
     * Get the maximum height of the game map.
     * This should be the same as that in {@link GameMap} class.
     *
     * @return maximum height.
     */
    public int getMapMaxHeight() {
        // TODO
        return this.maxHeight;
        // throw new NotImplementedException();
    }
}
