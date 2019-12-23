package Combat;

public class BarbarianOrc extends Personnage{

    public BarbarianOrc(){
        super();
        HPMax = 142;
        HP = HPMax;
        distCac = 10;
        armor = 4;
        str = 1;
        dex = 1;
        regen = 0;
        setEquipementCac(new EquipementCac(19, 10));
        setEquipementDistance(new EquipementDistance(500.0, 16, 6));
        equipementCac.degats = new Des(8);
        equipementDistance.degats = new Des(8);
    }

    public void doubleAxe(){
        attack(equipementCac);
        equipementCac.modificateur-=5;
        attack(equipementCac);
        equipementCac.modificateur-=5;
        attack(equipementCac);
        equipementCac.modificateur+=10;
    }

    public void shootShortBow(){
        attack(equipementDistance);
        equipementDistance.modificateur-=5;
        attack(equipementDistance);
        equipementDistance.modificateur-=5;
        attack(equipementDistance);
        equipementDistance.modificateur+=10;
    }
}
