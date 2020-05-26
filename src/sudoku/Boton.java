
package sudoku;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**
 *
 * @author Mario
 */
public class Boton extends StackPane {
    
    private final TipoBoton tipo ;
    private Text texto ;
    private String str ;
    
    private final EventHandler mouseEnterEH = mouseEHBuilder(Estilo.FONDO_BOTON_OVER),
            inactiveEH = mouseEHBuilder(Estilo.FONDO_BOTON_INACTIVO),
            mousePressEH = mouseEHBuilder(Estilo.FONDO_BOTON_PRESS) ;
    
    public Boton(TipoBoton tipo) {
        this.tipo = tipo ;
        this.texto = textBuilder() ;
        
        setBackground(Estilo.FONDO_BOTON_INACTIVO.toBackground()) ;
        setPadding(new Insets(4.0)) ;
        
        setOnMouseEntered(mouseEnterEH) ;
        setOnMouseExited(inactiveEH) ;
        if ( tipo != TipoBoton.TOGGLE ) {
            setOnMousePressed(mousePressEH) ;
            setOnMouseReleased(mouseEnterEH) ;
        }
        
        getChildren().add(this.texto) ;
        
        switch (tipo) {
            case RIGHT :
            case LEFT :
                texto.setStyle("-fx-font-size: 16pt") ;
                setPadding(new Insets(-5.0,0,0,0)) ;
            case UP :
            case DOWN :
                setPrefSize(30,30) ;
            case BORRAR :
                setBorder(Estilo.BORDE_BOTON.toBorder()) ;
        }
    }
    
    public Boton(int num) {
        tipo = TipoBoton.NUM ;
        str = String.valueOf(num) ;
        texto = new Text(str) ;
        setBackground(Estilo.FONDO_BOTON_INACTIVO.toBackground()) ;
        setBorder(Estilo.BORDE_BOTON.toBorder()) ;
        setPadding(new Insets(4.0)) ;
        getChildren().add(this.texto) ;
        setPrefSize(30,30) ;
        setOnMouseEntered(mouseEnterEH) ;
        setOnMouseExited(inactiveEH) ;
        setOnMousePressed(mousePressEH) ;
        setOnMouseReleased(mouseEnterEH) ;
    }
    
    public TipoBoton getTipoBoton() {
        return tipo ;
    }
    
    private EventHandler<MouseEvent> mouseEHBuilder(Estilo estilo) {
        EventHandler<MouseEvent> EH = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                setBackground(estilo.toBackground()) ;
            }
        } ;
        return EH ;
    }
    
    private Text textBuilder() {
        switch (tipo) {
            case REINICIAR :
            case NUEVO :
            case RESOLVER :
            case TOGGLE :
            case UP :
            case DOWN :
            case RIGHT :
            case LEFT :
            case BORRAR :
            case GUARDAR :
                str = tipo.texto ;
        }
        return new Text(str) ;
    }
    
    public void setText(String texto) {
        this.texto.setText(texto) ;
    }
    
    public void iluminar(boolean b) {
        switch (tipo) {
            case RIGHT :
            case LEFT :
            case UP :
            case DOWN :
            case BORRAR :
            case NUM :
                setBackground( ( b ? Estilo.FONDO_BOTON_PRESS : Estilo.FONDO_BOTON_INACTIVO ).toBackground() ) ;
        }
    }
    
}
