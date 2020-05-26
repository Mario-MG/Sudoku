
package sudoku;

/**
 *
 * @author Mario
 */
public class Position {
    
    public final int i ;
    public final int j ;
    
    public Position(int i, int j) {
        this.i = i ;
        this.j = j ;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Position pos = (Position) obj;
        return i == pos.i && j == pos.j ;
    }
    
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + i;
        result = 31 * result + j;
        return result;
    }
    
}
