package com.example.chitchat.Adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.chitchat.ChatDetailActivity
import com.example.chitchat.Model.MessagesModel
import com.example.chitchat.R
import com.example.chitchat.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlin.system.exitProcess

class UsersAdapter(val context: Context, val user: ArrayList<User>) :
        RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

   //var list: ArrayList<User>? = null


    lateinit var usersUse : ArrayList<User>

    inner  class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val profilePic: ImageView = itemView.findViewById(R.id.profile)
        val username:TextView = itemView.findViewById(R.id.tvUsername)
        val lastMessage:TextView =itemView.findViewById(R.id.tv_lastMessage)

        fun bind(user: User, context: Context){
            username.text= user.userName
            //lastMessage.text = user.lastMessage
        }

        fun bindLastMessage(user:User, context: Context){
            lastMessage.text = user.lastMessage
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.sample_show_user,parent,false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {

        return user!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var currentItem = user!!.get(position)

        Picasso.get().load(currentItem!!.profilePic).placeholder(R.drawable.ic_person).into(holder.profilePic)
        //holder.username.setText(currentItem.userName)


       var ref= FirebaseDatabase.getInstance().reference.child("chats").child(FirebaseAuth.getInstance().uid!! + currentItem.userId)
            .orderByChild("timeStamp").limitToLast(1)

        Log.i("Ref",ref.toString())


            ref.addValueEventListener(object: ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.hasChildren()){
                        for (datasnapshots in snapshot.children){
                            holder.lastMessage.setText(datasnapshots.child("message").getValue().toString())
                            //notifyDataSetChanged()
                            //notifyItemChanged(position)




                        }
                        //notifyItemChanged(position)


                    }
                }

            })


        holder.bind(user.get(position),context)

        Log.i("SetText"," Complete")
        holder.itemView.setOnClickListener {view ->
            val intent = Intent(context, ChatDetailActivity::class.java)


            if (user.get(position).userId.toString().isNullOrEmpty()){
                Log.i("Userid PUT", "Is Null")
            }else{
                intent.putExtra("userId", user.get(position).userId)
                Log.i("String Extra",user.get(position).userId.toString())

            }


            if (user.get(position).profilePic.toString().isNullOrEmpty()){
                Log.i("Put Profile Null", "Is null")

            }else{
                intent.putExtra("profilePic", user.get(position).profilePic)
                Log.i("put Profile Pic",user.get(position).profilePic.toString())

            }

            intent.putExtra("userName", user.get(position).userName)
            Log.i("Put Username",user.get(position).userName)
            context.startActivity(intent)

        }

    }



}