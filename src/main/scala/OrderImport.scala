package example

object ExampleApplication extends App {
 
  import Shop._
  import AddOrderLine._
  import SetComment._

  val importSource = "abc1,0.5\ndef2,23\nghi3,45"
  
  implicit lazy val orderWithAddOrderLineWithSetComment: AddOrderLine[Order] with SetComment[Order]
      = new AddOrderLine[Order] with SetComment[Order] {

    def addOrderLine[T](order: Order, line: T) = line match {
      case Some(orderline @ OrderLine(_,_)) => order.copy( lines = orderline :: order.lines)
      case _ => order
    }

    def setComment(order: Order, comment: String) = {
      order.copy(comment = Some(comment))
    }
  }

  val order = Order("empty order", Nil, None)
  def extractData(line: List[String]): Option[OrderLine] = line match {
    case articleRef :: quantity :: rest => Some(OrderLine(articleRef, BigDecimal(quantity)))
    case _ => None
  } 
  
  val importService = new ImportService(extractData)

  println(importService.doImport(order, importSource))

}

