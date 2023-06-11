package Algorithms;

import MatrixObjects.Tile;

import java.util.Stack;

public class FloodFillAlgorithm {

    public static void floodFill(Tile[][] matrix, int row, int col, int ownerId) {
        // Create a stack to keep track of the cells to visit
        Stack<int[]> stack = new Stack<>();

        // Push the starting cell onto the stack
        stack.push(new int[]{row, col});

        // Loop until the stack is empty
        while (!stack.isEmpty()) {
            // Pop a cell from the stack
            int[] current = stack.pop();
            int r = current[0];
            int c = current[1];

            // If the current cell is already filled, continue
            if (matrix[r][c].ownerId == ownerId) {
                continue;
            }

            // Fill the current cell
            matrix[r][c].ownerId = ownerId;

            // Check if the cell above the current cell is within bounds and is not already filled
            if (r > 0 && matrix[r-1][c].ownerId != ownerId) {
                stack.push(new int[]{r-1, c}); // Add the cell above to the stack
            }

            // Check if the cell below the current cell is within bounds and is not already filled
            if (r < matrix.length-1 && matrix[r+1][c].ownerId != ownerId) {
                stack.push(new int[]{r+1, c}); // Add the cell below to the stack
            }

            // Check if the cell to the left of the current cell is within bounds and is not already filled
            if (c > 0 && matrix[r][c-1].ownerId != ownerId) {
                stack.push(new int[]{r, c-1}); // Add the cell to the left to the stack
            }

            // Check if the cell to the right of the current cell is within bounds and is not already filled
            if (c < matrix[0].length-1 && matrix[r][c+1].ownerId != ownerId) {
                stack.push(new int[]{r, c+1}); // Add the cell to the right to the stack
            }
        }
    }

}
