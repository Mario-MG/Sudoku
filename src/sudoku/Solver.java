/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author Mario
 */
public class Solver {
    
    // ATRIBUTOS

    // Array bidimensional que almacenará el sudoku a resolver
    private int[][] sudoku ;
    // Array bidimensional de objetos HashSet para los posibles valores en cada celda
    private HashSet<Integer>[][] posible = new HashSet[9][9] ;
    
    public Solver(int[][] grid) {
        sudoku = new int[grid.length][] ;
        copy(grid,sudoku) ;
        initPosibles() ;
    }
    
    private void initPosibles() {
        for ( int i = 0 ; i < 9 ; i++ ) {
            for ( int j = 0 ; j < 9 ; j++ ) {
                posible[i][j] = new HashSet<>() ;
                for ( Integer num = 1 ; num <= 9 ; num++ ) {
                    posible[i][j].add(num) ;
                }
            }
        }
    }
    
    // MÉTODOS
    
    // Eliminar los valores repetidos en una fila
    private void checkValoresFila(int i, int j) {
        
        // Sólo si la celda que se está comprobando no tiene un valor
        if ( sudoku[i][j] == 0 ) {
            // Para cada una de las celdas de la fila
            for ( int k = 0 ; k < 9 ; k++ ) {
                // Si dicha celda contiene un valor, eliminarlo de los posibles
                // valores de la celda actual
                if ( sudoku[i][k] != 0 ) {
                    posible[i][j].remove(sudoku[i][k]) ;
                }
            }
        }
        
    }
    
    // Eliminar los valores repetidos en una columna
    private void checkValoresCol(int i, int j) {
        
        // Sólo si la celda que se está comprobando no tiene un valor
        if ( sudoku[i][j] == 0 ) {
            // Para cada una de las celdas de la fila
            for ( int k = 0 ; k < 9 ; k++ ) {
                // Si dicha celda contiene un valor, eliminarlo de los posibles
                // valores de la celda actual
                if ( sudoku[k][j] != 0 ) {
                    posible[i][j].remove(sudoku[k][j]) ;
                }
            }
        }
        
    }
    
    // Eliminar los valores repetidos en un bloque de 3x3
    private void checkValoresBloque (int i, int j) {
        
        // Sólo si la celda que se está comprobando no tiene un valor
        if ( sudoku[i][j] == 0 ) {
            // Se define la posición del bloque de 3x3
            int m, n ;
            m = (int) Math.floor(i/3.0) ;
            n = (int) Math.floor(j/3.0) ;
            // Para cada una de las celdas del bloque
            for ( int x = m*3 ; x <= 2+m*3 ; x++ ) {
                for ( int y = n*3 ; y <= 2+n*3 ; y++ ) {
                    // Si dicha celda contiene un valor, eliminarlo de los posibles
                    // valores de la celda actual
                    if ( sudoku[x][y] != 0 ) {
                        posible[i][j].remove(sudoku[x][y]) ;
                    }
                }
            }
        }
        
    }
    
    // Integración de los tres métodos anteriores en uno solo:
    // Eliminar los valores repetidos en una fila, columna o bloque
    private void checkValores(int i, int j) {
        
        checkValoresFila(i,j) ;
        checkValoresCol(i,j) ;
        checkValoresBloque(i,j) ;
        
        // Además, si después de esto sólo existe un posible valor en la celda,
        // obtener dicho valor y asignárselo a la celda
        // Después, eliminar dicho valor de los posibles valores en la fila,
        // columna y bloque correspondientes (el método eliminarPosible() se
        // declara más adelante
        
        if ( posible[i][j].size() == 1 ) {
            int num = (Integer) posible[i][j].toArray()[0] ;
            sudoku[i][j] = num ;
            eliminarPosible(i,j,num) ;
        }
        
    }
    
    // Eliminar valores repetidos en todas las celdas del sudoku
    private void checkAllValores() {
        for ( int i = 0 ; i < 9 ; i++ ) {
                for ( int j = 0 ; j < 9 ; j++ ) {
                    if ( sudoku [i][j] == 0 ) {
                        checkValores(i,j) ;
                    }
                }
            }
    }
    
    // SIGUIENTE: Posiciones; es decir, que un determinado número sólo pueda ir
    //              en una posición concreta de la fila/columna/bloque
    
