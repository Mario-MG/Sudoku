
package sudoku;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

/**
 *
 * @author Mario
 */
public enum Estilo {
    FONDO_VENTANA(Color.LIGHTGREY) ,
    FONDO_BOTON_INACTIVO(Color.LIGHTGREY) ,
    FONDO_BOTON_OVER(Color.DARKGREY) ,
    FONDO_BOTON_PRESS(Color.FLORALWHITE) ,
    FONDO_CELDA_NORMAL(Color.WHITE) ,
    FONDO_CELDA_OVER(Color.GAINSBORO) ,
    FONDO_CELDA_LOCKED(Color.AZURE) ,
    FONDO_CELDA_SELECTED(Color.YELLOW) ,
    FONDO_CELDA_RESUELTO(Color.LIME) ,
    BORDE_BOTON(Color.DARKGREY) ;
    
    public final Color COLOR ;
    
    private Estilo(Color color) {
        this.COLOR = color ;
    }
    
    /**
     * @author Zon
     * https://stackoverflow.com/a/17925679
     * @return Hexadecimal representation of the Color as a String
     */
    public String getHex() {
        String hex1;
        String hex2;

        hex1 = Integer.toHexString(COLOR.hashCode()).toUpperCase();

        switch (hex1.length()) {
        case 2:
            hex2 = "000000";
            break;
        case 3:
            hex2 = String.format("00000%s", hex1.substring(0,1));
            break;
        case 4:
            hex2 = String.format("0000%s", hex1.substring(0,2));
            break;
        case 5:
            hex2 = String.format("000%s", hex1.substring(0,3));
            break;
        case 6:
            hex2 = String.format("00%s", hex1.substring(0,4));
            break;
        case 7:
            hex2 = String.format("0%s", hex1.substring(0,5));
            break;
        default:
            hex2 = hex1.substring(0, 6);
        }
        return hex2;
    }
    
    public Background toBackground() {
        return new Background(new BackgroundFill(
                COLOR,
                new CornerRadii(0.0),
                Insets.EMPTY
        )) ;
    }
    
    public Border toBorder() {
        return new Border(new BorderStroke(
                COLOR,
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                BorderWidths.DEFAULT
        )) ;
    }
    
}
