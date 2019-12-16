public class BrutalWarlord extends Personnage {

    public BrutalWarlord(){
        super();
        HPMax = 141;
        HP = HPMax;
        distCac = 10;
        armor = 10;
        str = 1;
        dex = 1;
        regen = 0;
        setEquipementCac(new EquipementCac(24, 10));
        setEquipementDistance(new EquipementDistance(500.0, 19, 5));
        equipementCac.degats = new Des(8);
        equipementDistance.degats = new Des(6);
    }

    public void flailAttack(){
        attack(equipementCac);
        equipementCac.modificateur-=5;
        attack(equipementCac);
        equipementCac.modificateur-=5;
        attack(equipementCac);
        equipementCac.modificateur+=10;
    }

    public void throwAxe(){
        attack(equipementDistance);
    }
}
