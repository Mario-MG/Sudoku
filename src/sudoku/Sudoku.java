/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

import java.util.ArrayList ;

/**
 *
 * @author Mario
 */
public class Sudoku {
    
    // DECLARACIÓN DE ATRIBUTOS
    
    // Array que guardará los valores del sudoku
    private int[][] arr ;
    //private int[][] arr0 ;
    // ArrayList que guardará las posiciones de los números repetidos
    private ArrayList<Position> listaRep = new ArrayList<>() ;
    // ArrayList que guardará las posiciones que se eliminen del ArrayList 
    // anterior; es decir, posiciones que estaban repetidas y ya no
    private ArrayList<Position> listaNoRep = new ArrayList<>() ;
    // Array para determinar qué posiciones no se pueden modificar
    private boolean[][] lock ;
    
    private static Sudoku sudoku ;

    
    private Sudoku(int N, int zeroCount) {
        arr = Puzzle.buildPuzzle(N, zeroCount) ;
        
        // Generar array para determinar qué posiciones no se pueden modificar
        buildLockMatrix() ;
    }
    
    private Sudoku(int[][] arr) {
        this.arr = arr ;
    }
    
    private boolean[][] buildLockMatrix() {
        boolean[][] lock = new boolean[9][9] ;
        for ( int i = 0 ; i < 9 ; i++ ) {
            for ( int j = 0 ; j < 9 ; j++ ) {
                // Si el valor en la posición es distinto de cero, no se
                // puede modificar
                lock[i][j] = ( arr[i][j] != 0 ) ;
            }
        }
        return this.lock = lock ;
    }
    
    private boolean[][] buildLockMatrix(int[][] arr) {
        boolean[][] lock = new boolean[9][9] ;
        for ( int i = 0 ; i < 9 ; i++ ) {
            for ( int j = 0 ; j < 9 ; j++ ) {
                // Si el valor en la posición es distinto de cero, no se
                // puede modificar
                lock[i][j] = ( arr[i][j] != 0 ) ;
            }
        }
        return this.lock = lock ;
    }
    
    public static Sudoku getInstance() {
        return sudoku ;
    }
    
    public static Sudoku nuevoPuzzle(int N, int zeroCount) {
        sudoku = new Sudoku(N, zeroCount) ;
        Cell.reiniciar() ;
        return getInstance() ;
    }
    
    public static Sudoku nuevoPuzzle(int[][][] arr) {
        sudoku = new Sudoku(arr[1]) ;
        Puzzle.buildPuzzle(arr[0]) ;
        sudoku.buildLockMatrix(Puzzle.getInstance(9).getGrid()) ;
        //sudoku.buildListaRep() ; // TODO
        Cell.reiniciar() ;
        return getInstance() ;
    }
    
    public static Sudoku reiniciarPuzzle() {
        getInstance().arr = Puzzle.getInstance(9).getGrid() ;
        getInstance().buildLockMatrix() ;
        Cell.reiniciar() ;
        return getInstance() ;
    }
    
    public static int[][] resolverPuzzle() {
        return new Solver(getInstance().arr).resolver() ;
    }
    
    
    // MÉTODOS
    
    // Getter para valores en una posición concreta del array
    public String getNum(int i, int j) {
        
        int num ;
        if ( i >= 0 && i < 9 && j >= 0 && j < 9 ) {
            num = arr[i][j] ;
            if ( num > 0 ) return String.valueOf(num) ;
            else return "" ;
        } else return "" ;
        
    }
    
    // Setter para una valor en una posición concreta del array
    public void setNum(int i, int j, int num) {
        arr[i][j] = num ;
    }
    
    // Getter para la ArrayList de posiciones cuyos valores están repetidos
    public ArrayList<Position> getListaRep() {
        return listaRep ;
    }
    
    // Getter para la ArrayList de posiciones cuyos valores ya no se repiten
    public ArrayList<Position> getListaNoRep() {
        return listaNoRep ;
    }
    
    // Comprobar si una posición no se puede modificar
    public boolean isLocked(int i, int j) {
        
        if ( i >= 0 && i < 9 && j >= 0 && j < 9 ) return lock[i][j] ;
        else return true ;
        
    }
    
    // Vaciar el ArrayList de posiciones que ya no están repetidas
    public void limpiarListaNoRep() {
        listaNoRep.clear() ;
    }
    
    // Vaciar el ArrayList de posiciones repetidas
    public void limpiarListaRep() {
        listaRep.clear() ;
    }
    
    // Bloquear todas las posiciones para que no se puedan modificar
    // Se usa una vez que el sudoku se ha resuelto
    public void allLocked() {
        
        for ( int i = 0 ; i < 9 ; i++ ) {
            for ( int j = 0 ; j < 9 ; j ++ ) {
                lock[i][j] = true ;
            }
        }
        
    }
    
    // Devolver la primera posición que se puede modificar
    // Se usa en los controles por teclado
    public Position getFirstUnlocked() {
        
        for ( int i = 0 ; i < 9 ; i++ ) {
            for ( int j = 0 ; j < 9 ; j ++ ) {
                if ( !lock[i][j] ) {
                    return new Position(i,j) ;
                }
            }
        }
        return null ;
        
    }

