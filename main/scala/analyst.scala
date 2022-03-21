package analyst

import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}
import org.jsoup.select.Elements;

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
