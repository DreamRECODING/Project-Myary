package com.example.myary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.myary.databinding.ActivityMainBinding
import com.example.myary.Mychive
import com.example.myary.taskchaser.Task_Chaser
import com.example.myary.Mytory

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_main)
        replaceFragment(Mytory())

        binding.bottomNavigationView.setOnItemSelectedListener{

            when(it.itemId){

                R.id.mytory -> replaceFragment(Mytory())
                R.id.mychive -> replaceFragment(Mychive())
                R.id.taskchaser -> replaceFragment(Task_Chaser())
                else ->{

                }

            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTranscation = fragmentManager.beginTransaction()
        fragmentTranscation.replace(R.id.frame_layout, fragment)
        fragmentTranscation.commit()


    }
}