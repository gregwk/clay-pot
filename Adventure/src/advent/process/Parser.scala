package advent.process

import advent.data.Action
import advent.data.Pattern
import advent.data.Command
import advent.store.Dictionary

object Parser {

    case class MatchResult(
        val action: String = "none",
        val itemWords: List[String] = List(),
        val objWords: List[String] = List(),
        val usageString: String = "",
        val error: String = "")

    def parse(input: String): Command = {
        val cmd = parse2(input)
        assert(if (cmd.action == "none") cmd.error != "" else cmd.error == "")
        cmd
    }

    private def parse2(input: String): Command = {

        // Error: Unrecognized characters
        // Only whitespace | alphanumeric | _ | - | ' permitted
        val unknownCharacters = """[^\w\s-']""".r
        val unknownCharSet = unknownCharacters.findAllIn(input).toSet
        if (unknownCharSet.nonEmpty) {
            val unknownChars = unknownCharSet.mkString(" ")
            val errorMessage = "I don't recognize these characters: " + unknownChars;
            return new Command(action = "none", error = errorMessage)
        }

        // Remove extra white space; make all characters lower case
        val input2 = input.replaceAll("""\s+""", " ").trim.toLowerCase
        // Remove word 'the' from input
        val input3 = input2.replaceAll(""" the|the """, "")

        // Error: Unknown words
        val unknownWordSet = input3.split(" ").toList.filterNot(Dictionary.contains(_)).toSet
        if (unknownWordSet.nonEmpty) {
            val unknownWords = unknownWordSet.mkString(" ")
            val errorMessage = "I don't recognize these words: " + unknownWords
            return new Command(action = "none", error = errorMessage)
        }

        // Error: First word is not a verb
        val firstWord = input3.split(" ")(0)
        if (!Dictionary.hasVerb(firstWord)) {
            val errorMessage = "I don't recognize '" + firstWord + "' as a verb."
            return new Command(action = "none", error = errorMessage)
        }

        // Match grammar patterns
        val matchResult = matchCommand(input3)

        // Error: Could not match input to grammar pattern
        if (matchResult.error != "") {
            return new Command(action = "none", error = matchResult.error)
        }

        if (matchResult.itemWords.nonEmpty) {
            // Error: item words did not have a noun at end
            val itemNoun = matchResult.itemWords.last
            if (!Dictionary.hasNoun(itemNoun)) {
                val errorMessage = "Matched pattern: " + matchResult.usageString + "\n" +
                    "I don't recognize '" + itemNoun + "' as a noun."
                return new Command("none", error = errorMessage)
            }
            // Error: item words before noun were not all adjectives 
            val itemAdjList = matchResult.itemWords.init
            val nonItemAdjList = itemAdjList.filterNot(Dictionary.hasAdjective(_))
            if (nonItemAdjList.nonEmpty) {
                val errorMessage = "Matched pattern: " + matchResult.usageString + "\n" +
                    "I don't recognize the following words as adjectives: " + nonItemAdjList.mkString(" ")
                return new Command("none", error = errorMessage)
            }
        }

        if (matchResult.objWords.nonEmpty) {
            // Error: object words did not have a noun at end
            val objNoun = matchResult.objWords.last
            if (!Dictionary.hasNoun(objNoun)) {
                val errorMessage = "Matched pattern: " + matchResult.usageString + "\n" +
                    "I don't recognize '" + objNoun + "' as a noun."
                return new Command("none", error = errorMessage)
            }

            // Error: object words before noun were not all adjectives
            val objAdjList = matchResult.objWords.init
            val nonObjAdjList = objAdjList.filterNot(Dictionary.hasAdjective(_))
            if (nonObjAdjList.nonEmpty) {
                val errorMessage = "Matched pattern: " + matchResult.usageString + "\n" +
                    "I don't recognize the following words as adjectives: " + nonObjAdjList.mkString(" ")
                return new Command("none", error = errorMessage)
            }
        }

        // Get potential game objects for item words and object words
        val itemList = getPotentialGameObjects(matchResult.itemWords)
        val objList = getPotentialGameObjects(matchResult.objWords)

        // Error: no game object matches item words
        if (matchResult.itemWords.nonEmpty && itemList.isEmpty) {
            val errorMessage = "Matched pattern: " + matchResult.usageString + "\n" +
                "I could not find an object that matched item words: " + matchResult.itemWords.mkString(" ")
            return new Command("none", error = errorMessage)
        }

        // Error: no game object matches object words
        if (matchResult.objWords.nonEmpty && objList.isEmpty) {
            val errorMessage = "Matched pattern: " + matchResult.usageString + "\n" +
                "I could not find an object that matched object words: " + matchResult.objWords.mkString(" ")
            return new Command("none", error = errorMessage)
        }

        // Call Resolver to find best match for item and object in context, and return the command
        if (matchResult.itemWords.nonEmpty) { // assume object words are non-empty also
            Resolver.getCommand(matchResult.action, objList, itemList)
        } else if (matchResult.objWords.nonEmpty) {
            Resolver.getCommand(matchResult action, objList)
        } else {
            new Command(matchResult.action)
        }
    }

