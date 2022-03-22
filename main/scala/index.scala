package index

import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}
import org.jsoup.select.Elements;
import scala.collection.JavaConverters._
import scala.collection.mutable.ListBuffer
import scala.util.Try



object index{

  case class Index_object(Index: String, Stock: String)

  def landing_index(index: String, page: Int ) : String = s"XXX/${index}/werte?p=${page}"

  def tryToInt(s: String) = Try(s.toInt).toOption

  def get_pages_range(link_raw: Elements ) : Option[Int] = {

      val list_num = new ListBuffer[Option[Int]]()

        for (entry: Element <- link_raw.asScala){

          if (entry.attr("href").toLowerCase.contains("p=")){
            val temp = entry.attr("href").substring(3)
            list_num += tryToInt(temp)
          }
      }
      val max = if (list_num.nonEmpty) list_num.max else Some(1)
      return max
    }

  def get_stock_list(market_index: String) : ListBuffer[Index_object] = {

      val doc: Document = Jsoup.connect(landing_index(market_index, 1)).userAgent("Mozilla").get()
      val link_List: Elements = doc.select("a[href]")
      val Stocks_landing = new ListBuffer[Index_object]()
      val Stocks_analysts = new ListBuffer[Index_object]()
      val company_list = new ListBuffer[Index_object]()

      val max_range = get_pages_range(link_List).getOrElse(1)

      println("Get Stocklist for Index " + market_index)
      for( page <- 1 to max_range){ //max_range.getOrElse(1)){
          
          val doc: Document = Jsoup.connect(landing_index(market_index, page)).userAgent("Mozilla").get()
          val link_List: Elements = doc.select("a[href]")
     
          for (entry: Element <- link_List.asScala){

            if (entry.attr("href").toLowerCase.contains("-aktie") 
            && entry.attr("href").toLowerCase.contains("/aktien/") 
            && entry.attr("href").toLowerCase.length < 100){
                            
              Stocks_landing += Index_object(market_index.toString, "XXX" + entry.attr("href"))
              Stocks_analysts += Index_object(market_index.toString, "XXX" + entry.attr("href").replace("-aktie", "-analysen").replace("/aktien", ""))

              company_list += Index_object(market_index.toString, entry.attr("href").replace("-aktie", "").replace("/aktien", "").replace("/",""))
              
            }
          }

        }
        return company_list

      }

  def extract_date(link: String) : Array[String] = {

      //val link = "https://www.finanzen.net/analyse/carl_zeiss_meditec_halten-dz_bank_747698"
      val doc: Document = Jsoup.connect(link).userAgent("Mozilla").ignoreHttpErrors(true).get()
      val link_List: Elements = doc.select("div")
      
      val text_list = new ListBuffer[String]()
      
      for (entry: Element <- link_List.asScala){
        
        if (entry.text.contains("Original-Studie")){ // || entry.text.contains("Der Analyst Cascend Securities") ){
          text_list += entry.text
        }
      }

      val eles : Elements = doc.select("div.optionBar") //pull-left mright-20
      
      val topic_list = new ListBuffer[String]()

      for (entry: Element <- eles.asScala){
        topic_list += entry.text

      }

      val relevant_topic = try{topic_list(0)} catch {case e: Exception => "None"}

      val date_topic = try{relevant_topic.slice(relevant_topic.indexOfSlice("mright-20")+1, relevant_topic.indexOfSlice("mright-20")+17)}
                              catch {case e: Exception => "None"}

      
      val relevant_text = try {text_list(1)} catch {case e: Exception => "None"}

      val date_release = try {relevant_text.slice(relevant_text.indexOfSlice("Veröffentlichung der")+38, relevant_text.indexOfSlice("Veröffentlichung der")+63)
                                } catch {case e: Exception => "None"}
      val date_publish = try {relevant_text.slice(relevant_text.indexOfSlice("Erstmalige Weitergabe")+43, relevant_text.indexOfSlice("Erstmalige Weitergabe")+67) 
                                } catch {case e: Exception => "None"}
      
      return Array(date_topic, date_release, date_publish)
      }

}
