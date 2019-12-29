package fr.thiboud.teamup

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import fr.thiboud.teamup.apis.Breeds
import fr.thiboud.teamup.fragments.ListFragment
import fr.thiboud.teamup.fragments.dummy.DummyContent

class MainActivity : AppCompatActivity(), ListFragment.OnListFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onListFragmentInteraction(item: Breeds?) {
        Log.d("BOUUH", "Interaction with fragment")
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}