package save;

import java.util.ArrayList;
import blocks.Block;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import main.Niveau;

public class BlocksSave implements Save {

    public static void main (String[] args) {
        BlocksSave save = new BlocksSave();
    }

    public ArrayList<Block[][]> chunks;
    public static String fileDir = "..\\game\\saves\\world";
    private static File savefile = new File(fileDir + "\\chunks.s");
    private static File annuairefile = new File(fileDir + "\\annuaire.s");
    private RandomAccessFile saveaccess;
    private RandomAccessFile annuaireaccess;

    public BlocksSave(ArrayList<Block[][]> nchunks) {
        this.chunks = nchunks;
        constructorFacto();
    }

    public BlocksSave(Block[][] chunk) {
        ArrayList<Block[][]> arlb = new ArrayList<Block[][]>(1);
        arlb.add(chunk);
        this.chunks = arlb;
        constructorFacto();
    }

    public BlocksSave(){
        ArrayList<Block[][]> arlb = new ArrayList<Block[][]>(10);
        this.chunks = arlb;
        constructorFacto();
        
    }

    
    /**
     * Crée les dossiers et les fichiers s'ils n'existent pas, puis crée les objets RandomAccessFile qui seront
     * utilisés pour lire et écrire dans les fichiers
     */
    private void constructorFacto(){
        if(savefile.exists() && annuairefile.exists()){
            try {
                this.saveaccess = new RandomAccessFile(savefile, "rw");
                this.annuaireaccess = new RandomAccessFile(annuairefile, "rw");
            } catch (FileNotFoundException e) {
                System.out.println("Problème d'instanciation du flux de sauvegarde pour BlocksSave");
            }
        }
        else{
            try{
                File t = new File(fileDir);
                t.mkdirs();
                savefile.createNewFile();
                annuairefile.createNewFile();
                try {
                    this.saveaccess = new RandomAccessFile(savefile, "rw");
                    this.annuaireaccess = new RandomAccessFile(annuairefile, "rw");
                } catch (FileNotFoundException e) {
                    System.out.println("Problème d'instanciation du flux de sauvegarde pour BlocksSave");
                }
            }
            catch (IOException e){
                System.out.println("Erreur création des fichiers de save de block : " + e);
            }
        }
    }


  /**
   * Ecrit les blocs d'un chunk dans le fichier de sauvegarde
   * 
   * @param c le chunk à sauver
   * @param blockref Le bloc de référence pour le chunk.
   */
    private void ecritureChunk(Block[][] c, Block blockref) {
        for (int k = 0; k < Niveau.CHUNKSIZE; k++) {
            for (int j = 0; j < Niveau.CHUNKSIZE; j++) {
                Block bc = c[k][j];
                if (bc != null) {
                    try {
                        saveaccess.writeByte(bc.getBlockID());
                        saveaccess.writeByte(bc.getX() - blockref.getX());
                        saveaccess.writeByte(bc.getY() - blockref.getY());
                    } catch (IOException e) {
                        System.out.println("Erreur enregistrement du block : " + bc);
                    }
                } else {
                    try {
                        saveaccess.writeByte(0);
                        saveaccess.writeByte(0);
                        saveaccess.writeByte(0);
                    } catch (IOException e) {
                        System.out.println("Erreur enregistrement du block : null");
                    }
                }
            }
        }
    }

    private void ecritureChunkVide(){
        try {
            annuaireaccess.writeChar(0);
            annuaireaccess.writeChar(0);
            annuaireaccess.writeInt((int) savefile.length());
        }
        catch (IOException e){
            System.out.println("Erreur écriture du chunk dans l'annuaire");
        }
        for (int k = 0; k < Niveau.CHUNKSIZE; k++) {
            for (int j = 0; j < Niveau.CHUNKSIZE; j++) {
                try {
                    saveaccess.writeByte(0);
                    saveaccess.writeByte(0);
                    saveaccess.writeByte(0);
                } catch (IOException e) {
                    System.out.println("Erreur enregistrement du block : null");
                }
            }
        }
    }

