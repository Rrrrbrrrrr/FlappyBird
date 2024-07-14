import javax.swing.*;
public class App {
    public static void main(String[] args) throws Exception {
        int boardwidth=360;
        int boardheight=640;

        JFrame frame = new JFrame("Flappy Bird");
       // frame.setVisible(true);  
        frame.setSize(boardwidth,boardheight);          //frame visible
        frame.setLocationRelativeTo(null); //place the windown at centre of the screen
        frame.setResizable(false); //user cannot change frame size
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // when user clicks on x it will termionate the game
        
        flappybird fb= new flappybird();
        frame.add(fb);
        frame.pack();
        fb.requestFocus();
        frame.setVisible(true);  

    }
}
