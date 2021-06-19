package com.example.chitchat.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chitchat.Adapters.UsersAdapter
import com.example.chitchat.R
import com.example.chitchat.Model.User
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [ChatsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChatsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var database: FirebaseDatabase? = null
    var firebaseRecyclerOptions: FirebaseRecyclerOptions<User>?= null
    var ref:Query?= null

    private var firebaseRecyclerAdapter:FirebaseRecyclerAdapter<User, UsersAdapter.ViewHolder>? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)


        }




    }






   // var list: ArrayList<User>? =null
    var adapter:UsersAdapter? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var listItems: View = inflater.inflate(R.layout.fragment_chats,container, false)

        database = FirebaseDatabase.getInstance()

        var chatsRecyclerView: RecyclerView = listItems.findViewById(R.id.chatsRecyclerView)
        /*chatsRecyclerView.layoutManager =LinearLayoutManager(context)
        chatsRecyclerView.setHasFixedSize(true)*/

        var list = ArrayList<User>()

        adapter= UsersAdapter(context!!, list!!)
        //chatsRecyclerView.setAdapter(adapter)
        chatsRecyclerView.adapter = adapter

        chatsRecyclerView.layoutManager =LinearLayoutManager(context)
        chatsRecyclerView.setHasFixedSize(true)

        ref= database!!.reference.child("Users")
        Log.i("Reference", ref.toString())

        firebaseRecyclerOptions = FirebaseRecyclerOptions.Builder<User>().setQuery(ref!!, User::class.java).build()

        ref!!.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                list!!.clear()

                for (datasnapshot in snapshot.children){
                    //var users: User?=null

                    var users = datasnapshot.getValue(User::class.java)
                   users!!.userId = datasnapshot.key
                    Log.i("Datasnap", users.toString())

                    if(!users.userId.equals(FirebaseAuth.getInstance().uid)){
                        list!!.add(users!!)

                    }
                }
               /* adapter= UsersAdapter(context!!, list!!)

                chatsRecyclerView.setAdapter(adapter)*/
                Log.i("List", list.toString())
                adapter!!.notifyDataSetChanged()

            }

        })


      /*  val users: User? = null
        FirebaseDatabase.getInstance().getReference().child("chats").child(FirebaseAuth.getInstance().uid!! + users!!.userId)
            .orderByChild("timeStamp").limitToLast(1)
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.hasChildren()){
                        for (datasnapshots in snapshot.children){

                            val users = datasnapshots.getValue(User::class.java)
                            users!!.lastMessage = datasnapshots.child("message").getValue().toString()
                            //holder.lastMessage.setText(datasnapshots.child("message").getValue().toString())
                            //notifyDataSetChanged()

                            list?.add(users)

                        }
                        adapter!!.notifyDataSetChanged()

                    }
                }

            })
*/


        return listItems
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of1 fragment ChatsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChatsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}