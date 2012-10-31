package advent.process

import advent.store._
import advent.data._
import advent.game._
import scala.collection._

object Initializer {

	var complete = false
	var player = ""
	
    def initialize(customInfo: GameInfo = Game.gameInfo) {
    	
		// Add alias objects
        DictionaryBuilder.addObject("nothing")
        DictionaryBuilder.addObject("location")
        DictionaryBuilder.addObject("player")
        DictionaryBuilder.addObject("audience")
        DictionaryBuilder.addObject("attention")
        DictionaryBuilder.addObject("object")
        DictionaryBuilder.addObject("item")
        DictionaryBuilder.addObject("self")

        process(Base.gameInfo)
    	process(customInfo)
    	
    	assert(player != "")
    	World.player = player
    	
    	complete = true
    }

    def process(info: GameInfo) {
        info.actions.foreach(DictionaryBuilder.addAction(_))
        info.objects.foreach(World.addObject(_))
    	info.mutexProps.foreach(DictionaryBuilder.addMutexProps(_))
    	player = info.player
    }
}
