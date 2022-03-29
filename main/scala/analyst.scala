package analyst

import scala.collection.mutable.ListBuffer
import scala.collection.JavaConverters._

import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}
import org.jsoup.select.Elements;

object statement{

  case class statement(statement: String)
  case class figures(company: String, analyst: String, valuation_new: Double, 
                    rating: String, valuation_percent: Double, price_hist: Double, analyst_name: String ,
                    date_topic: String, date_release: String, date_publish: String)
  case class dates(date_topic:String, date_release: String, date_publish: String)

  def get_figures(link: String) = {

    val doc: Document = Jsoup.connect(link).userAgent("Mozilla").ignoreHttpErrors(true).get()
    val link_List: Elements = doc.select("tbody")

    var text_list = new ListBuffer[String]()    
    
    for (entry: Element <- link_List.asScala){
      if (entry.text.contains("Unternehmen:")
            && entry.text.contains("Analyst:")
            && entry.text.contains("Rating jetzt:")) {

        text_list +=entry.select("td").text}
      }
      val fig : Array[Any] = extract_figures(text_list)
      val dat  = extract_date(link)

    figures(fig(0).toString,fig(1).toString,fig(2).toString.toDouble,fig(3).toString,fig(4).toString.toDouble,fig(5).toString.toDouble, fig(6).toString, dat(0).toString, dat(1), dat(2) )
  }

  def extract_figures(text_list: ListBuffer[String]): Array[Any] = {

    val company = try {transform(text_list(0)).get.split(" : ").mkString(", ").split(", ").toList(0).toString
                      } catch {case e: Exception => "None"}

    val analyst = try {transform(text_list(0)).get.split(" : ").mkString(", ").split(", ").toList(1).toString
                      } catch {case e: Exception => "None"}

    val valuation_new = try {transform(text_list(0)).get.split(" : ").mkString(", ").split(", ").toList(2).toDouble
                        } catch {case e: Exception => 0.0}

    val rating = try {transform(text_list(0)).get.split(" : ").mkString(", ").split(", ").toList(3).toString
                        } catch {case e: Exception => "None"}


    val valuation_percent = try {transform(text_list(0)).get.split(" : ").mkString(", ").split(", ").toList(4).toDouble
                        } catch {case e: Exception => 0.0}

    val price_hist = try {transform(text_list(0)).get.split(" : ").mkString(", ").split(", ").toList(5).toDouble
                        } catch {case e: Exception => 0.0}
    
    val analyst_name = try {transform(text_list(0)).get.split(" : ").mkString(", ").split(", ").toList(6).toString
                        } catch {case e: Exception => "None"}

    return Array(company, analyst, valuation_new, rating, valuation_percent, price_hist, analyst_name)

  }
    
  def extract_date(link: String) : Array[String] = {

      //val link = "XXX"
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


  def crawl_text(link: String): statement = {

      val doc: Document = Jsoup.connect(link).userAgent("Mozilla").ignoreHttpErrors(true).get()
      val link_List: Elements = doc.select("p")

      for (entry: Element <- link_List.asScala){
        //println(entry.text)
        if (entry.text.contains("dpa-AFX Analyser") || entry.text.contains("Der Analyst Cascend Securities") ){

          return statement(entry.text)
        
        }} 
        return statement("None") }

  def transform(inputstring: String): Option[String] = {

    val tempstring = inputstring
    .replace("Unternehmen: ","")
    .replace("Analyst", "")
    .replace("Kursziel","")
    .replace("Rating jetzt","")
    .replace("Kurs*","")
    .replace("Abst. Kursziel*","")
    .replace("% Rating vorher: upgrade Kurs aktuell"," ")
    .replace("Kurs aktuell","")
    .replace("Abst. Kursziel aktuell","")
    .replace("Analyst Name","")
    .replace("KGV*","")
    .replace("Ø Kursziel", "")
    .replace("Abst. *", "")
    .replace(" $", "")
    //.replace("%","")
    .replace(" €","")
    .replace("Abst.  aktuell", "")
    .replace(",", ".")
    
    //val slice = Option(str2.slice(0, str2.indexOfSlice("%")+1).replace("%", " "))
    val final_string = Option(tempstring.slice(0, tempstring.indexOfSlice("Rating vorher:")-1).replace("%", " ") + tempstring.slice(tempstring.indexOfSlice("Name: ")+4, tempstring.indexOfSlice("- Ø")-3) )

    // println(Option(str2.slice(0, str2.indexOfSlice("%")+1).replace("%", " ")).filter(!_.isEmpty).getOrElse(       ))
    //val slice2 = Option(str2.slice(str2.indexOfSlice("Name: ")+4, str2.indexOfSlice("- Ø")-3))
    return final_string
  } 

}  
