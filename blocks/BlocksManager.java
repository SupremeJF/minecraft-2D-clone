package blocks;

import affichage.Affichable;
import utils.*;
import affichage.CameraManager;
import affichage.Camera;
import java.util.ArrayList;
import main.Niveau;
import save.*;
import save.Saveable;

public class BlocksManager implements Affichable, Saveable {
    public ArrayList<Chunk> generatedChunks = new ArrayList<Chunk>(100);
    public PlanMap<Block> blocks;

    public BlocksManager(){
        blocks = new PlanMap<>();
        generateWorld();
        generatedChunks.add(new Chunk(0,0));
        generatedChunks.add(new Chunk(0,1));
        generatedChunks.add(new Chunk(0,2));
        generatedChunks.add(new Chunk(0,3));
        generatedChunks.add(new Chunk(1,1));
        generatedChunks.add(new Chunk(1,2));
        generatedChunks.add(new Chunk(1,3));
        generatedChunks.add(new Chunk(2,1));
        generatedChunks.add(new Chunk(2,2));
        generatedChunks.add(new Chunk(2,3));
        generatedChunks.add(new Chunk(3,1));
        generatedChunks.add(new Chunk(3,2));
        generatedChunks.add(new Chunk(3,3));
        
        generatedChunks.add(new Chunk(0,0));
        generatedChunks.add(new Chunk(0,-1));
        generatedChunks.add(new Chunk(0,-2));
        generatedChunks.add(new Chunk(0,-3));
        generatedChunks.add(new Chunk(-1,-1));
        generatedChunks.add(new Chunk(-1,-2));
        generatedChunks.add(new Chunk(-1,-3));
        generatedChunks.add(new Chunk(-2,-1));
        generatedChunks.add(new Chunk(-2,-2));
        generatedChunks.add(new Chunk(-2,-3));
        generatedChunks.add(new Chunk(-3,-1));
        generatedChunks.add(new Chunk(-3,-2));
        generatedChunks.add(new Chunk(-3,-3));

    }
    
    public void afficher() {
        Camera camera = CameraManager.getCamera();
        for (int i = (int) camera.getX(); i <= camera.getX()+Camera.LARGEUR+1; i++) {
            for (int j = (int) camera.getY(); j <= camera.getY()+Camera.HAUTEUR+1; j++) {
                if (blocks.get(i,j) != null) {
                    blocks.get(i,j).afficher();
                    //blocks[i][j].debug = false;
                }
            }
        }
    }

    private void generateWorld(){
        for (int y = 0; y < Camera.HAUTEUR; y++) {
            for (int x = 0; x < Camera.LARGEUR; x++) {
                boolean placable = placable(x,y);
                if (placable){
                    blocks.put(x, y, a_placer(x,y));
                }              
            }
        }
    }

    private Block a_placer(int x, int y) {
        int random = (int) (Math.random()*200);
        //Cas sans blocs au dessus
        if (blocks.get(x, y-1) ==null){
            if (random<35){
                genererArbre(x,y-1);
            }
            if(y<14){
                return new Snow(x,y);
            }
            else{  
                return new Grass(x, y);
            }
           
        }
        //Couche moyenne charbon + fer 
        else if (y > 21 && y<=30) {
            if (random<170){
                return new Stone(x, y);
            }          
            else if(random < 185){
                return new Iron(x,y);
            }
            else{
                return new Coal(x,y);
            }
        } 

        //couche basse  fer +diamand
        else if (y>30 && y<=46){
            if (random<180){
                return new Stone(x, y);
            }          
            else if(random < 190){
                return new Diamond(x,y);
            }
            else{
                return new Iron(x,y);
            }

        }
        else if (y >=1) {
            try {
                if(blocks.get(x, y-5) ==null){
                    return new Dirt(x, y);
                }
                else{
                    if(random<180){
                        return new Stone(x,y);
                    }
                    else{
                        return new Coal(x,y);
                    }
                }
                
            } catch (ArrayIndexOutOfBoundsException e) {
                return new Dirt(x,y);
            }
                
        }
        
        return null;
    }


