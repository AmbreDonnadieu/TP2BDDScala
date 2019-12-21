public class SolarAngel extends Personnage {

    public SolarAngel(){
        super();
        HPMax = 363;
        HP = HPMax;
        distCac = 10;
        armor = 44;
        str = 3;
        dex = 2;
        regen = 15;
        setEquipementCac(new EquipementCac(35, 18));
        setEquipementDistance(new EquipementDistance(500.0, 31, 14));
        equipementCac.degats = new Des(6);
        equipementDistance.degats = new Des(6);
    }

    public void greatswordSlash(){
        attack(equipementCac);
        equipementCac.modificateur-=5;
        attack(equipementCac);
        equipementCac.modificateur-=5;
        attack(equipementCac);
        equipementCac.modificateur-=5;
        attack(equipementCac);
        equipementCac.modificateur+=15;
    }

    public void longBowShot(){
        attack(equipementDistance);
        equipementDistance.modificateur-=5;
        attack(equipementDistance);
        equipementDistance.modificateur-=5;
        attack(equipementDistance);
        equipementDistance.modificateur-=5;
        attack(equipementDistance);
        equipementDistance.modificateur+=15;
    }

    public void envol(){
        posZ=20;
    }
}
