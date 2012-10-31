package advent

import swing._
import event._
import scala.collection.mutable.ListBuffer
import advent.process.Initializer
import advent.process.Processor

object Adventure extends SimpleSwingApplication {
    override def main(args: Array[String]) = {
        super.main(args)
    }

    // constructor
    Initializer.initialize()
    
    def top = new MainFrame {
        var fontSize = 5
        var fontFace = "Georgia";
        var fontColor = "maroon";

        val commandBuffer = ListBuffer("")
        var commandBufferPos = 0

        val tenPointBorder = Swing.EmptyBorder(10, 10, 10, 10)

        val label = new Label("> ")
        val input = new TextField { columns = 50 }
        val button = new Button { text = "Enter" }
        val inputArea = new FlowPanel {
            contents += label
            contents += input
            contents += button
        }

        val output = new EditorPane {
            editable = false
            contentType = "text/html";
        }
        val outputArea = new ScrollPane(output) {
            preferredSize = new java.awt.Dimension(200, 400)
            minimumSize = new java.awt.Dimension(50, 100)
            border = Swing.CompoundBorder(Swing.TitledBorder(Swing.EtchedBorder, "Text Output"), tenPointBorder)
        }

        // constructor
        title = "Input/Output";
        contents = new BorderPanel {
            layout(outputArea) = BorderPanel.Position.Center
            layout(inputArea) = BorderPanel.Position.South
        }
        listenTo(input, button, input.keys)
        reactions += {
            case ButtonClicked(`button`) =>
                processInput
            case EditDone(`input`) =>
                processInput
            case KeyReleased(`input`, Key.Up, _, _) =>
                displayPreviousCommand
            case KeyReleased(`input`, Key.Down, _, _) =>
                displayNextCommand
        }
        input.requestFocus

        def displayPreviousCommand = {
        	if (input.text != commandBuffer(commandBufferPos)) commandBufferPos = 0
            if (commandBufferPos == commandBuffer.size - 1) { } // eventually make a beep sound
            else commandBufferPos = commandBufferPos + 1
            input.text = commandBuffer(commandBufferPos)
        }

        def displayNextCommand = {
        	if (input.text != commandBuffer(commandBufferPos)) commandBufferPos = 0
        	if (commandBufferPos == 0) { } // eventually make a beep sound
        	else commandBufferPos = commandBufferPos - 1
        	input.text = commandBuffer(commandBufferPos)
        }

        def processInput {
        	if (input.text != "") { // had to include; cmd-tab x 2 was triggering input 
        		output.text = Processor.processInput(input.text)
        		commandBuffer(0) = input.text
        		"" +: commandBuffer
        	}
            input.text = "";
            input.requestFocus
        }
    }
}