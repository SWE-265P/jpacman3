package nl.tudelft.jpacman.npc.ghost;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.sprite.Sprite;

/**
 * <p>
 * An implementation of the classic Pac-Man ghost Inky.
 * </p>
 * <b>AI:</b> Inky has the most complicated AI of all. Inky considers two
 * things: Blinky's location, and the location two grid spaces ahead of Pac-Man.
 * Inky draws a line from Blinky to the spot that is two squares in front of
 * Pac-Man and extends that line twice as far. Therefore, if Inky is alongside
 * Blinky when they are behind Pac-Man, Inky will usually follow Blinky the
 * whole time. But if Inky is in front of Pac-Man when Blinky is far behind him,
 * Inky tends to want to move away from Pac-Man (in reality, to a point very far
 * ahead of Pac-Man). Inky is affected by a similar targeting bug that affects
 * Speedy. When Pac-Man is moving or facing up, the spot Inky uses to draw the
 * line is two squares above and left of Pac-Man.
 * <p>
 * Source: http://strategywiki.org/wiki/Pac-Man/Getting_Started
 * </p>
 *
 * @author Jeroen Roosen
 */
public class Blinky extends Ghost {

    /**
     * The variation in intervals, this makes the ghosts look more dynamic and
     * less predictable.
     */
    private static final int INTERVAL_VARIATION = 50;

    /**
     * The base movement interval.
     */
    private static final int MOVE_INTERVAL = 250;

    /**
     * Creates a new "Blinky", a.k.a. "Shadow".
     *
     * @param spriteMap
     *            The sprites for this ghost.
     */
    // TODO Blinky should speed up when there are a few pellets left, but he
    // has no way to find out how many there are.
    public Blinky(Map<Direction, Sprite> spriteMap) {
        super(spriteMap, MOVE_INTERVAL, INTERVAL_VARIATION);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Inky has the most complicated AI of all. Inky considers two things: Blinky's
     * location, and the location two grid spaces ahead of Pac-Man. Inky draws a
     * line from Blinky to the spot that is two squares in front of Pac-Man and
     * extends that line twice as far. Therefore, if Inky is alongside Blinky when
     * they are behind Pac-Man, Inky will usually follow Blinky the whole time. But
     * if Inky is in front of Pac-Man when Blinky is far behind him, Inky tends to
     * want to move away from Pac-Man (in reality, to a point very far ahead of
     * Pac-Man). Inky is affected by a similar targeting bug that affects Speedy.
     * When Pac-Man is moving or facing up, the spot Inky uses to draw the line is
     * two squares above and left of Pac-Man.
     * </p>
     *
     * <p>
     * <b>Implementation:</b> To actually implement this in jpacman we have the
     * following approximation: first determine the square of Blinky (A) and the
     * square 2 squares away from Pac-Man (B). Then determine the shortest path from
     * A to B regardless of terrain and walk that same path from B. This is the
     * destination.
     * </p>
     */
    @Override
    public Optional<Direction> nextAiMove() {
        assert hasSquare();

        // TODO Blinky should patrol his corner every once in a while
        // TODO Implement his actual behaviour instead of simply chasing.
        Unit nearest = Navigation.findNearest(Player.class, getSquare());
        if (nearest == null) {
            return Optional.empty();
        }
        assert nearest.hasSquare();
        Square target = nearest.getSquare();

        List<Direction> path = Navigation.shortestPath(getSquare(), target, this);
        if (path != null && !path.isEmpty()) {
            return Optional.ofNullable(path.get(0));
        }
        return Optional.empty();
    }
}
