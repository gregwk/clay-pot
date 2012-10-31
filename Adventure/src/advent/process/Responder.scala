package advent.process

import advent.data._
import advent.store._

object Responder {

    var responder = ""
    	
    def respond(command: Command): (String, Boolean) = {
    	if (command.action == "none") {
        	val roomName = World.location(World.player)
        	val roomDesc = World.objDescription(roomName)
        	val heading = "<b>" + roomName.capitalize + "</b><br>\n" + roomDesc + "<br>\n"
        	val output = "<b>" + roomName.capitalize + "</b><br>\n" + roomDesc + "<br><br>\n" + textToHtml(command.error)
        	(output, false)
        } else {
            val resp = getResponse(command)
            // TODO for now, ignore outcome = cutscene
            // TODO for now, ignore outcome = redirect
            val message = getMessage(resp)
            updateWorld(resp)
        	val roomName = World.location(World.player)
        	val roomDesc = World.objDescription(roomName)
        	val heading = "<b>" + roomName.capitalize + "</b><br>\n" + roomDesc + "<br>\n"
        	val output = "<b>" + roomName.capitalize + "</b><br>\n" + roomDesc + "<br>" + (if (message == "") "" else "<br>\n" + message)
        	(output, false)
        }
    }
    
    private def actual(obj: String): String = {
        if (obj == "self") return responder
        if (obj == "player") return World.player
        if (obj == "location") return World.location(World.player)
        if (World.objObjects(responder).contains(obj)) return World.objObjects(responder)(obj)
        return obj
    }

    private def getResponse(command: Command): Response = {
        responder = command.obj match {
            case "nothing" => World.location(World.player)
            case _ => command.obj
        }
        val crs = World.objResponses(responder)
        crs.find(conditionMatch(_, command)) match {
            case None => CantResponse("Error: No response for this command.")
            case Some(crx) => crx.response
        }
    }

    private def conditionMatch(condResp: CondResp, command: Command): Boolean = {
        command.action == condResp.command.action &&
            command.obj == actual(condResp.command.obj) &&
            command.item == actual(condResp.command.item) &&
            condResp.conditions.forall(checkCond(_))
    }

    private def getOutcome(response: Response) = {
        response match {
            case NoResponse() => "none"
            case CantResponse(key, message) => "cant"
            case NotYetResponse(key, message) => "not yet"
            case AlreadyResponse(key, message) => "already"
            case OkayResponse(key, message, effects) => "okay"
            case CutsceneResponse(key, message, effects) => "cutscene"
            case RedirectResponse(command: Command) => "redirect"
        }
    }

    private def getMessage(response: Response): String = {
        val (key, message) = response match {
            case NoResponse() => ("", "")
            case CantResponse(key, message) => (key, message)
            case NotYetResponse(key, message) => (key, message)
            case AlreadyResponse(key, message) => (key, message)
            case OkayResponse(key, message, effects) => (key, message)
            case CutsceneResponse(key, message, effects) => (key, message)
            case RedirectResponse(command: Command) => ("", "")
        }
        if (key == "custom") return message
        if (key == "description") return World.objDescription(responder)
        if (World.objMessages(responder).contains("key")) return World.objMessages(responder)(key)
        return "Error: No responder message."
    }

    private def updateWorld(response: Response) {
        response match {
            case NoResponse() => //
            case CantResponse(key, message) => //
            case NotYetResponse(key, message) => //
            case AlreadyResponse(key, message) => //
            case OkayResponse(key, message, effects) => effects.foreach(applyEffect(_))
            case CutsceneResponse(key, message, effects) => effects.foreach(applyEffect(_))
            case RedirectResponse(command) => getResponse(command) // check termination
        }
    }

    private def checkCond(condition: Condition): Boolean = {
        condition match {
            case PropertyCondition(obj, prop) =>
                World.objProperties(actual(obj)).contains(prop)
            case LocationCondition(obj, sec) =>
                World.objIsWithinSecond(actual(obj), sec)
            case RelationCondition(obj, key, obj2) =>
                World.objObjects(actual(obj))(key) == obj2
            case ValueCondition(obj, key, value) =>
                World.objValues(actual(obj))(key) == value
            case _ => false // should never reach here
        }
    }

    private def applyEffect(effect: Effect) {
        effect match {
            case PropertyEffect(obj, prop) =>
                World.updateObjProperty(actual(obj), prop)
            case MoveEffect(obj, sec) =>
                World.moveObjToSecond(actual(obj), actual(sec))
            case ValueEffect(obj, key, num) =>
                World.updateObjValue(actual(obj), key, num)
            case _ => // should never reach here
        }
    }

    private def resolveBraces(message: String, command: Command): String = {
    	val IfThenExtractor = """\{(.+) ?? (.+)\}""".r
    	val IfThenElseExtractor = """\{(.+) ?? (.+) :: (.+)\}""".r
        def replacement(s: String) = {
        	s match {
        		case "{object}" => objStr(command.obj)
        		case "{item}" => objStr(command.item)
        		case "{self}" => objStr(responder)
        		case IfThenExtractor(cond, str) => {
        			//assert(wellFormed(cond))
       				if (check(cond)) str else ""
        		}
        		case IfThenElseExtractor(cond, str1, str2) => {
        			//assert(wellFormed(cond))
        			if (check(cond)) str1 else str2
        		}
        		case t => t
        	}
        }
        def objStr(obj: String) = {
            if (World.objProperties(obj).contains("proper")) {
                obj.capitalize
            } else {
                "the " + obj
            }
        }
//        def wellFormed(cond: String): Boolean = {
//        	val words = cond.split(" ").toList
//        	words.size match {
//        		case 0 => false
//        		case 1 => Constraint.isProperty(words(0))
//        		case 2 => false
//        		case 3 => {
//        			(Constraint.isAliasObj(words(0)) || World.contains(words(0))) &&
//        			words(1) == "has" &&
//        			Constraint.isProperty(words(2))
//        		}
//        		case _ => false
//        	}
//        }
        def check(cond: String): Boolean = {
        	false
        }
        val pattern = """\{[^\}]+\}""".r

        // { cond ?? words :: words } if - then - else
        // { cond ?? words } if - then
        // cond = prop | obj has prop
        // { object } => command.object
        // { item } => command.item
        // { self } => responder
        // { location } => location(responder) ??
        // { key } ??

        return ""
    }
    
    private def textToHtml(input: String) = {
		input.replaceAll(""" """, "&nbsp;").replaceAll("""\n""", "<br>").replaceAll("""\t""", "&nbsp;&nbsp;&nbsp;&nbsp;")
	}
}