    // Comprobar si en una determinada fila sólo existe una celda que pueda
    // albergar un determinado valor
    private void checkPosFila(int i) {
        
        // Para cada uno de los valores posibles (del 1 al 9)
        for ( int num = 1 ; num <= 9 ; num++ ) {
            // Lista de posiciones donde es posible que se encuentre el valor actual
            ArrayList<Integer> lista = new ArrayList() ;
            // Para cada una de las celdas de la fila
            for ( int j = 0 ; j < 9 ; j++ ) {
                // Si la celda está vacía
                if ( sudoku[i][j] == 0 ) {
                    // Si contiene el valor actual como posible, añadirla a la lista
                    if ( posible[i][j].contains(num) ) {
                        lista.add(j) ;
                    }
                }
            }
            // Si al final del bucle, sólo existe una posición en la lista,
            // asignar el valor actual a esa celda y eliminar ese valor de las
            // demás celdas en la fila, columna y bloque (el método eliminarPosible()
            // se declara más adelante)
            if ( lista.size() == 1 ) {
                int j = lista.get(0) ;
                sudoku[i][j] = num ;
                eliminarPosible(i,j,num) ;
            }
        }
        
    }
    
    private void checkPosCol(int j) {
        
        // Para cada uno de los valores posibles (del 1 al 9)
        for ( int num = 1 ; num <= 9 ; num++ ) {
            // Lista de posiciones donde es posible que se encuentre el valor actual
            ArrayList<Integer> lista = new ArrayList() ;
            // Para cada una de las celdas de la columna
            for ( int i = 0 ; i < 9 ; i++ ) {
                // Si la celda está vacía
                if ( sudoku[i][j] == 0 ) {
                    // Si contiene el valor actual como posible, añadirla a la lista
                    if ( posible[i][j].contains(num) ) {
                        lista.add(i) ;
                    }
                }
            }
            // Si al final del bucle, sólo existe una posición en la lista,
            // asignar el valor actual a esa celda y eliminar ese valor de las
            // demás celdas en la fila, columna y bloque (el método eliminarPosible()
            // se declara más adelante)
            if ( lista.size() == 1 ) {
                int i = lista.get(0) ;
                sudoku[i][j] = num ;
                eliminarPosible(i,j,num) ;
            }
        }
        
    }
    
    private void checkPosBloque(int m, int n) {
        // Para cada uno de los valores posibles (del 1 al 9)
        for ( int num = 1 ; num <= 9 ; num++ ) {
            // Lista de posiciones donde es posible que se encuentre el valor actual
            ArrayList<int[]> lista = new ArrayList() ;
            // Para cada una de las celdas del bloque
            for ( int x = m*3 ; x <= 2+m*3 ; x++ ) {
                for ( int y = n*3 ; y <= 2+n*3 ; y++ ) {
                    // Si la celda está vacía
                    if ( sudoku[x][y] == 0 ) {
                        // Si contiene el valor actual como posible, añadirla a la lista
                        if ( posible[x][y].contains(num) ) {
                            int[] posicion = {x, y} ;
                            lista.add(posicion) ;
                        }
                    }
                }
            }
            // Si al final del bucle, sólo existe una posición en la lista,
            // asignar el valor actual a esa celda y eliminar ese valor de las
            // demás celdas en la fila, columna y bloque (el método eliminarPosible()
            // se declara más adelante)
            if ( lista.size() == 1 ) {
                int x = lista.get(0)[0] ;
                int y = lista.get(0)[1] ;
                sudoku[x][y] = num ;
                eliminarPosible(x,y,num) ;
            }
        }
            
    }
    
    // Integración de los tres métodos anteriores para todo el sudoku
    private void checkPosiciones() {
        
        for ( int i = 0 ; i < 9 ; i++ ) checkPosFila(i) ;
        for ( int j = 0 ; j < 9 ; j++ ) checkPosCol(j) ;
        for ( int m = 0 ; m < 3 ; m++ ) {
            for ( int n = 0 ; n < 3 ; n++ ) {
                checkPosBloque(m,n) ;
            }
        }
        
    }
    
