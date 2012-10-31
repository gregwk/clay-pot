package advent.data

import advent.store.Dictionary

class Action(val name: String, val textPatterns: List[String], val responses: List[CondResp] = List.empty) {

    val patterns: List[Pattern] = textPatterns.map(new Pattern(_, name))
    
    override def toString = name + "\n" + textPatterns.mkString("\t", "\n\t", "\n") + "DEFAULT RESPONSES NOT IMPLEMENTED"
}