    /**
     * Ecrit sur le disque la sauvegarde des chunks présent dans l'objet save.
     */
    public void saveToFile(){
        if (!savefile.exists()){
            try{
                savefile.createNewFile();
                annuairefile.createNewFile();
            }
            catch (IOException e){
                System.out.println("Erreur création des fichiers de save de block");
            }
        }
        if(savefile.length() == 0){
            int nsize = chunks.size()*8;
            try {
                annuaireaccess.writeInt(nsize);
            }
            catch (IOException e){
                System.out.println("Erreur écriture de la taille de l'annuaire");
            }
            for(int i=0; i<chunks.size(); i++){
                Block binst = chunks.get(i)[0][0];
                if (binst != null){
                    int cx = (int) binst.getX()/Niveau.CHUNKSIZE;
                    int cy = (int) binst.getY()/Niveau.CHUNKSIZE;
                    try {
                        annuaireaccess.writeChar(cx);
                        annuaireaccess.writeChar(cy);
                        annuaireaccess.writeInt((int) savefile.length());
                    }
                    catch (IOException e){
                        System.out.println("Erreur écriture du chunk dans l'annuaire");
                    }
                    ecritureChunk(chunks.get(i), binst);
                }
                else{
                    ecritureChunkVide();
                }
            }
        }
        else{
            try{
                annuaireaccess.seek(0);
                int annuT = annuaireaccess.readInt();
                //chercher les chunks déjà présent pour les modifier et ne pas rajouter une nouvelle occurence de ceux çi (et donc ne pas augmenter annuT pour ceux ci)
                int[][] annu = new int[annuT][3];
                for(int i=0; i<annuT; i++){
                    annu[i] = new int[] {annuaireaccess.readChar(),  annuaireaccess.readChar(),annuaireaccess.readInt()};
                }
                for(int i=0; i<chunks.size(); i++){
                    Block binst = chunks.get(i)[0][0];
                    int cx = (int) binst.getX()/Niveau.CHUNKSIZE;
                    int cy = (int) binst.getY()/Niveau.CHUNKSIZE;
                    boolean found = false;
                    for(int j=0; j<annuT; j++){
                        if(cx == annu[j][0] && cy == annu[j][1]){
                            found = true;
                            annuaireaccess.seek(annu[j][2]);
                            ecritureChunk(chunks.get(i), binst);
                        }
                    }
                    if(!found){
                        annuaireaccess.seek(annuaireaccess.length());
                        annuaireaccess.writeChar(cx);
                        annuaireaccess.writeChar(cy);
                        annuaireaccess.writeInt((int) savefile.length());
                        ecritureChunk(chunks.get(i), binst);
                    }
                }
            }
            catch(IOException e){
                System.out.println("Erreur écriture de l'annuaire ou de l'écriture des chunks");
            }
        }
    }

    /**
     * Lit le fichier contenant les positions des chunks dans le fichier de sauvegarde, puis lit
     * le fichier de sauvegarde et reconstitue les chunks correspondants.
     */
    public void updFromFile(){
        if (savefile.exists() && savefile.length() != 0){
            System.out.println(savefile.length());
            try{
                annuaireaccess.seek(0);
                int annuT = annuaireaccess.readInt();
                for(int i=0; i<annuT; i++){
                    int cx = annuaireaccess.readChar();
                    int cy = annuaireaccess.readChar();
                    int pos = annuaireaccess.readInt();
                    Block[][] chunk = new Block[Niveau.CHUNKSIZE][Niveau.CHUNKSIZE];
                    try{
                        saveaccess.seek(pos);
                        for (int l = 0; l<Math.pow(Niveau.CHUNKSIZE,2); l++){
                            int id = saveaccess.readByte();
                            int x = saveaccess.readByte();
                            int y = saveaccess.readByte();
                            Block b = Block.blockByID(x + cx, y + cy, id);
                            chunk[x][y] = b;
                            chunks.add(chunk);
                        }
                    }
                    catch(IOException e){
                        System.out.println("Erreur lecture du chunk dans la save");
                    }
                }
            }
            catch(IOException e){
                System.out.println("Erreur lecture de l'annuaire 1");
            }
        }
    }

    /**
     * Lit le fichier contenant les positions des chunks dans le fichier de sauvegarde, puis lit
     * le fichier de sauvegarde et reconstitue le chunks demandé.
     * 
     * @param param les coordonnées du morceau
     */
    public void updFromFile(double[] param){
        if (savefile.exists() && savefile.length() != 0){
            try{
                annuaireaccess.seek(0);
                int annuT = annuaireaccess.readInt();
                for(int i=0; i<annuT; i++){
                    int cx = annuaireaccess.readChar();
                    int cy = annuaireaccess.readChar();
                    int pos = annuaireaccess.readInt();
                    Block[][] chunk = new Block[Niveau.CHUNKSIZE][Niveau.CHUNKSIZE];
                    if (cx == param[0] && cy == param[1]){
                        try{
                            saveaccess.seek(pos);
                            for (int l = 0; l<Math.pow(Niveau.CHUNKSIZE,2); l++){
                                int id = saveaccess.readByte();
                                int x = saveaccess.readByte();
                                int y = saveaccess.readByte();
                                Block b = Block.blockByID(x + cx, y + cy, id);
                                chunk[x][y] = b;
                            }
                        }
                        catch(IOException e){
                            System.out.println("Erreur lecture du chunk dans la save");
                        }
                        finally{
                            chunks.add(chunk);
                        }
                    }
                }
            }
            catch(IOException e){
                System.out.println("Erreur lecture de l'annuaire 2");
            }
        }
    }
}