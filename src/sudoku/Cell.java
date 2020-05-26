
package sudoku;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 *
 * @author Mario
 */
public final class Cell extends StackPane {
    
    private final Text text = new Text() ;
    private LapizText lapizText = new LapizText() ;
    private final int i, j ;
    private boolean locked ;
    private String textValue ;
    private ArrayList<Integer> list = new ArrayList() ;
    
    private final EventHandler<MouseEvent> mouseEnterEH = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            mouseEntered(e);
        }
    } ;
    
    private final EventHandler<MouseEvent> mouseExitEH = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            mouseExited(e);
        }
    } ;
    
    private final EventHandler<MouseEvent> mouseClickEH = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            mousePressed(e);
        }
    } ;
    
    private static Cell selectedCell ;
    private static Sudoku Sdk ;
    private static HashMap<Position,Cell> map = new HashMap() ;
    
    
    public Cell(int i, int j) {
        this.i = i ;
        this.j = j ;
        initText() ;
        locked = Sdk.isLocked(i,j) ;
        setPrefSize(40,40) ;
        if ( !locked ) {
            addMouseBehaviour() ;
            toWhite() ;
            toBlackFont() ;
        } else
            toLockedColor() ;
        map.put(new Position(i,j), this) ;
    }
    
    public static void reiniciar() {
        Sdk = Sudoku.getInstance() ;
    }
    
    private void initText() {
        textValue = Sdk.getNum(i, j) ;
        text.setText(textValue) ;
        getChildren().add(text) ;
    }
    
    private void addMouseBehaviour() {
        addEventFilter(MouseEvent.MOUSE_ENTERED, mouseEnterEH) ;
        addEventFilter(MouseEvent.MOUSE_EXITED, mouseExitEH) ;
        addEventFilter(MouseEvent.MOUSE_CLICKED, mouseClickEH) ;
    }
    
    private void removeMouseBehaviour() {
        removeEventFilter(MouseEvent.MOUSE_ENTERED, mouseEnterEH);
        removeEventFilter(MouseEvent.MOUSE_EXITED, mouseExitEH);
        removeEventFilter(MouseEvent.MOUSE_CLICKED, mouseClickEH);
    }
    
    
    // Acciones al pulsar con el ratón
    private void mousePressed(MouseEvent evt) {                                 
        if ( selectedCell != null ) selectedCell.toWhite() ;
        selectedCell = (Cell) evt.getSource() ;
        if ( !Sdk.isLocked(i,j) ) toYellowBold() ;
        else unselect() ;
    }
    
    // Acciones al meter el ratón
    private void mouseEntered(MouseEvent evt) {
        Cell cell = (Cell) evt.getSource() ;
        if ( cell != selectedCell && !isLocked() ) toGray() ;
    }
    
    // Acciones al sacar el ratón
    private void mouseExited(MouseEvent evt) {
        Cell cell = (Cell) evt.getSource() ;
        if ( cell != selectedCell && !isLocked() ) toWhite() ;
    }
    
    public static void unselect() {
        selectedCell = null ;
    }
    
    public static Cell getSelectedCell() {
        return selectedCell ;
    }
    
    private boolean isLocked() {
        return locked ;
    }
    
    public int getI() {
        return i ;
    }
    
    public int getJ() {
        return j ;
    }
    
    public static void selectCellByPos(Position pos) {
        selectedCell = map.get(pos) ;
        selectedCell.toYellowBold() ;
    }
    
    public static Cell getCellByPos(Position pos) {
        return map.get(pos) ;
    }
    
    public static int getSelectedI() {
        return selectedCell.i ;
    }
    
    public static int getSelectedJ() {
        return selectedCell.j ;
    }
    
    public static void select(int i, int j) {
        selectedCell = map.get(new Position(i,j)) ;
        selectedCell.toYellowBold() ;
    }
    
    // Poner el texto negro sin negrita y el fondo blanco
    public void toWhite() {
        setFormat(FontWeight.NORMAL, Estilo.FONDO_CELDA_NORMAL.COLOR) ;
    }
    // Poner el texto  sin negrita y el fondo gris
    private void toGray() {
        setFormat(FontWeight.NORMAL, Estilo.FONDO_CELDA_OVER.COLOR) ;
    }
    // Poner el texto negro
    public void toBlackFont() {
        text.setFill(Color.BLACK) ;
    }
    // Poner el texto rojo
    public void toRedFont() {
        text.setFill(Color.RED) ;
    }
    // Poner el JLabel actual con fondo amarillo y texto en negrita
    private void toYellowBold() {
        setFormat(FontWeight.BOLD, Estilo.FONDO_CELDA_SELECTED.COLOR) ;
    }
    // Pone el fondo gris claro y el texto negro sin negrita
    private void toLockedColor() {
        setFormat(FontWeight.SEMI_BOLD, Estilo.FONDO_CELDA_LOCKED.COLOR) ;
        toBlackFont() ;
    }
    // Pone el texto negro en negrita y el fondo verde
    private void toGreen() {
        setFormat(FontWeight.BOLD, Estilo.FONDO_CELDA_RESUELTO.COLOR) ;
        toBlackFont() ;
        locked = true ;
    }
    
    private void setFormat(FontWeight fontWeight, Color bgColor) {
        text.setFont(Font.font("Tahoma", fontWeight, 24)) ;
        this.setBackground(new Background(new BackgroundFill(
                bgColor,
                new CornerRadii(0.0),
                Insets.EMPTY
        ))) ;
    }
    
    public void setText(String str) {
        textValue = str ;
        text.setText(textValue) ;
        getChildren().removeAll(getChildren()) ;
        getChildren().add(text) ;
        list.clear() ;
    }
    
    public void empty() {
        textValue = "" ;
        text.setText(textValue) ;
    }
    
    public boolean isEmpty() {
        return textValue.equals("") ;
    }
    
    public void addNum(int num, Color color) {
        lapizText = lapizText.add(num, color) ;
        getChildren().removeAll(getChildren()) ;
        getChildren().add(new Group(lapizText)) ;
    }
    
    public void replaceNums(HashMap<Integer,Text> map) {
        lapizText = new LapizText(map) ;
        getChildren().removeAll(getChildren()) ;
        getChildren().add(new Group(lapizText)) ;
    }
    
    public static void resuelto() {
        Iterator it = map.entrySet().iterator() ;
        HashMap.Entry<int[],Cell> elemento ;
        while(it.hasNext()) {
            elemento = (HashMap.Entry<int[],Cell>) it.next() ;
            elemento.getValue().toGreen() ;
        }
    }
    
    public static void resolver() {
        // Nuevo array para contener la solución
        int[][] solucion = Puzzle.getSolucion() ;
        // Leer la solución y cargarla en el array
        
        Iterator it = map.entrySet().iterator() ;
        HashMap.Entry<int[], Cell> entry ;
        Cell cell ;
        // Se recorren los objetos JLabel
        while ( it.hasNext() ) {
            entry = (HashMap.Entry<int[], Cell>) it.next() ;
            cell = entry.getValue() ;
            // Se asigna el valor del sudoku resuelto al JLabel
            cell.setText(String.valueOf(solucion[cell.getI()][cell.getJ()])) ;
            // Se pone la celda verde y en negrita
            cell.toGreen() ;
            // Se eliminan los controladores de eventos de ratón
            cell.removeMouseBehaviour() ;
            unselect() ;
            Sdk.allLocked() ;
        }
    }
    
    class LapizText extends GridPane {
        private HashMap<Integer,Text> map = new HashMap() ;
        private ArrayList<Integer> lista = new ArrayList() ;
        
        private LapizText add(int num, Color color) {
            if ( lista.contains(num) ) {
                map.remove(num) ;
                lista.remove(lista.indexOf(num)) ;
            } else {
                lista.add(num) ;
                Collections.sort(lista) ;
                Text texto = new Text(String.valueOf(num)) ;
                texto.setFill(color) ;
                texto.setFont(Font.font("Tahoma",FontWeight.NORMAL,10)) ;
                map.put(num, texto) ;
            }
            LapizText output = this ;
            output.getChildren().removeAll(getChildren()) ;
            int col = 0 ;
            int row = 0 ;
            for ( int i : lista ) {
                output.add(map.get(i), col, row) ;
                col++ ;
                if ( col == 3 ) {
                    col = 0 ;
                    row++ ;
                }
            }
            output.setVgap(0) ;
            output.setHgap(5) ;
            return output ;
        }
        
        private LapizText(HashMap<Integer,Text> map) {
            this.map = map ;
            this.lista = new ArrayList(map.keySet()) ;
            int col = 0 ;
            int row = 0 ;
            for ( int i : lista ) {
                this.add(map.get(i), col, row) ;
                col++ ;
                if ( col == 3 ) {
                    col = 0 ;
                    row++ ;
                }
            }
            this.setVgap(0) ;
            this.setHgap(5) ;
        }
        
        private LapizText() {}
        
        private HashMap<Integer,Text> getMap() {
            return (HashMap<Integer,Text>) map.clone() ;
        }
    }
    
    public HashMap<Integer,Text> getMap() {
        return lapizText.getMap() ;
    }

    
}
