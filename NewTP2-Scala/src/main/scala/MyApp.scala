import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

import scala.collection.mutable
import scala.swing.event.{ButtonClicked, TableRowsSelected}
import scala.swing.{BoxPanel, _}

object MyApp extends SimpleSwingApplication {

  val spark: SparkSession = SparkSession.builder.appName("Java Spark SQL basic example").config("spark.master", "local").getOrCreate()

  val index: RDD[(String, String)] = CodeScala.EXO1_RDD()

  //traitement pour l'affichage
  val arrayIndex:RDD[Array[Any]] =  index.map(x => Array(x._1,x._2))
  val model: Array[Array[Any]] = arrayIndex.collect().toList.toArray

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
  lazy val ui: BoxPanel = new BoxPanel(Orientation.Vertical){
    //L'affichage du tableau
    var table: Table = new Table(model, Seq("Spell","Monsters")){
      preferredViewportSize = new Dimension(700, 600)
    }
    var tablePanel = new ScrollPane(table)
    contents += tablePanel

    contents+= new Label("Search")
    //La recherche de sort
    contents+= new FlowPanel(){
      val sLabel = new Label("Spell name : ")
      val spellSearchField: TextField = new TextField(""){
        preferredSize = new Dimension(100,20)
      }
      val searchSpellButton = new Button("Search")
      contents+=sLabel
      contents+=spellSearchField
      contents+=searchSpellButton
      listenTo(searchSpellButton)
      reactions += {
        case ButtonClicked(searchSpellButton) => table = new Table(filterSpell(spellSearchField.text), Seq("Spell","Monsters"))
          listenTo(table.selection)
          reactions+={
            case TableRowsSelected(source,range,false) => println("Sort : " + table.apply(table.selection.rows.leadIndex,0) + "\t Monstres : " + table.apply(table.selection.rows.leadIndex,1))
          }
        tablePanel.contents = table
      }
    }
    //La recherche de monstre
    contents+= new FlowPanel(){
      val mLabel = new Label("Monster name : ")
      val monsterSearchField: TextField = new TextField(""){
        preferredSize = new Dimension(100,20)
      }
      val searchMonsterButton = new Button("Search")
      contents+=mLabel
      contents+=monsterSearchField
      contents+=searchMonsterButton
      listenTo(searchMonsterButton)
      reactions += {
        case ButtonClicked(searchMonsterButton) => table = new Table(filterMonster(monsterSearchField.text), Seq("Spell", "Monsters"))
          listenTo(table.selection)
          reactions+={
            case TableRowsSelected(source,range,false) => println("Sort : " + table.apply(table.selection.rows.leadIndex,0) + "\t Monstres : " + table.apply(table.selection.rows.leadIndex,1))
          }
          tablePanel.contents = table
      }

    }
    //Le bouton reset
    contents+=new FlowPanel(){
      val resetButton=new Button("Reset")
      contents+=resetButton
      listenTo(resetButton)
      reactions+={
        case ButtonClicked(searchSpellButton) => table = new Table(model, Seq("Spell","Monsters"))
          listenTo(table.selection)
          reactions+={
            case TableRowsSelected(source,range,false) => println("Sort : " + table.apply(table.selection.rows.leadIndex,0) + "\t Monstres : " + table.apply(table.selection.rows.leadIndex,1))
          }
          tablePanel.contents = table
      }
    }

    //affiche les données de la ligne sur laquelle on clique
    listenTo(table.selection)
    reactions+={
      case TableRowsSelected(source,range,false) => println("Sort : " + table.apply(table.selection.rows.leadIndex,0) + "\t Monstres : " + table.apply(table.selection.rows.leadIndex,1))
    }
  }



  def top: MainFrame = new MainFrame {
    title = "Spells database"
    contents = ui
    size = new Dimension(1000,600)
    maximize()
  }

}