package com.example.s_shopping.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.s_shopping.data.model.response.ProductUI
import com.example.s_shopping.databinding.ItemFavoriteBinding

class FavoriteAdapter(
    private val favProductListener: FavProductListener
) : ListAdapter<ProductUI, FavoriteAdapter.FavProductViewHolder>(FavProductDiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavProductViewHolder {
        return FavProductViewHolder(
            ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            favProductListener
        )
    }

    override fun onBindViewHolder(holder: FavProductViewHolder, position: Int) =
        holder.bind(getItem(position))

    class FavProductViewHolder(
        private val binding: ItemFavoriteBinding,
        private val favProductListener: FavProductListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductUI) {
            with(binding) {
                tvTitle.text = product.title

                Glide.with(ivProduct).load(product.imageOne).into(ivProduct)

                if (product.saleState == true) {
                    tvPrice.isVisible = true
                    tvPrice.text = "${product.salePrice} ₺"
                } else {
                    tvPrice.text = "${product.price} ₺"
                    tvPrice.isVisible = true
                }

                root.setOnClickListener {
                    favProductListener.onProductClick(product.id)
                }

                ivDelete.setOnClickListener {
                    favProductListener.onDeleteClick(product)
                }
            }
        }
    }

    class FavProductDiffUtilCallBack : DiffUtil.ItemCallback<ProductUI>() {
        override fun areItemsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem == newItem
        }
    }

    interface FavProductListener {
        fun onProductClick(id: Int)
        fun onDeleteClick(product: ProductUI)
    }
}