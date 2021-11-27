package com.atria.seva

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.sign


class LoginFragment : Fragment() {

    companion object{
        private const val TAG = "LoginFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val signUpButton = view.findViewById<TextView>(R.id.signUpButton)
        signUpButton.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }

        val loginButton = view.findViewById<Button>(R.id.loginButton)
        val emailEditText = view.findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = view.findViewById<EditText>(R.id.passwordEditText)
        loginButton.setOnClickListener {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(
                emailEditText.text.toString(),
                passwordEditText.text.toString()
            ).addOnSuccessListener {

                if(it.user?.isEmailVerified == true){
                    Log.i(TAG, "onViewCreated: email verified so can go to home")
                }
                else{
                    Log.i(TAG, "onViewCreated: email not verified first need to set up")
                }

            }.addOnFailureListener {
                if (it is FirebaseAuthInvalidUserException){
                    // no such user exists
                    Log.i(TAG, "onViewCreated: No Such User")
                }
                Log.e(TAG, "onViewCreated: ", it)
            }
        }

    }
}