package util

import org.scalatest.junit.AssertionsForJUnit
import org.junit.Assert._
import org.junit.Test
import org.junit.Before

class TreeTest extends AssertionsForJUnit {

	// no variables
	
	@Before def initialize() {
		// no methods
	}
	
	@Test def testEmptyTree() {
		val eTree = new MapTree[String, Int]
		
		assert(eTree.isEmpty)
		assert(eTree.size == 0)
		assert(!eTree.contains("root"))
		assert(eTree.toString == "[]()")
		assert(eTree.prettyPrint == "[]\n")
	}

	@Test def testSingletonTree() {
		val sTree = new MapTree[String, Int]
		sTree.addRoot("root" -> 0)
		
		assert(!sTree.isEmpty)
		assert(sTree.size == 1)
		assert(sTree.contains("root"))
		assert(!sTree.contains("hello"))
		assert(sTree.hasRoot("root"))
		assert(sTree.hasLeaf("root"))
		assert(!sTree.hasPrev("root"))
		assert(!sTree.hasNext("root"))
		assert(sTree.hasDescendingPath("root", "root"))
		assert(sTree.toString == "[root->0]()")
		assert(sTree.prettyPrint == "[root->0]\n")
		assert(sTree.getValue("root") == 0)

		val x = sTree.replace("root", 100)
		assert(x == 0)
		assert(sTree.getValue("root") == 100)
		val y = sTree.removeLeaf("root")
		assert(y == 100)
		assert(sTree.isEmpty)
		assert(sTree.toString == "[]()")
	}

	@Test def testTree() {
		val tree = new MapTree[String, Int]
		tree.addRoot("root" -> 0)
		tree.addChild("root", "hello" -> 1)
		tree.addChild("root", "world" -> 2)
		tree.addChild("hello", "goodbye" -> 3)
		tree.addChild("hello", "universe" -> 4)

		// queries
		assert(!tree.isEmpty)
		assert(tree.size == 5)
		assert(tree.contains("root"))
		assert(tree.contains("hello"))
		assert(!tree.contains("whatever"))
		assert(tree.hasRoot("root"))
		assert(!tree.hasRoot("hello"))
		assert(tree.hasLeaf("goodbye"))
		assert(tree.hasLeaf("universe"))
		assert(tree.hasLeaf("world"))
		assert(!tree.hasLeaf("root"))
		assert(!tree.hasLeaf("hello"))
		assert(!tree.hasPrev("root"))
		assert(!tree.hasNext("root"))
		assert(tree.hasDescendingPath("root", "root"))
		assert(tree.hasDescendingPath("hello", "hello"))
		assert(tree.hasDescendingPath("root", "hello"))
		assert(tree.hasDescendingPath("root", "universe"))
		assert(tree.hasDescendingPath("hello", "universe"))
		assert(!tree.hasDescendingPath("hello", "root"))
		assert(!tree.hasDescendingPath("universe", "hello"))
		assert(!tree.hasDescendingPath("hello", "world"))
		assert(!tree.hasDescendingPath("world", "hello"))
		assert(!tree.hasDescendingPath("world", "root"))
		
		// get and replace
		assert(tree.getValue("hello") == 1)
		assert(tree.getValue("universe") == 4)
		var x = 100
		x = tree.replace("hello", x)
		assert(x == 1)
		assert(tree.getValue("hello") == 100)
		x = tree.replace("hello", x)
		assert(x == 100)
		assert(tree.getValue("hello") == 1)

		// print
		assert(tree.toString == "[root->0]([hello->1]([goodbye->3](),[universe->4]()),[world->2]())")
		assert(tree.prettyPrint == "[root->0]\n  [hello->1]\n    [goodbye->3]\n    [universe->4]\n  [world->2]\n")
		
		tree.addChild("goodbye", "whatever" -> 47)
		assert(tree.toString == "[root->0]([hello->1]([goodbye->3]([whatever->47]()),[universe->4]()),[world->2]())")

		tree.moveSubtree("goodbye", "world")
		assert(tree.toString == "[root->0]([hello->1]([universe->4]()),[world->2]([goodbye->3]([whatever->47]())))")

		tree.addChild("root", 1, "good" -> 47)
		assert(tree.toString == "[root->0]([hello->1]([universe->4]()),[good->47](),[world->2]([goodbye->3]([whatever->47]())))")
		
		x = tree.removeLeaf("whatever")
		assert(x == 47)
		assert(!tree.contains("whatever"))
		assert(tree.toString == "[root->0]([hello->1]([universe->4]()),[good->47](),[world->2]([goodbye->3]()))")
	}	
}
