# ByteReader
  Reads binary commands from a .asm file and executes them step by step.

## Instructions:

  The memory cells after running a machine file:

      java Run data/fibonacci

  <img src="https://i.imgur.com/XnOPQIs.png"/>

  The green cells represent the cells that have been changed. The cell with a RED
  background is the current operation (PC register). The following program generates
  a fibonacci sequence in addresses starting from 80h to 8Bh.

| A | B | C | D |
|---|---|---|---|
| <img src="https://i.imgur.com/XnOPQIs.png"/> | <img src="https://i.imgur.com/bRsLszW.png"/> | <img src="https://i.imgur.com/MtkBLOv.png"/> | <img src="https://i.imgur.com/fYdLGMA.png"/> |

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
| -s | Shows all steps at once |

## Contributors:
  :bowtie: Michel Balamou
