package editor

import editor.model.Model
import editor.gui.ObjectEditControl

object ObjectEditor {
	def main(args: Array[String]) {
	    val model = new Model
	    new ObjectEditControl(model)
	}
}