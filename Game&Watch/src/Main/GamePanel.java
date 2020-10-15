package Main;

import GameState.GameStateMaganer;
import Handler.Keys;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable, KeyListener {
    
    //Dimensões
    public static final int COMPRIMENTO = 320;
    public static final int ALTURA = 240;
    public static int ESCALA = 2; // Para dobrar o tamanho da janela. Serve para deixar o jogo pixelizado
    
    //THREAD DO JOGO
    private Thread thread; //PESQUISA AI MANÉ
    private boolean rodando; //O programa está rodando. Serve para parar o loop de frames, caso necessário
    private final int FPS = 60;
    private final long targetTime = 1000/FPS; //Para contruir a framerate. Tempo em milisegundos de cada frame. Aka segundos por frame
    
    //Imagem
    private BufferedImage image; //Junta todos os desenhos numa imagem só, esta é o frame. Esta que será impressa.
    private static Graphics2D g; //Funções de desenho. Equivalente ao "sf::" do SFML
    
    //Game State Manager
    private GameStateMaganer gsm;
    
    private JFrame window;
    
    public GamePanel(JFrame w){
        super();
        window = w;
        setPreferredSize(new Dimension(COMPRIMENTO * ESCALA, ALTURA * ESCALA));// P tamanho que a janela começa
        setFocusable(true); //Seta o foco da janela
        requestFocusInWindow(); //Vai abrir essa janela em prioridade
    }
    
    public void addNotify(){
        super.addNotify();
        if(thread == null){
            thread = new Thread(this);
            addKeyListener(this);
            thread.start();
        }
    }
    
    //Inicializa e instancia o que foi criado antes
    public void inicializar(){
        image = new BufferedImage(COMPRIMENTO, ALTURA, BufferedImage.TYPE_INT_RGB); //Tá instanciando o objeto "image". Informa o tamanho do papel e o tipo de cores que vai usar
        g = (Graphics2D) image.getGraphics(); // Usa casting para converter a imagem para o tipo Graphics2D. Aka transforma o ambiente de trabalho de "image" em uma folha de papel
        
        rodando = true;
        
        gsm = new GameStateMaganer(this);
    }
    
    
    @Override // A função a seguir é implementada de "Runnable".
    public void run(){
        inicializar(); //inicia a image.
        
        // long para precisão
        long start;
        long elapsed = 0;
        long wait;
        
        //LOOP DO JOGO ("Rodando nunca vai mudar. Serve para ter um loop infinito. É igual a While(true)")
        while(rodando){
            start = System.nanoTime();//Comela a contagem de tempo.
            
            //Faz tudo do frame
            update();
            draw();
            drawToScreen();
            
            elapsed = targetTime - elapsed /1000000; //vai ver quanto tempo passou desde dar update e desenhar o frame. O programa mede tempo.
            //"elapsed /1000000" converte de nanosegundos para milisegundos
            
            
            /*vê o tempo te espera para ver se ele foi menor ou maior que a quantia necessária
            de segundos para um frame. Se sim, passa para o próximo. Se não, vai esperar o tempo do wait
            na função "sleep" até dar o tempo de um frame.*/
            
            wait = targetTime - elapsed / 1000000; 
            
            if(wait < 0) wait = 5;
            
            /*A função sleep é problemática. Para caso ela falhe(o que acarretaria em erro), entra o Try. 
            Caso a função dê errado, ela entra no catch com o nome "e". Em seguida, ela printa o nome 
            da exceção/erro. */
            
            try{
                Thread.sleep(wait);
            }
            catch(Exception e){
                e.printStackTrace();
            }
            
        }
        
    }
    
    //
    private void update(){
        gsm.update();
        Keys.update();
    }
    
    private void draw(){
        gsm.draw(g);
    }
    
    private void drawToScreen(){
        Graphics g2 = getGraphics();
        g2.drawImage(image, 0, 0, COMPRIMENTO * ESCALA, ALTURA * ESCALA, null);
        g2.dispose();
    }
    
    @Override //Implementa de KeyListener;
    public void keyPressed(KeyEvent k){
        Keys.keySet(k.getKeyCode(), true);
    }
    
    @Override //Implementa de KeyListener;
    public void keyReleased(KeyEvent k){
        Keys.keySet(k.getKeyCode(), false);
    }
    
    @Override //Implementa de KeyListener;
    public void keyTyped(KeyEvent key) {
        
    }
    
    public void setEscala(int e){
        ESCALA = e;
        setPreferredSize(new Dimension(COMPRIMENTO * ESCALA, ALTURA * ESCALA));
        window.pack();
        window.setLocationRelativeTo(null);
    }
    
}
