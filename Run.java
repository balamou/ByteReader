/**
* This class runs and tests the integrity of the machine executing code.
*
* @author michelbalamou@gmail.com
*/
import java.util.Arrays;

public class Run
{
    /**
    * Instantiates a class to load and execute machine code.
    *
    * @param args [0] is the name of the file with the machine instructions.
    */
    public static void main(String[] args)
    {
      if (args.length<1)
      {
        System.out.println("Please specify a file to load the machine code from.");
        return ;
      }

      Asm machine = new Asm();
      String filename = args[0];

      // Display warnings
      machine.showWarnings();

      if (args[0].equals("-t"))
      {
        test();
      }
      else
      // Convert step exeuction into pseudo code
      if (args.length>1 && args[1].equals("-s"))
      {
        System.out.println(Formatting.col("START", Formatting.GREEN));
        machine.execute(filename, false);
        System.out.println();

        machine.showSteps();
        machine.printNonEmptyDiff();
      }
      // Run stepwise execution
      else
      {
        System.out.println();

        machine.execute(filename, true);
      }
    }


    /**
    * Method that performs tests on the machine.
    *
    * @param machine
    */
    public static void former_test(Asm machine)
    {
      System.out.println();
      System.out.println();

      System.out.println(Integer.toBinaryString(30));
      System.out.println(Integer.toBinaryString(23));

      System.out.println();

      System.out.println(Logic.complement(30));
      System.out.println(Logic.complement(23));

      System.out.println();

      System.out.println(Logic.and(30, 23));

      int[] num = {0x21, 0xB5, 0x37, 0x08, 0x5C, 0x84, 0xA1, 0x1D, 0x72, 0xFF, 0xF6, 0x43, 0x03, 0xA9, 0xD4, 0x19, 0x31, 0xD9, 0x47, 0x82, 0x14, 0x52, 0x07, 0xCA, 0x04};
      int result = 0;

      for (int i=0; i<num.length; i++)
      {
        result += num[i];
        System.out.println(Logic.intToHex(result));
      }

      System.out.println(Logic.asl(170));
      System.out.println(Logic.asr(170));

      //machine.print2dMemory(5);
    }

    /**
    * Tests the integrity of the code.
    *
    */
    public static void test()
    {
      Asm machine = new Asm();

      boolean test1 = check(machine, "fibonacci.asm");
      boolean test2 = check(machine, "test1.asm");
      boolean test3 = check(machine, "test2.asm");

      if (test1)
        System.out.println("Test 1: Fibonacci sequence passed successfully");
      else
        System.out.println("Test 1 failed");

      if (test2)
        System.out.println("Test 2: passed successfully");
      else
        System.out.println("Test 2 failed");

      if (test3)
        System.out.println("Test 3: passed successfully");
      else
        System.out.println("Test 3 failed");
    }

    public static boolean check(Asm machine, String file)
    {
      machine.execute("data/" + file, false);
      int[] exp = machine.loadToMemory("expected/" + file);
      int[] result = machine.getMem();

      return Arrays.equals(result, exp);
    }
}
