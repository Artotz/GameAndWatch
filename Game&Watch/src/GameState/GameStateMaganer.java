package GameState;

import Handler.Keys;
import Main.GamePanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class GameStateMaganer {
    private final GamePanel gamePanel;
    
    private final GameState[] gameStates;
    
    private int currentState;
    
    public static final int NUM_STATES = 2;
    public static final int MENU_STATE = 0;
    public static final int LEVEL01_STATE = 1;
    
    //GAME OVER----------
    private boolean GAME_OVER = false;
    
    private String[] menuOptions = {"Retry", "Menu", "Exit"};
    private int currentOption = 0;
    
    private Font fontTitulo = new Font("Arial", Font.BOLD, 20);
    private Font fontTudo = new Font("Arial", Font.PLAIN, 15);
    
    private int frames = 0;
    //GAME OVER----------
    
    public GameStateMaganer(GamePanel gp){
        gamePanel = gp;
        
        gameStates = new GameState[NUM_STATES];
        
        currentState = MENU_STATE;
        loadState(currentState);
    }
    
    private void loadState(int state){
        switch(state){
            case MENU_STATE:
                gameStates[state] = new MenuState(this);
                break;
            case LEVEL01_STATE:
                gameStates[state] = new Level01State(this);
                break;
        } 
    }
    
    private void unloadState(int state){
        gameStates[state] = null;
    }
    
    public void setState(int state){
        unloadState(currentState);
        currentState = state;
        loadState(currentState);
    }
    
    public void update(){
        if(gameStates[currentState] != null && !GAME_OVER){ //Só para não dar bleuris
            gameStates[currentState].update();
        }
        else{
            updateGameOver();
        }
    }
    
    public void draw(Graphics2D g){
        if(gameStates[currentState] != null){ //Só para não dar bleuris
            gameStates[currentState].draw(g);
        }
        if(GAME_OVER){
           drawGameOver(g); 
        }
    }
    
    public void updateGameOver(){        
        gerenciaInput();
        frames++;
    }
    
    public void drawGameOver(Graphics2D g){
        //g.clearRect(0, 0, GamePanel.COMPRIMENTO, GamePanel.ALTURA);
        g.setColor(new Color(0, 0, 0, 50));
        g.fillRect(0, 0, GamePanel.COMPRIMENTO, GamePanel.ALTURA);
        
        g.setColor(Color.WHITE);
        g.setFont(fontTitulo);
        centralizarTexto(g, "GAME OVER", 35);
        
        g.setColor(Color.WHITE);
        g.setFont(fontTudo);
        for(int i = 0; i < menuOptions.length; i++){
            g.setColor(Color.WHITE);
            if (i == currentOption){
                g.setColor(Color.RED);
            } 
            centralizarTexto(g, menuOptions[i], 120 + i*20);
        }
    }
    
    public void gerenciaInput(){
        if (Keys.onlyPressed(Keys.UP)) currentOption = currentOption == 0 ? menuOptions.length - 1 :  --currentOption;
        else if (Keys.onlyPressed(Keys.DOWN)) currentOption = currentOption == menuOptions.length - 1 ? 0  :  ++currentOption;
        else if (Keys.onlyPressed(Keys.Z)) select();
    }
    
    public void select(){
        switch(currentOption){
            case 0:
                setGameOver(false);
                setState(currentState);
                break;
            case 1:
                setGameOver(false);
                setState(MENU_STATE);
                break;
            case 2:
                System.exit(0);
                break;   
        }
    }
    
    public void setGameOver(boolean b){
        if(b){
            GAME_OVER = true;
        }
        else{
            GAME_OVER = false;
            frames = 0;
            currentOption = 0;
        }
    }
    
    public void centralizarTexto(Graphics2D g, String texto, int y){
        g.drawString(texto, GamePanel.COMPRIMENTO/2 - g.getFontMetrics().stringWidth(texto)/2, y);
    }
    
    public void setEscala(int e){
        gamePanel.setEscala(e);
    }
    
}
