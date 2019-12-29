package fr.thiboud.teamup.fragments

import android.app.DatePickerDialog
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.InverseMethod
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar

import fr.thiboud.teamup.R
import fr.thiboud.teamup.database.UserDB
import fr.thiboud.teamup.databinding.SubscriptionFragmentBinding
import fr.thiboud.teamup.model.User
import fr.thiboud.teamup.viewmodel.SubscriptionViewModel
import fr.thiboud.teamup.viewmodelfactory.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

object LongConverter {
    @JvmStatic
    @InverseMethod("stringToDate")
    fun dateToString(
        value: Long
    ): String {
        val date = Date(value)
        val f = SimpleDateFormat("dd/MM/yyyy")
        val dateText = f.format(date)
        return dateText
    }

    @JvmStatic
    fun stringToDate(
        value: String
    ): Long {
        val f = SimpleDateFormat("dd/MM/yyyy")
        val d = f.parse(value)
        return d.time
    }
}

@BindingAdapter("userDate")
fun TextView.setUserDate(item: User) {
    val date = Date(item.birthdayDate)
    val f = SimpleDateFormat("dd/MM/yy")
    val dateText = f.format(date)
    text = dateText
}

class SubscriptionFragment : Fragment() {

    private lateinit var viewModel: SubscriptionViewModel
    private lateinit var binding: SubscriptionFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application
        val dataSource = UserDB.getInstance(application).userDao
        val viewModelFactory = ViewModelFactory(dataSource, application)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SubscriptionViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.subscription_fragment,
            container, false)

        binding.viewmodel = viewModel

        val adapter = ArrayAdapter.createFromResource(binding.root.context,
            R.array.country_list, android.R.layout.simple_spinner_item)
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        binding.countrySpinner.adapter = adapter

//        binding.

        binding.tiAge.setOnClickListener {
            clickDatePicker()
        }

        viewModel.error.observe(this, Observer {error ->
            error?.let {
                val snack = Snackbar.make(binding.root,error, Snackbar.LENGTH_LONG)
                snack.show()
            }
        })

        viewModel.subscribed.observe(this, Observer { userId ->
            userId?.let {
                navigateToLogin(userId)
            }
        })

        return binding.root
    }

    fun navigateToLogin(userId: Long) {
        this.findNavController().navigate(
            SubscriptionFragmentDirections.actionSubscriptionFragmentToLoginFragment()
        )
    }

    fun clickDatePicker() {//view: View) {
        val c = getUserOrCurrentDate()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(binding.root.context, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            binding.viewmodel!!.user.value?.birthdayDate = LongConverter.stringToDate("""$dayOfMonth/${monthOfYear + 1}/$year""")

        }, year, month, day)
        dpd.show()
    }

    fun getUserOrCurrentDate(): Calendar {
        val cal = Calendar.getInstance()
        binding.viewmodel!!.user.value?.birthdayDate?.let { cal.time = Date(it) }
        return cal
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SubscriptionViewModel::class.java)
    }

}
