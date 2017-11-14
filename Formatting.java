/**
*
*
* @author Michel Balamou   8124843
* @version Nov 2017
*/

public class Formatting{
  //Class variables ***************************************************

  /**
  * True - displays table with colored format
  * False - disables color output
  * The color output works best in a xterm-256 color enabled Terminal
  */
  public static final boolean COLOR_OUTPUT = true;

  //Use the following class variables with the method col(String, int)
  public static final int BOLD_WHITE = 1;
  public static final int DARK_GREY = 2;
  public static final int UNDERLINE = 4;
  public static final int BLINK = 5;

  public static final int WHITE_BKG = 7;

  public static final int BLACK = 8;
  public static final int DARK_RED = 31;
  public static final int DARK_GREEN = 32;
  public static final int DARK_YELLOW = 33;
  public static final int DARK_BLUE = 34;
  public static final int DARK_MAGENTA = 35;
  public static final int DARK_SKY_BLUE = 36;
  public static final int LIGHT_GREY = 37;

  public static final int DARK_RED_BKG = 41;
  public static final int DARK_GREEN_BKG = 42;
  public static final int DARK_YELLOW_BKG = 43;
  public static final int DARK_BLUE_BKG = 44;
  public static final int DARK_MAGENTA_BKG = 45;
  public static final int DARK_SKY_BLUE_BKG = 46;
  public static final int DARK_GREY_BKG = 47;

  public static final int BRIGHT_RED_BKG = 101;
  public static final int BRIGHT_GREEN_BKG = 102;
  public static final int BRIGHT_YELLOW_BKG = 103;
  public static final int BRIGHT_BLUE_BKG = 104;
  public static final int BRIGHT_MAGENTA_BKG = 105;
  public static final int BRIGHT_SKY_BLUE_BKG = 106;

  public static final int RED = 91;
  public static final int GREEN = 92;
  public static final int YELLOW = 93;
  public static final int BLUE = 94;
  public static final int MAGENTA = 95;
  public static final int SKY_BLUE = 96;
  public static final int GREY = 97;

  //Class methods *****************************************************

  /**
  * Rounds a gived number to 2 decimal points and colors it
  * given the ID number in the terminal.
  * The color output works best in a xterm-256 color enabled Terminal.
  *
  * @param val the string to be colored
  * @param color the ID number of the color in the terminal
  * @return a formatted string that is colored
  */
  public static String col(String val, int color){
    return COLOR_OUTPUT ? "\u001B["+color+"m"+val+"\u001B[0m" : val;
  }

  /**
  * Clears the Terminal content
  */
  private void clear(){
    System.out.print("\033[H\033[2J");
  }

  /**
  *
  *
  */
  public static void printColorTable()
  {
    String line = "";

    for (int i = 0; i<400; i++)
    {
      if (i%20 == 0)
        line += "\n";

      line += col(format(i), i) + "  ";
    }

    System.out.println(line);
  }

  /**
  *
  * @param i
  */
  public static String format(int i)
  {
    return i<10 ? "00" + i : ((i<100) ? "0" + i : ""+ i);
  }


  /**
  *
  *
  */
  public static void main(String[] args)
  {
    printColorTable();
  }

}
