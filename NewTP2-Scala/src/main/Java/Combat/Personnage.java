package Combat;

import java.util.ArrayList;

import static java.lang.Math.sqrt;

public class Personnage {

    //liste des alliés
    public ArrayList<Personnage> friends = new ArrayList<>();

    //liste des ennemis
    public ArrayList<Personnage> ennemies = new ArrayList<>();

    public double HP;

    public double HPMax;

    public int regen;

    public int armor;

    public double posX;

    public double posY;

    public double posZ;

    //distance pour toucher au corps à corps
    public double distCac;

    public int str;

    public int dex;

    public boolean dead;

    public EquipementDistance equipementDistance;

    public EquipementCac equipementCac;

    public Personnage(){
        dead = false;

    }

    //calcul de la distance entre deux points
    public double distance(double X1, double Y1, double X2, double Y2, double Z1, double Z2){
        return sqrt((X2 - X1) * (X2 - X1) + (Y2 - Y1) * (Y2 - Y1) * (Z2 - Z1) * (Z2 - Z1));
    }

    //Fonction permettant de déplacer le personnage
    void move(double newX, double newY, double newZ){
        for(Personnage persoCac : friends){
            double loin = distance(newX,newY,persoCac.posX,persoCac.posY,newZ,persoCac.posZ);
            if(loin==0) {//Si sur un autre perso allié
                newY+=1;
            }
        }
        for(Personnage persoDist : ennemies){
            double loin = distance(newX,newY,persoDist.posX,persoDist.posY,newZ,persoDist.posZ);
            if(loin==0){//Si sur un autre perso ennemi
                newY+=1;
            }
        }
        posX = newX;
        posY = newY;
        posZ = newZ;
    }

    //fonction régénération
    public void regenerate(){
        if(HP>=HPMax-regen){
            HP=HPMax;
        }else{
            HP+=regen;
        }
    }


    //Renvoi l'ennemi le plus proche
    public Personnage findClosestEnnemy(){
        Personnage closest = ennemies.get(0);
        double dist = distance(posX, posY, closest.posX, closest.posY, posZ, closest.posZ);
        for (Personnage i : ennemies){
            if(distance(posX, posY, i.posX, i.posY,posZ,i.posZ)<dist){
                dist = distance(posX, posY, i.posX, i.posY,posZ,i.posZ);
                closest = i;
            }
        }
        return closest;
    }

    public void setEquipementDistance(EquipementDistance eq){
        equipementDistance = eq;
    }

    public void setEquipementCac(EquipementCac eq){
        equipementCac = eq;
    }

    //renvoi true si le perso le plus proche est à distance de corps à corps
    public boolean isCac(){
        return(distance(posX, posY, findClosestEnnemy().posX, findClosestEnnemy().posY,posZ,findClosestEnnemy().posZ)<=distCac);
    }

    //renvoi true si le perso le plus proche est à distance de tir à distance
    public boolean isDist(){
        try{
            return(distance(posX, posY, findClosestEnnemy().posX, findClosestEnnemy().posY,posZ,findClosestEnnemy().posZ)<=equipementDistance.porteeMax);
        }catch(Exception ex){
            return(distance(posX, posY, findClosestEnnemy().posX, findClosestEnnemy().posY,posZ,findClosestEnnemy().posZ)<=distCac);
        }
    }

    //attaque l'ennemi le plus proche
    public void attack(Equipement weapon){
        Personnage ennemy = findClosestEnnemy();
        if(weapon instanceof EquipementCac){
            if(isCac()){
                double touche = weapon.toucherCible(distance(posX, posY, ennemy.posX, ennemy.posY,posZ,ennemy.posZ));
                if(ennemy.isTouched(touche) || touche == -2){
                    double damage = weapon.lancerDegats(str);
                    ennemy.receiveDamage(damage);
                }

            }
        }
        if(weapon instanceof  EquipementDistance){
            if(isDist()){
                double touche = weapon.toucherCible(distance(posX, posY, ennemy.posX, ennemy.posY,posZ,ennemy.posZ));
                if(ennemy.isTouched(touche) || touche == -2){
                    double damage = weapon.lancerDegats(dex);
                    ennemy.receiveDamage(damage);
                }
            }
        }
    }

    //Retourne true si l'attaque entrée dépasse l'armure du perso
    public boolean isTouched(double jab){return(jab>armor);}

    //permet de recevoir un coup
    public void receiveDamage(double dmg){
        HP-=dmg;
        if(HP<=0){
            HP=0;
            die();
        }
    }

    //Fait mourir un personnage
    public void die(){
        dead = true;
        posX += 10000;
        posY += 10000;
    }

    //permet à un personnage de faire un déplacement vers le centre du terrain
    public void deplacementVersCentre(){
        if(posX>0 && posY>0) move(posX - sqrt(50), posY - sqrt(50), posZ);
        if(posX<0 && posY<0) move(posX + sqrt(50), posY + sqrt(50), posZ);
        if(posX>0 && posY<0) move(posX - sqrt(50), posY + sqrt(50), posZ);
        if(posX<0 && posY>0) move(posX + sqrt(50), posY - sqrt(50), posZ);
        if(posX==0 && posY<0) move(posX, posY + 10, posZ);
        if(posX==0 && posY>0) move(posX, posY - 10, posZ);
        if(posX<0 && posY==0) move(posX + 10, posY, posZ);
        if(posX>0 && posY==0) move(posX - 10, posY, posZ);
    }

}
