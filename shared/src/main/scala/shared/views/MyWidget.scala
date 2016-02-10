package shared
package views

import scalatags.Text.all._

object MyWidget {
  def template = {
    div(
      p("This is my cross compiled shared widget")
    )
  }.render
}
