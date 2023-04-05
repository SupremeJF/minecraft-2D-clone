package items;
import affichage.Sprite;

public class Bucket extends Item {

    public Bucket(int x, int y) {
        super(false, false, 1, x, y, 1);
    }

    public Bucket() {
        this(0, 0);
    }

    @Override
    public Item newItem(int x, int y) {
        return new Bucket(x, y);
    }

    @Override
    public Sprite getSprite() {
        return Sprite.coal;
    }
    
}
