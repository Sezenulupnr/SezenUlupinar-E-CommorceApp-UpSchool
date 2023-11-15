package com.example.s_shopping.ui.home

import android.view.LayoutInflater
import android.graphics.Paint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import androidx.core.view.isVisible
import com.example.s_shopping.R
import com.example.s_shopping.data.model.response.ProductUI
import com.example.s_shopping.databinding.ItemProductBinding

class ProductAdapter(
    private val productListener: ProductListener
) : ListAdapter<ProductUI, ProductAdapter.ProductViewHolder>(ProductDiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            productListener
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) =
        holder.bind(getItem(position))

    class ProductViewHolder(
        private val binding: ItemProductBinding,
        private val productListener: ProductListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductUI) {
            with(binding) {
                tvTitle.text = product.title

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

                btnFavv.setBackgroundResource(
                    if (product.isFav) R.drawable.favorite_icon
                    else R.drawable.favorite_icon_unselected
                )

                btnFavv.setOnClickListener {
                    productListener.onFavoriteClick(product)
                }

                root.setOnClickListener {
                    productListener.onProductClick(product.id)
                }
            }
        }
    }

    class ProductDiffUtilCallBack : DiffUtil.ItemCallback<ProductUI>() {
        override fun areItemsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem.id == newItem.id
            //Sadece id lere bakarak ilk kontrol yapılıyor, true dönerse devam ediyor
        }

        override fun areContentsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem == newItem
            //İd ler eşitse tüm içeriklere bakıyor burada da true dönerse o itemı tekrardan çizmiyor
        }
    }

    interface ProductListener {
        fun onProductClick(id: Int)
        fun onFavoriteClick(product: ProductUI)
    }
}