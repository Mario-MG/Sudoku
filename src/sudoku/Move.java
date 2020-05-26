/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

import java.util.HashMap;
import javafx.scene.text.Text;

/**
 *
 * @author Mario
 */
public class Move {
    
    private final Cell cell ;
    private final int i, j, oldNum, newNum ;
    public final boolean modoBoli ;
    private final HashMap<Integer,Text> map ;

    public Move(Cell cell, int newNum) {
        this.cell = cell;
        this.i = cell.getI() ;
        this.j = cell.getJ() ;
        this.modoBoli = true ;
        String oldNumStr = Sudoku.getInstance().getNum(i, j) ;
        if ( !oldNumStr.equals("") )
            this.oldNum = Integer.parseInt(oldNumStr) ;
        else
            this.oldNum = 0 ;
        this.newNum = newNum;
        this.map = null ;
    }
    
    public Move(Cell cell) {
        this.cell = cell;
        this.i = cell.getI() ;
        this.j = cell.getJ() ;
        this.modoBoli = false ;
        this.oldNum = 0 ;
        this.newNum = 0 ;
        this.map = cell.getMap() ;
    }

    public Cell getCell() {
        return cell;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public int getOldNum() {
        return oldNum;
    }

    public int getNewNum() {
        return newNum;
    }
    
    public HashMap<Integer,Text> getMap() {
        return map ;
    }
    
    
    
}
