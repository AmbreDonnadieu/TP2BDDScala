import org.apache.spark.sql.SparkSession;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class CodeIntegrale {


    public static void CreateJSONFile() throws IOException {
        // 1 - liste des types de monstres dans le bestiaire
        // 2 - Liste des monstres dans chaque catégorie du bestiaire
        // 3 - ajout de chaque Monstre (nom + list sort) dans un json

        try {
            String FileName = "AllMonsterBestiary.json";
            FileWriter file = new FileWriter(FileName);
            org.json.JSONArray MonsterBestiary = new org.json.JSONArray();

            Document doc = Jsoup.connect("https://www.d20pfsrd.com/bestiary/monster-listings/").get();

            Elements e = doc.select(".toc_list li");
            Elements eTest = doc.select("td.text h4 a[href]");
            List<String> MonsterCategory = eTest.eachAttr("href"); // catégories de monstres dans une liste


            //On parcourt chaque categories maintenant
            for (int i = 0; i < MonsterCategory.size()-1; i++) {
                String url = MonsterCategory.get(i);
                Document doc2 = Jsoup.connect(url).get();
                System.out.println(i + " " + MonsterCategory.get(i));
                System.out.println(url);

                //mnt on recupère les tables of content de chaque page pour avoir la liste de tous les sorts par catégories

                Elements e2 = doc2.select(".ogn-childpages a[href]");
                List<String> MonsterList = e2.eachAttr("href"); // on a la liste des url permettant d'aller à chaque monstre
                System.out.println(MonsterList);


                // mnt qu'on a la liste des monstres pour chaque catégorie, il faut qu'on pique la liste des sorts de chaque monstre
                for (int j = 0; j < MonsterList.size(); j++) {
                    //On va sur chaque url de la catégorie de monstres (sur la page de chaque monstre quoi)
                    url = MonsterList.get(j);
                    doc2 = Jsoup.connect(url).get();
                    //System.out.println(url);

                    Elements eNomMonstre = doc2.select("p.title, th[class=text]");
                    if(eNomMonstre.size() != 0){
                        String NomMonstre = eNomMonstre.first().ownText();
                        System.out.print("\n"+NomMonstre);

                        // trouver les sorts du monstre
                        Elements e3 = doc2.select("a.spell");
                        List<String> MonsterSpell = e3.eachText();
                        //System.out.print(MonsterSpell);

                        //les mettre dans un json du style [monstre { nom : ***, url: ***}]
                        JSONObject Monster = new JSONObject();
                        try {
                            Monster.put("Name", NomMonstre);
                            Monster.put("Sorts", MonsterSpell);
                            MonsterBestiary.put(Monster);
                        } catch (JSONException je) {
                            je.printStackTrace();
                        }
                    }
                    else{               //si la page est juste un sommaire regroupant les sous-catégories du monstre
                        System.out.print("\n\n"+url+"\n\n");
                    }
                }
            }
            try {
                // Writing to a file

                file.write(MonsterBestiary.toString());
                file.flush();
                file.close();
            } catch (IOException ei) {
                ei.printStackTrace();
            }

            System.out.println("JSon file created");
            System.out.println("finish");
            // mettre sur mongodb


        } catch (IOException e) {

        }
    }



    public static void main(String[] args) throws IOException {
        //CreateJSONFile();
        System.out.println("Cacahuete");
    }
}
