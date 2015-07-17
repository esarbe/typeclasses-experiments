package example

class ImportService[T](extractData: List[String] => T) {
  
  import SetComment._
  import AddOrderLine._
  
  def doImport[B : AddOrderLine : SetComment](builder: B, source: String): B = {

    val data = for {
      line <- source.split('\n')
    } yield line.split(',').toList

    val lines = data.toList.map { extractData }

    val withArticles = lines.foldLeft(builder){ case (builder, data) => builder.addOrderLine(data)}
    
    withArticles.setComment("created by import")
  }
}

