package csvwriter


import scala.collection.mutable.ListBuffer
import java.util.Date
import au.com.bytecode.opencsv.CSVWriter
import java.io.{BufferedWriter, FileWriter}
import scala.collection.JavaConverters._

import index.index

object csv_writer{

  def init_for_csv(): ListBuffer[Array[String]] = {
      val listOfRecords = new ListBuffer[Array[String]]()
      val CSVschema = Array("company", "analyst", "valuation_new", "rating", "valuation_percent", "price_hist", "analyst_name", "date_topic", "date_release", "date_publish")
      listOfRecords += CSVschema

    return listOfRecords
}
// : ListBuffer[Array[String]]
  def figures_to_csv(listOfRecords: ListBuffer[Array[String]], stock: index.Index_object) = {
      println("CSV writer called for ", stock.Stock)
      val date = java.time.LocalDate.now
      val outputFile = new BufferedWriter(new FileWriter(s"/Users/Rich/Desktop/PlanD/Analyst_feed/analyst_${stock.Stock}_${date}.csv"))
      val csvWriter = new CSVWriter(outputFile)
      
      val listOfRecords_fin = listOfRecords.reverse :+ Array("company", "analyst", "valuation_new", "rating", "valuation_percent", "price_hist", "analyst_name") 

      csvWriter.writeAll(listOfRecords_fin.reverse.toList.asJava)
      outputFile.close
  } 
}