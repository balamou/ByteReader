# ByteReader
  Reads binary commands from a .asm file and executes them step by step.

## Instructions:

  Open the terminal, go to the bin/ folder and type (without the dollar sign):

      $ . setup

  This allows bash to run temporarily the program from anywhere in your filesystem.
  If you want it to persist through logins, add the absolute path to bin in your ~/.bashrc file
  in the $PATH variable.  

  You can test the program by going to the src/folder and typing:

      $ run data/fibonacci

  The following command will create a 2D view of the memory and run a step by step
  execution of the program:

  <img src="https://i.imgur.com/XnOPQIs.png" width="40%" />

  The GREEN cells represent the cells that have been changed.

  The cell with a RED background is the current operation (PC register).

  The following program generates a fibonacci sequence in addresses starting from 80h to 8Bh.

  The format of the .asm files looks like this:

      % Comments starts with a percent %
      <address of the command> <command>
      <address of the command> <command>
      <address of the command> <command>

  Example:

  <img src="https://i.imgur.com/bRsLszW.png" width="30%" />

  The final result can be shown by running:

      $ run data/fibonacci.asm -s

  Result:

| Instructions | Final result |
|---|---|
| <img src="https://i.imgur.com/MtkBLOv.png"  width="20%" /> | <img src="https://i.imgur.com/fYdLGMA.png"  width="20%"/> |

## TODO

  - [X] Step by step execution with memory cell display
  - [ ] Convert formats from binary to pseudo code
  - [X] Add a test class that checks the integrity of the program
  - [ ] Make a class for for storing every step
  - [ ] Add a gif with the memory execution
  - [ ] Add user friendly options
  - [ ] Add a help command

## Commands:

| Command | Function |
|---------|----------|
| -t | Tests the code for accuracy |
| -f | Shows the final memory state |
| -s | Shows all steps at once |

## Contributors:
  :bowtie: Michel Balamou
