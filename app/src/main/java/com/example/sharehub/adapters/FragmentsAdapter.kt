package com.example.sharehub.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.sharehub.fragments.FilesFragment
import com.example.sharehub.fragments.LinksFragment
import com.example.sharehub.fragments.VideosFragment
import com.example.sharehub.models.ShareRoomModel

class FragmentsAdapter(private val myContext: Context, fm: FragmentManager, private val room: ShareRoomModel) : FragmentPagerAdapter(fm) {
    // creating the fragment tabs
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                return LinksFragment(myContext,room.id!!)
            }
            else -> return LinksFragment(myContext,room.id!!)
        }
    }

    override fun getCount(): Int {
        return 1
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position){
            0 -> "LINKS"
            else -> "LINKS"
        }
    }
    /*override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Files"
            1 -> "Videos"
            else -> {
                return "Links"
            }
        }
    }*/

}
