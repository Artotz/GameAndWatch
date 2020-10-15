package Handler;

import java.awt.event.KeyEvent;

public class Keys {
    
    public static final int NUM_KEYS = 12;
    
    public static boolean keyState[] = new boolean[NUM_KEYS];
    public static boolean prevKeyState[] = new boolean[NUM_KEYS];
    
    public static int UP = 0;
    public static int DOWN = 1;
    public static int LEFT = 2;
    public static int RIGHT = 3;
    public static int Z = 4;
    public static int X = 5;
    public static int ENTER = 6;
    
    //USADO APENAS PARA O GAMEPANEL
    public static void keySet(int i, boolean b){
        if(i == KeyEvent.VK_UP) {keyState[UP] = b;}
        else if (i == KeyEvent.VK_DOWN) {keyState[DOWN] = b;}
        else if (i == KeyEvent.VK_LEFT) {keyState[LEFT] = b;}
        else if (i == KeyEvent.VK_RIGHT) {keyState[RIGHT] = b;}
        else if (i == KeyEvent.VK_Z) {keyState[Z] = b;}
        else if (i == KeyEvent.VK_X) {keyState[X] = b;}
        else if (i == KeyEvent.VK_ENTER) {keyState[ENTER] = b;}
        
    }
    
    public static void update(){
        System.arraycopy(keyState, 0, prevKeyState, 0, NUM_KEYS);
    }
    
    public static boolean onlyPressed(int i){
        //Retorna o momento em que o estado passado era false e o atual é true da tecla pressionada. A função retorna true se esse for o caso.
        return keyState[i] && !prevKeyState[i];
    }
    
    public static boolean anyKeyPressed(){
        for(int i = 0; i < NUM_KEYS; i++){
            if(onlyPressed(i)) return true;
        }
        return false;
    }
    
}
