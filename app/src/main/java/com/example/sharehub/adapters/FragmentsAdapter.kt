package com.example.sharehub.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.sharehub.fragments.FilesFragment
import com.example.sharehub.fragments.LinksFragment
import com.example.sharehub.fragments.VideosFragment

class FragmentsAdapter(private val myContext: Context, fm: FragmentManager, internal var totalTabs: Int) : FragmentPagerAdapter(fm) {

    // creating the fragment tabs
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                return VideosFragment()
            }
            1 -> {
                return LinksFragment()
            }
            2 -> {
                return FilesFragment()
            }
            else -> return VideosFragment()
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }
}
