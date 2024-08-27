package com.example.stocktracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.stocktracker.databinding.FragmentRegisterBinding
import com.google.firebase.database.FirebaseDatabase

class RegisterFragment : Fragment() {


    private lateinit var _binding:FragmentRegisterBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding=FragmentRegisterBinding.inflate(inflater,container,false)

        var vt=MainDatabase(requireContext())
        _binding.register.setOnClickListener {
            MainDatabaseDao().PersonalAdd(vt,_binding.mail.text.toString(),_binding.username.text.toString(),_binding.password.text.toString())
            var list=MainDatabaseDao().LastData(vt)
            for(k in list){
                val database=FirebaseDatabase.getInstance()
                val Users=database.getReference("Users")
                val user=FPersonalInformation(k.id,k.mail,k.username,k.password )
                Users.push().setValue(user)
            }

        }


        return _binding.root
    }


}