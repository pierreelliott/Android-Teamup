package fr.thiboud.teamup.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import fr.thiboud.teamup.R
import fr.thiboud.teamup.apis.Breeds
import fr.thiboud.teamup.databinding.FragmentItemBinding
import fr.thiboud.teamup.utils.setBreedImage
import fr.thiboud.teamup.viewmodel.ItemViewModel
import fr.thiboud.teamup.viewmodelfactory.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ItemFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ItemFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ItemFragment : Fragment() {
    private var breed: Breeds? = null

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            breed = it.getParcelable("BREED")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentItemBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_item, container, false)

        val application = requireNotNull(this.activity).application
        val viewModelFactory = ViewModelFactory(application, breed!!)

        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(ItemViewModel::class.java)
        binding.viewmodel = viewModel

        val frag = this

        coroutineScope.launch {
            binding.viewmodel?.breed?.value?.loadImage()

            binding.viewmodel?.breed?.value?.image?.observe(frag, Observer {
                binding.breedImage.setBreedImage(it)
            })
        }

        return binding.root
    }
}
