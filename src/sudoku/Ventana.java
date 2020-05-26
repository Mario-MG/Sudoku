
package sudoku;

import java.util.ArrayList;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import static sudoku.Cell.getCellByPos;
import static sudoku.Cell.getSelectedCell;
import static sudoku.Cell.getSelectedI;
import static sudoku.Cell.getSelectedJ;
import static sudoku.Cell.resolver;
import static sudoku.Cell.resuelto;
import static sudoku.Cell.select;
import static sudoku.Cell.selectCellByPos;
import static sudoku.Cell.unselect;

/**
 *
 * @author Mario
 */
public class Ventana extends Application {
    
    // ATRIBUTOS
    
    private Sudoku Sdk ;                            // Objeto Sudoku
    private static boolean modoBoli = true ;        // Modo bolígrafo (true) o lápiz (false)
    private static ColorPicker colorpicker ;        // Objeto ColorPicker
    public static void switchModoBoli() {           // Método de cambio de modo
        modoBoli = !modoBoli ;
        colorpicker.setVisible(!colorpicker.visibleProperty().getValue()) ;
    }
    private Stage stage = null ;
    private Scene scene = null ;
    private VBox root = null ;
    private Node control = null, botonesModoNums = null, botonesNum = null, botonesFlecha = null ;
    private boolean guardarSalir = true ;
    
    // ArrayList para almacenar los cambios en el sudoku (necesario para Deshacer y Rehacer)
    private ArrayList<Move> moves = new ArrayList() ;
    
    // GridPane con las celdas del sudoku
    private GridPane gridPane = new GridPane() ;
    
    // Botones de flecha, de borrar y de números (estos últimos en el ArrayList)
    Boton up = new Boton(TipoBoton.UP) ;
    Boton down = new Boton(TipoBoton.DOWN) ;
    Boton right = new Boton(TipoBoton.RIGHT) ;
    Boton left = new Boton(TipoBoton.LEFT) ;
    Boton botonBorrar = new Boton(TipoBoton.BORRAR) ;
    ArrayList<Boton> listaBotonesNum = new ArrayList() ;
    
    
    
    // Seleccionar la primera celda que no esté bloqueada
    private void selectFirstUnlocked() {
        Position pos = Sdk.getFirstUnlocked() ;
        if ( pos != null ) {
            selectCellByPos(pos) ;
        }
    }
    
    // Métodos asociados a las teclas de flecha
    
    // Arriba
    private void goUp() {
        if ( getSelectedCell() == null ) {
            selectFirstUnlocked() ;
            return ;
        }
        int i = getSelectedI() ;
        int j = getSelectedJ() ;
        int a = i ;
        do {
            a-- ;
            if ( a < 0 ) {
                a = i ;
                break ;
            }
        }
        while ( Sdk.isLocked(a,j) ) ;
        i = a ;
        
        getSelectedCell().toWhite() ;
        select(i,j) ;
    }
    // Abajo
    private void goDown() {
        if ( getSelectedCell() == null ) {
            selectFirstUnlocked() ;
            return ;
        }
        int i = getSelectedI() ;
        int j = getSelectedJ() ;
        int a = i ;
        do {
            a++ ;
            if ( a > 8 ) {
                a = i ;
                break ;
            }
        }
        while ( Sdk.isLocked(a,j) ) ;
        i = a ;
        
        getSelectedCell().toWhite() ;
        select(i,j) ;
    }
    // Izquierda
    private void goLeft() {
        if ( getSelectedCell() == null ) {
            selectFirstUnlocked() ;
            return ;
        }
        int i = getSelectedI() ;
        int j = getSelectedJ() ;
        int a = j ;
        do {
            a-- ;
            if ( a < 0 ) {
                a = j ;
                break ;
            }
        }
        while ( Sdk.isLocked(i,a) ) ;
        j = a ;
        
        getSelectedCell().toWhite() ;
        select(i,j) ;
    }
    // Derecha
    private void goRight() {
        if ( getSelectedCell() == null ) {
            selectFirstUnlocked() ;
            return ;
        }
        int i = getSelectedI() ;
        int j = getSelectedJ() ;
        int a = j ;
        do {
            a++ ;
            if ( a > 8 ) {
                a = j ;
                break ;
            }
        }
        while ( Sdk.isLocked(i,a) ) ;
        j = a ;
        
        getSelectedCell().toWhite() ;
        select(i,j) ;
    }
    
    // Métodos relacionados con cargar un nuevo Sudoku
    
