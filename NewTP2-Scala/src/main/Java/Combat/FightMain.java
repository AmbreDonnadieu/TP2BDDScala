package Combat;

import java.util.ArrayList;


public class FightMain {

    public static void main(String[] args){

        //Création des personnages
        SolarAngel solar = new SolarAngel();

        ArrayList<Personnage> orcs = new ArrayList<>();

        orcs.add(new BrutalWarlord());
        for(int i = 0; i < 9; i++){
            orcs.add(new OrcWorgRider());
        }
        for(int i = 0; i < 4; i++){
            orcs.add(new BarbarianOrc());
        }

        solar.ennemies = orcs;

        for(int i = 0; i < 14; i++){
            orcs.get(i).ennemies.add(solar);
            orcs.get(i).friends = (ArrayList<Personnage>) orcs.clone();
            orcs.get(i).friends.remove(orcs.get(i));
        }

        //Positionnement des troupes
        solar.posX = -55.0;
        solar.posY = 0.0;
        solar.posZ = 0.0;
        orcs.get(0).posX = 75.0;
        orcs.get(0).posY = 0.0;
        orcs.get(0).posZ = 0.0;
        for(int i = 1; i<=9; i++){
            orcs.get(i).posX = 55.0;
            orcs.get(i).posY = 50.0 - i * 10.0;
            orcs.get(i).posZ = 0.0;
        }
        for(int i = 0; i<4; i++){
            orcs.get(i+10).posX = 75;
            orcs.get(i+10).posY = 20-i*10;
            orcs.get(i).posZ = 0.0;
        }

        //Boucle de combat
        int comptOrcMort = 0;
        while(solar.dead==false && comptOrcMort<14) {
            ArrayList<Personnage> tempon = (ArrayList<Personnage>) orcs.clone();
            //Solar attaque
            if (solar.isCac()) {
                System.out.println("Solar attaque au corp à corp");
                solar.greatswordSlash();
            } else if (solar.isDist()) {
                solar.longBowShot();
                solar.deplacementVersCentre();
                System.out.println("Solar attaque à distance");
            } else {
                solar.deplacementVersCentre();
            }
            //Orcs attaquent
            for (Personnage i : orcs) {
                if(i.dead){//Si orc mort, on le retire de la liste
                    comptOrcMort++;
                    tempon.remove(i);
                }else {
                    if (i instanceof BrutalWarlord) {
                        if (i.isCac()) {
                            ((BrutalWarlord) i).flailAttack();
                            System.out.println("Brutal Warlord attaque au corp à corp");
                        } else if (i.isDist()) {
                            ((BrutalWarlord) i).throwAxe();
                            i.deplacementVersCentre();
                            System.out.println("Brutal Warlord attaque à distance");
                        } else {
                            i.deplacementVersCentre();
                        }
                    }
                    if (i instanceof OrcWorgRider) {
                        if (i.isCac()) {
                            ((OrcWorgRider) i).battleAxeAttack();
                            System.out.println("Orc Worg Rider attaque au corp à corp");
                        } else if (i.isDist()) {
                            ((OrcWorgRider) i).shootShortBow();
                            i.deplacementVersCentre();
                            System.out.println("Orc Worg Rider attaque à distance");
                        } else {
                            i.deplacementVersCentre();
                        }
                    }
                    if (i instanceof BarbarianOrc) {
                        if (i.isCac()) {
                            ((BarbarianOrc) i).doubleAxe();
                            System.out.println("Barbarian Orc attaque au corp à corp");
                        } else if (i.isDist()) {
                            ((BarbarianOrc) i).shootShortBow();
                            i.deplacementVersCentre();
                            System.out.println("Barbarian Orc attaque à distance");
                        } else {
                            i.deplacementVersCentre();
                        }
                    }
                }
            }
            orcs = (ArrayList<Personnage>) tempon.clone();
            if(solar.HP<solar.HPMax){//Si le solar a pris un coup, il s'envole
                solar.envol();
                System.out.println("Le solar s'envole");
            }
            solar.regenerate();//Le solar se régénère en fin de tour
        }




    }
}
