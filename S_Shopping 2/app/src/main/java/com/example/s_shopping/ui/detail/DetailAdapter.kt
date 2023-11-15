package com.example.s_shopping.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.s_shopping.databinding.ItemDetailImageBinding

class DetailAdapter : RecyclerView.Adapter<DetailAdapter.ProductsViewHolder>() {

    private val list = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder =
        ProductsViewHolder(
            ItemDetailImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) =
        holder.bind(list[position])

    inner class ProductsViewHolder(private var binding: ItemDetailImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {
            with(binding) {
                Glide.with(imgProduct).load(item).into(imgProduct)
            }
        }
    }

    override fun getItemCount() = list.size

    fun updateList(updatedList: List<String>) {
        if (updatedList != null) {
            list.clear()
            list.addAll(updatedList)
            notifyDataSetChanged()
        }
    }
}
