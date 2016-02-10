package controllers

import play.api.mvc.{Action, Controller}
import shared.views.MyWidget

/**
  * Created by matthewdedetrich on 11/02/2016.
  */
class MainController extends Controller {
  def myWdiget = Action {
    Ok(MyWidget.template)
  }
  
}
