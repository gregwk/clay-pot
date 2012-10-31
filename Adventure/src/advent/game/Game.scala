package advent.game

import advent.data.Action
import advent.data.GameObject

object Game {

	// ==========================================================
	// Actions
	// ==========================================================

	val actions = List[Action]()

	// ==========================================================
	// Objects
	// ==========================================================

	val objects = List[GameObject](
        new GameObject(
            name = "kitchen",
            kind = "room",
            description = "You are standing in a small kitchen.",
            objects = Map("north_exit" -> "garage")),

        new GameObject(
            name = "garage",
            kind = "room",
            description = "You are standing in a small garage.",
            objects = Map("south_exit" -> "kitchen")),

        new GameObject(
            name = "bob",
            parent = "kitchen",
            kind = "actor",
            description = "You look like any other Bob."))

	// ==========================================================
	// Mutually exclusive properties
	// ==========================================================

	val mutexProps = List[List[String]]()
	
	// ==========================================================
	// Game Info variable
	// ==========================================================

	val gameInfo = new GameInfo(actions, objects, mutexProps, "", "bob")


}