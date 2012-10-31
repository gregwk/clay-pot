package advent.process

import advent.data.Command

object Resolver {
	
	// returns (Command, String) where String is an error message
	def getCommand(action: String, objList: List[String], itemList: List[String]) = {
		assert(objList.nonEmpty)
		assert(itemList.nonEmpty)
		val obj = objList.head
		val item = itemList.head
		new Command(action, obj, item)
	}
	
	// returns (Command, String) where String is an error message
	def getCommand(action: String, objList: List[String]) = {
		assert(objList.nonEmpty)
		val obj = objList.head
		new Command(action, obj)
	}

}