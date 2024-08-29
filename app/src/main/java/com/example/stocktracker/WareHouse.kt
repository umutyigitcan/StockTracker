package com.example.stocktracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.stocktracker.databinding.FragmentWareHouseBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class WareHouse : Fragment() {

    private lateinit var binding: FragmentWareHouseBinding
    private lateinit var adapter: RVAdapter
    private lateinit var sendedItem: ArrayList<StockInformation>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWareHouseBinding.inflate(inflater, container, false)
        sendedItem = ArrayList()
        adapter = RVAdapter(requireContext(), sendedItem)
        binding.rv.adapter = adapter
        binding.rv.setHasFixedSize(true)
        binding.rv.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)

        val database = FirebaseDatabase.getInstance()
        val data = database.getReference("StockInformation")
        data.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(ds: DataSnapshot) {
                for (p in ds.children) {
                    val item = p.getValue(FStockInformation::class.java)
                    if (item != null) {

                        sendedItem.add(StockInformation(item.qrcode?:"",item.stockName?:"", item.stockQuantity?:0))
                    }
                }
                // RecyclerView'i güncelle

                adapter.notifyDataSetChanged()


            }


            override fun onCancelled(error: DatabaseError) {
                // Hata durumunda yapılacak işlemler
            }
        })

        binding.warehouse.setOnClickListener {
            Snackbar.make(requireView(),"Zaten bu sayfadasınız...",Snackbar.LENGTH_SHORT).show()
        }
        binding.scanner.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_wareHouse_to_homepageFragment)
        }

        return binding.root
    }
}
