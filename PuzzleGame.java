import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class PuzzleGame extends JFrame {
    private final int width = 500;
    private int height = 500; // going to change later
    JPanel panel;
    private int rows = 4; // for homework this will be changed to allow user to set this
    private int columns = 3; // same as above
    private ArrayList<FancyButton> listOfButtons = new ArrayList<FancyButton>();
    private ArrayList<FancyButton> buttonsSolution = new ArrayList<FancyButton>();
    private BufferedImage imageSource;
    private BufferedImage imageResized;
    private int countMoves = 0; // initialize the counter that will count the number of moves a person makes to get the imgage correctly displayed (score)

    public PuzzleGame() {
        super("Puzzle Game");

        panel = new JPanel(); // sets a new instance of the JPanel
        panel.setLayout(new GridLayout(rows, columns)); // sets the grid layout
        add(panel); // adds the panel to the JFrame
        try {
            imageSource = loadImage();
            int sourceWidth = imageSource.getWidth(); // get width
            int sourceHeight = imageSource.getHeight(); // get height
            double aspectRatio = (double)sourceHeight / sourceWidth;
            height = (int)(aspectRatio * width);

            imageResized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = imageResized.createGraphics();
            graphics.drawImage(imageSource, 0, 0, width, height, null);
            graphics.dispose();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(rootPane, "Error loading the image.");
        }
        listOfButtons = new ArrayList<FancyButton>(); // arraylist to hold list of buttons

        for (int i = 0; i < columns * rows; i++) { // loop through

            int row = i / columns; // get the row
            int column = i % columns; // get the column

            Image imageSlice = createImage( // get each slice of the image
                new FilteredImageSource(imageResized.getSource(),  
                new CropImageFilter(column * width/columns, row * height/rows, width/columns, height/rows)
                ));

            JMenuBar menuBar = new JMenuBar(); // add a menu bar for score and game options
            setJMenuBar(menuBar); // set the menu bar 
            JMenu gameOptions = new JMenu("Game Options"); // give the mneu some context
            menuBar.add(gameOptions); // add the context to menu bar

            JMenuItem newGame = new JMenuItem("New Game"); // new game option
            newGame.addActionListener(new ActionListener() { // when the user clicks the newgame option, trigger the reset game method
                @Override
                public void actionPerformed(ActionEvent e){
                    resetGame();
                }
            });
            gameOptions.add(newGame); // add the new game option as a new menu item

            JMenuItem userChoice = new JMenuItem("Set Custom Layout"); // add a custom layout option
            userChoice.addActionListener(new ActionListener() { // when user wants this, trigger the set user choice method 
                @Override 
                public void actionPerformed(ActionEvent e){
                    setUserChoice();
                }
            });
            gameOptions.add(userChoice); // add the custom layout option 
            resetGame(); // reset the game 


            FancyButton button = new FancyButton(); // make a new button of fancybutton type
            button.addActionListener(e -> myClickEventHandler(e)); // add an action listener that listens for clicks, and triggers an event
            if(i == columns * rows - 1){ // last button
                button.setBorderPainted(false); 
                button.setContentAreaFilled(false);
            } else {
                button.setIcon(new ImageIcon(imageSlice)); 
            }
            buttonsSolution.add(button); // add each button to buttons solution
            listOfButtons.add(button); // add each button to the list of buttons
          
        }

        
        Collections.shuffle(listOfButtons); // shuffle the buttons
        for(var button:listOfButtons){ // loop through the list of buttons
            panel.add(button); // add each button to the panel
        }

        // JFrame configs
        setSize(width, height);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

// methods

    private void resetGame() { // resets the game
        countMoves = 0; // originally set to 0
        updateMovesCounter(); // call the method that updates counter
     
        Collections.shuffle(listOfButtons); // reshuffles the game 
        for(var button:listOfButtons){ // loops through list of buttons
            panel.add(button); // adds the buttons to panel
        }
        updateButtons(); 
    }

    public void setUserChoice(){
        String rowsString = JOptionPane.showInputDialog(this, "Enter number of rows");
        String columnsString = JOptionPane.showInputDialog(this, "Enter number of columns");

        try{
            int newNumberRow = Integer.parseInt(rowsString);
            int newNumberColumns = Integer.parseInt(columnsString);

            rows = newNumberRow;
            columns = newNumberColumns;

            resetGame();
         
        } catch (NumberFormatException e){
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid integer");
        }
    }


    private void updateMovesCounter() {
        setTitle("Puzzle Game - Moves: " + countMoves);
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

        countMoves++;
        updateMovesCounter();

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
        return ImageIO.read(new File("saints.jpeg"));
    }
}
