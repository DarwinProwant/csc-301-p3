import java.awt.*;
import java.util.*;

/**
 * Inky - The hybrid (Blue Ghost).
 * Inky's movement is a combination of Blinky and Pinky's strategies.
 * It targets a point that blends Pac-Man's current position and an
 * anticipated position ahead of Pac-Man.
 */
public class Inky extends Ghost {

    private int tileSize;
    private int rows;
    private int cols;
    private boolean[][] wallGrid;

    /**
     * Constructor for Inky (Blue Ghost).
     */
    public Inky(PacMan game, Image image, int x, int y, int width, int height) {
        super(game, image, x, y, width, height);

        if (game != null) {
            tileSize = game.getTileSize();
            rows = game.getBoardHeight() / tileSize;
            cols = game.getBoardWidth() / tileSize;
            wallGrid = new boolean[rows][cols];

            for (Block wall : game.getWalls()) {
                int wr = wall.y / tileSize;
                int wc = wall.x / tileSize;
                if (wr >= 0 && wr < rows && wc >= 0 && wc < cols) {
                    wallGrid[wr][wc] = true;
                }
            }
        }
    }

    /**
     * Calculates the target position for Inky.
     * Hybrid of Blinky-style direct chase and Pinky-style anticipatory chase.
     */
    @Override
    public Point calculateTarget() {
        if (game != null && game.getPacman() != null) {
            return combineStrategies();
        }
        return new Point(x, y);
    }

    /**
     * Combines Blinky's and Pinky's strategies to create a hybrid target.
     * Blinky: Pac-Man's current position.
     * Pinky:  2 tiles ahead of Pac-Man in its current direction.
     */
    public Point combineStrategies() {
        Block pacman = game.getPacman();
        if (pacman == null) {
            return new Point(x, y);
        }

        // Blinky's direct target
        Point blinkyTarget = new Point(pacman.x, pacman.y);

        // Pinky's anticipatory target (2 tiles ahead)
        int aheadDistance = tileSize * 2;
        Point pinkyTarget = new Point(pacman.x, pacman.y);

        if (pacman.direction == 'U') {
            pinkyTarget.y -= aheadDistance;
        } else if (pacman.direction == 'D') {
            pinkyTarget.y += aheadDistance;
        } else if (pacman.direction == 'L') {
            pinkyTarget.x -= aheadDistance;
        } else if (pacman.direction == 'R') {
            pinkyTarget.x += aheadDistance;
        }

        // Hybrid: average of both
        int combinedX = (blinkyTarget.x + pinkyTarget.x) / 2;
        int combinedY = (blinkyTarget.y + pinkyTarget.y) / 2;

        return new Point(combinedX, combinedY);
    }

    private int getCol() {
        return x / tileSize;
    }

    private int getRow() {
        return y / tileSize;
    }

    private boolean isWall(int r, int c) {
        return (r < 0 || r >= rows || c < 0 || c >= cols) || wallGrid[r][c];
    }

    private char getOpposite(char dir) {
        switch (dir) {
            case 'U': return 'D';
            case 'D': return 'U';
            case 'L': return 'R';
            case 'R': return 'L';
            default:  return ' ';
        }
    }

    /**
     * Chooses the best direction using BFS toward Inky's hybrid target.
     */
    @Override
    public char chooseDirection() {
        if (game == null || game.getPacman() == null) {
            return direction;
        }

        Point target = calculateTarget();
        if (target == null) {
            return direction;
        }

        int gcol = getCol();
        int grow = getRow();
        int tcol = target.x / tileSize;
        int trow = target.y / tileSize;

        // Clamp target into bounds
        tcol = Math.max(0, Math.min(cols - 1, tcol));
        trow = Math.max(0, Math.min(rows - 1, trow));

        if (gcol == tcol && grow == trow) {
            return direction;
        }

        Point start = new Point(gcol, grow);
        Point goal  = new Point(tcol, trow);

        Queue<Point> queue = new LinkedList<>();
        Map<Point, Point> cameFrom = new HashMap<>();
        Set<Point> visited = new HashSet<>();

        queue.offer(start);
        visited.add(start);
        cameFrom.put(start, null);

        boolean found = false;
        Point goalPoint = null;

        int[] drs = {-1, 1, 0, 0};      // U, D, L, R
        int[] dcs = {0, 0, -1, 1};
        char[] dirChars = {'U', 'D', 'L', 'R'};

        while (!queue.isEmpty() && !found) {
            Point curr = queue.poll();
            if (curr.x == goal.x && curr.y == goal.y) {
                goalPoint = curr;
                found = true;
                break;
            }

            for (int i = 0; i < 4; i++) {
                // avoid immediate reverse from the current direction at the start
                if (curr.equals(start) && dirChars[i] == getOpposite(direction)) {
                    continue;
                }

                int nr = curr.y + drs[i];
                int nc = curr.x + dcs[i];

                if (!isWall(nr, nc)) {
                    Point nextPoint = new Point(nc, nr);
                    if (!visited.contains(nextPoint)) {
                        visited.add(nextPoint);
                        queue.offer(nextPoint);
                        cameFrom.put(nextPoint, curr);
                    }
                }
            }
        }

        if (goalPoint == null) {
            return direction; // no path found
        }

        // Reconstruct path
        java.util.List<Point> path = new ArrayList<>();
        Point curr = goalPoint;
        while (curr != null) {
            path.add(curr);
            curr = cameFrom.get(curr);
        }
        Collections.reverse(path);

        if (path.size() < 2) {
            return direction;
        }

        Point nextPos = path.get(1);
        int dr = nextPos.y - start.y;
        int dc = nextPos.x - start.x;

        if (dr == -1) return 'U';
        if (dr == 1)  return 'D';
        if (dc == -1) return 'L';
        if (dc == 1)  return 'R';

        return direction;
    }

    /**
     * Moves Inky based on hybrid chase behavior.
     * Position update and collision handling are done in PacMan.move().
     */
    @Override
    public void move() {
        char newDir = chooseDirection();
        updateDirection(newDir);
    }
}