    // Nuevo sudoku
    private void nuevo() {
        boolean loaded = false ;
        if ( !Fichero.exists() )
            Sdk = Sudoku.nuevoPuzzle(9, 50) ;
        else {
            Sdk = Sudoku.nuevoPuzzle(Fichero.leer()) ;
            loaded = true ;
        }
        initCells() ;
        if ( loaded ) {
            ArrayList<Position> listaRep = Sdk.getListaRep() ;
            // Recorrer la lista y poner todos los valores repetidos en rojo
            for ( Position pos : listaRep ) {
                Cell cellRep = getCellByPos(pos) ;
                cellRep.toRedFont() ;
            }
        }
        unselect() ;
    }
    
    // Reiniciar el sudoku actual
    private void reiniciar() {
        Sdk = Sudoku.reiniciarPuzzle() ;
        moves = new ArrayList() ;
        initCells() ;
        unselect() ;
    }
    
    // Inicializar las celdas del sudoku
    private void initCells() {
        gridPane.getChildren().removeAll(gridPane.getChildren()) ;
        for ( int m = 0 ; m < 3 ; m++ ) {
            for ( int n = 0 ; n < 3 ; n++ ) {
                GridPane internalGrid = new GridPane() ;
                for ( int i = 0 ; i < 3 ; i++ ) {
                    for ( int j = 0 ; j < 3 ; j++ ) {
                        Cell cell = new Cell(m*3+i,n*3+j) ;
                        internalGrid.add(cell, j, i);
                    }
                }
                internalGrid.setHgap(2) ;
                internalGrid.setVgap(2) ;
                gridPane.add(internalGrid, n, m);
            }
        }
        gridPane.setHgap(5) ;
        gridPane.setVgap(5) ;
    }
    
    // Agregar un número en la celda seleccionada (sea en modo bolígrafo o lápiz)
    private void setNum(int num) {
        Cell selectedCell = getSelectedCell() ;
        if ( selectedCell != null && num > 0 ) {
            if ( modoBoli ) {
                if ( !selectedCell.isEmpty() )
                    // Generar el registro de movimiento
                    moves.add( new Move(selectedCell, num) ) ;
                else
                    moves.add( new Move(selectedCell) ) ;
                // Realizar los cambios necesarios
                setNum(selectedCell, num) ;
            } else if ( selectedCell.isEmpty() ) {
                moves.add( new Move(selectedCell) ) ;
                selectedCell.addNum(num, colorpicker.getValue()) ;
            }
        }
    }
    
    // Borrar el/los número(s) de la celda seleccionada
    private void borrar() {
        Cell selectedCell = getSelectedCell() ;
        if ( selectedCell != null ) {
            // Generar el registro de movimiento
            moves.add( new Move(selectedCell, 0) ) ;
            // Borrar el texto que hubiera en la celda
            selectedCell.setText("") ;
            // Asignar un cero a esa posicion en el sudoku
            int i = selectedCell.getI() ;
            int j = selectedCell.getJ() ;
            Sdk.setNum(i, j, 0) ;
            // Poner el texto en negro
            selectedCell.toBlackFont() ;
        }
    }
    
