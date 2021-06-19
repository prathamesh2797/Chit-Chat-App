package com.example.chitchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    var firebaseUser: FirebaseUser? = null
    var firebaseAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth!!.currentUser

        if (firebaseUser != null){
            val intent = Intent(this, MainScreen::class.java)
            startActivity(intent)
            finish()
        }

        btn_login_signup.setOnClickListener { view ->
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
            finish()
        }

        tv_forget_password.setOnClickListener { view->

            Toast.makeText(this, "Forget password loading", Toast.LENGTH_SHORT).show()
        }

//        btn_login.setOnClickListener { view->
//            loginFunction()
//
//        }

        loginFunction()
    }

    fun loginFunction(){

        btn_login.setOnClickListener { view->

            val emailId= et_login_email_id.text.toString()
            val password= et_login_password.text.toString()

            if (emailId.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Email ID and Password are mandatory fields", Toast.LENGTH_SHORT).show()
            }else{

                progressBar.visibility= View.VISIBLE

                firebaseAuth!!.signInWithEmailAndPassword(emailId,password).addOnCompleteListener { task->

                    if (task.isSuccessful){
                        checkMailVerification()
                    }else{
                        Toast.makeText(this, task.exception!!.message, Toast.LENGTH_SHORT).show()
                        progressBar.visibility = View.INVISIBLE
                    }

                }
            }

        }


    }

    fun checkMailVerification(){

        firebaseUser = firebaseAuth!!.currentUser
        if (firebaseUser!!.isEmailVerified){

            Toast.makeText(this, "Loggin In", Toast.LENGTH_SHORT).show()

            val intent = Intent(this,MainScreen::class.java)
            startActivity(intent)
            finish()
        }else{
            progressBar.visibility = View.INVISIBLE
            Toast.makeText(this, "Please Verify Your Email", Toast.LENGTH_SHORT).show()

            val intent= Intent(this, SignUp::class.java)
            startActivity(intent)

            firebaseAuth!!.signOut()
        }
    }
}