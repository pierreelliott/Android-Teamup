package fr.thiboud.teamup.adapter

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import fr.thiboud.teamup.R
import fr.thiboud.teamup.apis.Breeds
import fr.thiboud.teamup.databinding.ItemViewBinding


import fr.thiboud.teamup.fragments.ListFragment.OnListFragmentInteractionListener
import fr.thiboud.teamup.fragments.dummy.DummyContent.DummyItem
import fr.thiboud.teamup.model.User
import fr.thiboud.teamup.utils.setBreedImage

import kotlinx.android.synthetic.main.item_view.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */
class APIItemRecyclerViewAdapter(
    //private val mValues: List<User>,
    private val mListener: OnListFragmentInteractionListener,
    private val lifecycleOwner: LifecycleOwner
) : ListAdapter<Breeds, APIItemRecyclerViewAdapter.ViewHolder>(UserDiffCallback()) {

    // FIXME
    private val mOnClickListener: ItemListener

    init {
        mOnClickListener = ItemListener { breed ->
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener.onListFragmentInteraction(breed)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, lifecycleOwner)
    }

    class ViewHolder private constructor(val binding: ItemViewBinding) : RecyclerView.ViewHolder(binding.root){

        private val coroutineScope = CoroutineScope(Job() + Dispatchers.Main )

        fun bind(item: Breeds, lifecycleOwner: LifecycleOwner) {
            binding.breed = item
            binding.executePendingBindings()
            if(binding.breed?.image?.value == null) {
                coroutineScope.launch {
                    binding.breed?.loadImage()
                }
            }
            binding.breed?.image?.observe(lifecycleOwner, Observer {
                it?.let {
                    binding.breedImage.setBreedImage(it)
                }
            })
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemViewBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    class UserDiffCallback : DiffUtil.ItemCallback<Breeds>() {
        override fun areItemsTheSame(oldItem: Breeds, newItem: Breeds): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Breeds, newItem: Breeds): Boolean {
            return oldItem == newItem
        }
    }
}

class ItemListener(val clickListener: (breed: Breeds) -> Unit) {
    fun onClick(breed: Breeds) = clickListener(breed)
}