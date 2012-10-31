package editor.gui

import scala.swing._
import scala.swing.event._
import scala.swing.GridBagPanel._

class ObjectEditView extends Frame {
    
    val responseList: Seq[String] = List("hello", "world", "where", "are", "you", "going",
            "hello", "world", "where", "are", "you", "going",
            "hello", "world", "where", "are", "you", "going").toSeq
    
    val nameLabel = new Label("Name")
	val nameField = new TextField(40)

	val parentLabel = new Label("Parent")
	val parentField = new TextField(20)
    
    val kindLabel = new Label("Kind")
	val kindComboBox = new ComboBox(List("Header", "Generic", "Room", "Actor", "Thing", "Door").toSeq)
    
	val actionsLabel = new Label("Actions")
	val actionsField = new TextField(40)

	val propertiesLabel = new Label("Properties")
	val propertiesField = new TextField(40)

    val descriptionLabel = new Label("Description")
	val descriptionArea = new ScrollPane(new TextArea(5, 40)) {
        horizontalScrollBarPolicy = ScrollPane.BarPolicy.Never
        verticalScrollBarPolicy = ScrollPane.BarPolicy.Always
    }

	val valuesLabel = new Label("Values")
	val valuesArea = new ScrollPane(new TextArea(5, 20)) {
		horizontalScrollBarPolicy = ScrollPane.BarPolicy.Never
        verticalScrollBarPolicy = ScrollPane.BarPolicy.Always
	}
        
    val relationsLabel = new Label("Relations")
	val relationsArea = new ScrollPane(new TextArea(5, 40)) {
        horizontalScrollBarPolicy = ScrollPane.BarPolicy.Never        
        verticalScrollBarPolicy = ScrollPane.BarPolicy.Always
    }

	val messagesLabel = new Label("Messages")
	val messagesArea = new ScrollPane(new TextArea(5, 40)) {
        horizontalScrollBarPolicy = ScrollPane.BarPolicy.Never	    
        verticalScrollBarPolicy = ScrollPane.BarPolicy.Always
	}

	val responsesLabel = new Label("Responses")
	val responsesListView = new ScrollPane(
	        new ListView(responseList) {
	        	// preferredSize = new java.awt.Dimension(50, 50)
	        }
	) {
        horizontalScrollBarPolicy = ScrollPane.BarPolicy.Never
        verticalScrollBarPolicy = ScrollPane.BarPolicy.Always
	}
	
    lazy val addButton = new Button("Add")
    lazy val removeButton = new Button("Remove")
    lazy val editButton = new Button("Edit")	

    lazy val responseButtonPanel = new GridPanel(3, 1) {
        contents += addButton
        contents += removeButton
        contents += editButton
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
	    layout(nameLabel) = c
	    c.gridy = 1
	    layout(parentLabel) = c
	    c.gridy = 2
	    layout(kindLabel) = c
	    c.gridy = 3
	    layout(actionsLabel) = c
	    c.gridy = 4
	    layout(propertiesLabel) = c
	    c.gridy = 5
	    layout(descriptionLabel) = c
	    c.gridy = 6
	    layout(valuesLabel) = c
	    c.gridy = 7
	    layout(relationsLabel) = c
	    c.gridy = 8
	    layout(messagesLabel) = c
	    c.gridy = 9
	    layout(responsesLabel) = c	    

	    c.fill = Fill.Horizontal
	    c.gridx = 1
	    c.anchor = Anchor.NorthWest
	    c.weighty = 1.0
	    //c.ipadx = 2
	    c.ipady = 2
	    c.insets = new java.awt.Insets(0,0,0,0);  //reset padding
	    
	    c.gridy = 0
	    c.gridwidth = 2
	    layout(nameField) = c
	    c.gridy = 1
	    c.gridwidth = 1
	    layout(parentField) = c
	    c.gridy = 2
	    layout(kindComboBox) = c
	    c.gridy = 3
	    c.gridwidth = 2
	    layout(actionsField) = c
	    c.gridy = 4
	    c.insets = new java.awt.Insets(0,0,10,0);  // pad bottom
	    layout(propertiesField) = c
	    c.gridy = 5
	    layout(descriptionArea) = c
	    c.gridy = 6
	    c.gridwidth = 1	    
	    layout(valuesArea) = c
	    c.gridy = 7
	    c.gridwidth = 2	    
	    layout(relationsArea) = c
	    c.gridy = 8
	    layout(messagesArea) = c
	    c.gridy = 9
	    c.gridwidth = 1
	    layout(responsesListView) = c
	    
	    c.gridx = 2
	    c.gridy = 9
	    c.anchor = Anchor.NorthWest
	    layout(responseButtonPanel) = c
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
    
    title = "Object Editor"
    contents = mainPanel
    location = new java.awt.Point(100, 100)
    preferredSize = new java.awt.Dimension(600, 400)
    visible = true
}
