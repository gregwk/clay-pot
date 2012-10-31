package editor.model

import scala.collection.mutable._

class EditableGameObject(var name: String = "object", var parent: String = "root", var kind: String = "thing") {
    var article = "a"
    var description: String = "This object has no description."

    val adjectives: Set[String] = Set.empty
    val nouns: Set[String] = Set.empty
    val actions: Set[String] = Set.empty
    val properties: Set[String] = Set.empty
    val values: Map[String, Int] = Map.empty
    val objects: Map[String, String] = Map.empty
    val relations: Map[String, Set[String]] = Map.empty
    val messages: Map[String, String] = Map.empty
    val responses: ListBuffer[EditableCondResp] = ListBuffer.empty
}
