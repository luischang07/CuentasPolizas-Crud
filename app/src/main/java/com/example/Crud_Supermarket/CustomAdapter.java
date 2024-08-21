package com.example.cuentaspolizas;

import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private ArrayList<Catalogo> localDataSet;
    private static TextView cuentaBD, nombreBD, cargoBD, abonoBD;
    private static ConstraintLayout layout;
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            cuentaBD = view.findViewById(R.id.cuentaBD);
            nombreBD = view.findViewById(R.id.nombreBD);
            cargoBD = view.findViewById(R.id.cargoBD);
            abonoBD = view.findViewById(R.id.abonoBD);
            layout = view.findViewById(R.id.myLayout);

        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public CustomAdapter(ArrayList<Catalogo> dataSet) {
        localDataSet = dataSet;
        System.out.println("Total de datos "+localDataSet.size());
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.activity_custom_adapter, null, false);


        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

            cuentaBD.setText(localDataSet.get(position).getCuenta());
            nombreBD.setText(localDataSet.get(position).getNombre());
            cargoBD.setText(localDataSet.get(position).getCargo()+"");
            abonoBD.setText(localDataSet.get(position).getAbono()+"");
        try {
            if (localDataSet.get(position).getNivel() == 1) {
                layout.setBackgroundColor(Color.parseColor("#80CAF1"));
            } else if (localDataSet.get(position).getNivel() == 2) {
                layout.setBackgroundColor(Color.parseColor("#BCD776"));
            } else if (localDataSet.get(position).getNivel() == 3) {
                layout.setBackgroundColor(Color.parseColor("#F8CFD7"));
            }

        }catch (Exception e){
            Rutinas.mensajeDialog("Error en el adapter: "+e.getMessage(),layout.getContext());
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}