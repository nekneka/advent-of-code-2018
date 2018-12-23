import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static java.util.stream.IntStream.*;


public class Day17 {

  static class Result {
    boolean hitAngle;
    int count;
  }

  private static final short SPRING = 4;
  private static final short SAND = 0;
  private static final short STONE = 1;
  private static final short STILL_WATER = 3;
  private static final short RUNNING_WATER = 2;
  private static short[][] slice;
  private static int LAST_ROW = 0;

  public static void main(String[] args) {
    slice = readSlice();
    slice[0][500] = SPRING;
    Result result = fillDown(new Result(), 500, 1);
    System.out.println(result.count);
    printSlice(slice);
  }

  private static Result fillDown(Result result, int x, int y) {
    final short currentSquare = slice[y][x];
    if (y > LAST_ROW) {
      return result;
    }

    if (currentSquare == STONE || currentSquare == STILL_WATER) {
      Result leftResult = fillLeftOrRight(new Result(), x-1, y-1, false);
      Result rightResult = fillLeftOrRight(new Result(), x+1, y-1, true);

      result.count += leftResult.count + rightResult.count;

      if (leftResult.hitAngle && rightResult.hitAngle) {
        boolean isFilledWithStillWater = makeStillWater(x, y - 1);
        if (isFilledWithStillWater) {
          return overFlow(result, x, y - 2);
        } else {
          makeRunningWater(x,y-1);
        }
      }

      return result;
    }

    if (currentSquare == SAND) {
      slice[y][x] = RUNNING_WATER;
      result.count += 1;
      return fillDown(result, x, y+1);
    }

    if (currentSquare == RUNNING_WATER) {
//       situation where the water falls from 2 different sides in one container
      if (slice[y - 1][x - 1] == STONE || slice[y - 1][x + 1] == STONE) {
        return overFlow(result, x, y - 1);
      }
    }

    return result;
  }
  private static void makeRunningWater(int x, int y){
    int xleft = x;
    while(slice[y][xleft] == STILL_WATER) {
      slice[y][xleft] = RUNNING_WATER;
      xleft-=1;
    }
    int xRight = x+1;
    while(slice[y][xRight] == STILL_WATER) {
      slice[y][xRight] = RUNNING_WATER;
      xRight+=1;
    }
  }

  private static boolean makeStillWater(int x, int y) {
    boolean didNotRetrace = true;
    slice[y][x] = STILL_WATER;

    int xLeft = x -1;
    while(slice[y][xLeft] == RUNNING_WATER) {
      slice[y][xLeft] = STILL_WATER;

      if (slice[y][xLeft-1] == SAND) {
        didNotRetrace = false;
      }
      xLeft -= 1;
    }

    int xRight = x + 1;
    while(slice[y][xRight] == RUNNING_WATER) {
      slice[y][xRight] = STILL_WATER;

      if ( slice[y][xRight+1] == SAND) {
        didNotRetrace = false;
      }
      xRight += 1;
    }
    return didNotRetrace;
  }

  private static Result fillLeftOrRight(Result result, int x, int y, boolean right) {
    final short currentSquare = slice[y][x];
    if (currentSquare == STONE || currentSquare == STILL_WATER) {
      result.hitAngle = true;
      return result;
    }
    if (currentSquare == SAND || currentSquare == RUNNING_WATER) {
      if (currentSquare == SAND) {
        result.count += 1;
        slice[y][x] = RUNNING_WATER;
      }
      if (slice[y+1][x] == SAND || slice[y+1][x] == RUNNING_WATER) {
        if (slice[y+1][x] == SAND) {
          return fillDown(result, x, y + 1);
        }
      } else {
        if (right) {
          return fillLeftOrRight(result, x+1, y, true);
        } else {
          return fillLeftOrRight(result, x-1, y, false);
        }
      }
    }
    return result;
  }

  private static Result overFlow(Result result, int x, int y) {
    Result leftResult = fillLeftOrRight(new Result(), x-1, y, false);
    Result rightResult = fillLeftOrRight(new Result(), x+1, y, true);

    result.count = result.count + leftResult.count + rightResult.count;

    boolean isFilledWithStillWater = makeStillWater(x, y);
    if (isFilledWithStillWater) {
      return overFlow(result, x, y-1);
    } else {
      makeRunningWater(x,y);
    }
    return result;
  }

  private static short[][] readSlice() {
    short[][] slice = new short[1800][700];
    Pattern pattern = Pattern.compile("^([xy]+)=([\\d]*),\\s[xy]+=([\\d]*)..([\\d]*)");
    int max_y = 0;

    URL path = Day17.class.getResource("17-18-input");
    File file = new File(path.getFile());
    Scanner sc;
    try {
      sc = new Scanner(file);
      while(sc.hasNextLine()){

        Matcher matcher = pattern.matcher(sc.nextLine());
        int x0, x1, y0, y1;

        if (matcher.find()) {
          if(matcher.group(1).equals("x")) {
            x0 = Integer.parseInt(matcher.group(2));
            x1 = x0;
            y0 = Integer.parseInt(matcher.group(3));
            y1 = Integer.parseInt(matcher.group(4));
          } else {
            y0 = Integer.parseInt(matcher.group(2));
            y1 = y0;
            x0 = Integer.parseInt(matcher.group(3));
            x1 = Integer.parseInt(matcher.group(4));
          }

          for (int x : rangeClosed(x0, x1).toArray()) {
            for (int y : rangeClosed(y0, y1).toArray()) {
              slice[y][x] = STONE;
              LAST_ROW = Math.max(LAST_ROW, y);
            }
          }

        }

      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    return slice;
  }

  private static void printSlice(short[][] slice) {
    int still_water = 0;
    int running_water = 0;
//    int count = 0;

    for (short[] y : slice) {
      for (short x : y ) {
        if (x == RUNNING_WATER){
          running_water += 1;
        }
        if (x == STILL_WATER){
          still_water += 1;
        }
//        System.out.print(x);
      }
//      System.out.println();

//      count++;
//      if (count > 900) {
//        return;
//      }

    }
    System.out.println("running_water: " + running_water + ", still_water: " + still_water + ", all water: " + (running_water + still_water));
  }

}
