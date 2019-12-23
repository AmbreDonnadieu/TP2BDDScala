package Combat;

public class EquipementDistance extends Equipement {

    double porteeMax;

    public EquipementDistance(double maxRange, int mod, int modDmg){
        super(mod, modDmg);
        porteeMax = maxRange;
    }

    public int toucherCible(double distance){
        int lancer = touche.lancer();
        if(lancer == 20){
            return(-2);
        }
        if(lancer == 1){
            return(-1);
        }
        if(distance<=portee){
            return touche.lancer() + 10;
        }
        if(portee+(porteeMax-portee/4)>=distance) {
            return touche.lancer() + 8;
        }
        if(portee+2*(porteeMax-portee/4)>=distance) {
            return touche.lancer() + 6;
        }
        if(portee+3*(porteeMax-portee/4)>=distance) {
            return touche.lancer() + 4;
        }
        if(portee+4*(porteeMax-portee/4)>=distance) {
            return touche.lancer() + 2;
        }
        return(0);
    }

    public double lancerDegats(int nbDes){
        double dgt = 0.0;
        for(int i = 0; i<nbDes; i++){
            dgt+=degats.lancer();
        }

        return (dgt+modificateurDamage);
    }
}
