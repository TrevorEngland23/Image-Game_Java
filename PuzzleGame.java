import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class PuzzleGame extends JFrame {
    private final int width = 500;
    private int height = 500; // going to change later
    JPanel panel;
    private final int rows = 4; // for homework this will be changed to allow user to set this
    private final int columns = 3; // same as above
    private ArrayList<FancyButton> listOfButtons;

    public PuzzleGame() {
        super("Puzzle Game");

        panel = new JPanel(); // sets a new instance of the JPanel
        panel.setLayout(new GridLayout(rows, columns)); // sets the grid layout
        add(panel); // adds the panel to the JFrame
        try{
        loadImage();
        } 
        catch(IOException e){
            JOptionPane.showMessageDialog(rootPane, "Error loading the image.");
        }
        listOfButtons = new ArrayList<FancyButton> ();

        for(int i = 0; i < columns * rows; i++){
            FancyButton button = new FancyButton();
            listOfButtons.add(button);
            panel.add(button);
        }

        setSize(width, height);
        setLocationRelativeTo(null);
        setResizable(false);    
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private BufferedImage loadImage() throws IOException{
        return ImageIO.read(new File("Dwight.jpeg"));
    }
}
