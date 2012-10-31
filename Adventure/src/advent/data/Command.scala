package advent.data

case class Command(
		action: String,
		obj: String = "nothing",
		item: String = "nothing",
		error: String = "")