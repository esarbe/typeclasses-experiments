package example

trait SetComment[C] {
  def setComment(commentable: C, comment: String): C
}

object SetComment {
  implicit class SetCommentOps[C](commentable: C)(implicit ev: SetComment[C]) {
    def setComment(comment: String): C = ev.setComment(commentable, comment)
  }
}

