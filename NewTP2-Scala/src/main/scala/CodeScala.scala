import org.apache.spark.api.java.JavaRDD
import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.apache.spark.sql.functions.col

object CodeScala extends App {

  def EXO1_RDD(): Unit = {
    val spark = SparkSession.builder.appName("Java Spark SQL basic example").config("spark.master", "local").getOrCreate

    val monsters = spark.read.json("AllMonsterBestiary.json")
    monsters.printSchema()
    monsters.createOrReplaceTempView("MonsterSpells")
    //monsters.select(col("Name"), col("Sorts")).show();


    val test = monsters.toJavaRDD
    monsters.select(col("Name"), col("Sorts")).show()
    //val test2 = test.map(  )
  }


}
