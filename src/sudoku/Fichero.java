
package sudoku;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
//import java.time.LocalDateTime;

/**
 *
 * @author Mario
 */
public class Fichero {
    
    private static File datafile = new File("savedgame") ; ;
    //private static final File NAMEFILE = new File("namefile") ;
    
    private Fichero() {}
    
    public static boolean guardar() {
        DataOutputStream dos = null ;
        try {
            //String nombre = LocalDateTime.now().toString().replaceAll("[^0-9a-zA-Z]", "") ;
            //System.out.println("Nombre: " + nombre) ;
            //datafile = new File(nombre) ;
            datafile.createNewFile() ;
            
            datafile.setWritable(true) ;
            Files.setAttribute(datafile.toPath(), "dos:hidden", false) ;
            
            dos = new DataOutputStream(new FileOutputStream(datafile)) ;
            dos.writeUTF(getGrid()) ;
            dos.writeUTF(getSdk()) ;
            
            datafile.setWritable(false) ;
            Files.setAttribute(datafile.toPath(), "dos:hidden", true) ;
            //System.out.println("Fichero guardado en: " + datafile.getAbsolutePath()) ;
            
            /*NAMEFILE.createNewFile() ;
            
            dos = new DataOutputStream(new FileOutputStream(NAMEFILE)) ;
            dos.writeUTF(nombre) ;
            
            NAMEFILE.setWritable(false) ;
            Files.setAttribute(NAMEFILE.toPath(), "dos:hidden", true) ;*/
            
            return true ;
        } catch ( IOException e ) {
            e.printStackTrace() ;
            return false ;
        } finally {
            try {
                if ( dos != null )
                    dos.close() ;
            } catch (IOException ex) {}
        }
    }
    
    private static String getGrid() {
        int[][] arr = Puzzle.getInstance(9).getGrid() ;
        String output = "" ;
        for ( int[] fila : arr ) {
            for ( int num : fila ) {
                output += num ;
            }
            output += "-" ;
        }
        return output ;
    }
    
    private static String getSdk() {
        String output = "" ;
        Sudoku sdk = Sudoku.getInstance() ;
        for ( int i = 0 ; i < 9 ; i++ ) {
            for ( int j = 0 ; j < 9 ; j++ ) {
                String s = sdk.getNum(i, j) ;
                output += !s.equals("") ? s : "0" ;
            }
            output += "-" ;
        }
        return output ;
    }
    
    public static boolean exists() {
        /*DataInputStream dis = null ;
        try {
            if ( !NAMEFILE.exists() ) return false ;
            dis = new DataInputStream(new FileInputStream(NAMEFILE)) ;
            String name = dis.readUTF() ;*/
            
            //datafile = new File(name) ;
            return datafile.exists() ;
        /*} catch ( IOException e ) {
            e.printStackTrace() ;
            return false ;
        } finally {
            try {
                if ( dis != null )
                    dis.close() ;
            } catch (IOException ex) {}
        }*/
    }
    
    public static int[][][] leer() { // TODO
        if ( !exists() ) return null ;
        
        DataInputStream dis = null ;
        int[][][] output = new int[2][][] ;
        try {
            /*dis = new DataInputStream(new FileInputStream(NAMEFILE)) ;
            String name = dis.readUTF() ;
            dis.close() ;
            
            dis = new DataInputStream(new FileInputStream(name)) ;*/
            dis = new DataInputStream(new FileInputStream(datafile)) ;
            String[] gridStr = dis.readUTF().split("-") ;
            int[][] grid = new int[gridStr.length][] ;
            for ( int i = 0 ; i < gridStr.length ; i++ ) {
                grid[i] = new int[gridStr[i].length()] ;
                for ( int j = 0 ; j < gridStr[i].length() ; j++ ) {
                    grid[i][j] = Character.getNumericValue(gridStr[i].charAt(j)) ;
                }
            }
            
            String[] sdkStr = dis.readUTF().split("-") ;
            int[][] sdk = new int[sdkStr.length][] ;
            for ( int i = 0 ; i < sdkStr.length ; i++ ) {
                sdk[i] = new int[sdkStr[i].length()] ;
                for ( int j = 0 ; j < sdkStr[i].length() ; j++ ) {
                    sdk[i][j] = Character.getNumericValue(sdkStr[i].charAt(j)) ;
                }
            }
            
            output[0] = grid ;
            output[1] = sdk ;
            
            dis.close() ;
            //NAMEFILE.delete() ;
            datafile.delete() ;
            return output ;
        } catch ( IOException e ) {
            e.printStackTrace() ;
            return null ;
        } finally {
            try {
                if ( dis != null )
                    dis.close() ;
            } catch (IOException ex) {}
        }
    }
    
}
