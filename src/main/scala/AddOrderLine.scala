package example

trait AddOrderLine[O] {
    def addOrderLine[T](order: O, line: T): O
}

object AddOrderLine {
  implicit class AddOrderLineOps[O](order: O)(implicit ev: AddOrderLine[O]) {
    def addOrderLine[T](line: T): O = ev.addOrderLine(order, line)
  }
}

