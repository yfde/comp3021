package hk.ust.comp3021.game;

import hk.ust.comp3021.entities.Box;
import hk.ust.comp3021.entities.Empty;
import hk.ust.comp3021.entities.Player;
import hk.ust.comp3021.entities.Wall;
import hk.ust.comp3021.utils.TestHelper;
import hk.ust.comp3021.utils.TestKind;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class GameMapTest {

    private static final String rectangularMap = """
            233
            ######
            #A..@#
            #...@#
            #....#
            #.a..#
            #..a.#
            ######
            """;

    @Tag(TestKind.REGRESSION)
    @Test
    void testWidthForRectangularMap() {
        final var gameMap = TestHelper.parseGameMap(rectangularMap);
        assertEquals(6, gameMap.getMaxWidth());
    }

    @Tag(TestKind.REGRESSION)
    @Test
    void testHeightForRectangularMap() {
        final var gameMap = TestHelper.parseGameMap(rectangularMap);
        assertEquals(7, gameMap.getMaxHeight());
    }

    @Tag(TestKind.REGRESSION)
    @Test
    void testGetDestinations() {
        final var gameMap = TestHelper.parseGameMap(rectangularMap);
        assertEquals(2, gameMap.getDestinations().size());
        assertInstanceOf(Empty.class, gameMap.getEntity(Position.of(4, 1)));
    }

    @Tag(TestKind.REGRESSION)
    @Test
    void testWallParsing() {
        final var gameMap = TestHelper.parseGameMap(rectangularMap);
        assertInstanceOf(Wall.class, gameMap.getEntity(Position.of(0, 0)));
    }

    @Tag(TestKind.REGRESSION)
    @Test
    void testPlayerParsing() {
        final var gameMap = TestHelper.parseGameMap(rectangularMap);
        final var player = assertInstanceOf(Player.class, gameMap.getEntity(Position.of(1, 1)));
        assertNotNull(player);
        assertEquals(0, player.getId());
    }

    @Tag(TestKind.REGRESSION)
    @Test
    void testBoxParsing() {
        final var gameMap = TestHelper.parseGameMap(rectangularMap);
        final var box = assertInstanceOf(Box.class, gameMap.getEntity(Position.of(2, 4)));
        assertNotNull(box);
        assertEquals(0, box.getPlayerId());
    }

    @Tag(TestKind.REGRESSION)
    @Test
    void testEmptyCellParsing() {
        final var gameMap = TestHelper.parseGameMap(rectangularMap);
        assertInstanceOf(Empty.class, gameMap.getEntity(Position.of(2, 1)));
    }

    @Tag(TestKind.REGRESSION)
    @Test
    void testGetEntity() {
        final var gameMap = TestHelper.parseGameMap(rectangularMap);
        final var entity = gameMap.getEntity(Position.of(0, 0));
        assertTrue(entity instanceof Wall);
    }

    private static final String nonRectangularMap = """
            233
            ######
            #A..@#
            #...@###
            #a....@##
            #.a.....#
            #..a.####
            ######
            """;

    @Tag(TestKind.REGRESSION)
    @Test
    void testWidthForNonRectangularMap() {
        final var gameMap = TestHelper.parseGameMap(nonRectangularMap);
        assertEquals(9, gameMap.getMaxWidth());
    }

    @Tag(TestKind.REGRESSION)
    @Test
    void testHeightForNonRectangularMap() {
        final var gameMap = TestHelper.parseGameMap(nonRectangularMap);
        assertEquals(7, gameMap.getMaxHeight());
    }

    @Tag(TestKind.REGRESSION)
    @Test
    void testUndoLimitParsing() {
        final var gameMap = TestHelper.parseGameMap(rectangularMap);
        assertEquals(233, gameMap.getUndoLimit().orElse(null));
    }

    @Tag(TestKind.REGRESSION)
    @Test
    void testInsufficientDestinations() {
        final var invalidMap = """
                233
                ######
                #A..@#
                #...@#
                #a...#
                #.a..#
                #..a.#
                ######
                """;
        assertThrowsExactly(IllegalArgumentException.class, () -> TestHelper.parseGameMap(invalidMap));
    }

    @Tag(TestKind.REGRESSION)
    @Test
    void testEmptyMap() {
        final var invalidMap = "";
        assertThrowsExactly(IllegalArgumentException.class, () -> TestHelper.parseGameMap(invalidMap));
    }

    @Tag(TestKind.REGRESSION)
    @Test
    void testMapWithoutPlayer() {
        final var invalidMap = """
                233
                ###
                #.#
                ###
                """;
        assertThrowsExactly(IllegalArgumentException.class, () -> TestHelper.parseGameMap(invalidMap));
    }

    @Tag(TestKind.REGRESSION)
    @Test
    void testUnmatchedPlayersAndBoxes() {
        final var invalidMap = """
                233
                ######
                #A..@#
                #...@#
                #a.b@#
                #.a.@#
                #..a.#
                ######
                """;
        assertThrowsExactly(IllegalArgumentException.class, () -> TestHelper.parseGameMap(invalidMap));
    }

    @Tag(TestKind.REGRESSION)
    @Test
    void testInvalidUndoLimit() {
        final var invalidMap = """
                -233
                ######
                #A..@#
                #...@#
                #a.b@#
                #.a.@#
                #..a.#
                ######
                """;
        assertThrowsExactly(IllegalArgumentException.class, () -> TestHelper.parseGameMap(invalidMap));
    }

    @Tag(TestKind.REGRESSION)
    @Test
    void testTwoPlayersMap() {
        final var twoPlayersMap = """
                233
                ######
                #A..@#
                #..B@#
                #....#
                #.a..#
                #..b.#
                ######
                """;
        final var gameMap = TestHelper.parseGameMap(twoPlayersMap);
        final var playerIds = gameMap.getPlayerIds();

        final var expectedIds = new HashSet<Integer>();
        expectedIds.add(0);
        expectedIds.add(1);
        assertEquals(expectedIds, playerIds);
    }

    @Tag(TestKind.REGRESSION)
    @Test
    void testManyPlayersMap() {
        final var manyPlayersMap = """
                233
                ######
                #A.P@@#
                #vCB@@#
                #.cpD@#
                #Ga.d@#
                #g.bV@#
                ######
                """;
        final var gameMap = TestHelper.parseGameMap(manyPlayersMap);
        final var playerIds = gameMap.getPlayerIds();

        final var expectedIds = new HashSet<>(Arrays.asList(
                0,
                'P' - 'A',
                'C' - 'A',
                'B' - 'A',
                'D' - 'A',
                'G' - 'A',
                'V' - 'A'
        ));
        assertEquals(expectedIds, playerIds);
    }

    @Tag(TestKind.REGRESSION)
    @Test
    void testDuplicatedPlayers() {
        final var invalidMap = """
                233
                ######
                #A..@#
                #..A@#
                #....#
                #.a..#
                #..b.#
                ######
                """;
        assertThrowsExactly(IllegalArgumentException.class, () -> TestHelper.parseGameMap(invalidMap));
    }

    @Tag(TestKind.REGRESSION)
    @Test
    void testParseMapWithoutUndoLimit() {
        final var invalidMap = String.join("\n", rectangularMap.lines().skip(1).toList());
        assertThrowsExactly(IllegalArgumentException.class, () -> TestHelper.parseGameMap(invalidMap));
    }

    @Tag(TestKind.REGRESSION)
    @Test
    void testSuperLargeMap() {
        assertDoesNotThrow(() -> {
            new GameMap(Integer.MAX_VALUE, Integer.MAX_VALUE, Collections.emptySet(), 0);
        });
    }

    @Tag(TestKind.REGRESSION)
    @Test
    void testOpenMap() {
        final var invalidMap = """
            233
            #####
            #A..@
            #..B@#
            #....#
            #.a..#
            #..b.#
            ######
            """;
        assertThrowsExactly(IllegalArgumentException.class, () -> TestHelper.parseGameMap(invalidMap));
    }

    @Tag(TestKind.REGRESSION)
    @Test
    void testMapWithoutWall() {
        final var invalidMap = """
            233
            A...
            ....
            ...@
            .a..
            """;
        assertThrowsExactly(IllegalArgumentException.class, () -> TestHelper.parseGameMap(invalidMap));
    }
}
