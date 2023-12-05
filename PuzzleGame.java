import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class PuzzleGame extends JFrame {
    private final int width = 500;
    private int height = 500; // going to change later
    JPanel panel;
    private final int rows = 4; // for homework this will be changed to allow user to set this
    private final int columns = 3; // same as above
    private ArrayList<FancyButton> listOfButtons = new ArrayList<FancyButton>();
    private ArrayList<FancyButton> buttonsSolution = new ArrayList<FancyButton>();
    private BufferedImage imageSource;
    private BufferedImage imageResized;

    public PuzzleGame() {
        super("Puzzle Game");

        panel = new JPanel(); // sets a new instance of the JPanel
        panel.setLayout(new GridLayout(rows, columns)); // sets the grid layout
        add(panel); // adds the panel to the JFrame
        try {
            imageSource = loadImage();
            int sourceWidth = imageSource.getWidth();
            int sourceHeight = imageSource.getHeight();
            double aspectRatio = (double)sourceHeight / sourceWidth;
            height = (int)(aspectRatio * width);

            imageResized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = imageResized.createGraphics();
            graphics.drawImage(imageSource, 0, 0, width, height, null);
            graphics.dispose();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(rootPane, "Error loading the image.");
        }
        listOfButtons = new ArrayList<FancyButton>();

        for (int i = 0; i < columns * rows; i++) {

            int row = i / columns;
            int column = i % columns;

            Image imageSlice = createImage(
                new FilteredImageSource(imageResized.getSource(), 
                new CropImageFilter(column * width/columns, row * height/rows, width/columns, height/rows)
                ));

            FancyButton button = new FancyButton();
            button.addActionListener(e -> myClickEventHandler(e));
            if(i == columns * rows - 1){ // last button
                button.setBorderPainted(false);
                button.setContentAreaFilled(false);
            } else {
                button.setIcon(new ImageIcon(imageSlice));
            }
            buttonsSolution.add(button);
            listOfButtons.add(button);
          
        }

        
        Collections.shuffle(listOfButtons);
        for(var button:listOfButtons){
            panel.add(button);
        }

        setSize(width, height);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void myClickEventHandler(ActionEvent e){
        FancyButton buttonClicked =  (FancyButton)e.getSource();
        int i = listOfButtons.indexOf(buttonClicked);
        int column = i % columns;
        int row = i / columns;
        int emptyIndex = -1;

        for(int j = 0; j < listOfButtons.size(); j++){
            if(listOfButtons.get(j).getIcon() == null){
                emptyIndex = j;
            }
        }

        int emptyColumn = emptyIndex % columns;
        int emptyRow = emptyIndex / columns;

        if(emptyRow == row && Math.abs(column - emptyColumn) == 1 || 
        emptyColumn == column && Math.abs(row - emptyRow) == 1){

            Collections.swap(listOfButtons, i, emptyIndex);
            updateButtons();
        }

        if(buttonsSolution.equals(listOfButtons)){
            JOptionPane.showMessageDialog(buttonClicked, "Congratulations! You are correct!");
        }
    }

    public void updateButtons(){
        panel.removeAll();
        for(var btn:listOfButtons){
            panel.add(btn);
        }
        panel.validate();
    }



    private BufferedImage loadImage() throws IOException {
        return ImageIO.read(new File("nature.jpeg"));
    }
}
