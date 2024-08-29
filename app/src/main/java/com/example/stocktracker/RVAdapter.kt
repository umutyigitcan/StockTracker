package com.example.stocktracker

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RVAdapter(var mContext:Context,var disaridanGelenListesi:ArrayList<StockInformation>):RecyclerView.Adapter<RVAdapter.myCardViewItemHolder>() {
    inner class myCardViewItemHolder(view: View):RecyclerView.ViewHolder(view){
        val stockName:TextView
        val stockQuantity:TextView
        val delete:ImageView
        val cardView:CardView

        init {
            cardView=view.findViewById(R.id.cardView)
            stockName=view.findViewById(R.id.stockName)
            stockQuantity=view.findViewById(R.id.stockQuantity)
            delete=view.findViewById(R.id.delete)
        }
    }

    override fun onBindViewHolder(holder: myCardViewItemHolder, position: Int) {
        val myholder=disaridanGelenListesi[position]
        holder.stockName.text="Item Name: "+myholder.StockName
        holder.stockQuantity.text="Stock Quantity: "+myholder.StockQuantity.toString()
        holder.delete.setOnClickListener {
            var database=FirebaseDatabase.getInstance()
            var data=database.getReference("StockInformation")
            data.addValueEventListener(object :ValueEventListener{
                override fun onDataChange(ds: DataSnapshot) {
                    for(p in ds.children){
                        var item=p.getValue(FStockInformation::class.java)
                        if(item!=null){
                            if(myholder.QRCode==item.qrcode){
                                var key=p.key
                                data.child("$key").removeValue()
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        }

        holder.cardView.setOnClickListener {
            var vt=lastEnteredDataInformationDatabase(mContext)
            lastEnteredDataInformationDatabaseDao().changeData(vt,myholder.QRCode,myholder.StockName,myholder.StockQuantity.toString())

            it.findNavController().navigate(R.id.action_wareHouse_to_stockStatus)
        }






    }

    override fun getItemCount(): Int {
        return disaridanGelenListesi.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myCardViewItemHolder {
        val design=LayoutInflater.from(mContext).inflate(R.layout.cardview,parent,false)
        return myCardViewItemHolder(design)
    }
}