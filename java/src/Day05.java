import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Day05 {

  public static void main(String[] args) {
    String polymer = readInput();
    String reducedPolymer = reducePolymer(polymer);
    System.out.println("Part 1 answer: " + reducedPolymer.length());
    System.out.println("Part 1 answer: " + reducedPolymer);

    // part 2
    System.out.println("Part 2 answer: " + findMinPolymerLength(reducedPolymer));
  }

  private static int findMinPolymerLength (String reducedPolymer) {
    String output = "";
    int minLength = reducedPolymer.length();


    for(char ch = 'a'; ch <='z'; ch++) {
      // remove char from the string:
      StringBuilder polymerWRemovedChar = new StringBuilder();
      for (char c : reducedPolymer.toCharArray()) {
        if (Character.toLowerCase(c) != ch) {
          polymerWRemovedChar.append(c);
        }
      }
      String minReducedPolymer = reducePolymer(polymerWRemovedChar.toString());
      minLength = Math.min(minLength, minReducedPolymer.length());
    }
    return minLength;
  }

  private static String reducePolymer(String polymer) {
    boolean reduced = false;
    int i = 0;
    StringBuilder result = new StringBuilder();

    while (i < polymer.length()) {
      if (i == polymer.length() - 1) {
        result.append(polymer.charAt(i));
        break;
      }
      int xoredChar = polymer.charAt(i) ^ polymer.charAt(i+1);
      if (xoredChar == 32) {
        reduced = true;
        i += 2;
      } else {
        result.append(polymer.charAt(i));
        i++;
      }
    }

    if (reduced) {
      return reducePolymer(result.toString());
    }
    return result.toString();
  }

  private static String readInput() {
    try {
      String path = Day17.class.getResource("05-18-input").getPath();
      List<String> input = Files.lines(Paths.get(path)).collect(Collectors.toList());
      return input.get(0);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

}
