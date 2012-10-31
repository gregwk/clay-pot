package advent.game

import scala.collection._
import advent.data._

object Base {
	
	// ==========================================================
	// Actions
	// ==========================================================

	val actions = List(
	    new Action("go_north", List("go north", "north", "n")),
		new Action("go_south", List("go south", "south", "s")),
		new Action("go_east", List("go east", "east", "e")),
		new Action("go_west", List("go west", "west", "w")),
		new Action("go_northeast", List("go northeast", "northeast", "ne")),
		new Action("go_northwest", List("go northwest", "northwest", "nw")),
		new Action("go_southeast", List("go southeast", "southeast", "se")),
		new Action("go_southwest", List("go southwest", "southwest", "sw")),
		new Action("go_up", List("go up", "up", "u")),
		new Action("go_down", List("go down", "down", "d")),
		// player movement
		new Action("climb", List("climb {object}")),
		new Action("hide_under", List("hide under {object}")),
		new Action("hide_behind", List("hide behind {object}")),
		new Action("sit_down", List("sit down", "sit")),
		new Action("lie_down", List("lie down")),
		// player inspection
		new Action("examine", List("examine {object}", "x {object}")),
		new Action("look", List("look", "l")),
		new Action("inventory", List("inventory", "inv", "i")),
		new Action("search", List("search")),
		new Action("listen", List("listen")),
		new Action("smell", List("smell")),
		new Action("look_under", List("look under {object}")),
		new Action("look_behind", List("look behind {object}")),
		new Action("look_through", List("look through {object}")),
		// object manipulation
		new Action("use", List("use {object}")),
		new Action("turn_on", List("turn on {object}")),
		new Action("turn_off", List("turn off {object}")),
		new Action("open", List("open {object}")),
		new Action("close", List("close {object}")),
		new Action("lock", List("unlock {object}")),
		new Action("unlock", List("unlock {object}")),
		new Action("push", List("push {object}")),
		new Action("pull", List("pull {object}")),
		new Action("turn", List("turn {object}")),
		new Action("eat", List("eat {object}")),
		new Action("drink", List("drink {object}")),
		// object combination
		new Action("combine_with", List("combine {item} with {object}")),
		new Action("put_in", List("put {item} in {object}", "insert {item} in {object}")),
		new Action("put_on", List("put {item} on {object}")),
		new Action("tie_to", List("tie {item} to {object}")),
		// object taking
		new Action("take", List("take {object}")),
		new Action("wear", List("wear {object}")),
		// NPC interaction
		new Action("talk_to", List("talk to {object}")),
		new Action("attack", List("attack {object}"))
	)

	// ==========================================================
	// Objects
	// ==========================================================

	val objects: List[GameObject] = List(genericRoom, genericActor, genericThing)

    def genericRoom = {
        val directions = List("north", "south", "east", "west", "ne", "nw", "se", "sw", "up", "down")

        def crList(dir: String) =
            List(
                CondResp(Command("look"),
                    List(),
                    OkayResponse()),
                // can't
                CondResp(Command("go_" + dir),
                    List(RelationCondition("self", dir + "_exit", "nothing")),
                    CantResponse(message = "You can't go " + dir + " from here.")),
                // okay
                CondResp(Command("go_" + dir),
                    List(RelationCondition("self", dir + "_door", "nothing")),
                    OkayResponse(effects = List(MoveEffect("player", dir + "_exit")))),
                // not yet
                CondResp(Command("go_" + dir),
                    List(PropertyCondition(dir + "_door", "closed")),
                    NotYetResponse(message = "You need to open {" + dir + "_door} before going " + dir + ".")),
                // okay
                CondResp(Command("go_" + dir),
                    List(PropertyCondition(dir + "_door", "open")),
                    OkayResponse(effects = List(MoveEffect("player", dir + "_exit")))))

        val dirResponses = directions.flatMap(dir => crList(dir))

        new GameObject(
            name = "room",
            kind = "generic",
            properties = mutable.Set("light"),
            objects = directions.flatMap(dir => List(dir + "_door" -> "nothing", dir + "_exit" -> "nothing")).toMap,
            responses = dirResponses)
    }

    private def genericActor = {
        
        val crList =
            List(CondResp(Command("examine", obj = "self"), List(), OkayResponse(key = "description")))

        new GameObject(
            name = "actor",
            kind = "generic",
            properties = mutable.Set("proper"),
            responses = crList)
    }

    private def genericThing = new GameObject(name = "thing", kind = "generic")
    
	// ==========================================================
	// Mutually exclusive properties
	// ==========================================================

	val mutexProps = List(
        List("open", "closed"),
        List("unlocked", "locked"),
        List("off", "on"),
        List("male", "female", "neutral"),
        List("light", "dark"),
        List("common", "proper"),
        List("solved", "unsolved")
    )
    
	// ==========================================================
	// Game Info variable
	// ==========================================================

    val gameInfo = new GameInfo(actions, objects, mutexProps, "", "")

}