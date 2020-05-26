
package sudoku;

/**
 *
 * @author Ankur Trisal (ankur.trisal@gmail.com)
 * https://www.geeksforgeeks.org/program-sudoku-generator/
 */
public final class Puzzle {
    private final int[][] grid; 
    private final int N; // number of columns/rows. 
    private final int SRN; // square root of N 
    private final int K; // No. Of missing digits 
    
    private Solver solver ;
    private final int[][] solucion ;
    
    private static Puzzle puzzle ;
  
    
    private Puzzle(int N) 
    { 
        this.N = N; 
  
        // Compute square root of N 
        Double SRNd = Math.sqrt(N); 
        SRN = SRNd.intValue(); 
  
        grid = new int[N][N];
        fillValues() ;
        
        solucion = getGrid() ;
        
        K = removeDigits() ;
    }
    
    private Puzzle(int[][] arr) {
        N = arr.length ;
        Double SRNd = Math.sqrt(N); 
        SRN = SRNd.intValue();
        grid = arr ;
        solucion = new Solver(grid).resolver() ;    // Se podría modificar Fichero.guardar()
                                                    // y Fichero.leer() para no tener que
                                                    // resolverlo (ya está resuelto)
        int count = 0 ;
        for ( int[] fila : arr ) {
            for ( int num : fila ) {
                if ( num == 0 ) count++ ;
            }
        }
        K = count ;
    }
    
    public static int[][] buildPuzzle(int[][] arr) {
        puzzle = new Puzzle(arr) ;
        return puzzle.getGrid() ;
    }
  
    public static int[][] buildPuzzle(int N, int zeroCount)
    { 
        do
            puzzle = new Puzzle(N) ;
        while ( puzzle.K < zeroCount ) ;
        
        return puzzle.getGrid() ;
    }
    
    public static Puzzle getInstance(int N) {
        if ( puzzle == null || puzzle.N != N ) puzzle = new Puzzle(N) ;
        return puzzle ;
    }
    
    public int[][] getGrid() {
        int[][] grid = new int[this.grid.length][] ;
        for ( int i = 0 ; i < this.grid.length ; i++ ) {
            grid[i] = new int[this.grid[i].length] ;
            for ( int j = 0 ; j < this.grid[i].length ; j++ ) {
                grid[i][j] = this.grid[i][j] ;
            }
        }
        return grid ;
    }
  
    // Sudoku Generator 
    public void fillValues() 
    { 
        // Fill the diagonal of SRN x SRN matrices 
        fillDiagonal(); 
  
        // Fill remaining blocks 
        fillRemaining(0, SRN);
        
    } 
  
    // Fill the diagonal SRN number of SRN x SRN matrices 
    void fillDiagonal() 
    { 
  
        for (int i = 0; i<N; i=i+SRN) 
  
            // for diagonal box, start coordinates->i==j 
            fillBox(i, i); 
    } 
  
    // Returns false if given 3 x 3 block contains num. 
    boolean unUsedInBox(int rowStart, int colStart, int num) 
    { 
        for (int i = 0; i<SRN; i++) 
            for (int j = 0; j<SRN; j++) 
                if (grid[rowStart+i][colStart+j]==num) 
                    return false; 
  
        return true; 
    } 
  
    // Fill a 3 x 3 matrix. 
    void fillBox(int row,int col) 
    { 
        int num; 
        for (int i=0; i<SRN; i++) 
        { 
            for (int j=0; j<SRN; j++) 
            { 
                do
                { 
                    num = randomGenerator(N); 
                } 
                while (!unUsedInBox(row, col, num)); 
  
                grid[row+i][col+j] = num; 
            } 
        } 
    } 
  
    // Random generator 
    int randomGenerator(int num) 
    { 
        return (int) Math.floor((Math.random()*num+1)); 
    } 
  
    // Check if safe to put in cell 
    boolean CheckIfSafe(int i,int j,int num) 
    { 
        return (unUsedInRow(i, num) && 
                unUsedInCol(j, num) && 
                unUsedInBox(i-i%SRN, j-j%SRN, num)); 
    } 
  
    // check in the row for existence 
    boolean unUsedInRow(int i,int num) 
    { 
        for (int j = 0; j<N; j++) 
           if (grid[i][j] == num) 
                return false; 
        return true; 
    } 
  
    // check in the row for existence 
    boolean unUsedInCol(int j,int num) 
    { 
        for (int i = 0; i<N; i++) 
            if (grid[i][j] == num) 
                return false; 
        return true; 
    } 
  
    // A recursive function to fill remaining  
    // matrix 
    boolean fillRemaining(int i, int j) 
    { 
        //  System.out.println(i+" "+j); 
        if (j>=N && i<N-1) 
        { 
            i = i + 1; 
            j = 0; 
        } 
        if (i>=N && j>=N) 
            return true; 
  
        if (i < SRN) 
        { 
            if (j < SRN) 
                j = SRN; 
        } 
        else if (i < N-SRN) 
        { 
            if (j==(int)(i/SRN)*SRN) 
                j =  j + SRN; 
        } 
        else
        { 
            if (j == N-SRN) 
            { 
                i = i + 1; 
                j = 0; 
                if (i>=N) 
                    return true; 
            } 
        } 
  
        for (int num = 1; num<=N; num++) 
        { 
            if (CheckIfSafe(i, j, num)) 
            { 
                grid[i][j] = num; 
                if (fillRemaining(i, j+1)) 
                    return true; 
  
                grid[i][j] = 0; 
            } 
        } 
        return false; 
    } 
  
    // Remove the K no. of digits to 
    // complete game 
    /*public void removeKDigits() 
    { 
        int count = K; 
        while (count != 0) 
        { 
            int cellId = randomGenerator(N*N); 
  
            // System.out.println(cellId); 
            // extract coordinates i  and j 
            int i = (cellId/N); 
            int j = cellId%9; 
            if (j != 0) 
                j = j - 1; 
  
            // System.out.println(i+" "+j); 
            if (grid[i][j] != 0) 
            { 
                count--; 
                grid[i][j] = 0; 
            } 
        } 
    }*/
    
    private int removeDigits() {
        int count = 0 ;
        while (true)
        { 
            int num ;
            int cellId = randomGenerator(N*N); 
  
            // System.out.println(cellId); 
            // extract coordinates i  and j 
            int i = (cellId/N); 
            int j = cellId%9; 
            
            if ( i >= 9 || j >= 9 ) continue ;
            
            // System.out.println(i+" "+j); 
            if (grid[i][j] != 0) 
            { 
                num = grid[i][j] ;
                grid[i][j] = 0;

                solver = new Solver(grid) ;
                if ( !solver.resoluble() ) {
                    grid[i][j] = num ;
                    solver = new Solver(grid) ;
                    return count ;
                }
                
                count++ ;
            }
            
        }
    }
    
  
    // Print sudoku 
    /*private void printSudoku() 
    { 
        for (int i = 0; i<N; i++) 
        { 
            for (int j = 0; j<N; j++) 
                System.out.print(grid[i][j] + " "); 
            System.out.println(); 
        } 
        System.out.println(); 
    } 
    
    private void printSolution() {
        for (int i = 0; i<N; i++) 
        { 
            for (int j = 0; j<N; j++) 
                System.out.print(solucion[i][j] + " "); 
            System.out.println(); 
        } 
        System.out.println(); 
    }*/
    
    public static int[][] getSolucion() {
        return puzzle.solucion ;
    }
    
}
