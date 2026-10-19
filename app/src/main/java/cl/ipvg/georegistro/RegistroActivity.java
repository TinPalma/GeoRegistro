package cl.ipvg.georegistro;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RegistroActivity extends AppCompatActivity {

    private EditText Titulo, Descripcion, FechaHora, Coordenadas;
    private Spinner Categoria;
    private Button GPS, Guardar;

    private FusedLocationProviderClient clienterUbicacion;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);


        Titulo = findViewById(R.id.editTextText);
        Descripcion = findViewById(R.id.editTextText2);
        FechaHora = findViewById(R.id.editTextText3);
        Coordenadas = findViewById(R.id.editTextText4);
        Categoria = findViewById(R.id.spinner);
        GPS = findViewById(R.id.button4);
        Guardar = findViewById(R.id.button5);


        clienterUbicacion = LocationServices.getFusedLocationProviderClient(this);
        db = FirebaseFirestore.getInstance();


        String[] listaCategorias = {"Seleccione Categoria", "Vía Pública", "Seguridad", "Limpieza", "Otros"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listaCategorias);
        Categoria.setAdapter(adapter);


        FechaHora.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date()));


        GPS.setOnClickListener(v -> obtenerUbicacion());
        Guardar.setOnClickListener(v -> guardarEnFirebase());
    }

    private void obtenerUbicacion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            return;
        }

        clienterUbicacion.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                Coordenadas.setText(location.getLatitude() + ", " + location.getLongitude());
                Toast.makeText(this, "GPS obtenido", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Active el GPS o ingrese coordenadas manualmente", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void guardarEnFirebase() {
        String titulo = Titulo.getText().toString().trim();
        String descripcion = Descripcion.getText().toString().trim();
        String coordenadas = Coordenadas.getText().toString().trim();
        String categoria = Categoria.getSelectedItem().toString();


        if (titulo.isEmpty() || descripcion.isEmpty() || coordenadas.isEmpty() || Categoria.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }


        Map<String, Object> incidencia = new HashMap<>();
        incidencia.put("titulo", titulo);
        incidencia.put("descripcion", descripcion);
        incidencia.put("categoria", categoria);
        incidencia.put("fechaHora", FechaHora.getText().toString().trim());
        incidencia.put("coordenadas", coordenadas);


        db.collection("incidencias")
                .add(incidencia)
                .addOnSuccessListener(ref -> {
                    Toast.makeText(this, "¡Guardado con éxito!", Toast.LENGTH_SHORT).show();
                    finish(); // Cierra la actividad al guardar
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}