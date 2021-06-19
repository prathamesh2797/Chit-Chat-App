package com.example.chitchat

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.chitchat.Adapters.FragmentsAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main_screen.*

class MainScreen : AppCompatActivity() {

    var firebaseAuth:FirebaseAuth? = null
    var firebaseUser: FirebaseUser? = null
    var firebaseFirestore: FirebaseFirestore? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        supportActionBar!!.setElevation(0F)


        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser= firebaseAuth!!.currentUser
        firebaseFirestore= FirebaseFirestore.getInstance()


        var pAdapter: FragmentsAdapter

        pAdapter = FragmentsAdapter(supportFragmentManager)
        viewPager.adapter= pAdapter

        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.logout ->{

                firebaseAuth!!.signOut()
                finish()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }

            R.id.settings ->{
                val intent = Intent(this, Settings::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)

    }
}