    // Poner los valores a cero de todas las posiciones que se pueden modificar
    // Se usa para resetear el sudoku
    /*public void setToZero() {
        
        for ( int a = 0 ; a < 9 ; a++ ) {
            for ( int b = 0 ; b < 9 ; b++ ) {
                if ( !isLocked(a,b) ) setNum(a, b, 0) ;
            }
        }
        
    }*/
    
    
    // Añadir dos posiciones a la lista de posiciones repetidas si estas no se
    // encontraban ya en ella
    // posicion1: la que se está comprobando en el método exterior
    // posicion2: contra la que se está comparando
    private void addRepetido(int a, int b, Position posicion1) {
        Position posicion2 = new Position(a,b) ;
        if (!listaRep.contains(posicion1)) listaRep.add(posicion1) ;
        if (!listaRep.contains(posicion2)) listaRep.add(posicion2) ;
    }
    
    // Comprobar que un valor concreto no se repite en su fila
    private boolean checkFila(int i, int j) {
        
        // Devuelve true si no se repite
        boolean check = true ;
        // Posición que se está comprobando
        Position posicion1 = new Position(i,j) ;
        // Iteración en la fila
        for ( int k = 0 ; k < 9 ; k++ ) {
            if ( k == j ) continue ;
            // Si se repite el valor, añadir ambas posiciones a la lista
            // y provocar que el método devuelva false
            if ( arr[i][j] == arr[i][k] ) {
                check = false ;
                addRepetido(i,k,posicion1) ;
            }
        }      
        
        return check ;
        
    }
    
    // Comprobar que un valor concreto no se repite en su columna
    private boolean checkCol(int i, int j) {
        
        // Devuelve true si no se repite
        boolean check = true ;
        // Posición que se está comprobando
        Position posicion1 = new Position(i,j) ;
        // Iteración en la columna
        for ( int k = 0 ; k < 9 ; k++ ) {
            if ( k == i ) continue ;
            // Si se repite el valor, añadir ambas posiciones a la lista
            // y provocar que el método devuelva false
            if ( arr[i][j] == arr[k][j] ) {
                check = false ;
                addRepetido(k,j,posicion1) ;
            }
        }
        
        return check ;
        
    }
    
    // Comprobar que un valor concreto no se repite en su bloque de 3x3
    private boolean checkBloque(int i, int j) {
        
        // Devuelve true si no se repite
        boolean check = true ;
        // Posición que se está comprobando
        Position posicion1 = new Position(i,j) ;
        // Definición de la posición del bloque
        int m, n ;
        m = (int) Math.floor(i/3.0) ;
        n = (int) Math.floor(j/3.0) ;
        // Iteración por el bloque
        for ( int x = m*3 ; x < 3+m*3 ; x++ ) {
            for ( int y = n*3 ; y < 3+n*3 ; y++ ) {
                if ( x == i && y == j ) continue ;
                // Si se repite el valor, añadir ambas posiciones a la lista
                // y provocar que el método devuelva false
                if ( arr[i][j] == arr[x][y] ) {
                    check = false ;
                    addRepetido(x,y,posicion1) ;
                }
            }
        }
        
        return check ;
        
    }
    
    // Integración de los tres métodos anteriores en uno solo
    public boolean check(int i, int j) {
        
        // boolean devueltos por cada uno de los métodos
        boolean checkFila = checkFila(i,j) ;
        boolean checkCol = checkCol(i,j) ;
        boolean checkBloque = checkBloque(i,j) ;
        // Si todos devuelven true, el boolean general será true
        boolean check = checkFila&&checkCol&&checkBloque ;
        // Posición que se está comprobando
        Position posicion = new Position(i,j) ;
        // Si no se repite el valor en la posición, eliminar de la lista
        if (check)
            listaRep.remove(posicion) ;
        
        return check ;
        
    }
    
    // Comprobar los repetidos y devolver true si alguno ya no lo está
    public boolean checkRep() {
        
        // Devuelve false por defecto
        boolean eliminado = false ;
        // Iteración por cada una de las posiciones
        for ( int a = 0 ; a < listaRep.size() ; a++ ) {
            // Posición que se está comprobando
            Position posicion = listaRep.get(a) ;
            int i = posicion.i ;
            int j = posicion.j ;
            // Si ya no se repite, añadir a la lista de aquellas que ya no se
            // repiten y eliminar de la lista de la que se repiten
            // Devolver true
            if ( check(i,j) ) {
                listaNoRep.add(posicion) ;
                listaRep.remove(posicion) ;
                eliminado = true ;
                a-- ;
            }
        }
        
        return eliminado ;
    }
    
    // Comprobar si el sudoku está lleno y no existen valores repetidos
    public boolean checkSudoku() {
        
        for ( int i = 0 ; i < 9 ; i++ ) {
            for ( int j = 0 ; j < 9 ; j ++ ) {
                if ( arr[i][j] == 0 ) return false ;
            }
        }
        
        return listaRep.isEmpty() ;
        
    }
    
}
