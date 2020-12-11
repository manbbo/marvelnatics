package br.com.digitalhouse.marvelnaticos.marvelnatics.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.viewpager2.widget.ViewPager2
import br.com.digitalhouse.marvelnaticos.marvelnatics.R
import br.com.digitalhouse.marvelnaticos.marvelnatics.adapters.MainPagerAdapter
import br.com.digitalhouse.marvelnaticos.marvelnatics.ui.comic.ComicFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode

class MainActivity : AppCompatActivity() {

    private lateinit var mainPagerAdapter: MainPagerAdapter
    private lateinit var navbar: BottomNavigationView
    private lateinit var pager : ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pager = findViewById(R.id.vp_main_fragmentViewer)
        val includeMainNavbar: View = findViewById(R.id.include_main_navbar)
        navbar = includeMainNavbar.findViewById(R.id.bn_main)

        navbar.itemIconTintList = null
        navbar.menu.get(0).setIcon(R.drawable.ic_selected_home)

        mainPagerAdapter = MainPagerAdapter(this, 3)
        pager.adapter = mainPagerAdapter
        pager.isUserInputEnabled = false


        navbar.setOnNavigationItemSelectedListener {
            it.isChecked = true
            when (it.itemId) {
                R.id.nb_home -> {
                    it.setIcon(R.drawable.ic_selected_home)
                    navbar.menu.get(1).setIcon(R.drawable.ic_search_white)
                    navbar.menu.get(2).setIcon(R.drawable.ic_profile_white)

                    pager.currentItem = 0
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.nb_search -> {
                    it.setIcon(R.drawable.ic_selected_search)
                    navbar.menu.get(0).setIcon(R.drawable.ic_home_white)
                    navbar.menu.get(2).setIcon(R.drawable.ic_profile_white)

                    pager.currentItem = 1
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.nb_profile -> {
                    it.setIcon(R.drawable.ic_selected_profile)
                    navbar.menu.get(0).setIcon(R.drawable.ic_home_white)
                    navbar.menu.get(1).setIcon(R.drawable.ic_search_white)

                    pager.currentItem = 2
                    return@setOnNavigationItemSelectedListener true
                }

            }
            true
        }
    }

    override fun onBackPressed() {
        if (navbar.menu.get(0).isChecked) super.onBackPressed()
        else{
            navbar.menu.get(0).isChecked = true
            navbar.menu.get(0).setIcon(R.drawable.ic_selected_home)
            navbar.menu.get(1).setIcon(R.drawable.ic_search_white)
            navbar.menu.get(2).setIcon(R.drawable.ic_profile_white)

            pager.currentItem = 0
        }
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