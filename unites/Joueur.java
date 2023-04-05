package unites;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import affichage.CameraManager;
import main.Niveau;
import utils.Point;
import affichage.Camera;

public class Joueur extends Unite {

    private final static double largeur = 0.99;
    private final static double hauteur = 1.99;
    private ControleurJoueur controleur;

    public Joueur(double  x, double y) {
        super(x, y, largeur, hauteur, new ControleurJoueur());
        super.getHitbox().onHit(new degat());
        this.controleur = (ControleurJoueur) super.getControleur();
        this.controleur.setJoueur(this);
    }

    private class degat implements ActionListener {    
        public void actionPerformed(ActionEvent e) {
            int remaining = Joueur.this.getHealth() - (int) e.getSource();
            if (remaining <= 0) {
                Joueur.this.setAlive(false);
            }
            else {
                Joueur.this.setHealth(remaining);
            }
        }
    }

    @Override
    public void afficher (){
        Point pos = this.H.getPosition();
        Camera camera = CameraManager.getCamera();
        double d_cam_x = (pos.getX()-camera.getX())*16;
        double d_cam_Y = (pos.getY()-camera.getY())*16;
        String pathName;

        if (this.getHealth() > 0) {
            if (this.isLookingRight()) {
                pathName = "src/unites/joueur_droite.png";
            }
            else {
                pathName = "src/unites/joueur_gauche.png";
            }
        }
        else {
            pathName = "src/unites/joueur_mort.png";
        }
        try {
            Niveau.afficheur.g.drawImage(ImageIO.read(new File(pathName)),(int) (d_cam_x -8), (int)(d_cam_Y -16),null);
        } catch (IOException e) {
            System.out.println("Error loading playerImage : il faut surement rajouter /unites au lieu de unites dans Joueur.java");
        }
    }

}


