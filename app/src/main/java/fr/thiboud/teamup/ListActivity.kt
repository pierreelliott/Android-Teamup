package fr.thiboud.teamup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import fr.thiboud.teamup.fragments.ListFragment
import fr.thiboud.teamup.fragments.dummy.DummyContent

class ListActivity : AppCompatActivity(), ListFragment.OnListFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
    }

    override fun onListFragmentInteraction(item: DummyContent.DummyItem?) {
        Log.d("BOUUH", "Interaction with fragment in ListActivity")
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
