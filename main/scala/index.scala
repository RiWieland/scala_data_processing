package index



object index{

  case class Index_object(Index: String, Stock: String)

  def landing_index(index: String, page: Int ) : String = s"XXX/${index}/werte?p=${page}"

  def tryToInt(s: String) = Try(s.toInt).toOption


}
