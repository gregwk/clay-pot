package advent.store

import scala.collection._
import util.KeyTree
import util.MapTree
import advent.data.GameObject

object World {

    var player = ""; // initializer *must* set this to the player
    private val tree = new MapTree[String, GameObject]

    tree.addRoot("root", new GameObject("root"))
    tree.addChild("root", "generic" -> new GameObject("generic"))

    def location(obj: String) = tree.parent(obj)
    def inScope(obj: String): Boolean = (location(obj) == location(player))
    def contains(obj: String) = tree.contains(obj)

    def objKind(obj: String) = tree.getValue(obj).kind
    def objParent(obj: String) = tree.getValue(obj).parent
    def objAdjectives(obj: String) = tree.getValue(obj).adjectives
    def objNouns(obj: String) = tree.getValue(obj).nouns
    def objProperties(obj: String) = tree.getValue(obj).properties.toSet
    def objValues(obj: String) = tree.getValue(obj).values.toMap
    def objObjects(obj: String) = tree.getValue(obj).objects
    def objDescription(obj: String) = tree.getValue(obj).description
    def objMessages(obj: String) = tree.getValue(obj).messages
    def objResponses(obj: String) = tree.getValue(obj).responses

    /* 
     * Careful, this calls Dictionary. Make sure DictionaryBuilder has finished
     * populating before this happens.
     */
    def updateObjProperty(obj: String, prop: String) {
        val props = tree.getValue(obj).properties
        val incomProp = Dictionary.getIncompatable(prop)
        incomProp.foreach(props -= _)
        props += prop
    }

    def updateObjValue(obj: String, value: String, n: Int) {
        tree.getValue(obj).values += (value -> n)
    }

    def +=(gameObj: GameObject) {
        tree.addChild(gameObj.parent, gameObj.name -> gameObj)
    }

    def objIsWithinSecond(obj: String, sec: String) = tree.hasDescendingPath(sec, obj)
    def moveObjToSecond(obj: String, sec: String) = tree.moveSubtree(obj, sec)

    def addObject(gameObj: GameObject) {
        def addProperty(gameObj: GameObject, prop: String) {
            val props = gameObj.properties
            val incomProp = DictionaryBuilder.getIncompatable(prop)
            incomProp.foreach(props -= _)
            props += prop
        }

        assert(contains(gameObj.kind))
        assert(!contains(gameObj.name))
        val newObj = new GameObject(
            gameObj.name,
            adjectives = gameObj.name.split(" ").toList.init.toSet ++ gameObj.adjectives,
            nouns = Set(gameObj.name.split(" ").toList.last) ++ gameObj.nouns,
            parent = (if (gameObj.kind == "generic") "generic" else gameObj.parent),
            kind = gameObj.kind,
            description = (if (gameObj.description != "") gameObj.description else objDescription(gameObj.kind)),
            properties = mutable.Set.empty,
            values = mutable.Map() ++ objValues(gameObj.kind) ++ gameObj.values,
            objects = objObjects(gameObj.kind) ++ gameObj.objects,
            messages = objMessages(gameObj.kind) ++ gameObj.messages,
            responses = gameObj.responses ::: objResponses(gameObj.kind))

        objProperties(gameObj.kind).foreach(addProperty(newObj, _))
        gameObj.properties.foreach(addProperty(newObj, _))
        newObj.adjectives.foreach(DictionaryBuilder.addAdjective(_, newObj.name))
        newObj.nouns.foreach(DictionaryBuilder.addNoun(_, newObj.name))
        DictionaryBuilder.addObject(newObj.name)
        World += newObj
    }

    def showObject(obj: String) = {
        if (tree.contains(obj))
            tree.getValue(obj).prettyPrint
        else
            "No such object: " + obj
    }

    def showTree(obj: String) = {
        if (tree.contains(obj))
            tree.prettyPrintKeys(obj, 0)
        else
            "No such object: " + obj
    }
}
