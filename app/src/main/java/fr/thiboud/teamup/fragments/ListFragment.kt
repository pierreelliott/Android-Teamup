package fr.thiboud.teamup.fragments

import android.content.Context
import android.graphics.Movie
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import fr.thiboud.teamup.R
import fr.thiboud.teamup.adapter.APIItemRecyclerViewAdapter
import fr.thiboud.teamup.apis.Breeds
import fr.thiboud.teamup.database.UserDB
import fr.thiboud.teamup.databinding.FragmentItemListBinding
import fr.thiboud.teamup.utils.RecyclerTouchListener
import fr.thiboud.teamup.viewmodel.ListViewModel
import fr.thiboud.teamup.viewmodelfactory.ViewModelFactory
import kotlinx.coroutines.*


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
        val viewModelFactory = ViewModelFactory(dataSource, application)
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(ListViewModel::class.java)

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

//        val adapter = APIItemRecyclerViewAdapter(listener)
        val adapter = APIItemRecyclerViewAdapter(object : OnListFragmentInteractionListener {
            override fun onListFragmentInteraction(item: Breeds?) {
                item?.let { navigateToDetails(it) }
            }
        }, viewLifecycleOwner)
        binding.list.adapter = adapter

        binding.list.addOnItemTouchListener(
            RecyclerTouchListener(
                binding.root.context,
                binding.list,
                object : RecyclerTouchListener.ClickListener {
                    override fun onClick(view: View?, position: Int) {
                        val breed: Breeds? = binding.viewmodel?.listItems?.value?.get(position)
                        breed?.let { navigateToDetails(it) }
                    }

                    override fun onLongClick(view: View?, position: Int) {}
                })
        )

        coroutineScope.launch {
            viewModel.listItems.observe(viewLifecycleOwner, Observer {
                if(binding.progressBar.visibility == View.VISIBLE) {
                    binding.progressBar.visibility = View.GONE
                    binding.list.visibility = View.VISIBLE
                }
                it?.let {
                    adapter.submitList(it)
                }

//                coroutineScope.launch {
//                    withContext(Dispatchers.IO) {
//                        binding.viewmodel?.listItems?.value?.forEach {
//                            it.loadImage()
//                        }
//                    }
//                }
            })
        }


        return binding.root
    }

    private fun navigateToDetails(breed: Breeds) {
        this.findNavController().navigate(
            ListFragmentDirections.actionItemFragmentToItemFragment2(breed)
        )
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

    fun reloadItemView(viewholder: APIItemRecyclerViewAdapter.ViewHolder) {

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
        fun onListFragmentInteraction(item: Breeds?)
    }

}
