package advent.data

import scala.collection._

class GameObject(val name: String,
    val kind: String = "thing",
    val parent: String = "root",
    val adjectives: Set[String] = Set.empty,
    val nouns: Set[String] = Set.empty,
    val actions: Set[String] = Set.empty,
    val properties: mutable.Set[String] = mutable.Set.empty, // mutable
    val values: mutable.Map[String, Int] = mutable.Map.empty, // mutable
    val objects: Map[String, String] = Map.empty,
    val relations: Map[String, Set[String]] = Map.empty,
    val description: String = "This object has no description",
    val messages: Map[String, String] = Map.empty,
    val responses: List[CondResp] = List.empty) {
    override def toString = name
    def prettyPrint =
        name + " (" + kind + ")" + " [ " +
            (if (adjectives.isEmpty) "" else adjectives.toList.sorted.mkString(" ") + " | ") +
            nouns.toList.sorted.mkString(" ") + " ]\n" +
            "actions: " + actions.toList.sorted.mkString(" ") + "\n" +
            "description: " + description + "\n" +
            "original location: " + parent + "\n" +
            "properties: { " + properties.toList.sorted.mkString(" ") + " }\n" +
            "values: " + nonConstMap(values, 0) +
            "objects: " + nonConstMap(objects, "nothing") +
            "messages: " + nonConstMap(messages, "") +
            "responses: NOT IMPLEMENTED YET"
    private def nonConstMap[V](m: Map[String, V], constant: V) = {
    	val nonConst = m.filterNot(_._2 == constant)
    	if (nonConst.isEmpty) {
    		"N/A\n"
    	} else {
    		"\n" + nonConst.mkString(":").split(":").toList.sorted.mkString("\n") + "\n"
    	}
    }
}
