package advent.store

import org.scalatest.junit.AssertionsForJUnit
import org.junit.Assert._
import org.junit.Test
import org.junit.Before
import advent.data.Command

class DictionaryTest {

	// no variables
	
	@Before def initialize() { /* do nothing */	}
	
	@Test def testDictionary() {
		
		// Define objects
		DictionaryBuilder.addObject("small_green_frog")
		DictionaryBuilder.addObject("large_blue_box")
		DictionaryBuilder.addObject("large_red_box")	
		
		// Populate the dictionary
		DictionaryBuilder.addAdjective("small", obj = "small_green_frog")
		DictionaryBuilder.addAdjective("green", obj = "small_green_frog")
		DictionaryBuilder.addNoun("frog", obj = "small_green_frog")
		DictionaryBuilder.addAdjective("large", obj = "large_blue_box")
		DictionaryBuilder.addAdjective("blue", obj = "large_blue_box")
		DictionaryBuilder.addNoun("box", obj = "large_blue_box")
		DictionaryBuilder.addAdjective("large", obj = "large_red_box")
		DictionaryBuilder.addAdjective("red", obj = "large_red_box")
		DictionaryBuilder.addNoun("box", obj = "large_red_box")

		println(Dictionary.toString)		
	}	
}
