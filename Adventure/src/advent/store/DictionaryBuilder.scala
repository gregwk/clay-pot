package advent.store

import scala.collection.mutable
import advent.data.Action
import advent.data.Pattern

object DictionaryBuilder {

    private val dMap: mutable.Map[String, Set[Entry]] = mutable.Map.empty
    private val pMap: mutable.Map[String, List[Pattern]] = mutable.Map.empty

    private val actions: mutable.Set[String] = mutable.Set.empty
    private val objects: mutable.Set[String] = mutable.Set.empty
    private val mutexProps: mutable.Set[List[String]] = mutable.Set.empty

    def fixDMap = dMap.toMap
    def fixPMap = pMap.toMap
    
    def fixActions = actions.toSet
    def fixObjects = objects.toSet
    def fixMutexProps = mutexProps.toSet
    
    def addObject(obj: String) { objects += obj }
    def addMutexProps(mprops: List[String]) { mutexProps += mprops }
    
    /* 
     * This is the same function that is in Dictionary. Because we are essentially using
     * a singleton builder for dictionary, we need to ensure that Dictionary is not called
     * until everything has been added to DictionaryBuilder. Since adding properties
     * to World requires eliminating mutex properties, that must be done with a DictionaryBuilder
     * function.
     * 
     * One alternative is to check at the time of adding properties that they
     * are not incompatible with an assert statement
     */
    def getIncompatable(property: String): List[String] = {
        mutexProps.find(_.contains(property)) match {
            case None => List[String]()
            case Some(xs) => (xs - property)
        }
    }

    private def add(word: String, pos: String, action: String, obj: String) {
        assert(if (pos == "unknown") action == "none" && obj == "nothing" else true)
        assert(if (pos == "verb" || pos == "adverb" || pos == "preposition") action != "none" && obj == "nothing" else true)
        assert(if (pos == "adjective" || pos == "noun") action == "none" && obj != "nothing" else true)
        val entry = new Entry(word, pos, action, obj)
        val entrySet = if (dMap.contains(word)) dMap(word) + entry else Set(entry)
        dMap += (word -> entrySet)
    }

    /**
     * Adds a new action to the grammar.
     */
    def addAction(action: Action) {
        def addPattern(p: Pattern) {
            addVerb(p.verb, p.action)
            if (p.adverb != "") addAdverb(p.adverb, p.action)
            if (p.prep != "") addPreposition(p.prep, p.action)
            val patList = if (pMap.contains(p.verb)) p :: pMap(p.verb) else List(p)
            pMap += (p.verb -> patList)
        }
        actions += action.name
        action.patterns.foreach(addPattern(_))
    }

    def addVerb(word: String, action: String) { add(word, "verb", action, "nothing") }
    def addAdverb(word: String, action: String) { add(word, "adverb", action, "nothing") }
    def addPreposition(word: String, action: String) { add(word, "preposition", action, "nothing") }
    def addAdjective(word: String, obj: String) { add(word, "adjective", "none", obj) }
    def addNoun(word: String, obj: String) { add(word, "noun", "none", obj) }
    def addUnknown(word: String) { add(word, "unknown", "none", "nothing") }
}
