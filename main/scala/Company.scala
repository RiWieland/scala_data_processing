

object stock_analyst_links {

  case class link(Link: String)

  def tryToInt( s: String ) = Try(s.toInt).toOption

  def create_landing_link(company: String, page: Int) : String = s"XXX"

  

  def get_max_page(link_raw: Elements ) : Option[Int] = {

    val list_num = new ListBuffer[Option[Int]]()

      for (entry: Element <- link_raw.asScala){

        if (entry.attr("href").toLowerCase.contains("p=")){
          val temp = entry.attr("href").substring(3)
          list_num += tryToInt(temp)
        }
    }
    return list_num.max
  }

  def get_webpage_content(link:String, html_element:String) : Elements = {
    val doc: Document = Jsoup.connect(link).userAgent("Mozilla").get()
    val link_raw: Elements = doc.select(html_element)

    return link_raw
  }

  def get_history(company: String) : ListBuffer[link] ={
  
    // Init: 
    val links_list = new ListBuffer[link]()
    val max_range = get_max_page(get_webpage_content(create_landing_link(company, 1), "a[href]"))

    for( page <- 1 to max_range.getOrElse(1) ){ //max_range.getOrElse(1)){//max_range.getOrElse(1)){
      
      val link_raw : Elements = get_webpage_content(create_landing_link(company, page), "a[href]")

      println("crawling page " + page + " for company " + company)

      for (entry: Element <- link_raw.asScala){      
        
        if (entry.attr("href").toLowerCase.contains("/analyse/")){
          
          links_list += link("https://www.finanzen.net" + entry.attr("href")) 

        }
      }
    }
    return links_list
  }
}

  }

