package advent.data

class Pattern(val text: String, val action: String) {

    private val words = text.split(" ")

    assert(words.forall(w => w == "{item}" || w == "{object}" || wellFormedWord(w)), "One or more ill-formed words in grammar pattern " + text)
    assert(words.nonEmpty, "No words in grammar pattern.")
    assert(wellFormedWord(words(0)), "First word cannot be {item} or {object} in grammar pattern: " + text)
    assert(words.count(_ == "{item}") <= 1, "More than one {item} in grammar pattern: " + text)
    assert(words.count(_ == "{object}") <= 1, "More than on {object} in grammar pattern: " + text)
    assert(!hasAdjBraces(words.toList), "{item} and {object} cannot be next to each other in grammar pattern: " + text)

    private def hasAdjBraces(words: List[String]): Boolean = words match {
        case Nil => false
        case x :: Nil => false
        case x :: y :: zs => x.startsWith("{") && y.startsWith("{") || hasAdjBraces(y :: zs)
    }
    
    // This should have the same definition as Dictionary.wellFormedWord(w: String)
    def wellFormedWord(w: String) = w.matches("[-a-zA-Z0-9'_]+")

    val verb = words(0)
    val (adverb: String, hasItem: Boolean, prep: String, hasObj: Boolean, itemBeforeObj: Boolean) = words.size match {
        case 1 => ("", false, "", false, true)
        case 2 => words(1) match {
            case "{item}" => throw new RuntimeException("{item} not permitted without preposition in pattern: " + text)
            case "{object}" => ("", true, "", false, true)
            case _ => (words(1), false, "", false, true)
        }
        case 3 => words(2) match {
            case "{item}" => throw new RuntimeException("{item} not permitted without preposition in pattern: " + text)
            case "{object}" => (words(1), true, "", false, true)
            case _ => new RuntimeException("Ill-formed grammar pattern: " + text)
        }
        case 4 => (words(1), words(3)) match {
            case ("{item}", "{object}") => ("", true, words(2), true, true)
            case ("{object}", "{item}") => ("", true, words(2), true, false)
            case _ => throw new RuntimeException("Ill-formed grammar pattern: " + text)
        }
        case 5 => (words(2), words(4)) match {
            case ("{item}", "{object}") => (words(1), true, words(3), true, true)
            case ("{object}", "{item}") => (words(1), true, words(3), true, false)
            case _ => throw new RuntimeException("Ill-formed grammar pattern: " + text)
        }
    }
    
    //TODO This does not consider the case where the adverb comes after the object
    val regex =
        if (hasItem) List(verb, adverb, "(.+)", prep, "(.+)").filterNot(_ == "").mkString(" ").r
        else if (hasObj) List(verb, adverb, prep, "(.+)").filterNot(_ == "").mkString(" ").r
        else List(verb, adverb).filterNot(_ == "").mkString(" ").r

    override def toString = text + " -> " + action
}

