package advent.process

import advent.store.World
import advent.store.Dictionary

object Processor {
	
    private var fontSize = 5
    private var fontFace = "Georgia";
    private var fontColor = "maroon";

    private val SizeExtractor = """size: (\d)""".r
    private val FaceExtractor = """font: (\w*)""".r
    private val ColorExtractor = """color: (\w*)""".r
    private val ShowExtractor = """show: (\w*)""".r
    private val TreeExtractor = """tree: (\w*)""".r
    private val TraceExtractor = """trace: (\w*)""".r

    private var _trace = false
    def trace(): Boolean = _trace
    
    def processInput(input: String): String = {
        val output = input match {
            case SizeExtractor(size) => fontSize = size.toInt; "Font size is now " + size + ".";
            case FaceExtractor(face) => fontFace = face; "Font face is now " + face + ".";
            case ColorExtractor(color) => fontColor = color; "Font color is now " + color + ".";
            case ShowExtractor(obj) => textToHtml(World.showObject(obj))
            case TreeExtractor(obj) => textToHtml(World.showTree(obj))
            case TraceExtractor(x) =>
                x match {
                    case "on" => _trace = true; "Trace is on."
                    case "off" => _trace = false; "Trace is off."
                    case "dictionary" => println(Dictionary.toString); "Dictionary printed to stdout."
                    case _ => if (_trace) "Trace is on." else "Trace is off."
                }
            case _ => getCommandOutput(input)
        }
        toHtml(output)
    }

    private def getCommandOutput(input: String): String = {
        val cmd = Parser.parse(input)
        val (message, isCutscene) = Responder.respond(cmd)
        return message
    }
    
        private def textToHtml(input: String) = {
		input.replaceAll(""" """, "&nbsp;").replaceAll("""\n""", "<br>").replaceAll("""\t""", "&nbsp;&nbsp;&nbsp;&nbsp;")
	}

    private def toHtml(input: String) =
        "<html><head><title></title></head><body>" +
            "<font size=\"" + fontSize + "\" face=\"" + fontFace + "\" color=\"" + fontColor + "\">" +
            input +
            "</font></body></html>";
    
}