package com.example.stocktracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.stocktracker.databinding.FragmentLoginPageBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginPageFragment : Fragment() {

    private lateinit var _binding:FragmentLoginPageBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding= FragmentLoginPageBinding.inflate(inflater,container,false)
        _binding.register.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_loginPageFragment_to_registerFragment)
        }
        _binding.next.setOnClickListener {
            val database=FirebaseDatabase.getInstance()
            val Users=database.getReference("Users")
            Users.addValueEventListener(object:ValueEventListener{
                override fun onDataChange(ds: DataSnapshot) {
                    for(p in ds.children){
                        val user=p.getValue(FPersonalInformation::class.java)
                        if(user!=null){
                            val key=p.key
                            if(_binding.edittext.text.toString()==user.mail && _binding.edittext2.text.toString()==user.password){
                                Navigation.findNavController(it).navigate(R.id.action_loginPageFragment_to_homepageFragment)
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }

        return _binding.root
    }
}