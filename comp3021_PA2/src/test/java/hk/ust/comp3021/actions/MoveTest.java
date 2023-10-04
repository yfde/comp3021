package hk.ust.comp3021.actions;

import hk.ust.comp3021.game.Position;
import hk.ust.comp3021.utils.TestKind;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MoveTest {

    private final Position pos = Position.of(233, 233);

    @Tag(TestKind.SANITY)
    @Test
    void moveLeft() {
        assertEquals(
                Position.of(232, 233),
                new Move.Left(-1).nextPosition(pos)
        );
    }

    @Tag(TestKind.SANITY)
    @Test
    void moveRight() {
        assertEquals(
                Position.of(234, 233),
                new Move.Right(-1).nextPosition(pos)
        );
    }

    @Tag(TestKind.SANITY)
    @Test
    void moveUp() {
        assertEquals(
                Position.of(233, 232),
                new Move.Up(-1).nextPosition(pos)
        );
    }

    @Tag(TestKind.SANITY)
    @Test
    void moveDown() {
        assertEquals(
                Position.of(233, 234),
                new Move.Down(-1).nextPosition(pos)
        );
    }
}
