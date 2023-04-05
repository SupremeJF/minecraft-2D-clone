package unites;

import java.awt.Graphics2D;
import java.awt.Point;

import affichage.Affichable;
import blocks.*;
import items.InventoryItem;
import items.Item;
import items.PlayerInventory;
import main.Niveau;
import sound.SoundEffect;

import java.awt.Color;
import java.awt.BasicStroke;

public class PlayerInteraction implements Affichable {

    public Point blockCourant;
    private BlocksManager blocksManager;
    private PlayerInventory playerInventory;
    private UnitesManager unitesManager;
    private Unite uniteCourante;
    private int PORTEE_JOUEUR = 4;

    public PlayerInteraction(Niveau niveau) {
        this.blocksManager = niveau.getBlocksManager();
        this.playerInventory = niveau.getInventoryManager().getPlayerInventory();
        this.unitesManager = niveau.getUnitesManager();
    }
    

    public void maj(Niveau niveau) {
        /* récupérer la position absolue de la souris en blocs */
        utils.Point mousePos = Niveau.inputManager.getMousePosition();

        /* décalage coin/centre */
        int posX = (int) mousePos.getX();
        int posY = (int) mousePos.getY();
        blockCourant = new Point(posX, posY);
        uniteCourante = unitesManager.getUnite(blockCourant.x, blockCourant.y);
        
        if (niveau.mouse.isRightDown()) {
            Joueur joueur =unitesManager.getJoueur();
            double Xjoueur = joueur.getPosition().getX();
            double Yjoueur =joueur.getPosition().getY();

            if (blockCourant.distance(Xjoueur,Yjoueur) < PORTEE_JOUEUR) {
                if (this.blocksManager.getBlock(blockCourant.x, blockCourant.y) == null) {
                    InventoryItem inventoryItem = this.playerInventory.getInventoryItem(this.playerInventory.getCurrentIndex());
                    if (inventoryItem != null && inventoryItem.getItem().estPosable()) {
                        Block block = (Block) inventoryItem.getItem().newItem(blockCourant.x, blockCourant.y);
                        boolean result = this.blocksManager.placer(block, blockCourant.x, blockCourant.y); // transtypage pourrait poser porblème
                        if (result) {
                            this.playerInventory.decrementQuantity(this.playerInventory.getIndex(inventoryItem));
                            SoundEffect.play("place");
                        }
                    }
                }
            }
        }

        if (niveau.mouse.isLeftDown()) {

            if (uniteCourante != null) {
                if (!uniteCourante.getClass().getSimpleName().equals("Joueur")) {
                    Item item = this.playerInventory.getInventoryItem(this.playerInventory.getCurrentIndex()).getItem();
                    int dammage;
                    if (item == null) {
                        dammage = 1;
                    }
                    else {
                        dammage = item.getDammage();
                    }
                    uniteCourante.removeHealth(dammage);
                    SoundEffect.play("hit");
                }
            }
            else if (this.blocksManager.getBlock(blockCourant.x, blockCourant.y) != null) {
                Block block = this.blocksManager.getBlock(blockCourant.x, blockCourant.y);
                this.blocksManager.retirer(block.getX(), block.getY());
                this.playerInventory.addInventoryItem(new InventoryItem(block, 1));
                SoundEffect.play("dirt");
            }
        }
            
    }

    public void afficher() {

        Graphics2D g = Niveau.afficheur.g;

        if (uniteCourante != null) {
            g.setStroke(new BasicStroke(2));
            Niveau.afficheur.drawRect(blockCourant.x - 0.5,
            blockCourant.y - 0.5,
            1,
            1,
            Color.RED);
        }

        else if (blockCourant != null) {
            g.setStroke(new BasicStroke(2));
            Niveau.afficheur.drawRect(blockCourant.x - 0.5,
            blockCourant.y - 0.5,
            1,
            1,
            Color.BLACK);
        }
    }
}