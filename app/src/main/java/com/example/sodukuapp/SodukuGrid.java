package com.example.sodukuapp;


public class SodukuGrid {
    private static final int EMPTY = 0; //Represents an empty value.
    int[][] grid;

    /**
     * Creates a SodokuGrid based on a matrix.
     * @param matrix the grid.
     */
    public SodukuGrid(int[][] matrix) {
        grid = matrix;
    }

    /**
     * Returns a value on the board.
     * @param r the row
     * @param c the coloumn
     * @return  the value
     */
    public int getValue(int r, int c) {
        return grid[r][c];
    }
    /** Changes the value of element on row row column col to nbr.
     *
     * @param row number of rows
     * @param col number of cols
     * @param nbr value to change to
     */
    public void setValue(int row, int col, int nbr) {
        grid[row][col] = nbr;
    }
    /**
     * Checks if number n is placeable at row n column c in SodukuGrid sg.
     * @param n number
     * @param row the row
     * @param col the ccloumns
     * @return Changeability
     */
    protected boolean placeable (int n, int row, int col){
        if(n == EMPTY)
            return true;
        for (int i = 0; i<9; i++){

            if(grid[row][i] == n) //check same row for same value
                return false;
            if(grid[i][col] == n) //Check same col for same value.
                return false;
        }
        //Check square for same value.
        int r = row-row%3;
        int c = col-col%3;
        for (int x = r; x<r+3; x++){
            for (int y = c; y<c+3; y++){
                if(n == grid[x][y]){
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * An entered soduku may already not follow the restrictions. If the sodoku is valid it does.
     * @return Whether the sodoku is valid.
     */
    public boolean isValid() {
       for(int r = 0; r<9; r++) {
           for( int c = 0; c<9; c++) {
               int val = grid[r][c];
               grid[r][c] = EMPTY;
               if(!placeable(val,r,c))
                   return false;
               else
                   grid[r][c] = val;
           }
       }
       return true;
    }
    /**
     * Attempts to solve the soduku via backtracking. If the soduku entered is not valid return false.
     * @return Whether the soduku is solvable.
     */
    public boolean sodokuSolver() {
        if(!isValid())
            return false;
        else
            return sodokuSolver(0,0);
    }

    /**
     * Solves the soduku recursively.
     * @param r the row
     * @param c the column
     * @return whether the soduku is solvable.
     */
    private boolean sodokuSolver(int r, int c) {
        if(c == 9) {
            r++;
            c = 0;
        }
        if (r == 9) {
            System.out.println("Found solution.");
            return true;
        }
        if(grid[r][c] != EMPTY) {
            System.out.println("Not empty " + grid[r][c] );
            return sodokuSolver(r, c+1);
        }
        for(int i=1; i < 10 ; i++){
            if(placeable(i, r,c)) {
                setValue(r,c,i);
                if(sodokuSolver(r, c+1)) {
                    return true;
                }
                else {
                    setValue(r,c,EMPTY);
                }
            }
        }
        return false;

    }
}
