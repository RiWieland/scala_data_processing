

object stock_analyst_links {

  case class link(Link: String)

  def tryToInt( s: String ) = Try(s.toInt).toOption

  

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


  }

