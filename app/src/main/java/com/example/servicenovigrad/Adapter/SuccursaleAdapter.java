package com.example.servicenovigrad.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servicenovigrad.R;

import java.util.ArrayList;
import java.util.List;

public class SuccursaleAdapter  extends RecyclerView.Adapter<SuccursaleAdapter.ViewHolder> {

    List< String [] > succ;
    RecyclerViewClickListener listener;


    public SuccursaleAdapter (List<String [] > succ,
                              RecyclerViewClickListener listener) {

        this.succ = succ;
        this.listener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.succursale_list_view,
                parent,false);
        return  new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.number.setText("Numero de succursale: " + succ.get(position)[0]);
        holder.address.setText("Adresse : " + succ.get(position)[1]);
        holder.servicesAvailable.setText("Services offerts: " + succ.get(position)[2]);
    }

    @Override
    public int getItemCount() {
        return succ.size();
    }

    public void filterList(ArrayList<String[]> filterdList) {
        succ = filterdList;
        notifyDataSetChanged();

    }
    public interface RecyclerViewClickListener {
        void onClick(View v , int position);
    }

    public class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

        View mView;

        public TextView number, address, servicesAvailable;



        public  ViewHolder (View itemView) {
            super(itemView);
            mView = itemView;

            number = (TextView) mView.findViewById(R.id.suc_Number);
            address = (TextView) mView.findViewById(R.id.succ_Adress);
            servicesAvailable = mView.findViewById(R.id.succ_services);
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            listener.onClick(itemView,getAdapterPosition());
        }
    }
}
