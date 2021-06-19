package com.example.chitchat.Model

class User {

    var profilePic : String?= null
    var userName: String?= null
    var email : String?= null
    var password: String?= null
    var userId : String?= null
    var lastMessage: String?= null
    var status: String? = null


    constructor(username: String, emailId: String, password: String){
        this.userName= username
        this.email = emailId
        this.password = password
    }
    constructor()
    constructor(
        profilePic: String?,
        userName: String?,
        email: String?,
        password: String?,
        userId: String?,
        lastMessage: String?
    ) {
        this.profilePic = profilePic
        this.userName = userName
        this.email = email
        this.password = password
        this.userId = userId
        this.lastMessage = lastMessage
        this.status = status
    }



}