    // Para eliminar un cierto valor de la columna, la fila y el bloque
    private void eliminarPosible(int i, int j, int num) {
        
        // Eliminar un valor (num) de la fila y la columna
        for ( int a = 0 ; a < 9 ; a++ ) {
            if ( sudoku[a][j] == 0 ) posible[a][j].remove(num) ;
            if ( sudoku[i][a] == 0 ) posible[i][a].remove(num) ;
        }
        // Definir la posición del bloque
        int m, n ;
        m = (int) Math.floor(i/3.0) ;
        n = (int) Math.floor(j/3.0) ;
        // Eliminar el valor del bloque
        for ( int x = m*3 ; x <= 2+m*3 ; x++ ) {
            for ( int y = n*3 ; y <= 2+n*3 ; y++ ) {
                if ( sudoku[x][y] == 0 ) {
                    posible[x][y].remove(num) ;
                }
            }
        }
        
    }
    
    // Comprobar si una celda tiene un valor repetido
    private boolean checkCell(int i, int j) {
        
        // Comprobación en la fila
        for ( int a = 0 ; a < 9 ; a++ ) {
            if ( i == a ) continue ;
            if ( sudoku[a][j] == sudoku[i][j] ) return false ;
        }
        // Comprobación en la columna
        for ( int a = 0 ; a < 9 ; a++ ) {
            if ( j == a ) continue ;
            if ( sudoku[i][a] == sudoku[i][j] ) return false ;
        }
        // Definición de la posición del bloque
        int m, n ;
        m = (int) Math.floor(i/3.0) ;
        n = (int) Math.floor(j/3.0) ;
        // Comprobación en el bloque
        for ( int x = m*3 ; x <= 2+m*3 ; x++ ) {
            for ( int y = n*3 ; y <= 2+n*3 ; y++ ) {
                if ( x == i && y == j ) continue ;
                if ( sudoku[x][y] == sudoku[i][j] ) return false ;
            }
        }
        
        return true ;
        
    }
    
    // Iteracion del método anterior en todo el sudoku
    private boolean check() {
        
        for ( int i = 0 ; i < 9 ; i++ ) {
            for (int j = 0 ; j < 9 ; j++ ) {
                if ( !checkCell(i,j) ) return false ;
            }
        }
        
        return true ;
        
    }
    
    // Integración de los métodos anteriores en uno solo para resolver el sudoku
    public int[][] resolver() {
        
        // Si el sudoku no se resuelve en un número dado de iteraciones (50), abandonar
        int cuenta = 0 ;
        int[][] sud2 = new int[sudoku.length][] ;
        copy(sudoku,sud2) ;
        do {
            checkAllValores() ;
            checkPosiciones() ;
            cuenta++ ;
            if ( equal(sudoku, sud2) ) {
                System.out.println("El sudoku no se pudo resolver.") ;
                return sudoku ;
            }
            copy(sudoku, sud2) ;
        } while ( !check() ) ;
        
        System.out.println("Sudoku resuelto en " + cuenta + " iteraciones.");
        // Devolver la solución
        return sudoku ;
        
    }
    
    public boolean resoluble() {
        
        //int cuenta = 0 ;
        int[][] sud2 = new int[sudoku.length][] ;
        copy(sudoku, sud2) ;
        do {
            checkAllValores() ;
            checkPosiciones() ;
            //cuenta++ ;
            if ( equal(sudoku, sud2) ) {
                return false ;
            }
            copy(sudoku, sud2) ;
        } while ( !check() ) ;
        
        return true ;
        
    }
    
    /*private void print() {
        for (int i = 0; i<9; i++) 
        { 
            for (int j = 0; j<9; j++) 
                System.out.print(sudoku[i][j] + " "); 
            System.out.println(); 
        } 
        System.out.println(); 
    }*/
    
    private boolean equal(int[][] sud1, int[][] sud2) {
        if ( sud1.length != sud2.length ) return false ;
        for ( int i = 0 ; i < sud1.length ; i++ ) {
            if ( sud1.length != sud2.length ) return false ;
            for ( int j = 0 ; j < sud1[i].length ; j++ ) {
                if ( sud1[i][j] != sud2[i][j] ) return false ;
            }
        }
        return true ;
    }
    
    private boolean copy(int[][] sud1, int[][] sud2) {
        if ( sud1.length != sud2.length ) return false ;
        for ( int i = 0 ; i < sud1.length ; i++ ) {
            sud2[i] = new int[sud1[i].length] ;
            for ( int j = 0 ; j < sud1[i].length ; j++ ) {
                sud2[i][j] = sud1[i][j] ;
            }
        }
        return true ;
    }
    
}
