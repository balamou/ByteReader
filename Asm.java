/**
* This class runs machine code from a file and outputs a step by step execution.
*
* @author Michel Balamou (michelbalamou@gmail.com)
* @version Nov 2017
*/
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Asm
{
	public static final boolean WARNINGS = true;
	private static final int DEFAULT_SIZE = 256;

	private int[] memory;
	private int[] before;
	private int size;
	private Queue<String> warningQueue;
  private Queue<String> steps;

	//Class methods *****************************************************


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
							inst = Logic.truncate(inst);
								warningQueue.add("Attempting to insert instruction larger than 2 bytes 0x" + HEX(tmp) + "\n\t Default behaviour: truncate 0x" + HEX(inst));
						}

						memory[addr] = inst;
					}
					else
							warningQueue.add("Attempting to insert instruction at address 0x" + HEX(addr) + ". Max address is 0x" + HEX(size-1) + ". Check the instruction code.");
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
	* Outputs warnings to the screen only if flag WARNINGS is enabled.
	*/
	public void showWarnings(){
		System.out.println(Formatting.col("Total warnings: " + warningQueue.size(), Formatting.YELLOW));

		while (!warningQueue.isEmpty() && WARNINGS)
			System.out.println(Formatting.col("Warning: " + warningQueue.remove(), Formatting.YELLOW));
	}

  /**
  * Displays the steps taken during command execution from the Queue
	*/
	public void showSteps(){
		while (!steps.isEmpty())
			System.out.println(Formatting.col(steps.remove(), Formatting.YELLOW));
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
	* Prints the address of a memory cell, followed by an instruction from
	* the initial memory, then by an instruction in the new memory (after execution).
	* Doesn't print cells that have instructions equal to 0 before and after execution.
	* Highlights in RED any cells that changed after execution.
	*/
	public void printNonEmptyDiff()
	{
			for (int i = 0; i<size; i++)
			{
				int inst = memory[i];
				int instp = before[i];

				if (!(inst==0 && instp==0))
				{
					int color = (inst == instp ? Formatting.WHITE : Formatting.RED);
					String eq = (inst == instp) ? HEX(instp) : HEX(instp) + " -> " + HEX(inst);
					System.out.println(Formatting.col(HEX(i) + ": " + eq, color));
				}
			}
	}



  /**
  * Prints a two dimentional grid of the memory
  *
  * @param mem
  * @param addr
  * @param changed a list of all changed memory cells
	*/
	public void print2dMemory(int[] mem, int addr, LinkedList<Integer> changed)
	{
      int PC = Formatting.BRIGHT_RED_BKG; // color of the current command
      int ADDR_COL = Formatting.YELLOW; // color of the address bar
      int NON_ZERO = Formatting.RED; // color of the non-zero cells
      int LAST_CHANGED = Formatting.GREEN; // color of the last changed memory cell
      int CHANGED = Formatting.GREEN; // color of all modified cells
      int ZERO = Formatting.WHITE; // color of the zero cells


      String line = "     ";
      int color;

      // Create a top row of addresses
      for (int i = 0; i<16; i++)
        line += HEX(i) + "  ";

      // color the top row of addresses
      line = Formatting.col(line + "\n  ", ADDR_COL);

			for (int i = 0; i<size; i++)
			{
        color = NON_ZERO;

        // Add newline and address
        if (i % 16 == 0)
          line+="\n" + Formatting.col(HEX(i), ADDR_COL) + "   ";

        color = (i == addr ? PC : color); // check if
        color = (mem[i]==0) ? ZERO : color; // check if cell is zero
        color = (changed.contains(i)) ? CHANGED : color;

        line += Formatting.col(HEX(mem[i]), color) + "  ";
			}

      System.out.println(line);
	}

  /**
  * Executes the intructions loaded to the memory from a file.
  *
  * @param filename the name of the file with the instructions
  */
  public void executeStepwise(String filename, boolean stepwise)
  {
    loadToMemory(filename); // load instructions to the memory matrix
    before = memory.clone(); // save the initial state of the memory

    int AC = 0; // set the Acumulator Register to 0
    int iteration = 0;
    LinkedList<Integer> changed = new LinkedList<Integer>();

    for (int i=0; i<size; i++)
    {
      int inst = memory[i]; // fetch instruction
      String hex = Logic.intToHex(inst); // hexadecimal representation of the instruction


      int direct = i<size - 1 ? memory[i + 1] : 0;
      int indirect = direct<size-1 ? memory[direct] : 0;

      String[] dir = {"1", "2", "3", "4", "8", "10", "20"};
      String[] line = {"41", "42", "44", "48", "50", "60"};

      boolean isDirect = hex.equals("1") || hex.equals("2") || hex.equals("3") || hex.equals("4") || hex.equals("8") || hex.equals("10") || hex.equals("20");
      int ref = isDirect ? direct : indirect;

      String hex2 = direct<size - 2 ? Logic.intToHex(memory[i + 2]) : "0";
      boolean oneLine =  hex2.equals("41") || hex2.equals("42") || hex2.equals("44") || hex2.equals("48") || hex2.equals("50") || hex2.equals("60");

      // Step screen data
      int bfr = i; // PC address before changes

      switch (hex)
      {
        case "1": //AND
        case "81": //AND indirect
          AC = Logic.and(AC, memory[ref]);
          i++;
          steps.add("AC AND " + HEX(memory[ref]) + " = " + AC);
        break;

        case "2": //ADD
        case "82": //ADD indirect
          AC += memory[ref];
          AC = Logic.truncate(AC);
          i++;
          steps.add("AC+=" + HEX(memory[ref]));
        break;

        case "3": //SUB
        case "83": //SUB indirect
          AC -= memory[ref];
          AC = Logic.truncate(AC);
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
          changed.add(ref);

          steps.add("M["+HEX(ref)+"]<-" + HEX(AC));
        break;

        case "10": //BUN
        case "90": //BUN indirect
          i = (isDirect ? direct : indirect) - 1;

          steps.add("  BUN to: " + HEX(i + 1));
        break;

        case "20": //ISZ
        case "A0": //ISZ indirect
          memory[ref] = Logic.truncate(memory[ref] + 1);
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
          AC = Logic.complement(AC);
          steps.add("(AC)'=" + HEX(AC));
        break;

        case "44": //ASL
          AC = Logic.asl(AC);
          steps.add("AC asl -> " + HEX(AC));
        break;

        case "48": //ASR
          AC = Logic.asr(AC);
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
          warningQueue.add("Attempting to execute unknown opcode: 0x" + HEX(inst));
          break;
      }

      if (stepwise)
      {
        Formatting.clear();
        System.out.println("Iteration = " + iteration);
        System.out.println("Command = ");
        System.out.println("PC = " + HEX(bfr));
        System.out.println("AC = " + HEX(AC));
        System.out.println();
        print2dMemory(memory, bfr, changed);
        iteration++;

        sleep(500);
        //Formatting.pressAnyKeyToContinue("Press Enter key to continue...");
      }
    }
  }

  public void sleep(int time)
  {
    try
    {
    Thread.sleep(time);
    }
    catch(InterruptedException ex)
    {
    Thread.currentThread().interrupt();
    }
  }
}
