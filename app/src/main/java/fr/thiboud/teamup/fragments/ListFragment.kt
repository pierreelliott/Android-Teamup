package fr.thiboud.teamup.fragments

import android.content.Context
import android.os.Bundle
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import fr.thiboud.teamup.R
import fr.thiboud.teamup.adapter.APIItemRecyclerViewAdapter
import fr.thiboud.teamup.adapter.UserListener
import fr.thiboud.teamup.apis.Breeds
import fr.thiboud.teamup.database.UserDB
import fr.thiboud.teamup.database.UserDao
import fr.thiboud.teamup.databinding.FragmentItemListBinding

import fr.thiboud.teamup.fragments.dummy.DummyContent
import fr.thiboud.teamup.fragments.dummy.DummyContent.DummyItem
import fr.thiboud.teamup.model.User
import fr.thiboud.teamup.viewmodel.ListViewModel
import fr.thiboud.teamup.viewmodelfactory.LoginViewModelFactory
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [ListFragment.OnListFragmentInteractionListener] interface.
 */
class ListFragment : Fragment() {

    private var listener: OnListFragmentInteractionListener? = null

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentItemListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_item_list, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = UserDB.getInstance(application).userDao
        val viewModelFactory = LoginViewModelFactory(dataSource, application)
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(ListViewModel::class.java)

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        val adapter = APIItemRecyclerViewAdapter(listener)
//        val adapter = APIItemRecyclerViewAdapter(UserListener { userId ->
//            Toast.makeText(this.context,"UserID '$userId' clicked",Toast.LENGTH_SHORT).show()
//        })
        binding.list.adapter = adapter

        coroutineScope.launch {
            viewModel.listItems.observe(viewLifecycleOwner, Observer {
                it?.let {
                    adapter.submitList(it)
                }
            })
        }


        return binding.root

//        val view = inflater.inflate(R.layout.fragment_item_list, container, false)
//
//        // Set the adapter
//        if (view is RecyclerView) {
//            with(view) {
//                layoutManager = when {
//                    columnCount <= 1 -> LinearLayoutManager(context)
//                    else -> GridLayoutManager(context, columnCount)
//                }
//                adapter = APIItemRecyclerViewAdapter(
//                    DummyContent.ITEMS,
//                    listener
//                )
//            }
//        }
//        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: DummyItem?)
    }

}
