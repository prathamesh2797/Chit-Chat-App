package com.example.chitchat

import com.example.chitchat.Model.User
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : AppCompatActivity() {

    var firebaseAuth: FirebaseAuth? = null
    var progressDialog: ProgressDialog? = null
    var firebaseDatabase: FirebaseDatabase? = null
    var firebaseUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_sign_up)

        supportActionBar?.hide()

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseUser = FirebaseAuth.getInstance().currentUser

        progressDialog = ProgressDialog(this)
        progressDialog!!.setTitle("Creating Account")
        progressDialog!!.setMessage("We are creating your account. Please Wait!")



        tv_login.setOnClickListener { view->

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()

        }

        btn_signUp.setOnClickListener { view ->

            var username = et_username.text.toString()
            var emailId = et_email_id.text.toString()
            var password= et_password.text.toString()

            if (emailId.isEmpty() || username.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Username, Email ID and Password are mandatory fields", Toast.LENGTH_SHORT).show()
            }else if (password.length <7){
                Toast.makeText(this, "Password lenth should be more than 7 characters", Toast.LENGTH_SHORT).show()
            }else{
                progressDialog!!.show()
                firebaseAuth!!.createUserWithEmailAndPassword(emailId, password).addOnCompleteListener(
                    OnCompleteListener { task ->
                        if (task.isSuccessful){
                            progressDialog!!.dismiss()

                            Log.i("Task",task.toString())

                            var user: User = User(
                                et_username.text.toString(),
                                et_email_id.text.toString(),
                                et_password.text.toString()
                            )

                            //var users: ArrayList<User> = ArrayList()




                            var id: String= task.result!!.user!!.uid

                            Log.i("ID String", id.toString())
                            Log.i("Database reference", firebaseDatabase!!.reference.toString())
                            Log.i("Users Value", user.toString())
                            firebaseDatabase!!.reference.child("Users").child(id).setValue(user)



                            //user.userId = firebaseUser!!.uid

                            Toast.makeText(this, "User created Successfully! Verification Email sent on $emailId", Toast.LENGTH_SHORT).show()
                            sentEmailverification()
                        }else{
                            progressDialog!!.dismiss()
                            Toast.makeText(this, task.exception!!.message, Toast.LENGTH_SHORT).show()
                        }
                    })
            }



        }

    }
    fun sentEmailverification(){

        var firebaseUser: FirebaseUser? =firebaseAuth!!.currentUser
        if (firebaseUser != null){
            firebaseUser.sendEmailVerification().addOnCompleteListener{ task->
                if (task.isSuccessful){
                    Toast.makeText(this, "Verification Email sent ", Toast.LENGTH_SHORT).show()



                    firebaseAuth!!.signOut()
                    finish()

                    var intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }

            }
        }else{
            Toast.makeText(this, "Error Occured!! Please try again", Toast.LENGTH_SHORT).show()
        }
    }
}