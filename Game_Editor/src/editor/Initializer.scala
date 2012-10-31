package editor

import editor.model.Model
import editor.model.EditableGameObject

class Initializer(val model: Model) {

    def initializeModel {
        
        // ======================================================
        // Initialize world
        // ======================================================
        
        model.addToWorld(new EditableGameObject("root", "root", "header"))
        model.addToWorld(new EditableGameObject("generic", "root", "header"))
        model.addToWorld(new EditableGameObject("actor", "generic", "generic"))
        model.addToWorld(new EditableGameObject("room", "generic", "generic"))
        model.addToWorld(new EditableGameObject("thing", "generic", "generic"))
        model.addToWorld(new EditableGameObject("door", "generic", "generic"))
        
        // ======================================================
        // Initialize actions
        // ======================================================
        
        model.actions += "Action_6"
        model.actions += "Action_5"
        model.actions += "Action_4"
        model.actions += "Action_3"
        model.actions += "Action_2"
        model.actions += "Action_1"        
        
        // ======================================================
        // Initialize properties
        // ======================================================
        
        model.properties += "Prop1"
        model.properties += "Prop2"
        model.properties += "Prop3"
        model.properties += "Prop4 Neg_Prop4"
        model.properties += "Prop5 Neg_Prop5"        
    }
    
    var testStr = "verylongstringtotestwhathappenswhenaveryverylongstringisinsertedsomewhereinthegraphicaluserinterface"
    
    def extendModelTest {
        model.addToWorld(new EditableGameObject("act_1", "root", "header"))
        model.addToWorld(new EditableGameObject("act_2", "root", "header"))
        model.addToWorld(new EditableGameObject("kitchen", "act_1", "room"))
        model.addToWorld(new EditableGameObject("garage", "act_1", "room"))
        model.addToWorld(new EditableGameObject(testStr, "kitchen", "actor"))
        model.addToWorld(new EditableGameObject("baseball", testStr, "thing"))
        model.addToWorld(new EditableGameObject("wooden_box", "kitchen", "thing"))
        model.addToWorld(new EditableGameObject("cardboard_box", "garage", "thing"))
        model.addToWorld(new EditableGameObject("beach_ball", "garage", "thing"))
    }

}
