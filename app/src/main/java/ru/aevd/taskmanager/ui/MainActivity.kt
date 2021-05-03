package ru.aevd.taskmanager.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.aevd.taskmanager.R
import ru.aevd.taskmanager.databinding.ActivityMainBinding
import ru.aevd.taskmanager.ui.fragments.CalendarFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .apply {
                    add(R.id.fragments_container, CalendarFragment())
                    commit()
                }
        }
    }

}