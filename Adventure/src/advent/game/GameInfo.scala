package advent.game

import advent.data.Action
import advent.data.GameObject

class GameInfo(
		val actions: List[Action],
		val objects: List[GameObject],
		val mutexProps: List[List[String]],
		val introduction: String,
		val player: String) {
}