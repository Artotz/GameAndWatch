package GameState;

import Handler.Keys;
import Main.GamePanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class MenuState extends GameState {
    private GameStateMaganer gsm;
    private String[] menuOptions = {"Start", "Options", "Credits", "Exit"};
    private int currentOption = 0;
    
    private Font fontTitulo = new Font("Arial", Font.BOLD, 20);
    private Font fontTudo = new Font("Arial", Font.PLAIN, 15);
    
    private int frames = 0;
    
    private int currentScreen = 0;
    private static final int START = 0;
    private static final int OPTIONS = 1;

    public MenuState(GameStateMaganer gsm){
        this.gsm = gsm;
    }
    
    @Override
    public void update() {
        gerenciaInput();
        frames++;
    }

    @Override
    public void draw(Graphics2D g) {
        g.clearRect(0, 0, GamePanel.COMPRIMENTO, GamePanel.ALTURA);
        
        switch(currentScreen){
            case START:
                g.setColor(Color.WHITE);
                g.setFont(fontTitulo);
                centralizarTexto(g, "Peize&Watch", 35);

                g.setColor(Color.WHITE);
                g.setFont(fontTudo);
                for(int i = 0; i < menuOptions.length; i++){
                    g.setColor(Color.WHITE);
                    if (i == currentOption){
                        g.setColor(Color.RED);
                    } 
                    centralizarTexto(g, menuOptions[i], 120 + i*20);
                }
                break;
                
            case OPTIONS:
                g.setColor(Color.WHITE);
                g.setFont(fontTitulo);
                centralizarTexto(g, "Options", 35);

                g.setColor(Color.WHITE);
                g.setFont(fontTudo);
                g.setColor(Color.RED);
                centralizarTexto(g, "Escala: " + GamePanel.ESCALA, 140);
                break;
        }
        
    }
    
    public void select(){
        if(currentScreen == START){
            switch(currentOption){
                case 0:
                    gsm.setState(GameStateMaganer.LEVEL01_STATE);
                    break;
                case 1:
                    currentScreen = OPTIONS;
                    currentOption = 0;
                    break;
                case 2:

                    break;
                case 3:
                    System.exit(0);
                    break;

            } 
        }
        else if(currentScreen == OPTIONS){
            currentScreen = START;
            currentOption = 0;
        }
    }
    
    public void gerenciaInput(){
        if (Keys.onlyPressed(Keys.UP)) currentOption = currentOption == 0 ? menuOptions.length - 1 :  --currentOption;
        else if (Keys.onlyPressed(Keys.DOWN)) currentOption = currentOption == menuOptions.length - 1 ? 0  :  ++currentOption;
        else if (Keys.onlyPressed(Keys.Z)) select();
        
        //LIMITAR ESCALA
        else if (Keys.onlyPressed(Keys.LEFT) && currentScreen == OPTIONS) gsm.setEscala(--GamePanel.ESCALA);
        else if (Keys.onlyPressed(Keys.RIGHT) && currentScreen == OPTIONS) gsm.setEscala(++GamePanel.ESCALA);
    }
    
    public void centralizarTexto(Graphics2D g, String texto, int y){
        g.drawString(texto, GamePanel.COMPRIMENTO/2 - g.getFontMetrics().stringWidth(texto)/2, y);
    }
    
}
