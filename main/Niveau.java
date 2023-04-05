package main;

import affichage.*;
import blocks.BlocksManager;
import items.InventoryManager;
import unites.UnitesManager;
import unites.PlayerInteraction;
import unites.Joueur;
import java.util.ArrayList;
import utils.Point;
import blocks.Chunk;

public class Niveau implements Affichable {

    private ArrayList<Chunk> chunks = new ArrayList<Chunk>(100); //chunks courrant
    private ArrayList<Chunk> nChunks = new ArrayList<Chunk>(100); //chunks à charger
    private ArrayList<Chunk> aChunks = new ArrayList<Chunk>(100); //chunks à décharger

    public static final int CHUNKSIZE = 8;
    private static final int DISTANCE_CHARGE = 4;
    private static final int DISTANCE_SAVE = 6;
    public Keyboard keyboard;
    public Mouse mouse;
    public static InputManager inputManager;

    /* l'afficheur utilisé pour le jeu */
    public static Afficheur afficheur = null;

    /* la caméra utilisée pour le jeu */
    public CameraManager cameraManager = null;

    private BlocksManager blocksManager;
    private UnitesManager unitesManager;
    private InventoryManager inventoryManager;
    private PlayerInteraction playerInteraction;

    /* le joueur du jeu */
    private Joueur joueur;

    public Niveau(Keyboard keyboard, Mouse mouse, Afficheur a) {

        // Déclarations des objets du niveau
        blocksManager = new BlocksManager(); 
        unitesManager = new UnitesManager(blocksManager);
        inventoryManager = new InventoryManager(this);
        playerInteraction = new PlayerInteraction(this);

        // Déclaration de la caméra
        this.cameraManager = new CameraManager();

        // Déclaration des entrées clavier et souris
        this.keyboard = keyboard;
        this.mouse = mouse;
        inputManager = new InputManager(mouse, keyboard);
        
        // Déclaration de l'afficheur
        Niveau.afficheur = a;

        joueur = unitesManager.getJoueur();
    }

    /* Met à jour la logique interne de tout les composants du niveau */
    public void maj() {
            
        // Mise à jour des entrées clavier
        keyboard.maj();
        mouse.maj();

        // Mise à jour du niveau
        unitesManager.maj(this, keyboard);  
        chunksMaj();
        blocksManager.cgChunk(nChunks, aChunks);
        inventoryManager.maj(this);
        playerInteraction.maj(this);
        cameraManager.maj(joueur.getPosition());
    }

    /* Affichage de tout les composants du niveau */
    public void afficher() {
        
        // Affichage du niveau
        blocksManager.afficher();
        unitesManager.afficher();
        inventoryManager.afficher();
        playerInteraction.afficher();
    }

    /* Getters */
    public BlocksManager getBlocksManager() {
        return blocksManager;
    }

    public UnitesManager getUnitesManager() {
        return unitesManager;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public PlayerInteraction getPlayerInteraction() {
        return playerInteraction;
    }

    private void chunksMaj(){
        Point posj = joueur.getPosition();
        this.chunks.removeAll(aChunks);
        this.chunks.addAll(nChunks);       
        int chunkCX = (int) posj.getX()/CHUNKSIZE;
        int chunkCY = (int) posj.getY()/CHUNKSIZE;
        aChunks.clear();
        nChunks.clear();
        //System.out.println("tailles de chunks " + chunks.size());
        ArrayList<Chunk> tchunks = new ArrayList<Chunk>(400);

        for(int i = -DISTANCE_SAVE; i <= DISTANCE_SAVE; i++){
            for(int j = -DISTANCE_SAVE; j<= DISTANCE_SAVE; j++){
                Chunk ct = new Chunk(chunkCX + i, chunkCY + j);
                if(chunks.contains(ct)) {
                    tchunks.add(ct);
                }
            }
        }

        //System.out.println("Chunk à charger");
        for(int i = -DISTANCE_CHARGE; i <= DISTANCE_CHARGE; i++){
            for(int j = -DISTANCE_CHARGE; j<= DISTANCE_CHARGE; j++){
                Chunk ct = new Chunk(chunkCX + i, chunkCY + j);
                if(!chunks.contains(ct)) {
                    nChunks.add(ct);
                    //System.out.println("Chunk à charger : " + ct[0] + " " + ct[1]);
                }
            }
        }

        chunks.removeAll(tchunks); //on enleve de chunk tout les chunks en commun avec ceux à charger, il ne reste donc que les anciens chunks, qui ne sont pas à charger, donc qui sont à décharger
        aChunks = new ArrayList<Chunk>(chunks); //c'est donc achunks
        //System.out.println("tailles de achunks " + aChunks.size());
        //System.out.println("tailles de nchunks " + nChunks.size());
        chunks.addAll(tchunks); //on remet chunks dans son état original (pourquoi pas)
    }
}
