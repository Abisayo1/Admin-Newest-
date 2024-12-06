package com.abisayo.cloudspace_scophy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class EnterWACESubjects: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.waec_combination, container, false
        )
    }
    // Here "layout_login" is a name of layout file
    // created for LoginFragment
}