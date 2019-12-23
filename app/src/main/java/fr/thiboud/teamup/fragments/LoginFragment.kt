package fr.thiboud.teamup.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar

import fr.thiboud.teamup.R
import fr.thiboud.teamup.database.UserDB
import fr.thiboud.teamup.databinding.FragmentLoginBinding
import fr.thiboud.teamup.viewmodel.LoginViewModel
import fr.thiboud.teamup.viewmodelfactory.LoginViewModelFactory

class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application
        val dataSource = UserDB.getInstance(application).userDao
        val viewModelFactory = LoginViewModelFactory(dataSource, application)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login,
            container, false)

        binding.viewmodel = viewModel

        binding.btSubscription.setOnClickListener {
            onNavigateToSubscription()
        }

        viewModel.error.observe(this, Observer {error ->
            error?.let {
                val snack = Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG)
                snack.show()
            }
        })

        viewModel.loggedIn.observe(this, Observer { userLoggedIn ->
            userLoggedIn?.let {
                val text = "L'utilisateur " + userLoggedIn.firstname + " " + userLoggedIn.lastname + " est connecté !"
                val snack = Snackbar.make(binding.root, text,Snackbar.LENGTH_LONG)
                snack.show()
                onNavigateToChoice(userLoggedIn.id)
            }
        })

        return binding.root
    }

    fun onNavigateToChoice(userId: Long) {
        this.findNavController().navigate(
            LoginFragmentDirections.actionLoginFragmentToChoiceFragment(userId)
        )
    }

    fun onNavigateToSubscription() {
        this.findNavController().navigate(
            LoginFragmentDirections.actionLoginFragmentToSubscriptionFragment()
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
