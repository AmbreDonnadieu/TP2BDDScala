import org.apache.spark.api.java.JavaRDD
import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.apache.spark.sql.functions.col

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

object CodeScala extends App {

  def EXO1_RDD(): Unit = {
    val spark = SparkSession.builder.appName("Java Spark SQL basic example").config("spark.master", "local").getOrCreate

    //val monsters = spark.read.json("C:\\Users\\Ambre\\Desktop\\TP2BDDScala\\NewTP2-Scala\\AllMonsterBestiary.json")
    val monsters = spark.read.json("C:\\Users\\aajin\\IdeaProjects\\TP2BDD\\TP2BDDScala\\NewTP2-Scala\\AllMonsterBestiary.json")

    //monsters.printSchema()
    //monsters.createOrReplaceTempView("MonsterSpells")
    //monsters.select(col("Name"), col("Sorts")).show();


    val test = monsters.toJavaRDD
    //monsters.select(col("Name"), col("Sorts")).show()
    //val test2 = test.map(  )


    val r1=monsters.flatMap(creature  =>{

     // val resultats = new ArrayBuffer[Tuple2[String,String]]
      val resultats = new ArrayBuffer[(String, String)]
      for(i<- 1 to 10) {
        resultats += Tuple2(creature(i).toString, creature(i).toString)
      }

    })()
    val schem2 = println(test)

  }

  override def main(args: Array[String]): Unit = {
    EXO1_RDD()
  }
}
