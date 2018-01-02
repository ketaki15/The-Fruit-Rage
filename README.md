# The Fruit Rage
The Fruit Rage is a game playing agent for a zero-sum game similar to candy crush. This was one of the assignments in the Artificial Intelligence course in which I had to implement the agent using minimax and alpha-beta pruning. The agent returns a valid move regardless of the amount of time remaining in the game.

## Input

First line: integer n, the width and height of the square board
Second line: integer p, the number of fruit types
Third line: strictly positive floating point number, the remaining time in seconds
Next n lines: the n x n board, with one board row per input line, and n characters (plus end- of-line marker) on each line. Each character can be either a digit from 0 to p-1, or a * to denote an empty cell.

10
4
1.276
3102322310
0121232013
3021111113
0221031132
0230011012
0323321010
2003022012
2202200021
0130000020
2200022231

## Output for the above input

First line: my selected move, represented as two characters:
A letter from A to Z representing the column number (where A is the leftmost column, B is the next one to the right, etc), and
A number from 1 to 26 representing the row number (where 1 is the top row, 2 is the row below it, etc).
Next n lines: the n x n board just after my move and after gravity has been applied to make any fruits fall into holes created by the move taking away some fruits.

G8
31******10
010*****13
3022322*13
0221232*32
0221111*12
0331031310
2020011012
2203321121
0103022120
2232222231