    private void genererArbre(int x, int y) {
        blocks.put(x, y, new Wood(x,y));
        blocks.put(x, y-1, new Wood(x,y-1));
        blocks.put(x, y-2, new Wood(x,y-2));
        blocks.put(x,y-3, new Leaf(x,y-3));
        blocks.put(x,y-4,new Leaf(x,y-4));
        if ( blocks.get(x-1, y-3)==null){
            blocks.put(x-1,y-3,new Leaf(x-1,y-3));
        }
        if (blocks.get(x+1,y-2)==null){
            blocks.put(x+1,y-3,new Leaf(x+1,y-3));
        }

    }

    private boolean placable(int x, int y) {
        //long seed = 123456;
        //Random randomGenerator = new Random(seed);
        //int random = randomGenerator.nextInt() *200 ;
        
        int random = (int) (Math.random()*200);
        boolean placable = false;
        try{
            if (y>=23 || blocks.get(x, y-1)!= null){
                placable  = true;
            }
            
            else if (blocks.get(x-1, y-1)!= null|| blocks.get(x+1, y-1) !=null ){
                   placable=random <150 ; 
            }
            else if (y <=17 && y>5){
                   placable = random<2;
            }
            else if (y<23 && y>15){
                    placable=random<5;
            }

        }
        catch ( ArrayIndexOutOfBoundsException e) {
            placable = false;
            
        }
        return placable; 
    }

                

    public Block getBlock(int x, int y) {
        return blocks.get(x, y);
    }

    public Boolean placer(Block block, int x, int y) {
        if (blocks.get(x, y) == null){
            //condition pour poser un bloc
            if((blocks.get(x,y+1) != null || blocks.get( x+1,y)!=null || blocks.get(x-1,y)!= null) || blocks.get(x,y-1)!= null) {
                blocks.put(x, y, block);
                return true;
            }
        }
        return false;
    }

    public Block retirer(int x, int y) {
        Block block = getBlock(x, y);
        blocks.remove(x, y);
        return block;
    }

    public void cgChunk(ArrayList<Chunk> nChunks, ArrayList<Chunk> aChunks){
        for(int i = 0; i<nChunks.size(); i++){ //nChunks étant les chunk à charger ou générer 
            //System.out.println("nChunks : " + nChunks.get(i)[0] + " " + nChunks.get(i)[1]);
            if (generatedChunks.contains(nChunks.get(i))) {
            //if(true){ 
                System.out.println("TEEEEZST");
                BlocksSave cs = new BlocksSave();
                
                cs.updFromFile(nChunks.get(i).toDouble());
                this.load(cs);
            }
            else{
                //generateChunk(nChunks.get(i));
            }
        }
        for(int i = 0; i<aChunks.size(); i++){
            BlocksSave as = (BlocksSave) this.save(aChunks.get(i).toDouble());
            as.saveToFile();
        }
    }

    public Save save() {
        return null;
    }

    public void load(Save s) {
        if (((BlocksSave) s).chunks == null) {
            return;
        }
        else{
            for (Block[][] chunk : ((BlocksSave) s).chunks) {
                for (int i = 0; i < Niveau.CHUNKSIZE; i++) {
                    for (int j = 0; j < Niveau.CHUNKSIZE; j++) {
                        blocks.put(i, j, chunk[i][j]);
                    }
                }
                //generatedChunks.add(chunk);
            }
        }
    }

    /* param : coordonnées du chunk à sauvegarder */
    public Save save(double[] param) {
        return new BlocksSave(extractChunk((int) param[0],(int) param[1]));
    }

    /* Les blocs du chunks d'indice x, y 
    retire aussi les blocs du mapping de blocs */
    public Block[][] extractChunk(int x, int y) {
        Block[][] chunk = new Block[Niveau.CHUNKSIZE][Niveau.CHUNKSIZE];
        for (int i = 0; i < Niveau.CHUNKSIZE; i++) {
            for (int j = 0; j < Niveau.CHUNKSIZE; j++) {
                int xBlock = x*Niveau.CHUNKSIZE + i;
                int yBlock = y*Niveau.CHUNKSIZE + j;
                chunk[i][j] = blocks.get(xBlock, yBlock);
                if (blocks.get(xBlock, yBlock) != null){
                    blocks.remove(xBlock, yBlock);
                }
            }
        }
        return chunk;
    }

}