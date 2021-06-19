package com.example.chitchat

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chitchat.Adapters.ChatAdapter
import com.example.chitchat.Model.MessagesModel
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_chat_detail.*
import java.util.*
import kotlin.collections.ArrayList

class ChatDetailActivity : AppCompatActivity() {

    var database: FirebaseDatabase? = null
    var auth: FirebaseAuth? = null
    var profilePic: String? = null
    var chatAdapter: ChatAdapter? = null
    var receivedId: String? = null
    var firebaseUser: FirebaseUser? = null
    var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_detail)

        supportActionBar!!.hide()

        database = FirebaseDatabase.getInstance()
        //auth= FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog!!.setTitle("Loading Chats")
        progressDialog!!.setMessage("We are loading chat. Please Wait")

        progressDialog!!.show()

        firebaseUser = FirebaseAuth.getInstance().currentUser



        var senderId: String? = firebaseUser!!.uid

        Log.i("senderId", senderId.toString())


        if (intent.getStringExtra("userId").isNullOrEmpty()){
            Log.i("User Id Get Extra"," Is Empty")
        }else{
             receivedId = intent.getStringExtra("userId")

        }

        var userName: String = intent.getStringExtra("userName").toString()

        if(intent.getStringExtra("profilePic").isNullOrEmpty()){
            Log.i("Profile Pic","Empty")

        }else{
            profilePic = intent.getStringExtra("profilePic").toString()

            Picasso.get().load(profilePic).placeholder(R.drawable.ic_person).into(profile)


        }


        et_userName_chat_detail.text = userName
        Picasso.get().load(profilePic).placeholder(R.drawable.ic_person).into(profile)

        Log.i("Picasso","Loaded")

        btn_back.setOnClickListener { view->
            var intent = Intent(this,MainScreen::class.java)
            startActivity(intent)
        }


        var messagesModel= ArrayList<MessagesModel>()
        chatAdapter = ChatAdapter(messagesModel, this, receivedId!!)
        chat_recycler_view.adapter = chatAdapter


        chat_recycler_view.layoutManager= LinearLayoutManager(this)
        chat_recycler_view.setHasFixedSize(true)

        var senderRoom = senderId + receivedId
        var receiverRoom = receivedId + senderId

        var ref = database!!.getReference().child("chats").child(senderRoom)

        ref.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                messagesModel.clear()
                for(datasnapshots in snapshot.children){
                    var model = datasnapshots.getValue(MessagesModel::class.java)
                    model!!.messageId = datasnapshots.key

                    messagesModel.add(model!!)
                }
                progressDialog!!.dismiss()
                chatAdapter!!.notifyDataSetChanged()
                chat_recycler_view.smoothScrollToPosition(chatAdapter!!.itemCount -1)

            }

        })

        btn_sent.setOnClickListener { view->

            var message: String = et_enter_message.text.toString()
            var model: MessagesModel = MessagesModel(senderId,message)
            model.timeStamp = Date().time

            et_enter_message.setText("")

            database!!.getReference().child("chats").child(senderRoom).push().setValue(model).addOnSuccessListener(OnSuccessListener {

                database!!.getReference().child("chats").child(receiverRoom).push().setValue(model).addOnSuccessListener(OnSuccessListener {

                })

            })
        }

    }
}