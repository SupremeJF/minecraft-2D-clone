package affichage;

import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.lang.Math;

import blocks.Block;

public class Sprite {
    
    private BufferedImage[] images;

    public static final Sprite grass = new Sprite(SpriteSheet.blocks, 0, 0);
    public static final Sprite dirt = new Sprite(SpriteSheet.blocks, 2, 0);
    public static final Sprite stone = new Sprite(SpriteSheet.blocks, 1, 0);
    public static final Sprite iron = new Sprite(SpriteSheet.blocks,3,0);
    public static final Sprite coal = new Sprite(SpriteSheet.blocks,4,0);
    public static final Sprite snow = new Sprite(SpriteSheet.blocks,1,1);
    public static final Sprite diamond = new Sprite(SpriteSheet.blocks,0,1);
    public static final Sprite bucket = new Sprite(SpriteSheet.blocks,1,1);
    public static final Sprite air = new Sprite(SpriteSheet.blocks,4,0);
    public static final Sprite wood = new Sprite(SpriteSheet.blocks,2 , 1);
    public static final Sprite leaf = new Sprite(SpriteSheet.blocks,3 , 1);

    public Sprite(SpriteSheet spriteSheet, int x, int y, int width, int height, int count) {
        if (spriteSheet == null) {
            System.out.println("Erreur sprite sheet null");
        }
        images = new BufferedImage[count];
        for (int i = 0; i < count; i++) {
            int w = width + SpriteSheet.SPRITE_SEPARATION;
            int h = height + SpriteSheet.SPRITE_SEPARATION;
            images[i] = spriteSheet.getImage().getSubimage((x + i) * w, y * h, width, height);
        }
    }

    public Sprite(SpriteSheet sheet, int x, int y) {
        this(sheet, x, y, Block.TAILLE, Block.TAILLE, 1);
    }

    public Sprite(BufferedImage[] images) {
        this.images = images;
    }

    public BufferedImage getImage() {
        return images[0];
    }

    public static BufferedImage scaleImage(BufferedImage image, float scale) {
        int width = (int) Math.ceil(image.getWidth() * scale);
        int height = (int) Math.ceil(image.getHeight() * scale);
        BufferedImage scaled = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        AffineTransform scaleInstance = AffineTransform.getScaleInstance(scale, scale);
        AffineTransformOp scaleOp = new AffineTransformOp(scaleInstance, AffineTransformOp.TYPE_BILINEAR);
    
        scaleOp.filter(image, scaled);
        return scaled;
    }

}
