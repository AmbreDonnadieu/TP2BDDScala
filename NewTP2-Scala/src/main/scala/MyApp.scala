import java.awt.FlowLayout

import org.apache.spark.sql.SparkSession

import scala.collection.mutable
import swing.{BoxPanel, _}
import org.apache.spark.rdd.RDD

import scala.swing.Table.AutoResizeMode
import scala.swing.event.ButtonClicked

//import CodeScala

object MyApp extends SimpleSwingApplication {

  val myButton = new Button("Click here")
  val spark = SparkSession.builder.appName("Java Spark SQL basic example").config("spark.master", "local").getOrCreate()
  import spark.implicits._

  //IMPORTANT : préciser le chemin de son fichier "AllMonsterBestiary.json" (généré dans CodeIntegrale)
  val monsters = spark.read.json("C:\\Users\\aajin\\IdeaProjects\\TP2BDD\\TP2BDDScala\\NewTP2-Scala\\AllMonsterBestiary.json")
  //val monsters = spark.read.json("C:\\Users\\Ambre\\Desktop\\TP2BDDScala\\NewTP2-Scala\\AllMonsterBestiary.json")

  val result = monsters.flatMap(v => v.getAs[mutable.WrappedArray[String]]("Sorts").map(g => ( g, v.getAs[String]("Name"))))
  val index = result.rdd.reduceByKey((acc, n) => acc+" ; "+n)

  //traitement pour l'affichage
  val arrayIndex:RDD[Array[Any]] =  index.map(x => Array(x._1,x._2))
  val model = arrayIndex.collect().toList.toArray

//La fonction qui filtre selon le nom du sort
  def filterSpell(spellName : String):Array[Array[Any]]={
    val filteredIndex =  index.filter(x => x._1.contains(spellName))
    val arrayFilteredIndex:RDD[Array[Any]] = filteredIndex.map(x => Array(x._1,x._2))
    val newModel = arrayFilteredIndex.collect().toList.toArray
    return newModel
  }
//La fonction qui filtre selon le nom du monstre
  def filterMonster(monsterName : String):Array[Array[Any]]={
    val filteredIndex =  index.filter(x => x._2.contains(monsterName) )
    val arrayFilteredIndex:RDD[Array[Any]] = filteredIndex.map(x => Array(x._1,x._2))
    val newModel = arrayFilteredIndex.collect().toList.toArray
    return newModel
  }

  //Le conteneur
  lazy val ui = new BoxPanel(Orientation.Vertical){
    //L'affichage du tableau
    var table = new Table(model, Seq("Spell","Monsters")){
      preferredViewportSize = new Dimension(700, 600)
    }
    var tablePanel = new ScrollPane(table)
    contents += tablePanel

    contents+= new Label("Search")
    //La recherche de sort
    contents+= new FlowPanel(){
      val sLabel = new Label("Spell name : ")
      val spellSearchField = new TextField(""){
        preferredSize = new Dimension(100,20)
      }
      val searchSpellButton = new Button("Search")
      contents+=sLabel
      contents+=spellSearchField
      contents+=searchSpellButton
      listenTo(searchSpellButton)
      reactions += {
        case ButtonClicked(searchSpellButton) => tablePanel.contents = new Table(filterSpell(spellSearchField.text),Seq("Spell","Monsters"))
      }
    }
    //La recherche de monstre
    contents+= new FlowPanel(){
      val mLabel = new Label("Monster name : ")
      val monsterSearchField = new TextField(""){
        preferredSize = new Dimension(100,20)
      }
      val searchMonsterButton = new Button("Search")
      contents+=mLabel
      contents+=monsterSearchField
      contents+=searchMonsterButton
      listenTo(searchMonsterButton)
      reactions += {
        case ButtonClicked(searchMonsterButton) => tablePanel.contents = new Table(filterMonster(monsterSearchField.text), Seq("Spell", "Monsters"))
      }
    }
    //Le bouton reset
    contents+=new FlowPanel(){
      val resetButton=new Button("Reset")
      contents+=resetButton
      listenTo(resetButton)
      reactions+={
        case ButtonClicked(searchSpellButton) => tablePanel.contents = new Table(model, Seq("Spell","Monsters"))
      }
    }
  }



  def top = new MainFrame {
    title = "Spells database"
    contents = ui
    maximize()
  }

}