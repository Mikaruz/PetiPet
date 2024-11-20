package com.utp.petipet.adapter;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.utp.petipet.R;
import com.utp.petipet.model.Pet;

import java.util.List;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.PetViewHolder> {
    private List<Pet> petList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Pet pet);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public PetAdapter(List<Pet> mascotaList) {
        this.petList = mascotaList;
    }

    @NonNull
    @Override
    public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pets, parent, false);
        return new PetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetViewHolder holder, int position) {
        Pet pet = petList.get(position);
        holder.textNombre.setText(pet.getName());

        // Si tienes una URL de imagen, usa Glide o Picasso para cargarla
        Glide.with(holder.itemView.getContext())
                .load(pet.getImageUrl()) // Reemplaza con el campo adecuado
                .placeholder(R.drawable.pet_placeholder) // Imagen de carga
                .error(R.drawable.error_placeholder)
                .into(holder.petImage);

        // Configurar el clic en el ítem
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(pet);
            }
        });
    }

    @Override
    public int getItemCount() {
        return petList.size();
    }

    static class PetViewHolder extends RecyclerView.ViewHolder {
        TextView textNombre, textRaza;
        ImageView petImage;

        PetViewHolder(@NonNull View itemView) {
            super(itemView);
            textNombre = itemView.findViewById(R.id.textNombre);
            petImage = itemView.findViewById(R.id.pet_image);
        }
    }
}
