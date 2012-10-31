package advent.store

class Entry(val word: String, val pos: String, val action: String, val obj: String) {
    override def toString() = {
        val abbrevPOS = pos match {
            case "adjective" => "adj"
            case "adverb" => "adv"
            case "noun" => "n"
            case "preposition" => "prep"
            case "unknonwn" => "unk"
            case "verb" => "v"
            case _ => "ERROR"
        }
        word + " (" + abbrevPOS + ") ->" +
            (if (action != "none") " " + action else "") +
            (if (obj != "nothing") " " + obj else "")
    }
}
