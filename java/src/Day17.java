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
    boolean wentDOwn;

    public Result(int count) {

      this.count = count;
      this.hitAngle = false;
    }

    @Override
    public String toString() {
      return "Result{" +
          ", hitAngle=" + hitAngle +
          ", count=" + count +
          '}';
    }
  }

  private static final short SPRING = 4;
  private static final short SAND = 0;
  private static final short STONE = 1;
  private static final short STILL_WATER = 3;
  private static final short RUNNING_WATER = 2;
  private static short[][] slice;
  private static int LAST_ROW = 0;

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

  public static void main(String[] args) {
    slice = readSlice();
    slice[0][500] = SPRING;
    Result result = fillDown(new Result(0), 500, 1);
    System.out.println(result.count);
    printSlice(slice);
  }


  private static Result fillDown(Result result, int x, int y) {
//    System.out.println(y);
    final short currentSquare = slice[y][x];
//    result.hitAngle = false;
//    System.out.println(result.hitAngle);
    result.wentDOwn = true;
    if(y > LAST_ROW)
      return result;

    if(currentSquare == STONE || currentSquare == STILL_WATER) {
      Result leftResult = fillLeftOrRight(new Result(0), x-1, y-1, false, false);
      Result rightResult = fillLeftOrRight(new Result(0), x+1, y-1, true, false);

      result.count = result.count + leftResult.count + rightResult.count;

      if (leftResult.hitAngle && rightResult.hitAngle) {
//        result.hitAngle = false;
        boolean isFilledWithStillWater = makeStaleWater(x, y - 1);
        if (isFilledWithStillWater) {
          return overFlow(result, x, y - 2);
        } else {
          makeRunningWater(x,y-1);
        }
      }
      return result;
    }

    if(currentSquare == SAND) {
      slice[y][x] = RUNNING_WATER;
      result.count += 1;
      return fillDown(result, x, y+1);
    }

    if(currentSquare == RUNNING_WATER) {
//       situation where the water falls from 2 different sides in one container
      if (slice[y - 1][x - 1] == STONE || slice[y - 1][x + 1] == STONE) {
        // falling from right
        // fill right and check whether should overflow, otherwise do not count that
//        Result fillRight = fillLeftOrRight(new Result(0), )
//      }
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

  private static boolean makeStaleWater(int x, int y) {
    boolean didNotRetrace = true;
    slice[y][x] = STILL_WATER;

    int xLeft = x -1;
    while(slice[y][xLeft] == RUNNING_WATER) {
      slice[y][xLeft] = STILL_WATER;

      if (slice[y][xLeft-1] == SAND) {  //!= RUNNING_WATER && slice[y][xLeft-1] != STONE) {
        didNotRetrace = false;
      }
      xLeft -= 1;
    }

    int xRight = x + 1;
    while(slice[y][xRight] == RUNNING_WATER) {
      slice[y][xRight] = STILL_WATER;

      if ( slice[y][xRight+1] == SAND) { //slice[y][xRight+1] != RUNNING_WATER && slice[y][xRight+1] != STONE) {
        didNotRetrace = false;
      }
      xRight += 1;
    }
    return didNotRetrace;
  }

  public static Result fillRightTentatively(Result result, int x, int y) {
    while (slice[y][x+1] == SAND && slice[y-1][x] == RUNNING_WATER) {
      result.count += 1;
    }
    return result;
  }

  private static Result fillLeftOrRight(Result result, int x, int y, boolean right, boolean overflowing) {
    final short currentSquare = slice[y][x];
    if (currentSquare == STONE || currentSquare == STILL_WATER) {
      result.hitAngle = true;
      return result;
    }
//    if (!overflowing && currentSquare == RUNNING_WATER && ((right && slice[y+1][x-1] == RUNNING_WATER) || !right && slice[y+1][x+1] == RUNNING_WATER)) {
//        // met another waterfall, should retrace
////      if (slice[y][right ? x+1 : x-1 ] == STONE) {
////        return result;
////      }
//      int count = retrace(right ? x-1 : x+1, y, right);
//      result.count -= count;
//      return result;
//    }
    if (currentSquare == SAND || currentSquare == RUNNING_WATER) {
      if (currentSquare == SAND) {
        result.count += 1;
        slice[y][x] = RUNNING_WATER;
      }
      if (slice[y+1][x] == SAND || slice[y+1][x] == RUNNING_WATER) {
        if (slice[y+1][x] == SAND) {
          // manage the case when flowing on top of uncontained water
          // for flowing left
//        if ((right && slice[y+1][x-1] == RUNNING_WATER) || !right && slice[y+1][x+1] == RUNNING_WATER) {
//          int count = retrace(x, y, right);
//          result.count -= count;
//          return result;
//        }
          return fillDown(result, x, y + 1);
        }
      } else {
        if (right) {
          return fillLeftOrRight(result, x+1, y, true, false);
        } else {
          return fillLeftOrRight(result, x-1, y, false, false);
        }
      }
    }
    return result;
  }

  private static int retrace(int x, int y, boolean right) {
    int count = 0;
    while (slice[y-1][x] != RUNNING_WATER) {
      slice[y][x] = SAND;
      count += 1;
      if (right) {
        x -= 1;
      } else {
        x += 1;
      }; // retracing in opposite directions
    }
    return count;
  }

//  private static Result fillLeft(Result result, int x, int y) {
//    final short currentSquare = slice[y][x];
//    if (currentSquare == STONE) {
//      result.hitAngle = true;
//      return result;
//    }
//
//    if (currentSquare == SAND) {
//      slice[y][x] = RUNNING_WATER;
//      result.count += 1;
//      if (slice[y-1][x] == SAND) {
//        return fillDown(result, x, y-1);
//      } else {
//        return fillLeft(result, x-1, y);
//      }
//    }
//    return result;
//  }

//  private static Result fillRight(Result result, int x, int y, boolean right) {
//    final short currentSquare = slice[y][x];
//    if (currentSquare == STONE) {
//      result.hitAngle = true;
//      return result;
//    }
//
//    if (currentSquare == SAND) {
//      slice[y][x] = RUNNING_WATER;
//      result.count += 1;
//      if (slice[y-1][x] == SAND) {
//        return fillDown(result, x, y-1);
//      } else {
//        // only this line differs
//        return fillRight(result, x+1, y);
//      }
//    }
//    return result;
//  }

  private static Result overFlow(Result result, int x, int y) {
//    final short currentSquare = slice[y][x];
//    if(currentSquare == STONE || currentSquare == RUNNING_WATER) {
//    result.hitAngle = false;

    Result leftResult = fillLeftOrRight(new Result(0), x-1, y, false, true);
    Result rightResult = fillLeftOrRight(new Result(0), x+1, y, true, true);

    result.count = result.count + leftResult.count + rightResult.count;

    //if (leftResult.hitAngle && rightResult.hitAngle ){ //&& !(leftResult.wentDOwn || rightResult.wentDOwn)) {
//      result.hitAngle = true; /////// important string
//      result.wentDOwn = false;
      boolean isFilledWithStillWater = makeStaleWater(x, y);
      if (isFilledWithStillWater) {
        return overFlow(result, x, y-1);
      } else {
        makeRunningWater(x,y);
      }
    //}
    return result;
  }


  private static void printSlice(short[][] slice) {
    int count = 0;
    int three = 0;
    int two = 0;
    for (short[] y : slice) {
      count += 1;
//      if (count > 900) {
//        continue;
//      }
//      if (count > LAST_ROW) {
//        break;
//      }

      for (short x : y ) {
        if (x == RUNNING_WATER){
          two += 1;
        }
        if (x == STILL_WATER){
          three += 1;
        }
//        System.out.print(x);
      }

      System.out.println();
    }
    System.out.println("two " + two + " three " + three + " b oth " + (two + three));
  }

}
