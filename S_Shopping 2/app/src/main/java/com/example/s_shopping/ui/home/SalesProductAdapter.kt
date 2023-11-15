package com.example.s_shopping.ui.home

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.s_shopping.data.model.response.ProductUI
import com.example.s_shopping.databinding.ItemSalesProductBinding

class SalesProductAdapter(
    private val productListener: ProductListener
) : ListAdapter<ProductUI, SalesProductAdapter.SalesProductViewHolder>(ProductDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalesProductViewHolder =
        SalesProductViewHolder(
            ItemSalesProductBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            productListener
        )

    override fun onBindViewHolder(holder: SalesProductViewHolder, position: Int) =
        holder.bind(getItem(position))

    class SalesProductViewHolder(
        private val binding: ItemSalesProductBinding,
        private val productListener: ProductListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductUI) {
            with(binding) {
                tvTitle.text = product.title
                tvPrice.text = "${product.price} ₺"

                Glide.with(productImage).load(product.imageOne).into(productImage)

                if (product.saleState == true) {
                    tvSalePrice.isVisible = true
                    tvSalePrice.text = "${product.salePrice} ₺"
                    tvPrice.text = "${product.price} ₺"
                    tvPrice.paintFlags = tvPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    tvPrice.text = "${product.price} ₺"
                    tvSalePrice.isVisible = false
                }

                root.setOnClickListener {
                    productListener.onProductClick(product.id ?: 1)
                }
            }
        }
    }

    class ProductDiffCallBack : DiffUtil.ItemCallback<ProductUI>() {
        override fun areItemsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem == newItem
        }
    }

    interface ProductListener {
        fun onProductClick(id: Int)
    }
}