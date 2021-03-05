package index



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

}
