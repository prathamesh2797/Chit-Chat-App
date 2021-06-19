package com.example.chitchat

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.chitchat.Model.User
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_settings.*
import java.net.URI

class Settings : AppCompatActivity() {

    var storage: FirebaseStorage? = null
    var auth: FirebaseAuth? = null
    var firebaseDatabase: FirebaseDatabase? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportActionBar!!.hide()

        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()

        btn_back.setOnClickListener { view->
            var intent = Intent(this, MainScreen::class.java)
            startActivity(intent)

        }

        firebaseDatabase!!.getReference().child("Users").child(FirebaseAuth.getInstance().uid!!)
                .addValueEventListener(object : ValueEventListener{
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {

                        for (datasnapshots in snapshot.children){
                            var users: User = snapshot.getValue(User::class.java)!!
                            Picasso.get()
                                    .load(users.profilePic).placeholder(R.drawable.ic_person).into(profile)


                            et_status.setText(users.status)
                            et_username.setText(users.userName)
                        }

                    }

                })

        btn_plus.setOnClickListener{ view->

            var intent = Intent()
            intent.setAction(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent,33)
        }

        btn_save.setOnClickListener { view->

            var status = et_status.text.toString()
            var username = et_username.text.toString()

            val obj: MutableMap<String, Any> = mutableMapOf<String, Any>()
            obj["userName"]= username
            obj["status"] = status

            firebaseDatabase!!.reference.child("Users").child(FirebaseAuth.getInstance().uid!!).updateChildren(obj)

            Toast.makeText(this, "Username And Status Updated", Toast.LENGTH_SHORT).show()


        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (data!!.data != null){
            var sFile: Uri? = data!!.data
            profile.setImageURI(sFile)

            var reference: StorageReference = storage!!.reference.child("profile_pictures").child(auth!!.uid!!)

            reference.putFile(sFile!!).addOnSuccessListener(OnSuccessListener {
               // Toast.makeText(this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show()

                reference.downloadUrl.addOnSuccessListener(OnSuccessListener {
                    firebaseDatabase!!.getReference().child("Users").child(FirebaseAuth.getInstance().uid!!)
                            .child("profilePic").setValue(it.toString())

                    Log.i("Uri", it.toString())

                     Toast.makeText(this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show()

                })
            })
        }
    }
}