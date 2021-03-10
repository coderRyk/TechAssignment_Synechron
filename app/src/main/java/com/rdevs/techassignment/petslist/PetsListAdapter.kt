package com.rdevs.techassignment

import Pet
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rdevs.techassignment.petdetails.PetDetailsActivity
import com.rdevs.techassignment.utils.imageloader.ImageLoader


class PetsAdapter(val context: Context) : RecyclerView.Adapter<PetsAdapter.PetViewHolder>() {

    var data = arrayListOf<Pet>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holderPet: PetViewHolder, position: Int) {
        val item = data[position]
        holderPet.bind(context, item)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        return PetViewHolder.from(parent)
    }

    class PetViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvPetName: TextView = itemView.findViewById(R.id.tvPetName)
        private val ivPetImage: ImageView = itemView.findViewById(R.id.ivPetImage)

        fun bind(context: Context, petItem: Pet) {
            tvPetName.text = petItem.title

            petItem.image_url?.let { ImageLoader.with(context).load(ivPetImage, it) }

            itemView.setOnClickListener {
                val detailsIntent: Intent = Intent(it.context, PetDetailsActivity::class.java).apply {
                    putExtra(PetDetailsActivity.KEY_EXTRA_PET, petItem)
                }

                it.context.startActivity(detailsIntent)
            }

        }

        companion object {
            fun from(parent: ViewGroup): PetViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.list_item_pet, parent, false)

                return PetViewHolder(view)
            }
        }
    }
}