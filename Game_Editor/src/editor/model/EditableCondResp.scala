package editor.model

import scala.collection.mutable._

class EditableCondResp {
    
    class EditableCondition {
        var kind = "property"
        var obj = ""
        var second = ""
        var key = ""
        var value = 0
    }
    
    class EditableEffect {
        var kind = "property"
        var obj = ""
        var second = ""
        var key = ""
        var value = 0
    }
    
    class EditableResponse {
        var kind = "okay"
        var key = "" // I'm not sure why this is here
        var message = ""
        val effects: ListBuffer[EditableEffect] = ListBuffer.empty
    }
    
    var action = ""
    var obj = ""
    var item = ""
    val conditions: ListBuffer[EditableCondition] = ListBuffer.empty
    var response = new EditableResponse()
}
