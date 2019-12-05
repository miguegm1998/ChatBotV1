package com.example.chatbotv1;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class MensajeAdapter extends RecyclerView.Adapter<MensajeAdapter.MyViewHolder> {

    private static boolean ORG_USER = true;
    private Context context;
    public static List<Mensaje> mensajes;
    private LayoutInflater inflater;

    public MensajeAdapter(Context context){
        this.context = context;
        mensajes = new ArrayList<>();
        inflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public MensajeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_msg,parent,false);
        MensajeAdapter.MyViewHolder vh = new MensajeAdapter.MyViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MensajeAdapter.MyViewHolder holder, int position) {
        if(mensajes != null){
            final Mensaje current = mensajes.get(position);
            if(current.getOrigen() == ORG_USER){
                holder.tvMensaje.setText("User: "+ current.getMensaje());
            }else{
                holder.tvMensaje.setText("Bot:"+ current.getMensaje());
            }
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvMensaje;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMensaje = itemView.findViewById(R.id.tvMensaje);

        }
    }

    public void setMensajeList(List<Mensaje>mensajeList){
        this.mensajes=mensajeList;
        notifyDataSetChanged();
    }
}
