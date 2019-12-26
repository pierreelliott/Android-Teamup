package fr.thiboud.teamup.adapter

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import fr.thiboud.teamup.R
import fr.thiboud.teamup.apis.Breeds
import fr.thiboud.teamup.databinding.ItemViewBinding


import fr.thiboud.teamup.fragments.ListFragment.OnListFragmentInteractionListener
import fr.thiboud.teamup.fragments.dummy.DummyContent.DummyItem
import fr.thiboud.teamup.model.User

import kotlinx.android.synthetic.main.item_view.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */
class APIItemRecyclerViewAdapter(
    //private val mValues: List<User>,
    private val mListener: OnListFragmentInteractionListener?
) : ListAdapter<Breeds, APIItemRecyclerViewAdapter.ViewHolder>(UserDiffCallback()) {

    // FIXME
    private val mOnClickListener: UserListener = UserListener { id: String -> Log.d("ID", """ID: $id""") }

//    init {
//        mOnClickListener = View.OnClickListener { v ->
//            val item = v.tag as DummyItem
//            // Notify the active callbacks interface (the activity, if the fragment is attached to
//            // one) that an item has been selected.
//            mListener?.onListFragmentInteraction(item)
//        }
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_view, parent, false)
//        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, mOnClickListener)

//        val item = mValues[position]
//        holder.mIdView.text = item.id
//        holder.mContentView.text = item.content
//
//        with(holder.mView) {
//            tag = item
//            setOnClickListener(mOnClickListener)
//        }
    }

    class ViewHolder private constructor(val binding: ItemViewBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: Breeds, clickListener: UserListener) {
            binding.breed = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
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

class UserListener(val clickListener: (userid: String) -> Unit) {
    fun onClick(user: Breeds) = clickListener(user.id)
}