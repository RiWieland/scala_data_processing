import scala.util.Random
import java.net.URL
import java.io._

import java.nio.file.{Path, Paths, Files}
import java.util.Date
import scala.util.Try

import scala.collection.JavaConverters._


import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}
import org.jsoup.select.Elements;

//import org.jsoup
import au.com.bytecode.opencsv.CSVWriter


import analyst.statement
import index.index
import Company.stock_analyst_links
import csvwriter.csv_writer
import scala.collection.mutable.ListBuffer

// future:
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}
import scala.concurrent.duration._
import scala.concurrent._

// write better code:
// - use regex
// - match pattern
// - try to apply to list


// to-do:
// - connect elements
// - error handling
// "Verkaufen" not implemented yet

// crawl_statement:
// - use option method
// - maybe map?

// crawl stock links:
// - include pattern match / map / filter to sequence, no if statement


// Error handling and how many are empty




object crawl_analyst_statement {

  val index_list: List[String] = List("tecdax", "s&p_500")//, "sdax",  "dow_jones", "mdax", "nasdaq_100", "euro_stoxx_50", "smi", "atx", "cac_40", "nikkei_225")         

  //case class final record(  )

  def main(args: Array[String])={
    val t1 = System.nanoTime

    
    for (market_index <- index_list.take(1)){
      println(market_index)

      //Index
      val stock_list = index.get_stock_list(market_index)

      for (stock <- stock_list.take(10)){

        val future_history = Future{
          val result = stock_analyst_links.get_history(stock.Stock)
          result
          }

        future_history.onComplete{
          case Success(y) => {

            //Thread.sleep(10)
            println("called method for ", stock.Stock)

          val final_figures = y.map{
            link_list => analyst.statement.get_figures(link_list.Link)
              }.map{
                x => Array(x.company.toString,
                                                x.analyst,
                                                x.valuation_new.toString, 
                                                x.rating.toString, 
                                                x.valuation_percent.toString, 
                                                x.price_hist.toString, 
                                                x.analyst_name.toString,
                                                x.date_topic.toString,
                                                x.date_release.toString,
                                                x.date_publish.toString)
          }
        
        csv_writer.figures_to_csv(final_figures, stock)
       }
        case Failure(e) => e.printStackTrace

      }

      Await.ready(future_history, 1000.seconds)        
    }   
    }        
  val duration = (System.nanoTime - t1) / 1e9d
  println(duration)
  }
}

