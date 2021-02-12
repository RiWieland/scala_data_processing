package csvwriter


import scala.collection.mutable.ListBuffer
import java.util.Date
import au.com.bytecode.opencsv.CSVWriter
import java.io.{BufferedWriter, FileWriter}
import scala.collection.JavaConverters._


object csv_writer{
  def init_for_csv(): ListBuffer[Array[String]] = {
      val listOfRecords = new ListBuffer[Array[String]]()
      val CSVschema = Array("company", "analyst", "valuation_new", "rating", "valuation_percent", "price_hist", "analyst_name", "date_topic", "date_release", "date_publish")
      listOfRecords += CSVschema

    return listOfRecords
}
  def figures_to_csv(listOfRecords: ListBuffer[Array[String]], stock: index.Index_object) = {

}

}