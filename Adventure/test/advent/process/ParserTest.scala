package advent.process

import org.scalatest.junit.AssertionsForJUnit
import org.junit.Assert._
import org.junit.Test
import org.junit.Before
import advent.store._
import advent.game.Base
import advent.data.Command
import advent.data.Action

class ParserTest {

    // no variables

    @Before def initialize() { /* do nothing */ }

    @Test def testParse() {

        val actions = List(
            new Action("look", List("look", "l")),
            new Action("put_in", List("put {item} in {object}", "insert {item} in {object}")),
            new Action("put_on", List("put {item} on {object}")),
            new Action("tie_to", List("tie {item} to {object}")))

        // Add actions to the Dictionary builder
        actions.foreach(DictionaryBuilder.addAction(_))

        // Define more objects
        DictionaryBuilder.addObject("small_green_frog")
        DictionaryBuilder.addObject("large_blue_box")

        // Populate the dictionary
        DictionaryBuilder.addAdjective("small", obj = "small_green_frog")
        DictionaryBuilder.addAdjective("green", obj = "small_green_frog")
        DictionaryBuilder.addNoun("frog", obj = "small_green_frog")
        DictionaryBuilder.addAdjective("large", obj = "large_blue_box")
        DictionaryBuilder.addAdjective("blue", obj = "large_blue_box")
        DictionaryBuilder.addNoun("box", obj = "large_blue_box")

        println("+++++ Dictionary +++++")
        // First call to dictionary instantiates the singleton object
        println(Dictionary.toString)
        println("++++++++++++++++++++++")

        var command: Command = null

        /*
		 * Unrecognized characters
		 * 
		 * The parser should recognize all alphanumeric characters (letters and numbers),
		 * all whitespace characters (spaces and tabs), as well as
		 * apostrophes ('), dashes (-), and underscores (_).
		 * Unrecognized characters should be reported using the following message format:
		 *     I don't recognize these characters: uc_1 uc_2 ... uc_k
		 * where all unrecognized characters in the command are listed (no more than once)
		 * and all are separated by a single space.
		 */
        command = Parser.parse("put the small green frog in the @#")
        assert(command.action == "none")
        assert(command.error == "I don't recognize these characters: @ #")

        /*
		 * Unrecognized words
		 * 
		 * The parser should recognize all words that are in the dictionary.
		 * However, it should not report an error for the word 'the' even though
		 * it is not in the dictionary.
		 * Unrecognized words should be reported using the following message format:
		 *     I don't recognize these words: uw_1 uw_2 ... uw_k
		 * where all unrecognized words in the command are listed (no more than once)
		 * and all are separated by a single space.
		 */
        command = Parser.parse("put the small green dog in the desk")
        assert(command.action == "none")
        assert(command.error == "I don't recognize these words: dog desk")

        /*
		 * Unrecognized verb
		 * 
		 * The first word in every command should be recognized by the dictionary as a verb.
		 * Unrecognized verbs should be reported using the following message format:
		 *     I don't recognize 'unk_verv' as a verb.
		 * where uv is a valid dictionary word that cannot be used as a verb.
		 */
        command = Parser.parse("small the put in the box")
        assert(command.action == "none")
        assert(command.error == "I don't recognize 'small' as a verb.")

        /*
		 * Unmatched pattern
		 * 
		 * The parser will call on the grammar to try to match the command
		 * to a grammar pattern. If a matching pattern is not found, the parser
		 * reports a usage error, listing all possible grammar patterns that begin
		 * with the first word (verb) of the command:
		 *     Usage:
		 *         gp_1
		 *         gp_2
		 *         ...
		 *         gp_k
		 * where gp_1 ... gp_k are all the grammar patterns that begin with the verb. 
		 */
        command = Parser.parse("put the small green frog to the box")
        assert(command.action == "none")
        // println(command.error) //DEBUG
        assert(command.error == "Usage:\n\t" + "put {item} on {object}\n\tput {item} in {object}")

        /*
		 * Bad form in item/object words
		 * 
		 * If a command matches a pattern in the grammar the command may
		 * have one or more words where an item is expected and one or more
		 * words where an object is expected. These words should have the form
		 *     {adjective}* noun
		 * That is, zero or more adjectives followed by a noun.
		 * If the item word list or object word list does not have a noun,
		 * the parser should report an error with the following form:
		 *     Matched pattern: gp_match
		 *     I don't recognize 'unk_noun' as a noun.
		 * In other words, it should first indicate the pattern that was matched
		 * and then report that the unknown noun.
		 * When a noun is found but an item/object word before the noun is not
		 * an adjective, it should report the following error.
		 *     Matched pattern: gp_match
		 *     I don't recognize the following words as adjectives: ua_1 ua_2 ... ua_k
		 * where all unrecognized adjectives are listed (no more than once) in the order they appear
		 * and all are separated by a single space.
		 */
        command = Parser.parse("put the small green in the large box")
        assert(command.action == "none")
        assert(command.error == "Matched pattern: put {item} in {object}\n" +
            "I don't recognize 'green' as a noun.")

        command = Parser.parse("put the small green frog in the box look large box")
        assert(command.action == "none")
        //println(command.error) //DEBUG
        assert(command.error == "Matched pattern: put {item} in {object}\n" +
            "I don't recognize the following words as adjectives: box look")

        /*
		 * Item/Object words do not correspond to an actual object
		 * 
		 * Item or object words should correspond to an actual object.
		 * For example, for words "adj_1 adj_2 adj_3 noun" there should be
		 * an object in the dictionary that is associated with all adjectives
		 * as well as the noun.
		 * If item/object words do not correspond to an actual object, the parser should report:
		 *     Matched pattern: gp_match
		 *     I could not find an object that matched item/object words: adj_1 adj_2 ... adj_k noun
		 */
        command = Parser.parse("put the small green frog in the large frog")
        assert(command.action == "none")
        assert(command.error == "Matched pattern: put {item} in {object}\n" +
            "I could not find an object that matched object words: large frog")

        /*
		 * Everything matches (exactly one match)
		 * 
		 * If everything matches, the parser should put the appropriate action
		 * in command.action and the appropriate object (if any) in command.item
		 * and the appropriate object (if any) in command.object.
		 */
        command = Parser.parse("put the small green frog in the blue box")
        assert(command.action == "put_in")
        assert(command.item == "small_green_frog")
        assert(command.obj == "large_blue_box")
        assert(command.error == "")

        /*
		 * Everything matches (more than one match)
		 * 
		 * If there is more than one object that matches item words or object words,
		 * the parser should call through to the resolver which returns exactly one of the options.
		 */
        DictionaryBuilder.addObject("large_red_box")

        DictionaryBuilder.addAdjective("large", obj = "large_red_box")
        DictionaryBuilder.addAdjective("red", obj = "large_red_box")
        DictionaryBuilder.addNoun("box", obj = "large_red_box")

        command = Parser.parse("put the small green frog in the large box")
        assert(command.action == "put_in")
        assert(command.item == "small_green_frog")
        assert(command.obj == "large_blue_box" || command.obj == "large_red_box")
    }

}
