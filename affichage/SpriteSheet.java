package affichage;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class SpriteSheet {
    
    public static final int SPRITE_SEPARATION = 1;

    public static SpriteSheet blocks = new SpriteSheet("src/affichage/blocks4.png");
    private BufferedImage image;

    public SpriteSheet(String path) {
        loadImage(path);
    }

    private void loadImage(String path) {
        try {
            image = ImageIO.read(new File(path));
        } catch (Exception e) {
            System.out.println("Error loading spriteSheet : il faut surement rajouter /src au lieu de src dans SpriteSheet.java");
        }
    }

    public BufferedImage getImage() {
        return image;
    }

}
