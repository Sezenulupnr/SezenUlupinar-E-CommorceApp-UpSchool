package com.example.s_shopping.ui.card

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.s_shopping.common.loadImage
import com.example.s_shopping.data.model.response.ProductUI
import com.example.s_shopping.databinding.ItemCartProductBinding

class CartAdapter(
    private val cartProductListener: CartProductListener
) : ListAdapter<ProductUI, CartAdapter.CartProductViewHolder>(ProductDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductViewHolder =
        CartProductViewHolder(
            ItemCartProductBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            cartProductListener
        )

    override fun onBindViewHolder(holder: CartProductViewHolder, position: Int) =
        holder.bind(getItem(position))

    inner class CartProductViewHolder(
        private val binding: ItemCartProductBinding,
        private val cartProductListener: CartProductListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductUI) = with(binding) {
            tvTitle.text = product.title

            if (product.saleState == true) {
                tvPrice.isVisible = true
                tvPrice.text = "${product.salePrice} ₺"
            } else {
                tvPrice.text = "${product.price} ₺"
                tvPrice.isVisible = true
            }

            ivProduct.loadImage(product.imageOne)

            root.setOnClickListener {
                cartProductListener.onProductClick(product.id)
            }

            ivDelete.setOnClickListener {
                cartProductListener.onDeleteClick(product.id, product.price)
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

    interface CartProductListener {
        fun onProductClick(id: Int)
        fun onDeleteClick(productId: Int, price: Double)
    }
}