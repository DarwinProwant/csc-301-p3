import java.awt.*;
import java.util.*;

/**
 * Clyde - The scaredy-cat (Orange Ghost).
 * Clyde will chase Pac-Man directly, but when it gets within 8 dots,
 * it gets scared and runs away to a corner or safe location.
 */
public class Clyde extends Ghost {

    private int tileSize;
    private int rows;
    private int cols;
    private boolean[][] wallGrid;

    /**
     * Constructor for Clyde (Orange Ghost).
     *
     * @param game   Reference to the PacMan game instance
     * @param image  The image to display for Clyde
     * @param x      Initial x position
     * @param y      Initial y position
     * @param width  Width of the ghost
     * @param height Height of the ghost
     */
    public Clyde(PacMan game, Image image, int x, int y, int width, int height) {
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
     * Calculates the target position for Clyde.
     * If Clyde is far from Pac-Man (>8 dots), target is Pac-Man's position.
     * If Clyde is close to Pac-Man (<=8 dots), target is a corner or safe location.
     */
    @Override
    public Point calculateTarget() {
        if (game != null && game.getPacman() != null) {
            double distance = getDistanceToPacMan();
            int scaredDistance = tileSize * 8; // 8 dots

            if (distance > scaredDistance) {
                // Chase Pac-Man directly
                Block pacman = game.getPacman();
                return new Point(pacman.x, pacman.y);
            } else {
                // Run to corner
                return getCornerTarget();
            }
        }
        return new Point(x, y);
    }

    /**
     * Calculates the distance from Clyde to Pac-Man.
     */
    public double getDistanceToPacMan() {
        if (game != null && game.getPacman() != null) {
            Block pacman = game.getPacman();
            int dx = pacman.x - this.x;
            int dy = pacman.y - this.y;
            return Math.sqrt(dx * dx + dy * dy);
        }
        return Double.MAX_VALUE;
    }

    /**
     * Checks if Clyde is within 8-dot scared radius.
     */
    public boolean isScared() {
        double distance = getDistanceToPacMan();
        int scaredDistance = tileSize * 8;
        return distance <= scaredDistance;
    }

    /**
     * Gets a corner/safe location target for when Clyde is scared.
     * Here we use an inner bottom-left tile (not the wall itself).
     */
    private Point getCornerTarget() {
        if (game != null) {
            // bottom inner-left corridor (row 19, col 1 in your map)
            int targetX = tileSize;                // col 1
            int targetY = (rows - 2) * tileSize;   // row 19 (0-based, 0..20)
            return new Point(targetX, targetY);
        }
        return new Point(x, y);
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
     * Chooses the best direction using BFS toward Clyde's current target:
     * - Far: chase Pac-Man
     * - Close: run to corner
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
            return direction; // already there
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
     * Moves Clyde: chooses direction and updates velocity.
     * Position update and collision handling are done in PacMan.move().
     */
    @Override
    public void move() {
        char newDir = chooseDirection();
        updateDirection(newDir);
    }
}

