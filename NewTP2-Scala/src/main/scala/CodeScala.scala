import org.apache.spark.sql.SparkSession

import scala.collection.mutable


object CodeScala extends App {

  def EXO1_RDD() {
    val spark = SparkSession.builder.appName("Java Spark SQL basic example").config("spark.master", "local").getOrCreate()
    import spark.implicits._

    //val monsters = spark.read.json("C:\\Users\\aajin\\IdeaProjects\\TP2BDD\\TP2BDDScala\\NewTP2-Scala\\AllMonsterBestiary.json")
    val monsters = spark.read.json("C:\\Users\\Ambre\\Desktop\\TP2BDDScala\\NewTP2-Scala\\AllMonsterBestiary.json")

    //monsters.printSchema()
    //monsters.select(col("Name"), col("Sorts")).show();
    //monsters.collect().foreach(println)

    //r1.collect().foreach(println)
    //val splitMe = List( ("key1", List(1,2,3)), ("key2", List(4,5)) )
    //val result = splitMe.flatMap(v=> v._2.map(g => (v._1, g)))

    val result = monsters.flatMap(v => v.getAs[mutable.WrappedArray[String]]("Sorts").map(g => ( g, v.getAs[String]("Name"))))


    val index = result.rdd.reduceByKey((acc, n) => acc+" - "+n)
    //index.collect().foreach(println)


    val dataframe = index.toDF()
    dataframe.write.json("Sorts_Monstres.json")
    val test = spark.read.json("C:\\Users\\Ambre\\Desktop\\TP2BDDScala\\NewTP2-Scala\\Sorts_Monstres.json\\part-00000-7a6e68c2-48cf-4f0f-b666-b4c96b676ad6-c000.json")
    test.toDF().collect().foreach(println)
    val miaou = index.coalesce(1).saveAsTextFile("C:\\Users\\Ambre\\Desktop\\TP2BDDScala\\NewTP2-Scala\\RDD_Sort_Monstres.txt")
    ///test.printSchema()

  }

  override def main(args: Array[String]) {
    EXO1_RDD()
  }
}
