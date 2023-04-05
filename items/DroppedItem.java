package items;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public abstract class DroppedItem extends Item {
    
    public DroppedItem(boolean estPosable, boolean estStackable, int maxStack, int x, int y) {
        super(false, true, maxStack, x, y);
        //TODO Auto-generated constructor stub
    }

    public static final int TAILLE = 8;

    public abstract void affiche(Graphics2D g);

    public abstract Item newItem(int x, int y);

    public abstract BufferedImage getImage();

}
