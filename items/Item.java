package items;

import affichage.*;

public abstract class Item {

    private boolean estPosable;
    private boolean estStackable;
    private int maxStack;
    protected int x;
    protected int y;
    protected int dammage;

    public static final int TAILLE = 32;

    public Item(boolean estPosable, boolean estStackable, int maxStack, int x, int y, int dammage) {
        this.estPosable = estPosable;
        this.estStackable = estStackable;
        this.maxStack = maxStack;
        this.x = x;
        this.y = y;
        this.dammage = dammage;
    }

    public boolean estPosable() {
        return estPosable;
    }

    public boolean estStackable() {
        return estStackable;
    }

    public int getMaxStack() {
        return maxStack;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDammage() {
        return dammage;
    }

    public abstract Sprite getSprite();

    public abstract Item newItem(int x, int y);
}
