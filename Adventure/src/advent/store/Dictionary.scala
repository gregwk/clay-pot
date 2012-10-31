package advent.store

import scala.collection.mutable
import advent.data.Action
import advent.data.Pattern

/**
 * The (singleton) Dictionary object contains both a Dictionary part -- a mapping from words to entries --
 * and a Grammar part -- a mapping from actions to patterns.
 *
 * The Dictionary part is a map from words (String) to sets of entries (Set[Entry]).
 * An entry contains: a part-of-speech (word class) which is one of:
 *     verb, adverb, preposition, adjective, noun, unknown
 *
 * If the part of speech is a verb, adverb, or preposition, then the word should be associated with some action.
 * If the part of speech is an adjective or noun, then the word should be associated with some object.
 * If the part of speech is unknown, then the word should not be associated with either action or object.
 *
 * The Grammar part can be viewed as a
 *
 * @author Gregory Kulczycki
 *
 */
object Dictionary {

    case class MatchResult(
        val action: String = "none",
        val itemWords: List[String] = List(),
        val objWords: List[String] = List(),
        val usageString: String = "",
        val error: String = "")

    val partsOfSpeech = Set("adjective", "adverb", "noun", "preposition", "unknown", "verb")

    private val dMap: Map[String, Set[Entry]] = DictionaryBuilder.fixDMap
    private val pMap: Map[String, List[Pattern]] = DictionaryBuilder.fixPMap

    private val actions: Set[String] = DictionaryBuilder.fixActions
    private val objects: Set[String] = DictionaryBuilder.fixObjects
    private val mutexProps: Set[List[String]] = DictionaryBuilder.fixMutexProps

    def hasAction(action: String) = actions.contains(action)
    def hasObject(obj: String) = objects.contains(obj)
    def hasProperty(prop: String) = mutexProps.flatten.contains(prop)

    def getIncompatable(property: String): List[String] = {
        mutexProps.find(_.contains(property)) match {
            case None => List[String]()
            case Some(xs) => (xs - property)
        }
    }

    def patternList(verb: String) = pMap(verb)

    def contains(word: String) = dMap.contains(word)

    /**
     * Returns a set of all objects associated with the specified noun.
     */
    def getObjectsForNoun(noun: String): Set[String] = {
        dMap(noun).filter(_.pos == "noun").map(_.obj)
    }

    /**
     * Returns a set of all objects associated with the specified adjective.
     */
    def getObjectsForAdjective(adj: String): Set[String] = {
        dMap(adj).filter(_.pos == "adjective").map(_.obj)
    }

    def wellFormedWord(w: String) = w.matches("[-a-zA-Z0-9'_]+")

    private def hasPos(word: String, pos: String) = {
        assert(dMap.contains(word))
        dMap(word).exists(_.pos == pos)
    }

    def hasVerb(word: String) = hasPos(word, "verb")
    def hasAdverb(word: String) = hasPos(word, "adverb")
    def hasPreposition(word: String) = hasPos(word, "preposition")
    def hasAdjective(word: String) = hasPos(word, "adjective")
    def hasNoun(word: String) = hasPos(word, "noun")

    override def toString() = {

        val entrySetIterator = dMap.values
        val entryIterator = entrySetIterator.flatMap(_.toList)
        val sortedEntryList = entryIterator.toList.sortBy(_.word)
        val dictionary = sortedEntryList.mkString("\n")

        val patternSetIterator = pMap.values
        val patternIterator = patternSetIterator.flatMap(_.toList)
        val sortedPatternList = patternIterator.toList.sortBy(_.text)
        val grammar = sortedPatternList.mkString("\n")

        "Actions(" + actions.mkString(", ") + ")\n" +
            "Objects(" + objects.mkString(", ") + ")\n" +
            "Properties(" + mutexProps.flatten.toList.sorted.mkString(", ") + ")\n" +
            "Dictionary\n" + dictionary +
            "Grammar\n" + grammar
    }
}