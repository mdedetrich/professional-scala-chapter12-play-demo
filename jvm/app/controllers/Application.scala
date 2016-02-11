package controllers

import play.api.mvc.{Action, Controller}
import shared.views.MyWidget

/**
  * Created by matthewdedetrich on 11/02/2016.
  */
class Application extends Controller {
  def index(any: String) = Action {
    Ok("index")
  }
  
  def myWidget = Action {
    Ok(MyWidget.template)
  }
  
}