    // Acciones al pulsar una tecla
    private final EventHandler<KeyEvent> keyPressedEH = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent e) {
            KeyCode kc = e.getCode() ;
            // Si es una tecla numérica
            if ( kc.isDigitKey() ) {
                int num = Integer.parseInt(e.getText()) ;
                setNum(num) ;
                listaBotonesNum.get(num-1).iluminar(true) ;
            }
            // Si es una tecla de flecha
            else if ( kc.isArrowKey() ) {
                switch (kc.getName()) {
                    case "Up":
                        goUp() ;
                        up.iluminar(true) ;
                        break ;
                    case "Down":
                        goDown() ;
                        down.iluminar(true) ;
                        break ;
                    case "Left":
                        goLeft() ;
                        left.iluminar(true) ;
                        break ;
                    case "Right":
                        goRight() ;
                        right.iluminar(true) ;
                        break ;
                }
            }
            // Si es borrar o suprimir
            else if ( kc == KeyCode.BACK_SPACE || kc == KeyCode.DELETE ) {
                borrar() ;
                botonBorrar.iluminar(true) ;
            }
            // Si es Ctrl + Z
            else if ( e.isControlDown() && (kc == KeyCode.Z) && moves.size() > 0 ) {
                // Obtener el último cambio
                Move move = moves.get(moves.size()-1) ;
                // Obtener los elementos involucrados
                Cell oldCell = move.getCell() ;
                if ( move.modoBoli ) {
                    int oldNum = move.getOldNum() ;
                    // Restaurar el valor anterior
                    setNum(oldCell, oldNum) ;
                    
                } else {
                    oldCell.replaceNums(move.getMap()) ;
                    int i = oldCell.getI() ;
                    int j = oldCell.getJ() ;
                    Sdk.setNum(i, j, 0) ;
                    oldCell.empty() ;
                }
                // Eliminar el cambio de la lista de cambios
                moves.remove(move) ;
            }
            // Comprobar los valores repetidos en el sudoku
            if ( Sdk.checkRep() ) {
                // Si es así, obtener la lista de valores que ya no están repetidos
                ArrayList<Position> listaNoRep = Sdk.getListaNoRep() ;
                // Para cada posición de la lista, poner los valores en negro
                for ( Position pos : listaNoRep ) {
                    Cell cell = getCellByPos(pos) ;
                    cell.toBlackFont() ;
                }
                // Vaciar la lista
                Sdk.limpiarListaNoRep() ;
            }
            // Si el sudoku está resuelto, aplicar el formato correspondiente
            if ( Sdk.checkSudoku() )
                resuelto() ;
        }
    } ;
    
    // Acciones al dejar de pulsar una tecla (para "desiluminar" los botones correspondientes)
    private final EventHandler<KeyEvent> keyReleasedEH = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent e) {
            KeyCode kc = e.getCode() ;
            if ( kc.isDigitKey() ) {
                int num = Integer.parseInt(e.getText()) ;
                listaBotonesNum.get(num-1).iluminar(false) ;
            }else if ( kc.isArrowKey() ) {
                switch (kc.getName()) {
                    case "Up":
                        up.iluminar(false) ;
                        break ;
                    case "Down":
                        down.iluminar(false) ;
                        break ;
                    case "Left":
                        left.iluminar(false) ;
                        break ;
                    case "Right":
                        right.iluminar(false) ;
                        break ;
                }
            } else if ( kc == KeyCode.BACK_SPACE || kc == KeyCode.DELETE )
                botonBorrar.iluminar(false) ;
        }
    } ;
    
    // Asignar el número de la celda seleccionada en modo bolígrafo
    private void setNum(Cell cell, int num) {
        int i = cell.getI() ;
        int j = cell.getJ() ;
        // Asignar dicho valor a la celda correspondiente en el sudoku
        Sdk.setNum(i, j, num) ;
        // Obtener el valor y mostrarlo como texto en la celda
        cell.setText( Sdk.getNum(i,j) ) ;
        // Si el valor no está repetido, ponerlo en negro
        if ( Sdk.check(i,j) )
            cell.toBlackFont() ;
        // En caso contrario:
        else {
            // Obtener la lista de posiciones con valores repetidos
            ArrayList<Position> listaRep = Sdk.getListaRep() ;
            // Recorrer la lista y poner todos los valores repetidos en rojo
            for ( Position pos : listaRep ) {
                Cell cellRep = getCellByPos(pos) ;
                cellRep.toRedFont() ;
            }
        }
    }
    
    // Dar acciones a una serie de botones
    private void setAcciones(Boton... botones) {
        for ( Boton boton : botones ) {
            TipoBoton tipo = boton.getTipoBoton() ;
            boton.setOnMouseClicked(accionEHBuilder(tipo)) ;
        }
    }
    
    // "Constructor" de acciones para los botones
    private EventHandler<MouseEvent> accionEHBuilder(TipoBoton tipo) {
        EventHandler<MouseEvent> EH = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                switch (tipo) {
                    case REINICIAR :
                        reiniciar() ;
                        break ;
                    case NUEVO :
                        nuevo() ;
                        break ;
                    case RESOLVER :
                        resolver() ;
                        break ;
                    case GUARDAR :
                        Fichero.guardar() ;
                        break ;
                    case UP :
                        goUp() ;
                        break ;
                    case DOWN :
                        goDown() ;
                        break ;
                    case LEFT :
                        goLeft() ;
                        break ;
                    case RIGHT :
                        goRight() ;
                        break ;
                }
            }
        } ;
        return EH ;
    }
    
    // Construir el grid del sudoku
    private StackPane buildSudokuGrid() {
        nuevo() ;
        Group group = new Group() ;
        group.getChildren().add(gridPane) ;
        StackPane pane = new StackPane(group) ;
        pane.setPadding(new Insets(4.0)) ;
        return pane ;
    }
    
    // Construir la caja de menú
    private HBox buildMenu() {
        Boton reiniciar = new Boton(TipoBoton.REINICIAR) ;
        Tooltip.install(reiniciar, new Tooltip("Reiniciar el sudoku actual a su estado original.")) ;
        Boton nuevo = new Boton(TipoBoton.NUEVO) ;
        Tooltip.install(nuevo, new Tooltip("Genera un nuevo sudoku.")) ;
        Boton resolver = new Boton(TipoBoton.RESOLVER) ;
        Tooltip.install(resolver, new Tooltip("Resuelve el sudoku actual, rellenando todas las casillas.")) ;
        Boton guardar  = new Boton(TipoBoton.GUARDAR) ;
        Tooltip.install(guardar, new Tooltip("Guarda el sudoku en su estado actual para seguir jugando más tarde.")) ;
        
        setAcciones(reiniciar, nuevo, resolver, guardar) ;
        
        HBox hbox = new HBox(reiniciar, nuevo, resolver, guardar) ;
        hbox.setPrefHeight(40.0) ;
        
        for ( Node node : hbox.getChildren() )
            HBox.setHgrow(node, Priority.ALWAYS);
        
        return hbox ;
    }
    
    // Construir el panel de flechas
    private GridPane buildControlFlechas() {
        GridPane gridPane = new GridPane() ;
        
        setAcciones(up, down, right, left) ;
        
        gridPane.add(up, 1, 0) ;
        gridPane.add(down, 1, 1) ;
        gridPane.add(right, 2, 1) ;
        gridPane.add(left, 0, 1) ;
        
        gridPane.setPadding(new Insets(8.0)) ;
        
        return gridPane ;
    }
    
    // Construir la caja de control del modo y el botón de borrar número(s)
    private HBox buildControlModo() {
        ToggleSwitch boli = new ToggleSwitch() ;
        boli.setBorder(Estilo.BORDE_BOTON.toBorder()) ;
        
        initColorPicker() ;
        
        botonBorrar.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                borrar() ;
            }
        }) ;
        
        HBox hbox = new HBox(boli, colorpicker, botonBorrar) ;
        HBox.setHgrow(botonBorrar, Priority.ALWAYS);
        
        hbox.setPadding(new Insets(8.0)) ;
        return hbox ;
    }
    
    // Construir la caja con botones de números
    private HBox buildControlNums() {
        HBox hboxNums = new HBox() ;
        for ( int i = 1 ; i <= 9 ; i++ ) {
            Boton botonNum = new Boton(i) ;
            final int num = i ;
            botonNum.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    setNum(num) ;
                }
            });
            hboxNums.getChildren().add(botonNum) ;
            listaBotonesNum.add(botonNum) ;
        }
        hboxNums.setPadding(new Insets(8.0)) ;
        return hboxNums ;
    }
    
    // Construir la caja de control de modo y de númmeros
    private HBox buildControl() {
        HBox hboxModo = buildControlModo() ;
        botonesNum = buildControlNums() ;
        botonesModoNums = new VBox(hboxModo, botonesNum) ;
        
        botonesFlecha = buildControlFlechas() ;
        
        control = new HBox(botonesModoNums, botonesFlecha) ;
        
        for ( Node node : ((HBox)control).getChildren() )
            HBox.setHgrow(node, Priority.ALWAYS);
        
        ((GridPane)botonesFlecha).setAlignment(Pos.CENTER) ;
        
        return (HBox) control ;
    }
    
    // Construir la caja de opciones
    private VBox buildOpciones() {
        VBox vboxInterna = new VBox() ;
        VBox vboxOpciones = new VBox() ;
        
        Hyperlink textoOpciones = new Hyperlink("Opciones \\/") ;
        textoOpciones.setFocusTraversable(false) ;
        vboxOpciones.getChildren().add(textoOpciones) ;
        
        CheckBox mostrarNum = new CheckBox("Mostrar botones numéricos y borrar") ;
        mostrarNum.setSelected(true) ;
        mostrarNum.setTooltip(new Tooltip("Muestra o esconde el panel de botones numéricos y el botón 'Borrar'.")) ;
        mostrarNum.setFocusTraversable(false) ;
        CheckBox mostrarFlechas = new CheckBox("Mostrar botones de flecha") ;
        mostrarFlechas.setSelected(true) ;
        mostrarFlechas.setTooltip(new Tooltip("Muestra o esconde el panel de botones de flecha.")) ;
        mostrarFlechas.setFocusTraversable(false) ;
        CheckBox guardarSalirBox = new CheckBox("Guardar al salir") ;
        guardarSalirBox.setSelected(guardarSalir) ;
        guardarSalirBox.setTooltip(new Tooltip("Guarda el sudoku al salir, en el estado en el que se encuentre.")) ;
        guardarSalirBox.setFocusTraversable(false) ;
        
        EventHandler checkBoxesEH = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                CheckBox cb = (CheckBox) e.getSource() ;
                boolean b = cb.selectedProperty().getValue() ;
                if ( cb.equals(mostrarNum) ) {
                    setNumsVisible(b) ;
                    ObservableList<Node> lista = ((VBox)botonesModoNums).getChildren() ;
                    if (b)
                        lista.add(botonesNum) ;
                    else
                        lista.remove(botonesNum) ;
                    stage.sizeToScene() ;
                } else if ( cb.equals(mostrarFlechas) ) {
                    ObservableList<Node> lista = ((HBox)control).getChildren() ;
                    if (b)
                        lista.add(botonesFlecha) ;
                    else
                        lista.remove(botonesFlecha) ;
                    stage.sizeToScene() ;
                } else if ( cb.equals(guardarSalirBox) ) {
                    guardarSalir = guardarSalirBox.selectedProperty().getValue() ;
                }
            }
        } ;
        
        mostrarNum.setOnAction(checkBoxesEH) ;
        mostrarFlechas.setOnAction(checkBoxesEH) ;
        guardarSalirBox.setOnAction(checkBoxesEH) ;
        
        vboxInterna.getChildren().addAll(mostrarNum, mostrarFlechas, guardarSalirBox) ;
        vboxInterna.setSpacing(2.0) ;
        
        textoOpciones.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if ( !vboxOpciones.getChildren().contains(vboxInterna) ) {
                    vboxOpciones.getChildren().add(vboxInterna) ;
                    textoOpciones.setText("Opciones /\\") ;
                    stage.sizeToScene();
                } else {
                    vboxOpciones.getChildren().remove(vboxInterna) ;
                    textoOpciones.setText("Opciones \\/") ;
                    stage.sizeToScene();
                }
            }
        }) ;
        
        vboxOpciones.setPadding(new Insets(8.0)) ;
        vboxOpciones.setSpacing(3.0) ;
        return vboxOpciones ;
    }
    
    private void setNumsVisible(boolean b) {
        botonBorrar.setVisible(b) ;
    }
    
    // Estilo de ColorPicker inactivo (pero visible); es decir, con el ratón fuera
    private final String estiloCPInactivo = "-fx-color-label-visible: false ;"
            + "-fx-background-color: " + Estilo.FONDO_BOTON_INACTIVO.getHex() + ";"
            + "-fx-border-color: " + Estilo.BORDE_BOTON.getHex() + ";"
            + "-fx-min-height: 30px ;"
            + "-fx-background-insets: 0" ;
    
    // Estilo de ColorPicker con el ratón dentro
    private final String estiloCPMouseOver = "-fx-color-label-visible: false ;"
            + "-fx-background-color: " + Estilo.FONDO_BOTON_OVER.getHex() + ";"
            + "-fx-border-color: " + Estilo.BORDE_BOTON.getHex() + ";"
            + "-fx-min-height: 30px ;"
            + "-fx-background-insets: 0" ;
    
    // Inicializar el ColorPicker (no visible en un principio)
    private void initColorPicker() {
        colorpicker = new ColorPicker(Color.GREY) ;
        colorpicker.setStyle(estiloCPInactivo) ;
        colorpicker.setFocusTraversable(false) ;
        colorpicker.setVisible(false) ;
        
        colorpicker.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                colorpicker.setStyle(estiloCPMouseOver) ;
            }
        } ) ;
        colorpicker.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                colorpicker.setStyle(estiloCPInactivo) ;
            }
        } ) ;
    }
    
    // Construir la escena
    private Scene buildScene() {
        HBox menu = buildMenu() ;
        StackPane sudokuGrid = buildSudokuGrid() ;
        HBox control = buildControl() ;
        VBox opciones = buildOpciones() ;
        
        root = new VBox(menu, sudokuGrid, control, opciones) ;
        root.setBackground(Estilo.FONDO_VENTANA.toBackground()) ;
        
        menu.setPrefWidth(root.getWidth()) ;
        control.setPrefWidth(root.getWidth()) ;
        
        scene = new Scene(root) ;
        return scene ;
    }
    
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sudoku");
        primaryStage.setScene(buildScene());
        primaryStage.show();
        primaryStage.sizeToScene();
        primaryStage.setResizable(false);
        primaryStage.setMinWidth(primaryStage.getWidth()) ;
        botonBorrar.setMaxWidth(botonBorrar.getWidth()) ;
        root.setMinWidth(root.getWidth()) ;
        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, keyPressedEH);
        primaryStage.addEventHandler(KeyEvent.KEY_RELEASED, keyReleasedEH);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                if ( guardarSalir )
                    Fichero.guardar() ;
            }
        });
        stage = primaryStage ;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
