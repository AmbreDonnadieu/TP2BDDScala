package Combat;

public class EquipementCac extends  Equipement {

    public EquipementCac(int mod, int modDmg){
        super(mod, modDmg);
    }

    //Retourne le lancer de précision
    public int toucherCible(double distance){
        int lancer = touche.lancer();
        if(lancer == 20){
            return(-2);
        }
        if(lancer == 1){
            return(-1);
        }
        return(touche.lancer() + modificateur);
    }

    //retourne le lancer de dégâts
    public double lancerDegats(int nbDes) {

        double dgt = 0.0;
        for(int i = 0; i<nbDes; i++){
            dgt+=degats.lancer();
        }

        return (dgt+modificateurDamage);
    }
}
