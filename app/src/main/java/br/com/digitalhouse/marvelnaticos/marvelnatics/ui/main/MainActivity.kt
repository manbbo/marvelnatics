package br.com.digitalhouse.marvelnaticos.marvelnatics.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.viewpager2.widget.ViewPager2
import br.com.digitalhouse.marvelnaticos.marvelnatics.R
import br.com.digitalhouse.marvelnaticos.marvelnatics.adapters.MainPagerAdapter
import br.com.digitalhouse.marvelnaticos.marvelnatics.ui.comics.ComicFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var mainPagerAdapter: MainPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pager: ViewPager2 = findViewById(R.id.vp_main_fragmentViewer)
        val includeMainNavbar: View = findViewById(R.id.include_main_navbar)
        val navbar: BottomNavigationView = includeMainNavbar.findViewById(R.id.bn_main)

        mainPagerAdapter = MainPagerAdapter(this, 3)
        pager.adapter = mainPagerAdapter

        navbar.setOnNavigationItemSelectedListener {
            it.isChecked = true
            when (it.itemId) {
                R.id.nb_home -> {
                    pager.currentItem = 0
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.nb_search -> {
                    pager.currentItem = 1
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.nb_profile -> {
                    pager.currentItem = 2
                    return@setOnNavigationItemSelectedListener true
                }

            }
            true
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    fun goToActivity(target: Class<*>?, enterAnim: Int, exitAnim: Int) {
        val intent = Intent(this, target)
        startActivity(intent)
        overridePendingTransition(enterAnim, exitAnim)
    }

    fun changeFragment(){
        val comicFragment = ComicFragment.newInstance()
        supportFragmentManager.commit {
            replace<ComicFragment>(R.id.vp_main_fragmentViewer)
        }
    }
}