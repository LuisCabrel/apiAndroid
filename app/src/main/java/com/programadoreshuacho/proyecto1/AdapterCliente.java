package com.programadoreshuacho.proyecto1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class AdapterCliente extends RecyclerView.Adapter<AdapterCliente.ClientesViewHolder> {
    private Context  nCtx;
    private ArrayList<Clientes> clientesList;
    public AdapterCliente(ArrayList<Clientes> clientesList){
        //this.nCtx= (Context) nCtx;
        this.clientesList = clientesList;
    }

    @NonNull
    @Override
    public ClientesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View contentView = inflater.inflate(R.layout.activity_listacliente,null);
        return new ClientesViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientesViewHolder holder, int position) {
        Clientes clientes = clientesList.get(position);
        //CARGAMOS IMAGEN
        Glide.with(holder.imgFoto)
                .load(clientes.getImagen())
                .into(holder.imgFoto);
        holder.txtNombresCliente.setText(clientes.getNombre());
        holder.txtApellidosCliente.setText(clientes.getApellido());
        holder.txtTelefonoCliente.setText(clientes.getTelefono());
        holder.txtCorreoCliente.setText(clientes.getCorreo());
    }

    @Override
    public int getItemCount() {
        return clientesList.size();
    }


    public class ClientesViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFoto;
        TextView txtNombresCliente;
        TextView txtApellidosCliente;
        TextView txtTelefonoCliente;
        TextView txtCorreoCliente;

        public ClientesViewHolder(View itemView){
            super(itemView);
            imgFoto =itemView.findViewById(R.id.imgFoto);
            txtNombresCliente =itemView.findViewById(R.id.txtNombresCliente);
            txtApellidosCliente =itemView.findViewById(R.id.txtApellidosCliente);
            txtTelefonoCliente =itemView.findViewById(R.id.txtTelefonoCliente);
            txtCorreoCliente =itemView.findViewById(R.id.txtCorreoCliente);
        }


    }
}
