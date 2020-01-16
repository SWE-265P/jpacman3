package nl.tudelft.jpacman.npc.ghost;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.sprite.Sprite;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * An implementation of the classic Pac-Man ghost Shadow.
 * </p>
 * <p>
 * Nickname: Blinky. As his name implies, Shadow is usually a constant shadow on
 * Pac-Man's tail. When he's not patrolling the top-right corner of the maze,
 * Shadow tries to find the quickest route to Pac-Man's position. Despite the
 * fact that Pinky's real name is Speedy, Shadow is actually the fastest of the
 * ghosts because of when there are only a few pellets left, Blinky drastically
 * speeds up, which can make him quite deadly. In the original Japanese version,
 * his name is Oikake/Akabei.
 * </p>
 * <p>
 * <b>AI:</b> When the ghosts are not patrolling in their home corners (Blinky:
 * top-right, Pinky: top-left, Inky: bottom-right, Clyde: bottom-left), Blinky
 * will attempt to shorten the distance between Pac-Man and himself. If he has
 * to choose between shortening the horizontal or vertical distance, he will
 * choose to shorten whichever is greatest. For example, if Pac-Man is four grid
 * spaces to the left, and seven grid spaces above Blinky, he'll try to move up
 * towards Pac-Man before he moves to the left.
 * </p>
 * <p>
 * Source: http://strategywiki.org/wiki/Pac-Man/Getting_Started
 * </p>
 *
 * @author Jeroen Roosen
 *
 */
public class Inky extends Ghost {

    private static final int SQUARES_AHEAD = 2;

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
     * Creates a new "Inky".
     *
     * @param spriteMap The sprites for this ghost.
     */
    public Inky(Map<Direction, Sprite> spriteMap) {
        super(spriteMap, MOVE_INTERVAL, INTERVAL_VARIATION);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * When the ghosts are not patrolling in their home corners (Blinky: top-right,
     * Pinky: top-left, Inky: bottom-right, Clyde: bottom-left), Blinky will attempt
     * to shorten the distance between Pac-Man and himself. If he has to choose
     * between shortening the horizontal or vertical distance, he will choose to
     * shorten whichever is greatest. For example, if Pac-Man is four grid spaces to
     * the left, and seven grid spaces above Blinky, he'll try to move up towards
     * Pac-Man before he moves to the left.
     * </p>
     */
    @Override
    public Optional<Direction> nextAiMove() {
        assert hasSquare();
        Unit blinky = Navigation.findNearest(Blinky.class, getSquare());
        Unit player = Navigation.findNearest(Player.class, getSquare());

        if (blinky == null || player == null) {
            return Optional.empty();
        }

        assert player.hasSquare();
        Square playerDestination = player.squaresAheadOf(SQUARES_AHEAD);

        List<Direction> firstHalf = Navigation.shortestPath(blinky.getSquare(),
            playerDestination, null);

        if (firstHalf == null) {
            return Optional.empty();
        }

        Square destination = followPath(firstHalf, playerDestination);
        List<Direction> path = Navigation.shortestPath(getSquare(),
            destination, this);

        if (path != null && !path.isEmpty()) {
            return Optional.ofNullable(path.get(0));
        }
        return Optional.empty();
    }


    private Square followPath(List<Direction> directions, Square start) {
        Square destination = start;

        for (Direction d : directions) {
            destination = destination.getSquareAt(d);
        }

        return destination;
    }
}
