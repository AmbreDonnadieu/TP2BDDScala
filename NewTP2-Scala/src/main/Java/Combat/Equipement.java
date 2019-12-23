package Combat;

public abstract class Equipement {

    public double portee;

    public Des degats;

    public Des touche;

    //modificateur de précision
    public int modificateur;

    //modificateur de dégâts
    public int modificateurDamage;

    //creer dés pour chance de toucher

    public Equipement(int mod, int modDmg){
        modificateur = mod;
        modificateurDamage = modDmg;
        touche = new Des(20);
    }

    public abstract int toucherCible(double distance);

    public abstract double lancerDegats(int nbDes);
}
