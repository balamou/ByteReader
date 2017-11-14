/**
*
*
* @author michelbalamou@gmail.com
*/
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

      /*
      System.out.println(col("START", GREEN));
      machine.execute(args[0]);
      System.out.println();

      machine.printNonEmptyDiff();
      */

      System.out.println();

      machine.showWarnings();
      machine.executeStepwise(args[0], true);

      //machine.printColorTable();

      //machine.showSteps();

      //test(machine);
    }


    /**
    * Method that performs tests on the machine.
    *
    * @param machine
    */
    public static void test(Asm machine)
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
}
