package GameState;

import Handler.Keys;
import Main.GamePanel;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.imageio.ImageIO;

public class Level01State extends GameState {
    
    private GameStateMaganer gsm;
    int contAnim = 0;
    int contAnimBG = 0;
    int contAnimNPC = 0;
    
    //IMAGENS CRIADAS
    private BufferedImage[][] playerImage = new BufferedImage[2][3]; 
    private BufferedImage[] backgroundImage = new BufferedImage[2]; 
    private BufferedImage[][] npcImage = new BufferedImage[7][5]; 
    
    //ENUM DAS IMAGENS
        //LINHAS
    private final int TOP_PONTE = 0;
    private final int BOTTOM_PONTE = 1;
    private final int BACKGROUND = 2;
    private final int NPC = 3;
        //COLUNAS
    private final int ANIMATION_MAX = 2;
    private int HIT_PONTE = 3;
    
    //MEDIÇÕES
    private int tick = 60;
    private int ticksTotais = 0;
    private int tickAux = 0;
    private int frames = 0;
    
    //GERAÇÃO DE NPC
    private Random randSpawn = new Random();
    int[][] canSpawn = new int[2][10];
    private int spawn;
    private int wave = 0;
    
    //POSIÇÕES
    private int playerX = 0;
    private int playerY = 0;
    private int[][] positionNPC = new int[2][10];
    
    //OUTROS
    private int score = 0;
    
    private int errou = 0;
    private int errouAux = 100;

    
    public Level01State(GameStateMaganer gsm){
        this.gsm = gsm;
        
        //ALOCAR MEMÓRIA E LOCALIZAR CAMINHO DE IMAGENS
        try {            
            BufferedImage ss = ImageIO.read(getClass().getResourceAsStream("../ResourcesManhole/Ponte.PNG"));
            for(int i = 0; i < 2; i++){
                for(int j = 0; j < 3; j++){
                    playerImage[i][j] = ss.getSubimage(0 +j*ss.getWidth()/3, 0 +i*ss.getHeight()/2, ss.getWidth()/3, ss.getHeight()/2);
                }
            }

            backgroundImage[0] = ImageIO.read(getClass().getResourceAsStream("../ResourcesManhole/Background_1.PNG"));
            backgroundImage[1] = ImageIO.read(getClass().getResourceAsStream("../ResourcesManhole/Background_2.PNG"));
            
            ss = ImageIO.read(getClass().getResourceAsStream("../ResourcesManhole/NPC.PNG"));
            for(int i = 0; i < 7; i++){
                for(int j = 0; j < 5; j++){
                npcImage[i][j] = ss.getSubimage(0 +j*ss.getWidth()/5, 0 +i*ss.getHeight()/7, 24, 24);
                }
            }
            
        } 
        catch (Exception e) {e.printStackTrace();}
        
    }
    
    @Override
    public void update() {
        if(errou == 0){
            comandarEntrada();
            tickAux++;

            //NPC        
            if (frames %(tick/4) == 0) contAnimNPC = contAnimNPC == 4 ? 4 : ++contAnimNPC;
            
            spawn = randSpawn.nextInt(6 - wave);

            if (tickAux == tick){
                if ((score +1) % 30 == 0 && wave < 4) wave++;
                tickAux = 0;
                contAnimNPC = 0;

                //ATUALIZA A POSIÇÃO DE CIMA
                for(int i = positionNPC[0].length - 1; i >= 0 ; i--){
                    if (positionNPC[0][i] != 0){
                        if (i != positionNPC[0].length - 1){
                            if(i+1 == 3){
                                if(playerX == 0 && playerY == 0) score++;
                                else {
                                    errou = 1;
                                }
                            }
                            else if(i+1 == 6){
                                if(playerX == 1 && playerY == 0) score++;
                                else {
                                    errou = 2;
                                }
                            }
                            positionNPC[0][i+1] = positionNPC[0][i];
                        }
                        positionNPC[0][i] = 0;
                    }
                }

                //ATUALIZA A POSIÇÃO DE BAIXO
                for(int i = positionNPC[1].length - 1; i >= 0 ; i--){
                    if (positionNPC[1][i] != 0){
                        if (i != positionNPC[1].length - 1){
                            if(i+1 == 3){
                                if(playerX == 1 && playerY == 1) score++;
                                else {
                                    errou = 3;
                                }
                            }
                            else if(i+1 == 6){
                                if(playerX == 0 && playerY == 1) score++;
                                else {
                                    errou = 4;
                                }
                            }
                            positionNPC[1][i+1] = positionNPC[1][i];
                        }
                        positionNPC[1][i] = 0;
                    }
                }

                //ATUALIZA A PERMISSÃO DE SPAWN EM CIMA
                for(int i = 0; i < canSpawn[0].length; i++){
                    if (canSpawn[0][i] == 1){
                        canSpawn[0][i] = 0;
                        if (i != 0)
                            canSpawn[0][i - 1] = 1;
                    }
                }

                //ATUALIZA A PERMISSÃO DE SPAWN EM BAIXO
                for(int i = 0; i < canSpawn[1].length; i++){
                    if (canSpawn[1][i] == 1){
                        canSpawn[1][i] = 0;
                        if (i != 0)
                            canSpawn[1][i - 1] = 1;
                    }
                }

                //BOTA NA FILA DE SPAWN
                if (spawn == 0 && canSpawn[0][0] == 0) {                
                    positionNPC[0][0] = randSpawn.nextInt(7);

                    canSpawn[0][3] = 1;                
                    canSpawn[1][3] = 1; 
                }            
                else if (spawn == 1 && canSpawn[1][0] == 0) {                
                    positionNPC[1][0] = randSpawn.nextInt(7);

                    canSpawn[0][3] = 1;                
                    canSpawn[1][3] = 1; 
                }

                ticksTotais++;
                if(tick>16)
                    tick = 60 - 4*(int)(ticksTotais/50);
            }
            
        }
        else if (errou > 0){
           errouAux--;
           if(errouAux == 0){
               errouAux = 100;
               errou = 0;
               miss();
           } 
        }
        
        
        
    }

