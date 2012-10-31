package advent.data

abstract class Response

case class NoResponse() extends Response
case class CantResponse(key: String = "custom", message: String = "") extends Response
case class NotYetResponse(key: String = "custom", message: String = "") extends Response
case class AlreadyResponse(key: String = "custom", message: String = "") extends Response
case class OkayResponse(key: String = "custom", message: String = "", effects: List[Effect] = List.empty) extends Response
case class CutsceneResponse(key: String = "custom", message: String = "", effects: List[Effect] = List.empty) extends Response
case class RedirectResponse(command: Command) extends Response
