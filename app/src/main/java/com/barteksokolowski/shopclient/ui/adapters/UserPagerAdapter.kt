package com.barteksokolowski.shopclient.ui.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.barteksokolowski.shopclient.ui.fragments.*

internal class UserPagerAdapter(supportFragmentManager: FragmentManager) : FragmentPagerAdapter(supportFragmentManager) {

    override fun getItem(position: Int): Fragment? {
        return when (position) {
            0, 1, 2, 3, 4 -> CategoryFragment.newInstance(position)
            else -> null
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Klasyka"
            1 -> "Fantastyka"
            2 -> "Dramat"
            3 -> "Ekonomia"
            4 -> "Informatyka"
            else -> null
        }
    }

    override fun getCount(): Int = 5
}