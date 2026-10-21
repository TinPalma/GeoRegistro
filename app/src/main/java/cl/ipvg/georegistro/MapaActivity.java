package cl.ipvg.georegistro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class MapaActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mMap;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        db = FirebaseFirestore.getInstance();


        Button buttonVolver = findViewById(R.id.Button7);
        if (buttonVolver == null) {
            buttonVolver = findViewById(R.id.Button7);
        }

        if (buttonVolver != null) {
            buttonVolver.setOnClickListener(v -> {
                startActivity(new Intent(MapaActivity.this, MainActivity.class));
                finish();
            });
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapGoogle);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        mMap.getUiSettings().setZoomControlsEnabled(true);

        LatLng centroInicial = new LatLng(-36.5898, -72.0826);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centroInicial, 13.0f));

        cargarMarcadoresFirestore();
    }

    void cargarMarcadoresFirestore() {
        db.collection("incidencias")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    mMap.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String titulo = doc.getString("titulo");
                        String categoria = doc.getString("categoria");
                        String coordsStr = doc.getString("coordenadas");

                        if (coordsStr != null && coordsStr.contains(",")) {
                            try {
                                String[] partes = coordsStr.split(",");
                                double lat = Double.parseDouble(partes[0].trim());
                                double lng = Double.parseDouble(partes[1].trim());

                                LatLng punto = new LatLng(lat, lng);
                                String t = (titulo != null) ? titulo : "Incidencia";
                                String snippet = "Categoría: " + ((categoria != null) ? categoria : "N/A");

                                mMap.addMarker(new MarkerOptions()
                                        .position(punto)
                                        .title(t)
                                        .snippet(snippet));

                            } catch (NumberFormatException ignored) {
                            }
                        }
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error al cargar marcadores: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}