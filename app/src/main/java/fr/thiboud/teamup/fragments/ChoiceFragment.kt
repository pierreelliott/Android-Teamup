package fr.thiboud.teamup.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import fr.thiboud.teamup.R

/**
 * A simple [Fragment] subclass.
 */
class ChoiceFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val args = ChoiceFragmentArgs.fromBundle(arguments!!)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.choice_fragment, container, false)
    }


}
