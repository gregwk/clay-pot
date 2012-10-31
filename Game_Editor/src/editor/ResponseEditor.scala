package editor

import editor.model.Model
import editor.gui.ResponseEditControl

object ResponseEditor {
	def main(args: Array[String]) {
	    val model = new Model
	    new ResponseEditControl(model)
	}
}