
package sudoku;

/**
 *
 * @author Mario
 */
public enum TipoBoton {
    REINICIAR("Reiniciar"),
    NUEVO("Nuevo"),
    RESOLVER("Resolver"),
    TOGGLE(""),
    UP("/\\"),
    DOWN("\\/"),
    RIGHT(">"),
    LEFT("<"),
    NUM(""),
    BORRAR("Borrar"),
    GUARDAR("Guardar") ;
    
    public final String texto ;
    
    private TipoBoton(String texto) {
        this.texto = texto ;
    }
}