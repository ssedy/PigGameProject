package com.sarahsedy.piggame
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class SplashScreen {


    class AboutActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_about)
        }

        fun btnSupportOnClick(v : View){
            val supportLink : Uri = Uri.parse("https://spaghetti-code-wizard.glitch.me/")
            val intent = Intent(Intent.ACTION_VIEW, supportLink)
            startActivity(intent)
        }

        fun btnAboutOnClick(v : View){
            val aboutLink : Uri = Uri.parse("https://keicodes.wordpress.com/about-us/")
            val intent = Intent(Intent.ACTION_VIEW, aboutLink)
            startActivity(intent)
        }

        fun imgViewOnClick(v : View){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

}