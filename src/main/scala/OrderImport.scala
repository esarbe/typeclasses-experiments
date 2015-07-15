package example

object ExampleApplication extends App {
 
  import AddOrderLine._
  import SetComment._

  val importSource = "abc1,0.5\ndef2,23\nghi3,45"

  val order = Order("empty order", Nil, None)

  println(doImport(order, importSource))


  def doImport[B : AddOrderLine : SetComment](builder: B, source: String): B = {

    val data = for {
      line <- source.split('\n')
    } yield line.split(',').toList

    data.foreach(println)

    val lines = data.collect { 
      case articleRef :: quantity :: rest => Some((articleRef, quantity.toFloat))
      case _ => None
    }

    lines.foreach(println)

    val withArticles = lines.foldLeft(builder){ case (builder, Some((articleRef, quantity))) => builder.addOrderLine(articleRef, quantity)}
    withArticles.setComment("created by import")
  }

  case class Order(name: String, lines: List[OrderLine], comment: Option[String])
  case class OrderLine(articleRef: String, quantity: BigDecimal)

  trait AddOrderLine[O] {
     def addOrderLine(order: O, articleRef: String, quantity: BigDecimal): O
  }

  object AddOrderLine {
    implicit class AddOrderLineOps[O](order: O)(implicit ev: AddOrderLine[O]) {
      def addOrderLine(articleRef: String, quantity: BigDecimal): O = ev.addOrderLine(order, articleRef, quantity)
    }
  }

  trait SetComment[C] {
    def setComment(commentable: C, comment: String): C
  }

  object SetComment {
    implicit class SetCommentOps[C](commentable: C)(implicit ev: SetComment[C]) {
      def setComment(comment: String): C = ev.setComment(commentable, comment)
    }
  }

  implicit lazy val orderWithAddOrderLineWithSetComment: AddOrderLine[Order] with SetComment[Order]
      = new AddOrderLine[Order] with SetComment[Order] {

    def addOrderLine(order: Order, articleRef: String, quantity: BigDecimal) = {
      val line = OrderLine(articleRef, quantity)
      order.copy( lines = line :: order.lines)
    }

    def setComment(order: Order, comment: String) = {
      order.copy(comment = Some(comment))
    }
  }
}

