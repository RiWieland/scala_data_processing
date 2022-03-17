


object crawl_analyst_statement {
  val index_list: List[String] = List("tecdax", "s&p_500")//, "sdax",  "dow_jones", "mdax", "nasdaq_100", "euro_stoxx_50", "smi", "atx", "cac_40", "nikkei_225")         

  def main(args: Array[String]){
    
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

 }

