import org.apache.spark.api.java.JavaRDD
import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.apache.spark.sql.functions.col

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

object CodeScala extends App {

  def EXO1_RDD() {
    val spark = SparkSession.builder.appName("Java Spark SQL basic example").config("spark.master", "local").getOrCreate
    val monsters = spark.read.json("C:/Users/Ambre/Desktop/TP2BDDScala/NewTP2-Scala/AllMonsterBestiary.json").toDF()

    //val monsters = spark.read.json("C:\\Users\\Ambre\\Desktop\\TP2BDDScala\\NewTP2-Scala\\AllMonsterBestiary.json")
    //val monsters = spark.read.json("C:\\Users\\aajin\\IdeaProjects\\TP2BDD\\TP2BDDScala\\NewTP2-Scala\\AllMonsterBestiary.json")
    //monsters.printSchema()
    //monsters.select(col("Name"), col("Sorts")).show();
    val test = monsters.printSchema()

   /* val r1 = monsters.rdd.map( creature => {
      val resultats = new ArrayBuffer[Tuple2[String,String]]
      val name = creature.getAs[String]("Name")
      val sort = creature.getAs[Array[String]]("Sorts")
      for(i<- 1 to 10) {
        val resultat3 = (name,sort(i))
        resultats+=resultat3
      }
    })*/

    //val splitMe = List( ("key1", List(1,2,3)), ("key2", List(4,5)) )
    //val result = splitMe.flatMap(v=> v._2.map(g => (v._1, g)))

    val preresult = monsters.collect()
    val result = monsters.collect().flatMap(v => v.getAs[mutable.WrappedArray[String]]("Sorts").map(g => (v.getAs[String]("Name"), g)))
    val j =2+2
    print(j.toString())
    //dataset = [Name:String, Sorts : Array<String>]
    val df = monsters.toDF("Name", "Sorts")
   // df.flatMap(v=> v._2.map(g => (v._1, g)))
    val schem2 = println(test)

  }

  override def main(args: Array[String]) {
    EXO1_RDD()
  }
}
