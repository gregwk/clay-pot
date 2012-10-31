package editor.gui

import scala.swing._
import scala.swing.event._
import scala.swing.GridBagPanel._

class ResponseEditView extends Frame {
    
    val actionLabel = new Label("Action")
	val actionField = new TextField(20)

	val objectLabel = new Label("Object")
	val objectField = new TextField(20)
    
	val secondLabel = new Label("Second")
	val secondField = new TextField(20)

    val outcomeLabel = new Label("Outcome")
	val outcomeComboBox = new ComboBox(List("Cannot", "Not yet", "Already", "Okay", "Cutscene", "Redirect").toSeq)
    
    val conditionsLabel = new Label("Conditions")
	val conditionsArea = new ScrollPane(new TextArea(5, 40)) {
        horizontalScrollBarPolicy = ScrollPane.BarPolicy.Never
        verticalScrollBarPolicy = ScrollPane.BarPolicy.Always
    }

    val messageLabel = new Label("Message")
	val messageArea = new ScrollPane(new TextArea(5, 40)) {
        horizontalScrollBarPolicy = ScrollPane.BarPolicy.Never
        verticalScrollBarPolicy = ScrollPane.BarPolicy.Always
    }

    val effectsLabel = new Label("Effects")
	val effectsArea = new ScrollPane(new TextArea(5, 40)) {
        horizontalScrollBarPolicy = ScrollPane.BarPolicy.Never
        verticalScrollBarPolicy = ScrollPane.BarPolicy.Always
    }
     
	val cancelButton = new Button("Cancel")
	val saveButton = new Button("Save")

	lazy val mainInfoPanel = new GridBagPanel {
	    val c = new Constraints
	    // labels beginning in column 0
	    c.fill = Fill.None
	    c.gridx = 0
	    c.anchor = Anchor.NorthEast
	    c.weighty = 0.0
	    //c.ipadx = 2
	    c.ipady = 2
	    c.insets = new java.awt.Insets(0,0,0,10);  //right padding
	    
	    c.gridy = 0
	    layout(actionLabel) = c
	    c.gridy = 1
	    layout(objectLabel) = c
	    c.gridy = 2
	    layout(secondLabel) = c
	    c.gridy = 3
	    layout(outcomeLabel) = c
	    c.gridy = 4
	    layout(conditionsLabel) = c
	    c.gridy = 5
	    layout(messageLabel) = c
	    c.gridy = 6
	    layout(effectsLabel) = c

	    c.fill = Fill.Horizontal
	    c.gridx = 1
	    c.anchor = Anchor.NorthWest
	    c.weighty = 1.0
	    //c.ipadx = 2
	    c.ipady = 2
	    c.insets = new java.awt.Insets(0,0,0,0);  //reset padding
	    
	    c.gridy = 0
	    c.gridwidth = 1
	    layout(actionField) = c
	    c.gridy = 1
	    layout(objectField) = c
	    c.gridy = 2
	    layout(secondField) = c
	    c.gridy = 3
	    layout(outcomeComboBox) = c
	    c.gridy = 4
	    c.gridwidth = 2
	    c.insets = new java.awt.Insets(0,0,10,0);  // pad bottom
	    layout(conditionsArea) = c
	    c.gridy = 5
	    layout(messageArea) = c
	    c.gridy = 6
	    layout(effectsArea) = c
    }

    lazy val cancelSavePanel = new FlowPanel {
        contents += cancelButton
        contents += saveButton
    }

    lazy val mainPanel = new BorderPanel {
        add(mainInfoPanel, BorderPanel.Position.Center)
        add(cancelSavePanel, BorderPanel.Position.South)
        border = Swing.EmptyBorder(10, 10, 10, 10)
    }
    
    title = "Response Editor"
    contents = mainPanel
    location = new java.awt.Point(100, 100)
    preferredSize = new java.awt.Dimension(600, 400)
    visible = true
}
