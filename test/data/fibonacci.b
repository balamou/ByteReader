% Generates a Fibonacci sequence in memory cells 80 to 8B %
% The result is a sequence: 01, 01, 02, 03, 05, 08, 0D, 15, 22, 37, 59, 90 %
% ========== MAIN ========== %
ORG 00
LDA a0
CMA
STA a0
ISZ a0
BUN 20
HLT
% ========== SUBROUTINE ========== %
ORG 20
LDA @a1
ADD @a2
STA @a3
LDA a1
INC
STA a1
INC
STA a2
INC
STA a3
BUN 05
% ========== DATA ========== %
80 01
81 01
a0 0a  % loop counter, which will be done 10 times %
a1 80  % pointer to the first number to be added %
a2 81  % pointer to the second number to be added %
a3 82  % pointer to memory location where result will be stored %
