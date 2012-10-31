package editor

import editor.gui.MainEditControl
import editor.model.Model

object GameEditor {
	def main(args: Array[String]) {
	    val model = new Model
	    val initializer = new Initializer(model)
	    initializer.initializeModel
	    initializer.extendModelTest
	    new MainEditControl(model)
	}
}