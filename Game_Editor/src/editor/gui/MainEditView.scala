package editor.gui

import scala.swing._
import scala.collection.Seq
import scala.swing.TabbedPane.Page

class MainEditView extends Frame {

    val objectList: Seq[String] = Seq.empty
    val actionList: Seq[String] = Seq.empty
    val propertyList: Seq[String] = Seq.empty
    
    // ==========================================================
    // Constants
    // ==========================================================
    
    val listViewBorder = Swing.EmptyBorder(5, 5, 5, 5)
    val listViewFont = new java.awt.Font("Verdana", java.awt.Font.PLAIN, 16)
    val listViewPreferredSize = new java.awt.Dimension(300, 200)
    
    // ==========================================================
    // Object editor portion
    // ==========================================================

    // declare leaf components
    lazy val breadCrumbLabel = new Label("Root")
    lazy val backButton = new Button("Back")
    lazy val objectAddButton = new Button("Add")
    lazy val objectRemoveButton = new Button("Remove")
    lazy val objectEditButton = new Button("Edit")
    lazy val openButton = new Button("Open")
    lazy val gameObjectList = new ListView(objectList) {
        preferredSize = listViewPreferredSize
        border = listViewBorder
        font = listViewFont
        selection.intervalMode = ListView.IntervalMode.Single
    }

    // combine bread-crumb label and back button
    lazy val breadCrumbPanel = new FlowPanel {
        contents += breadCrumbLabel
    }

    // use to keep the north bread crumb trail aligned left
    lazy val breadCrumbGroup = new BorderPanel {
        add(breadCrumbPanel, BorderPanel.Position.West)
    }

    // combine add, remove, edit, and open buttons
    // use grid panel instead of vertical box panel for button group to ensure buttons are equal length
    lazy val objectButtonPanel = new GridPanel(5, 1) {
        contents += objectAddButton
        contents += objectRemoveButton
        contents += objectEditButton
        contents += openButton
        contents += backButton
    }

    // use to keep the east button group aligned top
    lazy val objectButtonGroup = new BorderPanel {
        add(objectButtonPanel, BorderPanel.Position.North)
    }

    // use border panel when one element is central (in this case list view)
    lazy val objectPanel = new BorderPanel {
        add(gameObjectList, BorderPanel.Position.Center)
        add(breadCrumbGroup, BorderPanel.Position.North)
        add(objectButtonGroup, BorderPanel.Position.East)
        border = Swing.EmptyBorder(20, 20, 20, 20)
    }

    // ==========================================================
    // Action editor portion
    // ==========================================================

    lazy val actionAddButton = new Button("Add")
    lazy val actionRemoveButton = new Button("Remove")
    lazy val actionEditButton = new Button("Edit")
    lazy val gameActionList = new ListView(actionList) {
        preferredSize = listViewPreferredSize        
        border = listViewBorder
        font = listViewFont
        selection.intervalMode = ListView.IntervalMode.Single
    }
    
    // combine add, remove, edit, and open buttons
    // use grid panel instead of vertical box panel for button group to ensure buttons are equal length
    lazy val actionButtonPanel = new GridPanel(4, 1) {
        contents += actionAddButton
        contents += actionRemoveButton
        contents += actionEditButton
    }

    // use to keep the east button group aligned top
    lazy val actionButtonGroup = new BorderPanel {
        add(actionButtonPanel, BorderPanel.Position.North)
    }

    // use border panel when one element is central (in this case list view)
    lazy val actionPanel = new BorderPanel {
        add(gameActionList, BorderPanel.Position.Center)
        add(actionButtonGroup, BorderPanel.Position.East)
        border = Swing.EmptyBorder(20, 20, 20, 20)
    }

    // ==========================================================
    // Property editor portion
    // ==========================================================

    lazy val propertyAddButton = new Button("Add")
    lazy val propertyRemoveButton = new Button("Remove")
    lazy val propertyEditButton = new Button("Edit")
    lazy val gamePropertyList = new ListView(propertyList) {
        preferredSize = listViewPreferredSize        
        border = listViewBorder
        font = listViewFont
        selection.intervalMode = ListView.IntervalMode.Single
    }
    
    // combine add, remove, edit, and open buttons
    // use grid panel instead of vertical box panel for button group to ensure buttons are equal length
    lazy val propertyButtonPanel = new GridPanel(4, 1) {
        contents += propertyAddButton
        contents += propertyRemoveButton
        contents += propertyEditButton
    }

    // use to keep the east button group aligned top
    lazy val propertyButtonGroup = new BorderPanel {
        add(propertyButtonPanel, BorderPanel.Position.North)
    }

    // use border panel when one element is central (in this case list view)
    lazy val propertyPanel = new BorderPanel {
        add(gamePropertyList, BorderPanel.Position.Center)
        add(propertyButtonGroup, BorderPanel.Position.East)
        border = Swing.EmptyBorder(20, 20, 20, 20)
    }

    // ==========================================================
    // Game editor with tabs for each portion
    // ==========================================================
    
    lazy val tabs = new TabbedPane {
    	pages += new Page("Objects", objectPanel)
    	pages += new Page("Actions", actionPanel)
    	pages += new Page("Properties", propertyPanel)
    }
    
    title = "Game Editor"
    contents = tabs
    pack // with 'pack' it will ignore the preferred size
    gameObjectList.requestFocus
    //gameObjectList
    visible = true
}