    @Override
    public void draw(Graphics2D g) {
        frames++;
        
        //ANIMACOES
        //BACKGROUND
        if (frames %60 == 0) contAnimBG = contAnimBG == 1 ? 0 : ++contAnimBG;

        //PLAYER
        if (frames %10 == 0) contAnim = contAnim == 2 ? 0 : ++contAnim;
        
        g.drawImage(backgroundImage[contAnimBG], 0, 0, null);
        
        if (playerX == 0 && playerY == 0) g.drawImage(playerImage[TOP_PONTE][contAnim], 110, 75, null);
        else if (playerX == 0 && playerY == 1) g.drawImage(playerImage[BOTTOM_PONTE][contAnim], 110, 75, null);
        else if (playerX == 1 && playerY == 0) g.drawImage(playerImage[TOP_PONTE][contAnim], 110 + 100, 75, -100, 120, null);
        else if (playerX == 1 && playerY == 1) g.drawImage(playerImage[BOTTOM_PONTE][contAnim], 110 + 100, 75, -100, 120, null);
        
        g.setColor(new Color(255, 255, 255, 70));
        //PARTE DE CIMA
        for(int i = 0; i < positionNPC[0].length ; i++){ 
            //g.drawRect(33 + 25*i, 50, 25, 25);
                               
            if (positionNPC[0][i] != 0){
                //DESENHO DO NPC
                if(errou == 1 && i == 3){
                    if(errouAux % 2 == 0)
                        g.drawImage(npcImage[positionNPC[0][i]][contAnimNPC], 33 + 25*i +contAnimNPC*6, 51, null);
                }
                else if(errou == 2 && i == 6){
                    if(errouAux % 2 == 0)
                        g.drawImage(npcImage[positionNPC[0][i]][contAnimNPC], 33 + 25*i +contAnimNPC*6, 51, null);
                }
                else
                    g.drawImage(npcImage[positionNPC[0][i]][contAnimNPC], 33 + 25*i +contAnimNPC*6, 51, null);
            }
                
        }
        
        //PARTE DE BAIXO
        for(int i = 0; i < positionNPC[1].length ; i++){   
            //g.drawRect(258 - 25*i, 145, 25, 25); 
            
            if (positionNPC[1][i] != 0){
                //DESENHO DO NPC 
                if(errou == 3 && i == 3){
                    if(errouAux % 2 == 0)
                        g.drawImage(npcImage[positionNPC[1][i]][contAnimNPC], 258 - 25*i +25 -contAnimNPC*6, 145, -25, 25, null);
                } 
                else if(errou == 4 && i == 6){
                    if(errouAux % 2 == 0)
                        g.drawImage(npcImage[positionNPC[1][i]][contAnimNPC], 258 - 25*i +25 -contAnimNPC*6, 145, -25, 25, null);
                }
                else
                    g.drawImage(npcImage[positionNPC[1][i]][contAnimNPC], 258 - 25*i +25 -contAnimNPC*6, 145, -25, 25, null);
            }
        }
        
        //DEBUG
        g.setColor(Color.red);
        g.drawString(String.valueOf(tick), 290, 15);
        g.drawString(String.valueOf(wave), 290, 30);
        g.drawString(String.valueOf(score), 5, 15);
        g.drawString(String.valueOf(HIT_PONTE), 5, 30);
        //g.drawString(String.valueOf(ticksTotais), 290, 45);
        
    }
    
    public void miss(){
        HIT_PONTE--;
        positionNPC = new int[2][10];
        canSpawn = new int[2][10];
        tickAux = 0;
        frames = 0;
        
        if(HIT_PONTE == 0){
            gsm.setGameOver(true);
        }
    }
    
    public void comandarEntrada(){
        if (Keys.onlyPressed(Keys.UP)) playerY = 0;
        else if(Keys.onlyPressed(Keys.DOWN)) playerY = 1;
        
        if(Keys.onlyPressed(Keys.LEFT)) playerX = 0;
        else if(Keys.onlyPressed(Keys.RIGHT)) playerX = 1;  
        
        if(Keys.onlyPressed(Keys.ENTER))
            gsm.setGameOver(true);
    }
    
}
