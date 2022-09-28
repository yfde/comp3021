package hk.ust.comp3021.game;

import hk.ust.comp3021.entities.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * A Sokoban game board.
 * GameBoard consists of information loaded from map data, such as
 * <li>Width and height of the game map</li>
 * <li>Walls in the map</li>
 * <li>Box destinations</li>
 * <li>Initial locations of boxes and player</li>
 * <p/>
 * GameBoard is capable to create many GameState instances, each representing an ongoing game.
 */
public class GameMap {

    private int maxWidth;
    private int maxHeight;
    private int undoLimit;
    private Set<Position> destinations;
    private Set<Integer> playerIds;
    private HashMap<Position, Entity> allEntity;

    /**
     * Create a new GameMap with width, height, set of box destinations and undo limit.
     *
     * @param maxWidth     Width of the game map.
     * @param maxHeight    Height of the game map.
     * @param destinations Set of box destination positions.
     * @param undoLimit    Undo limit.
     *                     Positive numbers specify the maximum number of undo actions.
     *                     0 means undo is not allowed.
     *                     -1 means unlimited. Other negative numbers are not allowed.
     */
    public GameMap(int maxWidth, int maxHeight, Set<Position> destinations, int undoLimit) {
        // TODO
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.destinations = destinations;
        this.undoLimit = undoLimit;
        // throw new NotImplementedException();
    }

    /**
     * Parses the map from a string representation.
     * The first line is undo limit.
     * Starting from the second line, the game map is represented as follows,
     * <li># represents a {@link Wall}</li>
     * <li>@ represents a box destination.</li>
     * <li>Any upper-case letter represents a {@link Player}.</li>
     * <li>
     * Any lower-case letter represents a {@link Box} that is only movable by the player with the corresponding upper-case letter.
     * For instance, box "a" can only be moved by player "A" and not movable by player "B".
     * </li>
     * <li>. represents an {@link Empty} position in the map, meaning there is no player or box currently at this position.</li>
     * <p>
     * Notes:
     * <li>
     * There can be at most 26 players.
     * All implementations of classes in the hk.ust.comp3021.game package should support up to 26 players.
     * </li>
     * <li>
     * For simplicity, we assume the given map is bounded with a closed boundary.
     * There is no need to check this point.
     * </li>
     * <li>
     * Example maps can be found in "src/main/resources".
     * </li>
     *
     * @param mapText The string representation.
     * @return The parsed GameMap object.
     * @throws IllegalArgumentException if undo limit is negative but not -1.
     * @throws IllegalArgumentException if there are multiple same upper-case letters, i.e., one player can only exist at one position.
     * @throws IllegalArgumentException if there are no players in the map.
     * @throws IllegalArgumentException if the number of boxes is not equal to the number of box destinations.
     * @throws IllegalArgumentException if there are boxes whose {@link Box#getPlayerId()} do not match any player on the game board,
     *                                  or if there are players that have no corresponding boxes.
     */
    public static GameMap parse(String mapText) {
        // TODO
        var lines = mapText.split("\r?\n|\r");
        var undoLimit = Integer.parseInt(lines[0]);
        if (undoLimit < -1) {
            throw new IllegalArgumentException();
        }

        int maxWidth = 0;
        int maxHeight = 0;
        Set<Position> destinations = new HashSet<>();
        Set<Integer> playerIds = new HashSet<>();
        Set<Box> boxes = new HashSet<>();
        HashMap<Position, Entity> allEntity = new HashMap<>();
        for (int i = 1; i < lines.length; i++) {
            maxHeight++;
            if (lines[i].length() > maxWidth) {
                maxWidth = lines[i].length();
            }
        }
        for (int i = 0; i < maxHeight; i++) {
            var line = lines[i+1];
            for (int j = 0; j < maxWidth; j++) {
                if (j < line.length()) {
                    var c = line.charAt(j);
                    if ((int)(c - 'A') >= 0 && (int)(c - 'A') <= 25) {
                        if (playerIds.contains((int)(c - 'A'))) {
                            throw new IllegalArgumentException();
                        }
                        playerIds.add((int)(c - 'A'));
                        allEntity.put(new Position(j, i), new Player((int)(c - 'A')));
                    }else if ((int)(c - 'a') >= 0 && (int)(c - 'a') <= 25) {
                        boxes.add(new Box((int)(c - 'a')));
                        allEntity.put(new Position(j, i), new Box((int)(c - 'a')));
                    }else {
                        switch (c) {
                            case '.' -> allEntity.put(new Position(j, i), new Empty());
                            case '@' -> {
                                destinations.add(new Position(j, i));
                                allEntity.put(new Position(j, i), new Empty());
                            }
                            case '#' -> allEntity.put(new Position(j, i), new Wall());
                            default -> allEntity.put(new Position(j, i), null);
                        }
                    }
                }else {
                    allEntity.put(new Position(j, i), null);
                }
            }
        }
        Set<Integer> uniqueId = new HashSet<>();
        for (Box b: boxes) {
            if (!playerIds.contains(b.getPlayerId())) {
                throw new IllegalArgumentException();
            }
            if (!uniqueId.contains(b.getPlayerId())) {
                uniqueId.add(b.getPlayerId());
            }
        }
        if (uniqueId.size() != playerIds.size()) {
            throw new IllegalArgumentException();
        }
        if (playerIds.isEmpty()) {
            throw new IllegalArgumentException();
        }
        if (boxes.size() != destinations.size()) {
            throw new IllegalArgumentException();
        }
        var result = new GameMap(maxWidth, maxHeight, destinations, undoLimit);
        result.playerIds = playerIds;
        result.allEntity = allEntity;
        return result;
        // throw new NotImplementedException();
    }

    /**
     * Get the entity object at the given position.
     *
     * @param position the position of the entity in the game map.
     * @return Entity object.
     */
    @Nullable
    public Entity getEntity(Position position) {
        // TODO
        return this.allEntity.get(position);
        // throw new NotImplementedException();
    }

    /**
     * Put one entity at the given position in the game map.
     *
     * @param position the position in the game map to put the entity.
     * @param entity   the entity to put into game map.
     */
    public void putEntity(Position position, Entity entity) {
        // TODO
        this.allEntity.put(position, entity);
        // throw new NotImplementedException();
    }

    /**
     * Get all box destination positions as a set in the game map.
     *
     * @return a set of positions.
     */
    public @NotNull @Unmodifiable Set<Position> getDestinations() {
        // TODO
        return this.destinations;
        // throw new NotImplementedException();
    }

    /**
     * Get the undo limit of the game map.
     *
     * @return undo limit.
     */
    public Optional<Integer> getUndoLimit() {
        // TODO
        if (this.undoLimit == -1) {
            return Optional.empty();
        }else {
            return Optional.of(this.undoLimit);
        }
        // throw new NotImplementedException();
    }

    /**
     * Get all players' id as a set.
     *
     * @return a set of player id.
     */
    public Set<Integer> getPlayerIds() {
        // TODO
        return this.playerIds;
        // throw new NotImplementedException();
    }

    /**
     * Get the maximum width of the game map.
     *
     * @return maximum width.
     */
    public int getMaxWidth() {
        // TODO
        return this.maxWidth;
        // throw new NotImplementedException();
    }

    /**
     * Get the maximum height of the game map.
     *
     * @return maximum height.
     */
    public int getMaxHeight() {
        // TODO
        return maxHeight;
        // throw new NotImplementedException();
    }
}
