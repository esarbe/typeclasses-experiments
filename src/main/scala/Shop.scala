package example

object Shop { 

  case class Order(name: String, lines: List[OrderLine], comment: Option[String])
  case class OrderLine(articleRef: String, quantity: BigDecimal)

}
