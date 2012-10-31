package advent.data

abstract class Effect
case class PropertyEffect(obj: String, property: String) extends Effect
case class MoveEffect(obj: String, parent: String) extends Effect
case class ValueEffect(obj: String, key: String, value: Int) extends Effect
