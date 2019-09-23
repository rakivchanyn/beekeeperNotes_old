package com.carpathianapiary.beekeepernotes.database.apiaryData

class Apiary() {
    
    var name: String = ""
    
    var ownerId: String = ""
    
    var locationId: String = ""
    
    var info: String = ""
    
    var note: String = ""
    
    constructor(
            name: String = "",
            
            ovnerId: String = "",
            
            locationId: String = "",
            
            info: String = "",
            
            note: String = ""
    ) : this() {
        
        this.name = name
        
        this.ownerId = ovnerId
        
        this.locationId = locationId
        
        this.info = info
        
        this.note = note
    }
}
