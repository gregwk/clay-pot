package editor.model

import util.MapTree
import scala.collection.mutable.ListBuffer

class Model {
    val world = new MapTree[String, EditableGameObject]
    val actions = new ListBuffer[String]
    val properties = new ListBuffer[String]
    
    var rootAdded = false
    def addToWorld(obj: EditableGameObject) {
        if (rootAdded == false) {
            assert(obj.name == "root")
            assert(obj.parent == "root")
            assert(obj.kind == "header")
            world.addRoot((obj.name, obj))
            rootAdded = true
        } else {
        	world.addChild(obj.parent, (obj.name, obj))
        }
    }
}