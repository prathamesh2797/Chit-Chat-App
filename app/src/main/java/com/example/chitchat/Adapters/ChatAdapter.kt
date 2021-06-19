package com.example.chitchat.Adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.chitchat.Model.MessagesModel
import com.example.chitchat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ChatAdapter(val messagesModel: ArrayList<MessagesModel>, val context: Context, val recId: String)  : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var SENDER_VIEW_TYPE: Int?= 1
    var RECEIVER_VIEW_TYPE: Int?= 2
    var messageModel: ArrayList<MessagesModel>? = null

       class ReceiverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

         var receiverMessage: TextView = itemView.findViewById(R.id.receiver_text)
         var receiverTime: TextView = itemView.findViewById(R.id.receiver_time)

          fun receiverBind(messagesModel: MessagesModel, context : Context){
              receiverMessage.text = messagesModel.message
          }
    }

    class SenderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var senderMessage: TextView = itemView.findViewById(R.id.tv_sender_text)
        var senderTime: TextView = itemView.findViewById(R.id.tv_sender_time)

        fun senderBind(messagesModel: MessagesModel, context : Context){
            senderMessage.text = messagesModel.message
        }

    }

    abstract class BaseViewHolder<T> constructor(itemView: View): RecyclerView.ViewHolder(itemView) {

         abstract fun bind(_object: T)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == SENDER_VIEW_TYPE){
            var view: View = LayoutInflater.from(context).inflate(R.layout.sample_sender, parent, false)

            return SenderViewHolder(view)
        }else{
            var view: View = LayoutInflater.from(context).inflate(R.layout.sample_receiver, parent, false)

            return ReceiverViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        //return super.getItemViewType(position)

        Log.i("Messages Model",messagesModel!!.get(position).uId.toString())

        Log.i("Firebase Auth",(FirebaseAuth.getInstance().uid).toString())

        if (messagesModel!!.get(position).uId.equals(FirebaseAuth.getInstance().uid)){
            return SENDER_VIEW_TYPE!!
        }else{
            return RECEIVER_VIEW_TYPE!!
        }
    }

    override fun getItemCount(): Int {

        return messagesModel.size
    }

  /*  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
*/
     /* fun onBindViewHolder(holder: SenderViewHolder, position: Int) {
        var messagesModel: MessagesModel = messageModel!!.get(position)


      if (holder::class.java ==SenderViewHolder::class.java ){

          holder.senderBind(messagesModel, context)

      }
    }

    fun onBindViewHolder(holder: ReceiverViewHolder, position: Int) {
        var messagesModel: MessagesModel = messageModel!!.get(position)


        if (holder::class.java ==ReceiverViewHolder::class.java ){

            holder.receiverBind(messagesModel, context)

        }
    }*/

     override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
/*
         holder.itemView.setOnLongClickListener { view->
             var builder = AlertDialog.Builder(context)
             builder.setTitle("Delete")
             builder.setMessage("Are You Sure You Want To Delete This Message?")
             builder.show()


         }*/

         holder.itemView.setOnLongClickListener(OnLongClickListener {
             var builder = AlertDialog.Builder(context)
             builder.setTitle("Delete")
             builder.setMessage("Are You Sure You Want To Delete This Message?")
             builder.setPositiveButton("Yes",DialogInterface.OnClickListener { dialog, which ->
                 Toast.makeText(context, "Test", Toast.LENGTH_SHORT).show()

                 var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
                 var sender: String = FirebaseAuth.getInstance().uid + recId
                 firebaseDatabase.reference.child("chats").child(sender)
                         .child(messagesModel.get(position).messageId!!).setValue(null)
             })

             builder.setNegativeButton("No",DialogInterface.OnClickListener { dialog, which ->
                 Toast.makeText(context, "Test", Toast.LENGTH_SHORT).show()
                 dialog.dismiss()
             })
             builder.show()
             false // returning true instead of false, works for me
         })

        // var messagesModel: MessagesModel = messageModel!!.get(position)

         if (holder::class.java ==ReceiverViewHolder::class.java){

             (holder as ReceiverViewHolder).receiverBind(messagesModel.get(position),context)

         }else{
             (holder as SenderViewHolder).senderBind(messagesModel.get(position),context)
         }
     }


 }