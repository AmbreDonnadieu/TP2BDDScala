import org.apache.spark.api.java.JavaRDD
import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.apache.spark.sql.functions.col

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

object CodeScala extends App {

  def EXO1_RDD() {
    val spark = SparkSession.builder.appName("Java Spark SQL basic example").config("spark.master", "local").getOrCreate
    val monsters = spark.read.json("C:/Users/Ambre/Desktop/TP2BDDScala/NewTP2-Scala/AllMonsterBestiary.json").toDF()

    //monsters.printSchema()
    //monsters.select(col("Name"), col("Sorts")).show();
    val test = monsters.printSchema()

    val r1 = monsters.map( creature => {
      val resultats = new ArrayBuffer[Tuple2[String,String]]
      val name = creature.getAs[String]("Name")
      val sort = creature.getAs[Array[String]]("Sorts")
      for(i<- 1 to 10) {
        val resultat3 = (name,sort(i))
        resultats+=resultat3
      }
    })


    //dataset = [Name:String, Sorts : Array<String>]
    val df = monsters.toDF("Name", "Sorts")
   // df.flatMap(v=> v._2.map(g => (v._1, g)))
    val schem2 = println(test)

  }

  override def main(args: Array[String]) {
    EXO1_RDD()
  }
}
