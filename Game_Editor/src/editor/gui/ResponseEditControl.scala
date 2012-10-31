package editor.gui

import editor.model.Model
import editor.model.EditableGameObject

class ResponseEditControl(val model: Model) {
    val view = new ResponseEditView
    val obj = new EditableGameObject()
    
}