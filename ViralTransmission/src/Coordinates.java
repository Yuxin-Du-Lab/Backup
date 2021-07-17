public class Coordinates {
    private final int x;
    private final int y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getDistance(Coordinates point) {
        double xDistance = Math.abs(point.getX() - x);
        double yDistance = Math.abs(point.getY() - y);
        return Math.sqrt(xDistance * xDistance + yDistance * yDistance);
    }

    public boolean equal(Coordinates newPoint) {
        return x == newPoint.getX() && y == newPoint.getY();
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
