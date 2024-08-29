package com.example.stocktracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.stocktracker.databinding.FragmentStockStatusBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.journeyapps.barcodescanner.BarcodeEncoder

class StockStatus : Fragment() {

    private lateinit var _binding:FragmentStockStatusBinding
    var key=""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding= FragmentStockStatusBinding.inflate(inflater,container,false)

        _binding.scanner.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_stockStatus_to_homepageFragment)
        }
        _binding.warehouse.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_stockStatus_to_wareHouse)
        }
        _binding.increase.setOnClickListener {
            stockIncrease()
        }

        _binding.decrease.setOnClickListener {
            stockDecrease()
        }

        var vt=lastEnteredDataInformationDatabase(requireContext())
        var getData=lastEnteredDataInformationDatabaseDao().getData(vt)
        for(k in getData){
            _binding.selectedname.text="Item Name: "+k.itemName
            _binding.selectedquantitiy.text="Item Quantity: "+k.itemQuantity
            val writer=QRCodeWriter()
            val bitMatrix=writer.encode(k.qrcode,BarcodeFormat.QR_CODE,500,500)
            val barcodeEncoder=BarcodeEncoder()
            val bitmap=barcodeEncoder.createBitmap(bitMatrix)
            _binding.qr.setImageBitmap(bitmap)
        }

        var database=FirebaseDatabase.getInstance()
        var data=database.getReference("StockInformation")

        data.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(ds: DataSnapshot) {
                for(p in ds.children){
                    var stock=p.getValue(FStockInformation::class.java)
                    if(stock!=null){
                        for(k in getData){
                            if(k.qrcode==stock.qrcode){
                                key=p.key.toString()
                                //Snackbar.make(requireView(),key,Snackbar.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        return _binding.root
    }

    fun stockIncrease(){
        var stockIncrease=HashMap<String,Any>()
        var currentStock=0
        var stockName=""
        var qr=""
        var vt=lastEnteredDataInformationDatabase(requireContext())
        var lastData=lastEnteredDataInformationDatabaseDao().getData(vt)
        for(k in lastData){
            currentStock=k.itemQuantity.toInt()
            qr=k.qrcode
            stockName=k.itemName
            lastEnteredDataInformationDatabaseDao().changeQuantityIncreaseData(vt,k.qrcode,currentStock.toString())
        }
        currentStock++
        stockIncrease["qrcode"]=qr
        stockIncrease["stockName"]=stockName
        stockIncrease["stockQuantity"]=currentStock
        var database=FirebaseDatabase.getInstance()
        var data=database.getReference("StockInformation")
        data.child(key).updateChildren(stockIncrease)
        Snackbar.make(requireView(),"Increase Successful",Snackbar.LENGTH_SHORT).show()
        _binding.selectedquantitiy.text="Item Quantity: "+currentStock
    }


    fun stockDecrease(){
        var stockIncrease=HashMap<String,Any>()
        var currentStock=0
        var stockName=""
        var qr=""
        var vt=lastEnteredDataInformationDatabase(requireContext())
        var lastData=lastEnteredDataInformationDatabaseDao().getData(vt)
        for(k in lastData){
            currentStock=k.itemQuantity.toInt()
            qr=k.qrcode
            stockName=k.itemName
            lastEnteredDataInformationDatabaseDao().changeQuantityDecreaseData(vt,k.qrcode,currentStock.toString())
        }
        currentStock--
        stockIncrease["qrcode"]=qr
        stockIncrease["stockName"]=stockName
        stockIncrease["stockQuantity"]=currentStock
        var database=FirebaseDatabase.getInstance()
        var data=database.getReference("StockInformation")
        data.child(key).updateChildren(stockIncrease)
        Snackbar.make(requireView(),"Reduction Successful",Snackbar.LENGTH_SHORT).show()
        _binding.selectedquantitiy.text="Item Quantity: "+currentStock
    }



}