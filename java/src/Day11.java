import java.awt.*;


public class Day11 {
  final static short size = 300;
  private static Short[][] grid = new Short[size][size];

  public static void main(String[] args) {
    Point p = getPowerfullCell();
    System.out.println("Max Power at: " + p.x + "," + p.y);
  }

  private static Point getPowerfullCell() {
    short maxPower = 0;
    Point maxPowerCoordinates = new Point();

    for (short x = 0; x < size; x++) {
      for(short y = 0; y < size; y++) {
        short powerAt = getPowerAt(x, y);
        if (powerAt > maxPower) {
          maxPower = powerAt;
          maxPowerCoordinates = new Point(x + 1, y + 1);
        }
      }
    }
    return maxPowerCoordinates;
  }

  private static short getPowerAt(short x, short y) {
    short sum = 0;
    for (short i = x; i < x + 3; i++) {
      for (short j = y; j < y + 3; j++) {
        if (i >= size || j >= size) {
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
