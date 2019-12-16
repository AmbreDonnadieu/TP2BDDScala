import java.util.Random;

public class Des {

    public int nbFaces;

    public Des(int nb_faces){
        nbFaces = nb_faces;
    }

    public int lancer(){
        Random rand = new Random();
        int result = rand.nextInt(nbFaces)+1;
        return result;
    }
}
