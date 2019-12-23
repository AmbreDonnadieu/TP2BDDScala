import org.apache.spark.sql.SparkSession

import scala.collection.mutable
import swing.{BoxPanel, _}

import org.apache.spark.rdd.RDD

//import CodeScala

object MyApp extends SimpleSwingApplication {

  val myButton = new Button("Click here")
  val spark = SparkSession.builder.appName("Java Spark SQL basic example").config("spark.master", "local").getOrCreate()
  import spark.implicits._

  val monsters = spark.read.json("C:\\Users\\aajin\\IdeaProjects\\TP2BDD\\TP2BDDScala\\NewTP2-Scala\\AllMonsterBestiary.json")
  //val monsters = spark.read.json("C:\\Users\\Ambre\\Desktop\\TP2BDDScala\\NewTP2-Scala\\AllMonsterBestiary.json")
  val result = monsters.flatMap(v => v.getAs[mutable.WrappedArray[String]]("Sorts").map(g => ( g, v.getAs[String]("Name"))))

  val index = result.rdd.reduceByKey((acc, n) => acc+" - "+n)
  val arrayIndex:RDD[Array[Any]] =  index.map(x => Array(x._1,x._2))
  val model = arrayIndex.collect().toList.toArray

  lazy val ui = new BoxPanel(Orientation.Vertical){
    val table = new Table(model, Seq("Spell","Monsters")){
      preferredViewportSize = new Dimension(500, 70)
    }
    contents += new ScrollPane(table)
  }
  def top = new MainFrame {
    title = "Spells database"

    contents = ui
  }

}