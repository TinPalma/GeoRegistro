package cl.ipvg.georegistro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListadoActivity extends AppCompatActivity {

    ListView listViewIncidencias;
    FirebaseFirestore db;
    List<String> listaFormateada;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        Button Button6 = findViewById(R.id.button6);

        Button6.setOnClickListener(v -> startActivity(new Intent(ListadoActivity.this, MainActivity.class)));

        listViewIncidencias = findViewById(R.id.listViewIncidencias);
        db = FirebaseFirestore.getInstance();

        listaFormateada = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaFormateada);
        listViewIncidencias.setAdapter(adapter);

        cargarIncidencias();
    }

    void cargarIncidencias() {
        db.collection("incidencias")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    listaFormateada.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String titulo = doc.getString("titulo");
                        String categoria = doc.getString("categoria");
                        String fechaHora = doc.getString("fechaHora");
                        String coordenadas = doc.getString("coordenadas");

                        String elemento = (titulo != null ? titulo : "Sin título") +
                                "\nCategoría: " + (categoria != null ? categoria : "N/A") +
                                "\nFecha: " + (fechaHora != null ? fechaHora : "N/A") +
                                "\nCoordenadas: " + (coordenadas != null ? coordenadas : "N/A");

                        listaFormateada.add(elemento);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error de red: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

}