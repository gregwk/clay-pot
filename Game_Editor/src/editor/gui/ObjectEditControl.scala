package editor.gui

import editor.model.Model
import editor.model.EditableGameObject

class ObjectEditControl(val model: Model) {
    val view = new ObjectEditView
    val obj = new EditableGameObject()
    
}