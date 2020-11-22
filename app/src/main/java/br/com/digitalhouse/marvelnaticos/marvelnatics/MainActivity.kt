package br.com.digitalhouse.marvelnaticos.marvelnatics

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.navbar.*
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = tb_main_toolbar
        setSupportActionBar(toolbar)

        bn_main.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nb_home -> {
                    Toast.makeText(this, "asdasdasd", Toast.LENGTH_SHORT).show()
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.nb_search-> {
                    Toast.makeText(this, "asdasdasd", Toast.LENGTH_SHORT).show()
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.nb_profile-> {
                    Toast.makeText(this, "asdasdasd", Toast.LENGTH_SHORT).show()
                    return@setOnNavigationItemSelectedListener true
                }

            }
            false

        }

    }
}