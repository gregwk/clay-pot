package advent.data

abstract class Condition
case class PropertyCondition(obj: String, property: String) extends Condition
case class LocationCondition(obj: String, parent: String) extends Condition
case class RelationCondition(obj: String, relation: String, second: String) extends Condition
case class ValueCondition(obj: String, key: String, value: Int) extends Condition
