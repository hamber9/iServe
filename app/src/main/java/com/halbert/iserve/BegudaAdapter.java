package com.halbert.iserve;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BegudaAdapter extends RecyclerView.Adapter<BegudaAdapter.ViewHolder>  {


    Context context;
    List<Beguda> begudaList;
    private View.OnClickListener listener;
    private OnItemClickListener mListener;

    public BegudaAdapter(Context context, List<Beguda> begudaList) {
        this.context = context;
        this.begudaList = begudaList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_row_for_recyclerview, parent, false);
        //v.setOnClickListener(this);
        //--->View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_productes, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BegudaAdapter.ViewHolder holder, int position) {
        Beguda beguda = begudaList.get(position);
        holder.nom.setText(beguda.getNom());
        holder.descripcio.setText(beguda.getDescripcio());
        holder.preu.setText(beguda.getPreu());

        String imageUri = null;

        imageUri = beguda.getImage();
        Picasso.get().load(imageUri).into(holder.imageView);
    }



    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;

    }

    @Override
    public int getItemCount() {
        return begudaList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        ImageView imageView;
        TextView nom ,descripcio, preu;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_recyclerView_id);
            nom = itemView.findViewById(R.id.nom_recyclerView_id);
            descripcio = itemView.findViewById(R.id.descripcio_recyclerView_id);
            preu = itemView.findViewById(R.id.preu_recyclerView_id);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onClick(View view) {
            if (mListener != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Selecciona una acció");
            MenuItem modify = contextMenu.add(Menu.NONE, 1, 1,"Modificar");
            MenuItem delete = contextMenu.add(Menu.NONE, 2,2,"Esborrar");

            modify.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);

        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            if (mListener != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    switch (menuItem.getItemId()){
                        case 1 :
                            mListener.onModifyClick(position);
                            return true;
                        case 2:
                            mListener.onDeleteClick(position);
                            return true;
                    }
                }
            }

            return false;
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onModifyClick(int position);
        void onDeleteClick(int position);

    }

    public void setOnItemClickListener(BegudaActivity listener) {
        mListener = listener;
    }
}
