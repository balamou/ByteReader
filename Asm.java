/**
* This class runs machine code from a file and outputs a step by step execution.
*
* @author michelbalamou@gmail.com
*/
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Asm
{
  //Use the following class variables with the method col(String, int)
  public static final int YELLOW = 93;
  public static final int WHITE = 40;
  public static final int GREEN = 92;
  public static final int BLUE = 36;
  public static final int RED = 31;
  public static final int BRIGHT_RED = 91;
  public static final int BRIGHT_GREEN = 92;

  public static final int RED_BKG = 101;
  public static final int BLUE_BKG = 104;


	public static final boolean COLOR_OUTPUT = true;
	public static final boolean SHOW_STEPS = true;
	public static final boolean WARNINGS = true;

	private static final int DEFAULT_SIZE = 256;

	private int[] memory;
	private int[] before;
	private int size;
	private Queue<String> warningQueue;
  private Queue<String> steps;

	//Class methods *****************************************************
  /**
  * Colors a string based on the color ID number. The color output works best
	* in a xterm-256 color enabled Terminal.
  *
  * @param val the string to be colored
  * @param color the ID number of the color in the terminal
  * @return a formatted string that is colored
  */
  public static String col(String val, int color){
    return COLOR_OUTPUT ? "\u001B[" + color + "m" + val + "\u001B[0m" : val;
  }


	/**
	* Initializes two 2D arrays that represent the memory of the machine before
	* and after the exectution of the machine code.
	*
	* @param size the size of the address space
	*/
	public Asm(int size)
	{
		this.size = size;
		memory = new int[size]; // size x 1 byte
		before = new int[size]; // size x 1 byte

		// Fill the memory with zeros
		for (int i = 0; i<size; i++)
		{
			memory[i] = 0;
			before[i] = 0;
		}

		warningQueue = new LinkedList<String>(); // Initialize warning queue
    steps = new LinkedList<String>();
	}

	/**
	* Initializes a memory with 256 address spaces
	*/
	public Asm()
	{
		this(DEFAULT_SIZE);
	}

	/**
	* Reads line by line the file at 'filename'.
	* It ignores lines starting by %. Every other line is split and the
	* first two elements are used. The first element is the address and the
	* second element reperesents the instruction at that address. The instruction
	* is stored at memory[address][0].
	*
	*  Example (example.asm):
	*  % THIS LINE IS IGNORED%
	*  00 04
	*  01 09
	*  02 10 % THIS IS ALSO IGNORED %
	*  03 0A
	*
	* After running the first line, the instruction 04 will be stored in memory[00][0].
	* The third line instruction (10) is stored in memory[02][0].
	*
	* @param filename the name of the file with the instructions
	*/
	public void loadToMemory(String filename)
	{
		try
		{
			File file = new File(filename);
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;

			while ((line = bufferedReader.readLine()) != null)
			{
				// keep adding strings to the memory
				if (line.charAt(0)!='%') // ignore strings starting with %
				{
					String[] command = line.split(" "); // split the line at space

					int addr = Integer.parseInt(command[0], 16);
					int inst = Integer.parseInt(command[1], 16);
					if (addr<size) // make sure the address doesnt exceed the size of the matrix
					{
						if (inst>255)
						{
							int tmp = inst;
							inst = truncate(inst);
							warning("Attempting to insert instruction larger than 2 bytes 0x" + HEX(tmp) + "\n\t Default behaviour: truncate 0x" + HEX(inst));
						}

						memory[addr] = inst;
					}
					else
						warning("Attempting to insert instruction at address 0x" + HEX(addr) + ". Max address is 0x" + HEX(size-1) + ". Check the instruction code.");
				}
			}
			fileReader.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}


	/**
	* Stores warnings in a Queue
	*
	* @param msg warning message
	*/
	public void warning(String msg){
		warningQueue.add(msg);
	}

	/**
	* Outputs warnings to the screen only if flag WARNINGS is enabled.
	*/
	public void showWarnings(){
		System.out.println(col("Total warnings: " + warningQueue.size(), YELLOW));

		while (!warningQueue.isEmpty() && WARNINGS)
			System.out.println(col("Warning: " + warningQueue.remove(), YELLOW));
	}

  /**
	*/
	public void showSteps(){
		while (!steps.isEmpty())
			System.out.println(col(steps.remove(), YELLOW));
	}

	/**
	* Executes the intructions loaded to the memory from a file.
	*
	* @param filename the name of the file with the instructions
	*/
	public void execute(String filename)
	{
		loadToMemory(filename); // load instructions to the memory matrix
		before = memory.clone(); // save the initial state of the memory

		int AC = 0; // set the Acumulator Register to 0

		for (int i=0; i<size; i++)
		{
			int inst = memory[i]; // fetch instruction
			String hex = intToHex(inst); // hexadecimal representation of the instruction


			int direct = i<size - 1 ? memory[i + 1] : 0;
			int indirect = direct<size-1 ? memory[direct] : 0;

			String[] dir = {"1", "2", "3", "4", "8", "10", "20"};
			String[] line = {"41", "42", "44", "48", "50", "60"};

			boolean isDirect = hex.equals("1") || hex.equals("2") || hex.equals("3") || hex.equals("4") || hex.equals("8") || hex.equals("10") || hex.equals("20");
			int ref = isDirect ? direct : indirect;

			String hex2 = direct<size - 2 ? intToHex(memory[i + 2]) : "0";
			boolean oneLine =  hex2.equals("41") || hex2.equals("42") || hex2.equals("44") || hex2.equals("48") || hex2.equals("50") || hex2.equals("60");

			switch (hex)
			{
				case "1": //AND
				case "81": //AND indirect
					AC = and(AC, memory[ref]);
					i++;
					print("AC AND " + HEX(memory[ref]) + " = " + AC, RED);
				break;

				case "2": //ADD
				case "82": //ADD indirect
					AC += memory[ref];
					AC = truncate(AC);
					i++;
					print("AC+=" + HEX(memory[ref]), RED);
				break;

				case "3": //SUB
				case "83": //SUB indirect
					AC -= memory[ref];
					AC = truncate(AC);
					i++;
					print("AC-=" + HEX(memory[ref]), RED);
				break;

				case "4": //LDA
				case "84": //LDA indirect
					AC = memory[ref];
					i++; // skip address

					print("AC=" + HEX(AC), RED);
				break;

				case "8": //STA
				case "88": //STA indirect
					memory[ref] = AC;
					i++; // skip address

					print("M["+HEX(ref)+"]<-" + HEX(AC), YELLOW);
				break;

				case "10": //BUN
				case "90": //BUN indirect
					i = (isDirect ? direct : indirect) - 1;

					print("  BUN to: " + HEX(i + 1), WHITE);
				break;

				case "20": //ISZ
				case "A0": //ISZ indirect
					memory[ref] = truncate(memory[ref] + 1);
					i++;

					print("  ISZ - " + "M[" + HEX(ref) + "]: " + HEX(memory[ref]), WHITE);

					if (memory[ref]==0)
					{
						print("SKIP", WHITE);
						i += (oneLine ? 1 : 2);
					}
				break;


				case "41": //CLA
					AC = 0;
					print("AC=0", RED);
				break;

				case "42": //CMA
					AC = complement(AC);
					print("(AC)'=" + HEX(AC), YELLOW);
				break;

				case "44": //ASL
					AC = asl(AC);
					print("AC asl -> " + HEX(AC), YELLOW);
				break;

				case "48": //ASR
					AC = asr(AC);
					print("AC asr -> " + HEX(AC), YELLOW);
				break;

				case "50": //INC
					AC++;
					print("AC++", BLUE);
				break;

				case "60": //HLT
					print("HALT", RED);
					return ;

				default:
					warning("Attempting to execute unknown opcode: 0x" + HEX(inst));
					break;
			}

		}

	}

	/**
	* Converts a string that represents a HEX number into a DECIMAL number
	*
	* @param hexString HEX string
	* @return DEC number
	*/
	public int hexToInt(String hexString)
  {
		return Integer.parseInt(hexString, 16);
	}

	/**
	* Converts a DECIMAL number into a string in HEX.
	*
	* @param intHex DEC number
	* @return HEX string
	*/
	public String intToHex(int dec)
	{
		return Integer.toHexString(dec);
	}

	/**
	* Converts a DECIMAL number into a string in HEX. Makes it capital and adds
	* a zero if it is less than 2 characters in size.
	*
	* a -> 0A
	* 1b -> 1B
	*
	* @param intHex DEC number
	* @return HEX string
	*/
	public String HEX(int dec)
	{
		String result = Integer.toHexString(dec).toUpperCase();
		return result.length()<2 ? "0" + result : result;
	}

	/**
	* Truncates a number if it exceedes 255 (or FF), since each byte can only stored
	* up to 255 but Java allows integers to exceed that number. Just returns the Last
	* two digits of a hexadecimal number.
	*
	* 511 -> 1FF -(truncate)-> FF -> returns 255
	* 2748 -> ABC -(truncate)-> BC -> returns 188
	*
	* @param dec decimal number to truncate in HEX
	* @return returns the truncated DEC
	*/
	public int truncate(int dec)
	{
		int num = dec;
		String tmp = intToHex(dec); // convert to hexadecimal string

		if (tmp.length()>2)
		{
			tmp = tmp.substring(tmp.length()-2);
			num = hexToInt(tmp);
		}

		return num;
	}


	/**
	* Prints a string that represents the steps of execution of memory instructions.
	* SHOW_STEPS can be set to false to stop diplaying steps.
	*
	* @param command command currently executed
	* @param color color of the command
	* @return formatted command
	*/
	public void print(String command, int color)
	{
		if (SHOW_STEPS)
			System.out.println(col(command, color));
	}

	/**
	* Prints the address of a memory cell, followed by an instruction from
	* the initial memory, then by an instruction in the new memory (after execution).
	* Doesn't print cells that have instructions equal to 0 before and after execution.
	* Highlits in RED any cells that changed after execution.
	*/
	public void printNonEmptyDiff()
	{
			for (int i = 0; i<size; i++)
			{
				int inst = memory[i];
				int instp = before[i];

				if (!(inst==0 && instp==0))
				{
					int color = (inst == instp ? WHITE : RED);
					String eq = (inst == instp) ? HEX(instp) : HEX(instp) + " -> " + HEX(inst);
					System.out.println(col(HEX(i) + ": " + eq, color));
				}
			}
	}



  /**
  * Prints a two dimentional grid of the memory
  *
  * @param addr
	*/
	public void print2dMemory(int[] mem, int addr, int delta_cell)
	{
      String line = "     ";
      for (int i = 0; i<16; i++)
        line += HEX(i) + "  ";

      line = col(line, 104) + "\n" + col("  ", 104);

			for (int i = 0; i<size; i++)
			{
				int inst = mem[i];

        if (i % 16 == 0)
          line+="\n" + col(HEX(i), 104) + "   ";

        int color = (i == addr ? 5 : 91);
        color = (i==delta_cell) ? BRIGHT_GREEN : color;
        line += col(HEX(mem[i]), mem[i]!=0 ? color : 37) + "  ";
			}

      System.out.println(line);
	}

  /**
  * Executes the intructions loaded to the memory from a file.
  *
  * @param filename the name of the file with the instructions
  */
  public void executeStepwise(String filename)
  {
    loadToMemory(filename); // load instructions to the memory matrix
    before = memory.clone(); // save the initial state of the memory

    int AC = 0; // set the Acumulator Register to 0
    int iteration = 0;

    for (int i=0; i<size; i++)
    {
      int inst = memory[i]; // fetch instruction
      String hex = intToHex(inst); // hexadecimal representation of the instruction


      int direct = i<size - 1 ? memory[i + 1] : 0;
      int indirect = direct<size-1 ? memory[direct] : 0;

      String[] dir = {"1", "2", "3", "4", "8", "10", "20"};
      String[] line = {"41", "42", "44", "48", "50", "60"};

      boolean isDirect = hex.equals("1") || hex.equals("2") || hex.equals("3") || hex.equals("4") || hex.equals("8") || hex.equals("10") || hex.equals("20");
      int ref = isDirect ? direct : indirect;

      String hex2 = direct<size - 2 ? intToHex(memory[i + 2]) : "0";
      boolean oneLine =  hex2.equals("41") || hex2.equals("42") || hex2.equals("44") || hex2.equals("48") || hex2.equals("50") || hex2.equals("60");

      // Step screen data
      int bfr = i; // PC address before changes
      int delta_cell = -1;

      switch (hex)
      {
        case "1": //AND
        case "81": //AND indirect
          AC = and(AC, memory[ref]);
          i++;
          steps.add("AC AND " + HEX(memory[ref]) + " = " + AC);
        break;

        case "2": //ADD
        case "82": //ADD indirect
          AC += memory[ref];
          AC = truncate(AC);
          i++;
          steps.add("AC+=" + HEX(memory[ref]));
        break;

        case "3": //SUB
        case "83": //SUB indirect
          AC -= memory[ref];
          AC = truncate(AC);
          i++;
          steps.add("AC-=" + HEX(memory[ref]));
        break;

        case "4": //LDA
        case "84": //LDA indirect
          AC = memory[ref];
          i++; // skip address

          steps.add("AC=" + HEX(AC));
        break;

        case "8": //STA
        case "88": //STA indirect
          memory[ref] = AC;
          i++; // skip address
          delta_cell = ref;

          steps.add("M["+HEX(ref)+"]<-" + HEX(AC));
        break;

        case "10": //BUN
        case "90": //BUN indirect
          i = (isDirect ? direct : indirect) - 1;

          steps.add("  BUN to: " + HEX(i + 1));
        break;

        case "20": //ISZ
        case "A0": //ISZ indirect
          memory[ref] = truncate(memory[ref] + 1);
          i++;

          steps.add("  ISZ - " + "M[" + HEX(ref) + "]: " + HEX(memory[ref]));

          if (memory[ref]==0)
          {
            steps.add("SKIP");
            i += (oneLine ? 1 : 2);
          }
        break;


        case "41": //CLA
          AC = 0;
          steps.add("AC=0");
        break;

        case "42": //CMA
          AC = complement(AC);
          steps.add("(AC)'=" + HEX(AC));
        break;

        case "44": //ASL
          AC = asl(AC);
          steps.add("AC asl -> " + HEX(AC));
        break;

        case "48": //ASR
          AC = asr(AC);
          steps.add("AC asr -> " + HEX(AC));
        break;

        case "50": //INC
          AC++;
          steps.add("AC++");
        break;

        case "60": //HLT
          steps.add("HALT");
          return ;

        default:
          warning("Attempting to execute unknown opcode: 0x" + HEX(inst));
          break;
      }

      clear();
      System.out.println("Iteration = " + iteration);
      System.out.println("Command = ");
      System.out.println("PC = " + HEX(bfr));
      System.out.println("AC = " + HEX(AC));
      System.out.println();
      print2dMemory(memory, bfr, delta_cell);
      pressAnyKeyToContinue("Press Enter key to continue...");
      iteration++;
    }
  }

  /**
  * Waits for user to press a key before continuing
  *
  * @param msg
  */
  private void pressAnyKeyToContinue(String msg)
  {
      System.out.println(msg);
      try{
         System.in.read();
      }
      catch(Exception e){

      }
  }
	/**
	* Fills any binary string that is less than 8 bits with zeros in the most
	* significant positions.
	*
	* 1010 -> 00001010
	* 110011 -> 00110011
	*
	* @param bin string representing a binary number
	* @return a string of length 8 filled with 0 in missing spots
	*/
	public String fill(String bin)
	{
		String result = "";
		for (int i = 0; i<8-bin.length(); i++)
			result += "0";

		return result + bin;
	}

	/**
	* Converts a DEC into BIN and takes its complement then converts it back into
	* DEC.
	*
	* 10 -> A -> 1010 --(FILL)--> 00001010 --(COMPLEMENT)--> 11110101 -> return 245
	* 69 -> 45 -> 01000101 --(FILL)--> 01000101 --(COMPLEMENT)--> 10111010 -> return 186
	*
	* @param dec decimal number to be complemented
	* @return a complemented binary number in decimal
	*/
	public int complement(int dec)
	{
		String bin = fill(Integer.toBinaryString(dec)); // convert DEC to BIN, and fill missing zeros
		String result = "";

		for(int i=0; i<bin.length(); i++)
			 result += (bin.charAt(i)=='0' ? '1' : '0');

		return Integer.parseInt(result, 2); // convert BIN string into a DEC number
	}


	/**
	*		.d8b.    d8b   db   d8888b.
	*	 d8' `8b   888o  88   88  `8D
	*	 88ooo88   88V8o 88   88   88
	*	 88~~~88   88 V8o88   88   88
	*	 88   88   88  V888   88  .8D
	*	 YP   YP   VP   V8P   Y8888D'
	*
	* Performs a logical bitwise AND operation on two DEC numbers.
	*
	*  10 AND 171 -> 1010 AND 10101011 -> 00001010 AND 10101011 -> 00001010 -> return 10
	*  70 AND 12 -> 01000110 AND 1100 -> 00010000 AND 00001100 -> 00000000 -> return 0
	*
	* @param dec1
	* @param dec2
	* @return
	*/
	public int and(int dec1, int dec2)
	{
		// convert DEC to BIN, and fill missing zeros
		String bin1 = fill(Integer.toBinaryString(dec1));
		String bin2 = fill(Integer.toBinaryString(dec2));

		String result = "";

		for(int i=0; i<bin1.length(); i++)
		{
			boolean t1 = (bin1.charAt(i)=='1');
			boolean t2 = (bin2.charAt(i)=='1');

			result += (t1 && t2) ? '1' : '0';
		}

		return Integer.parseInt(result, 2);
	}

	/**
	* Performs an arithmetic LEFT shift.
	* The LSB becomes 0, every other bit shifts one to the left.
	*
	* 138 -> 10001010 -> 10001010 -(SHIFT)-> 00010100 -> return 20
	*
	* @param dec number to shift
	* @return shifted number
	*/
	public int asl(int dec)
	{
		String bin = fill(Integer.toBinaryString(dec));
		String result = bin.substring(1) + "0";

		return Integer.parseInt(result, 2);
	}

	/**
	* Performs an arithmetic RIGHT shift.
	* The MSB is left unchanged, every other bit shifts one to the right.
	*
	* 138 -> 10001010 -> 10001010 -(SHIFT)-> 11000101 -> return 197
	*
	* @param dec number to shift
	* @return shifted number
	*/
	public int asr(int dec)
	{
		String bin = fill(Integer.toBinaryString(dec));
		String result = bin.charAt(0)+bin.substring(0, bin.length()-1);

		return Integer.parseInt(result, 2);
	}
}
