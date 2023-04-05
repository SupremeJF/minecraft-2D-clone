package unites;
import main.Keyboard;
import utils.Vecteur;

public class ControleurAleatoire implements Controleur {

    public ControleurAleatoire() {
    }

    @Override
    public Vecteur getDeplacement(Keyboard keyboard) {
        return new Vecteur(Math.random()*2-1, Math.random()*2-1);
    }
}
