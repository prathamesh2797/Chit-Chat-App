package com.example.chitchat.Model

class MessagesModel {

    var uId: String? = null
    var message: String? = null
    var timeStamp: Long? = null
    var messageId: String? = null

    constructor(uId: String?, message: String?, timeStamp: Long?) {
        this.uId = uId
        this.message = message
        this.timeStamp = timeStamp
    }

    constructor()

    constructor(uId: String?, message: String?){
        this.uId= uId
        this.message= message
    }
}