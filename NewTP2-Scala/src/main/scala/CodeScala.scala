import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{SaveMode, SparkSession}

import scala.collection.mutable


object CodeScala extends App {

  def EXO1_RDD():RDD[(String,String)] =  {
    val spark = SparkSession.builder.appName("Java Spark SQL basic example").config("spark.master", "local").getOrCreate()
    import spark.implicits._

    val monsters = spark.read.json("C:\\Users\\aajin\\IdeaProjects\\TP2BDD\\TP2BDDScala\\NewTP2-Scala\\AllMonsterBestiary.json")
    //val monsters = spark.read.json("C:\\Users\\Ambre\\Desktop\\TP2BDDScala\\NewTP2-Scala\\AllMonsterBestiary.json")
    val result = monsters.flatMap(v => v.getAs[mutable.WrappedArray[String]]("Sorts").map(g => ( g, v.getAs[String]("Name"))))

    val index = result.rdd.reduceByKey((acc, n) => acc+" - "+n)
    //index.collect().foreach(println)

    val dataframe = index.toDF()
    dataframe.write.mode(SaveMode.Overwrite).json("Sorts_Monstres.json")

    return index
  }

  override def main(args: Array[String]) {
    EXO1_RDD()
  }
}
