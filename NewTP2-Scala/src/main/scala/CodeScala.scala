import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.api.java.JavaRDD
import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.types.{StringType, StructField, StructType}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.util.parsing.json._


object CodeScala extends App {

  def EXO1_RDD() {
    val spark = SparkSession.builder.appName("Java Spark SQL basic example").config("spark.master", "local").getOrCreate()
    import spark.implicits._

    val monsters = spark.read.json("C:\\Users\\aajin\\IdeaProjects\\TP2BDD\\TP2BDDScala\\NewTP2-Scala\\AllMonsterBestiary.json")
    //val monsters = spark.read.json("C:\\Users\\Ambre\\Desktop\\TP2BDDScala\\NewTP2-Scala\\AllMonsterBestiary.json")
    //monsters.printSchema()
    //monsters.select(col("Name"), col("Sorts")).show();
    //monsters.collect().foreach(println)

    //val splitMe = List( ("key1", List(1,2,3)), ("key2", List(4,5)) )
    //val result = splitMe.flatMap(v=> v._2.map(g => (v._1, g)))

   val result = monsters.flatMap(v => v.getAs[mutable.WrappedArray[String]]("Sorts").map(g => ( g, v.getAs[String]("Name"))))

    val index = result.rdd.reduceByKey((acc, n) => (acc+" - "+n))
    index.collect().foreach(println)
    

    print("Cacahuete")

  }

  override def main(args: Array[String]) {
    EXO1_RDD()
  }
}