    private def getPotentialGameObjects(words: List[String]): List[String] = {
        if (words.isEmpty) return List.empty

        assert(Dictionary.hasNoun(words.last))
        assert(words.init.forall(Dictionary.hasAdjective(_)))

        val revWords = words.reverse
        val noun = revWords.head
        val adjList = revWords.tail
        val nounObjSet = Dictionary.getObjectsForNoun(noun)
        val adjObjSet = adjList.flatMap(Dictionary.getObjectsForAdjective(_)).toSet
        if (adjObjSet.isEmpty) nounObjSet.toList
        else (nounObjSet & adjObjSet).toList
    }

    /**
     * Returns a match result that indicates whether the specified string matches any entry in the grammar.
     * If a match is found, the match result returns the associated action, a list of object words (if any)
     * and a list of item words (if any). It also returns a usage string that can be used in subsequent
     * error messages. If no match is found, the action "none" is returned along with an appropriate error message.
     *
     * This method makes the following assumptions:
     * + the input string is in all lower case
     * + the input string does not contain the word 'the'
     * + all words in the input string are separated by a single space
     * + all words in the input string are in the dictionary
     * + the first word is a verb according to the dictionary
     */
    private def matchCommand(command: String): MatchResult = {
        val cmdWords = command.split(" ").toList
        assert(command.toLowerCase == command)
        assert(cmdWords.forall(_ != "the"))
        assert(cmdWords.forall(Dictionary.contains(_)))
        assert(Dictionary.hasVerb(cmdWords.head))

        //TODO We do not consider the case where itemBeforeObj is false

        def matchCommand(pattern: Pattern): MatchResult = {
            val action = pattern.action
            val Extractor2 = if (pattern.hasItem && pattern.hasObj) pattern.regex else "".r
            val Extractor1 = if (!pattern.hasItem && pattern.hasObj) pattern.regex else "".r
            val Extractor0 = if (!pattern.hasItem && !pattern.hasObj) pattern.regex.toString else ""
            val result = command match {
                case Extractor2(item, obj) => MatchResult(action, item.split(" ").toList, obj.split(" ").toList, pattern.text, "")
                case Extractor1(obj) => MatchResult(action, List(), obj.split(" ").toList, pattern.text, "")
                case Extractor0 => MatchResult(action, List(), List(), pattern.text, "")
                case _ => MatchResult("none", error = "error")
            }
            return result
        }

        val verb = command.split(" ")(0)
        val patternList = Dictionary.patternList(verb)
        val firstMatch = patternList.find(matchCommand(_).action != "none")
        firstMatch match {
            case Some(entry) => { // there is a pattern that matches the command
                matchCommand(entry) //TODO Is there an elegant way to avoid doing this twice?
            }
            case None => // there is no pattern that matches the command
                val usageEntryList = patternList.map(_.text)
                new MatchResult(error = "Usage:\n\t" + usageEntryList.mkString("\n\t"))
        }
    }
}