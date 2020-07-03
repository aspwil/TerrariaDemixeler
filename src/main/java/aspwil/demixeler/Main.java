/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aspwil.demixeler;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 *
 * @author aspwi
 */
public class Main {
    private static MainUI GUI = new MainUI();
    
    public static void main(String args[]) {
        //open the main UI
        GUI.setVisible(true);
    }

    public static boolean convert(
            boolean dirCheck,
            String dirText
    ) {
        String dir = "";
        //decide directory to convert images in
        if (dirCheck) {
            //if check box is true use local directory
            //
            String path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            try {
                dir = URLDecoder.decode(path, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "incorrect directory, contains invalid charictors ", "INCORRECT DIRECTORY", JOptionPane.ERROR_MESSAGE);
                //end method, fail
                return false;
            }
            dir = dir.substring(1, dir.lastIndexOf("/"));
        } else {
            try {
                Paths.get(dirText);
            } catch (InvalidPathException | NullPointerException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "\""+dirText+"\" is not a valid directory ", "DIRECTORY ERROR", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            dir = dirText;
        }
        System.out.println(dir);
        //get array of file names
        File[] pngFiles = new File(dir).listFiles(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isFile() && f.getName().toLowerCase().endsWith(".png");
            }
        });
        //check if the files were read in
        if (pngFiles == null || pngFiles.length == 0){
            JOptionPane.showMessageDialog(null, "no png files found under directory \""+dir+"\" or directory does not exist", "MISSING FILES ERROR", JOptionPane.ERROR_MESSAGE);
            //end method, fail
            return false;
        } else {
            //show loading started
            for (File pngFile : pngFiles) {
                try {
                    //read in png file
                    ImageIO.write(ImageResize.resize(ImageIO.read(pngFile), 2), "png", pngFile);
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, "cannot read file at \"" + pngFile.getPath() + "\"", "FILE READING ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
            //show loading ending
        }
        //end method. compleated, yay
        return true;
    }
}
