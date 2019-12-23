import org.apache.spark.sql.SparkSession;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import spire.algebra.Bool;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

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

    private static Connection connect(String fileName) {
        //String url = "jdbc:sqlite:C:/Users/aajin/IdeaProjects/BD1/sqlite/db/" + fileName;
        String url = "jdbc:sqlite:" + fileName;

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    private static void createDatabase(String fileName){
        //String url = "jdbc:sqlite:C:/Users/aajin/IdeaProjects/BD1/sqlite/db/" + fileName;
        String url = "jdbc:sqlite:" + fileName;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Crée la table "spells" dans la base de données
     */
    private static void createTable(String NomDB, String NomTable) {
        String url = "jdbc:sqlite:"+NomDB+".db";
        String sql = "CREATE TABLE IF NOT EXISTS "+ NomTable +" (\n"
                + "    name text PRIMARY KEY,\n"
                + "    components text NOT NULL,\n"
                + "    school text NOT NULL,\n"
                + "    spell_resistance text NOT NULL,\n"
                + "    range_area text NOT NULL,\n"
                + "    level text NOT NULL,\n"
                + "    effect text NOT NULL,\n"
                + "    casting_time text NOT NULL,\n"
                + "    saving_throw text NOT NULL\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("table créée");
        } catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
        }
    }

    /**
     * Supprime la table "spells" de la base de données
     */
    private static void deleteTable(){
        String url = "jdbc:sqlite:test.db";
        String sql = "Drop Table spells";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
            System.out.println("table supprimée");
        } catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
        }
    }

    private static void insert(String name, List<String> comp, String school, Boolean spellres, String range, Integer level, String effect, String castTime, String savingThrow) {
        String sql = "INSERT INTO spells(name,components,school,spell_resistance,range_area,level,effect,casting_time,saving_throw) VALUES(?,?,?,?,?,?,?,?,?)";

        try (Connection conn = connect("test.db");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setObject(2, comp);
            pstmt.setString(3, school);
            pstmt.setBoolean(4, spellres);
            pstmt.setString(5, range);
            pstmt.setInt(6, level);
            pstmt.setString(7, effect);
            pstmt.setString(8, castTime);
            pstmt.setString(9, savingThrow);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Insère les sorts de mage dans la base de données SQLite
     */
    private  static void exportToSqlite() throws IOException, JSONException {
        String content = new String(Files.readAllBytes(Paths.get("Sorts.json")));
        JSONArray AllSorts = new JSONArray(content);
        for (int i = 0; i < AllSorts.length(); i++) {
            JSONObject obj = (JSONObject) AllSorts.get(i);

            List<String> compo = new ArrayList<>();
            for (int j = 0; j < obj.getJSONArray("Components").length(); j++) {
                compo.add(obj.getJSONArray("Components").get(j).toString());
            }

            String nom_sort = obj.getString("Name");
            List<String> Components = compo;
            String school = obj.getString("School");
            Boolean Spell_Resistance = obj.getBoolean("Spell_Resitance");
            String Range_area = obj.getString("Range_Area");
            Integer Level = obj.getInt("Level");
            String Effect = obj.getString("Effect");
            String Casting_Time = obj.getString("Casting_Time");
            String Saving_Throw = obj.getString("Saving_Throw");


        //Insertion dans la BD SQLite
            insert(nom_sort,Components,school,Spell_Resistance,Range_area,Level,Effect,Casting_Time,Saving_Throw);

            System.out.println("finish");

        }
    }



    public static void main(String[] args) throws IOException {
        CreateJSONFile();
    }
}
