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

public class ReceptaAdapter extends RecyclerView.Adapter<ReceptaAdapter.ViewHolder> {

    Context context;
    List<Recepta> receptaList;
    private View.OnClickListener listener;
    private ReceptaAdapter.OnItemClickListener mListener;

    public ReceptaAdapter(Context context, List<Recepta> receptaList) {
        this.context = context;
        this.receptaList = receptaList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_row_for_recyclerview, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ReceptaAdapter.ViewHolder holder, int position) {

        Recepta recepta = receptaList.get(position);
        holder.nom.setText(recepta.getNom());
        //holder.preparacio.setText(recepta.getPreparacio());
        holder.preu.setText(recepta.getPreu());

        String imageUri = null;

        imageUri = recepta.getImage();
        Picasso.get().load(imageUri).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return receptaList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        ImageView imageView;
        TextView nom, preu, preparacio;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_recyclerView_id);
            nom = itemView.findViewById(R.id.nom_recyclerView_id);
            preparacio = itemView.findViewById(R.id.descripcio_recyclerView_id);
            preu = itemView.findViewById(R.id.preu_recyclerView_id);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            if (mListener != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    switch (menuItem.getItemId()){
                        case 1 :
                            mListener.onOpenClick(position);
                            return  true;
                        case 2 :

                            mListener.onModifyClick(position);
                            return true;
                        case 3:
                            mListener.onDeleteClick(position);
                            return true;
                    }
                }
            }

            return false;
        }


        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

            contextMenu.setHeaderTitle("Selecciona una acció");
            MenuItem open = contextMenu.add(Menu.NONE,1,1,"Obrir" );
            MenuItem modify = contextMenu.add(Menu.NONE, 2, 2,"Modificar");
            MenuItem delete = contextMenu.add(Menu.NONE, 3,3,"Esborrar");

            open.setOnMenuItemClickListener(this);
            modify.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }
    }
    public interface OnItemClickListener{
        void onItemClick(int position);
        void onOpenClick(int position);
        void onModifyClick(int position);
        void onDeleteClick(int position);

    }

    public void setOnItemClickListener(ReceptaAdapter.OnItemClickListener listener) {
        mListener = listener;
    }
}
