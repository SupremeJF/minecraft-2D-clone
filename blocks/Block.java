package blocks;

import affichage.Affichable;
import hitbox.*;
import items.Item;
import main.Niveau;
import utils.*;

public abstract class Block extends Item implements Affichable, Collisionnable {
    
    public static final int TAILLE = 16;
    private int ID;
    private rectanglehitbox H;
    public final boolean collide;
    private final static int maxStack = 64;
    public boolean debug = false;

    public Block(int x, int y, boolean collision, int ID) {
        super(true, true, maxStack, x, y, 1);
        this.ID = ID;
        this.H = new rectanglehitbox(1, 1, x, y);  
        this.collide = collision;
    }

    public Block(int x, int y, boolean collision){
        this(x, y, collision,0);
    }

    public Block(int x, int y) {
        this(x,y,true,0);
    }

    public Block(int x, int y, int ID) {
        this(x,y,true,ID);
    }

    public int getX(){
        Point pos = this.H.getPosition();
        return (int) pos.getX();
    }

    public int getY(){
        Point pos = this.H.getPosition();
        return (int) pos.getY();
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public rectanglehitbox getHitbox(){
        return H;
    }

    @Override
    public void afficher(){
        Niveau.afficheur.afficher(this.getSprite(), this.getX()-0.5, this.getY()-0.5);
    }

    public int getBlockID(){
        return this.ID;
    }

    /**
     * Il renvoie un block basé sur l'ID
     * 
     * @param x La position x du block
     * @param y La position y du block
     * @param ID L'ID du block.
     * @return Un objet block.
     */
    public static Block blockByID(int x, int y,int ID){
        switch(ID){
            case 0:
                return new Air(x, y);
            case 1:
                return new Grass(x, y);
            case 2:
                return new Stone(x, y);
            case 3:
                return new Dirt(x, y);
            case 4:
                return new Iron(x, y);
            case 5:
                return new Coal(x, y);
            case 6:
                return new Diamond(x, y);
            case 7:
                return new Snow(x, y);
            
            default:
                return new Air(x, y);
        }
    }
}
