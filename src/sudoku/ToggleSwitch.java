
package sudoku;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 *
 * @author ItachiUchiha
 * https://gist.github.com/TheItachiUchiha/12e40a6f3af6e1eb6f75
 */
public class ToggleSwitch extends HBox {
	
	private final Label label = new Label();
	private final Boton button = new Boton(TipoBoton.TOGGLE);
	
	private SimpleBooleanProperty switchedOn = new SimpleBooleanProperty(true);
	public SimpleBooleanProperty switchOnProperty() { return switchedOn; }
	
	private void init() {
		
		label.setText("Bolígrafo");
                label.setStyle("-fx-font-weight: bold") ;
                button.setText("Lápiz") ;
		
		getChildren().addAll(label, button);	
		button.setOnMouseClicked((e) -> {
			click() ;
		});
		setStyle();
		bindProperties();
	}
        
        private void click() {
            switchedOn.set(!switchedOn.get());
            button.setBackground(Estilo.FONDO_BOTON_INACTIVO.toBackground()) ;
            if (switchedOn.get())
                button.setText("Lápiz") ;
            else
                button.setText("Bolígrafo") ;
            Ventana.switchModoBoli() ;
        }
	
	private void setStyle() {
		//Default Width
		setWidth(150) ;
                setMaxWidth(150) ;
                setHeight(30) ;
                setMaxHeight(30) ;
		label.setAlignment(Pos.CENTER);
                setBackground(Estilo.FONDO_BOTON_INACTIVO.toBackground()) ;
		setAlignment(Pos.CENTER_LEFT);
	}
	
	private void bindProperties() {
		label.prefWidthProperty().bind(widthProperty().divide(2));
		label.prefHeightProperty().bind(heightProperty());
		button.prefWidthProperty().bind(widthProperty().divide(2));
		button.prefHeightProperty().bind(heightProperty());
	}
	
	public ToggleSwitch() {
		init();
		switchedOn.addListener((a,b,c) -> {
			if (!c) {
                		label.setText("Lápiz");
                		label.toFront();
            		}
            		else {
            			label.setText("Bolígrafo");
                		button.toFront();
            		}
		});
	}
}
