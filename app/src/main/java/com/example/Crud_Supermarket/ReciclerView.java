package com.example.cuentaspolizas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import java.util.ArrayList;
public class ReciclerView extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recicler_view);

        ArrayList<Catalogo> catalogoList;
        catalogoList = (ArrayList<Catalogo>) getIntent().getSerializableExtra("catalogoList");

        RecyclerView recyclerView = findViewById(R.id.ReciclerView1);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        CustomAdapter adapter = new CustomAdapter(catalogoList);

        recyclerView.setAdapter(adapter);

    }
}