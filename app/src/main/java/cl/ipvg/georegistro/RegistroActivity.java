package cl.ipvg.georegistro;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.*;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;
import java.util.*;

public class RegistroActivity extends AppCompatActivity {

    EditText Titulo, Descripcion, FechaHora, Coordenadas;
    Spinner Categoria;
    FusedLocationProviderClient clienterUbicacion;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        Titulo = findViewById(R.id.editTextText);
        Descripcion = findViewById(R.id.editTextText2);
        FechaHora = findViewById(R.id.editTextText3);
        Coordenadas = findViewById(R.id.editTextText4);
        Categoria = findViewById(R.id.spinner);

        clienterUbicacion = LocationServices.getFusedLocationProviderClient(this);
        db = FirebaseFirestore.getInstance();

        String[] listaCategorias = {"Categorias", "Vía Pública", "Seguridad", "Natural", "Limpieza", "autos", "Otros"};
        Categoria.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listaCategorias));
        FechaHora.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date()));

        findViewById(R.id.button4).setOnClickListener(v -> obtenerUbicacion());
        findViewById(R.id.button5).setOnClickListener(v -> guardarEnFirebase());
    }

    void obtenerUbicacion() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            return;
        }

        clienterUbicacion.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).addOnSuccessListener(this, loc -> {
            if (loc != null) {
                Coordenadas.setText(loc.getLatitude() + ", " + loc.getLongitude());
                Toast.makeText(this, "GPS obtenido", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Active el GPS o ingrese coordenadas manualmente", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                obtenerUbicacion();
            } else {
                Toast.makeText(this, "Permiso denegado.", Toast.LENGTH_LONG).show();
            }
        }
    }

    void guardarEnFirebase() {
        String t = Titulo.getText().toString().trim(), d = Descripcion.getText().toString().trim(), c = Coordenadas.getText().toString().trim();
        if (t.isEmpty() || d.isEmpty() || c.isEmpty() || Categoria.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> incidencia = new HashMap<>();
        incidencia.put("titulo", t);
        incidencia.put("descripcion", d);
        incidencia.put("categoria", Categoria.getSelectedItem().toString());
        incidencia.put("fechaHora", FechaHora.getText().toString().trim());
        incidencia.put("coordenadas", c);

        db.collection("incidencias").add(incidencia)
                .addOnSuccessListener(ref -> { Toast.makeText(this, "¡Guardado con éxito!", Toast.LENGTH_SHORT).show(); finish(); })
                .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
