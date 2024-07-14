import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList; //stores all the pipes
import java.util.Random; //used to place pipes at random positions
import javax.swing.*;

public class flappybird extends JPanel implements ActionListener, KeyListener {
    int boardwidth = 360;
    int boardheight = 640;

    // Images
    Image backgroundImg;
    Image birdImg;
    Image topPipeImg;
    Image bottomPipeImg;

    // bird
    int birdX = boardwidth / 8;
    int birdY = boardheight / 2;
    int birdWidth = 34;
    int birdHeight = 24;

    class Bird {
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;
        Image img;

        Bird(Image img) {
            this.img = img;
        }

    }

    // pipes
    int pipeX = boardwidth;
    int pipeY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;

    class pipe {
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false;

        pipe(Image img) {
            this.img = img;
        }
    }

    // game logic
    Bird bird;
    int velocityY = 0;
    int velocityX = -4;
    int gravity = 1;
    ArrayList<pipe> pipes;
    Random random = new Random();
    Timer gameloop;
    Timer placePipesTimer;
    boolean gameOver = false;
    double score=0;

    flappybird() {
        setPreferredSize(new Dimension(boardwidth, boardheight));
        // setBackground(Color.blue);
        setFocusable(true);
        addKeyListener(this);
        // load images
        backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

        // bird
        bird = new Bird(birdImg);
        pipes = new ArrayList<pipe>();
        // place pipes timer
        placePipesTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });
        placePipesTimer.start();
        // game timer
        gameloop = new Timer(1000 / 60, this);
        gameloop.start();
    }

    public void placePipes() {
        int randomPipeY = (int) (pipeY - pipeHeight / 4 - Math.random() * (pipeHeight / 2));
        int openingSpace = boardheight/4;

        pipe topPipe = new pipe(topPipeImg);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        pipe bottompipe= new pipe(bottomPipeImg);
        bottompipe.y=topPipe.y +pipeHeight+openingSpace;
        pipes.add(bottompipe);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // background
        g.drawImage(backgroundImg, 0, 0, boardwidth, boardheight, null);
        // bird
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);

        // pipes
        for (int i = 0; i < pipes.size(); i++) {
            pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        //score
        g.setColor(Color.white);
        g.setFont(new Font("Arial",Font.PLAIN,32));
        if(gameOver){
            g.drawString("GAME OVER"+String.valueOf((int) score),10,35);
        }
        else{
            g.drawString(String.valueOf((int) score),10,35);
        }
    }

    public void move() {
        // bird
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0);

        
        
        // pipes
        for (int i = 0; i < pipes.size(); i++) {
            pipe pipe = pipes.get(i);
            pipe.x += velocityX;
            
            if(!pipe.passed && bird.x>pipe.x+pipe.width){
                pipe.passed=true;
                score+=0.5;
            }
            if(collision(bird, pipe)){
                gameOver=true;
            }
        }

        if(bird.y > boardheight){
            gameOver=true;
        }
    }

    public boolean collision(Bird a,pipe b){
        return a.x <b.x+ b.width &&
               a.x + a.width >b.x &&
               a.y <b.y + b.height &&
               a.y +a.height >b.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver){
            placePipesTimer.stop();
            gameloop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocityY = -9;
            if(gameOver){
                bird.y=birdY;
                pipes.clear();
                score=0;
                gameOver=false;
                gameloop.start();
                placePipesTimer.start();
            }
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
