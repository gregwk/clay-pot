package editor.gui

import editor.model.Model
import scala.swing.event._

class MainEditControl(val model: Model) {
    val view = new MainEditView
    var currParent = "root"
    var prevParent = "root"
    
    refreshViewData()
    
    view.listenTo(view.backButton, view.openButton)
    view.listenTo(view.objectAddButton, view.objectEditButton, view.objectRemoveButton)
    //view.listenTo(view.actionAddButton, view.actionEditButton, view.actionRemoveButton)
    //view.listenTo(view.propertyAddButton, view.propertyEditButton, view.propertyRemoveButton)    

    view.reactions += {
        case ButtonClicked(view.backButton) =>
            if (currParent != "root") {
                prevParent = currParent
                currParent = model.world.parent(prevParent)
            }
            refreshViewData()
        case ButtonClicked(view.openButton) =>
            val selection = view.gameObjectList.selection.items.head.toString()
            if (model.world.contains(selection) && !model.world.hasLeaf(selection)) {
            	currParent = selection
            }
            refreshViewData()
    }
    
    // ==========================================================
    // Methods for refreshing the view
    // ==========================================================
    
    def getBreadCrumbText(node: String): String = node match {
        case "root" => "root"
        case _ => getBreadCrumbText(model.world.parent(node)) + ":" + node
    }
    
    def getPosition(parent: String, child: String) = 
        model.world.childNodes(parent).indexOf(child)
    
    def refreshViewData() {
        view.breadCrumbLabel.text = getBreadCrumbText(currParent)
        view.gameActionList.listData = model.actions.toSeq
        view.gameObjectList.listData = model.world.childNodes(currParent).toSeq
        view.gamePropertyList.listData = model.properties.toSeq
        if (prevParent != "root" && model.world.parent(prevParent) == currParent) {
        	view.gameObjectList.selectIndices(getPosition(currParent, prevParent))
        } else {
            view.gameObjectList.selectIndices(0)
        }
    }
}
