package Combat;

public class OrcWorgRider extends Personnage{

    public OrcWorgRider(){
        super();
        HPMax = 13;
        HP = HPMax;
        distCac = 10;
        armor = 4;
        str = 1;
        dex = 1;
        regen = 0;
        setEquipementCac(new EquipementCac(6, 2));
        setEquipementDistance(new EquipementDistance(500.0, 4, 3));
        equipementCac.degats = new Des(8);
        equipementDistance.degats = new Des(6);

    }

    public void battleAxeAttack(){
        attack(equipementCac);
        attack(equipementCac);
        attack(equipementCac);
    }

    public void shootShortBow(){
        attack(equipementDistance);
        attack(equipementDistance);
        attack(equipementDistance);
    }
}
