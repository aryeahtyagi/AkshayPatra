package com.atria.seva

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loginButton = view.findViewById<Button>(R.id.signUpButton)
        val emailEditText = view.findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = view.findViewById<EditText>(R.id.passwordEditText)

        loginButton.setOnClickListener {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                emailEditText.text.toString(),
                passwordEditText.text.toString()
            ).addOnSuccessListener {

                if(it.user?.isEmailVerified != true){
                    it.user?.sendEmailVerification()?.addOnSuccessListener {
                        Toast.makeText(context, "send Verification", Toast.LENGTH_SHORT).show()
                        looperCheckEmailVerified{
                            Toast.makeText(context, "Email Verified", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }.addOnFailureListener {
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun looperCheckEmailVerified(emailVerified:()->Unit)  {
        CoroutineScope(Dispatchers.IO).launch {
            FirebaseAuth.getInstance().currentUser?.reload()?.addOnSuccessListener {
                when(FirebaseAuth.getInstance().currentUser?.isEmailVerified){
                    true->{
                        emailVerified()
                    }
                    false->{
                        Thread.sleep(2000L)
                        looperCheckEmailVerified(emailVerified)
                    }
                }
            }
        }
    }

}