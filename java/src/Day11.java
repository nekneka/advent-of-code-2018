import java.awt.*;


public class Day11 {
  private final static short GRID_SIZE = 300;
  private static Short[][] grid = new Short[GRID_SIZE][GRID_SIZE];

  public static void main(String[] args) {
    Point p = getPowerfullCell((short)3, (short)3);
    System.out.println("Max Power at: " + p.x + "," + p.y);

    // part 2
    // TODO: brute forcing, memoize already calculated power for smaller square sizes
    Point p2 = getPowerfullCell((short)1, GRID_SIZE);
    System.out.println("Max Power at: " + p2.x + "," + p2.y);
  }

  private static Point getPowerfullCell(short startSize, short endSize) {
    int maxPower = 0;
    int maxSize = -1;
    Point maxPowerCoordinates = new Point();

    for (short size = startSize; size <= endSize; size++) {
      for (short x = 0; x < GRID_SIZE; x++) {
        for(short y = 0; y < GRID_SIZE; y++) {
          int powerAt = getPowerAt(x, y, size);
          if (powerAt > maxPower) {
            maxPower = powerAt;
            maxSize = size;
            maxPowerCoordinates = new Point(x + 1, y + 1);
          }
        }
      }
    }
    System.out.println("maxSize:" + maxSize);
    return maxPowerCoordinates;
  }

  private static int getPowerAt(short x, short y, short size) {
    int sum = 0;
    for (short i = x; i < x + size; i++) {
      for (short j = y; j < y + size; j++) {
        if (i >= GRID_SIZE || j >= GRID_SIZE) {
          return 0;
        }
        sum += getCellPower(i, j);
      }
    }
    return sum;
  }

  private static short getCellPower(short x, short y) {
    if (grid[y][x] == null) {
      final int power = ((x + 11) * (y + 1) + 9995) * (x + 11);
      final int thirdDigit = (power / 100) % 10;
      grid[y][x] = (short) (thirdDigit - 5);
    }
    return grid[y][x];
  }
}
