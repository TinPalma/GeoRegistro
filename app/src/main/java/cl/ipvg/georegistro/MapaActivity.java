package cl.ipvg.georegistro;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MapaActivity extends AppCompatActivity {

    MapView map;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration.getInstance().setUserAgentValue(getPackageName());
        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        setContentView(R.layout.activity_mapa);

        map = findViewById(R.id.mapView);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);

        GeoPoint startPoint = new GeoPoint(-36.5898, -72.0826);
        map.getController().setZoom(14.0);
        map.getController().setCenter(startPoint);

        Button Button7 = findViewById(R.id.Button7);
        if (Button7 != null) {
            Button7.setOnClickListener(v -> {
                startActivity(new Intent(MapaActivity.this, MainActivity.class));
                finish();
            });
        }

        db = FirebaseFirestore.getInstance();

        cargarPuntosFirestore();
    }

    void cargarPuntosFirestore() {
        db.collection("incidencias")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String titulo = doc.getString("titulo");
                        String categoria = doc.getString("categoria");
                        String coordsStr = doc.getString("coordenadas");

                        if (coordsStr != null && coordsStr.contains(",")) {
                            try {
                                String[] partes = coordsStr.split(",");
                                double lat = Double.parseDouble(partes[0].trim());
                                double lng = Double.parseDouble(partes[1].trim());

                                GeoPoint punto = new GeoPoint(lat, lng);
                                Marker marker = new Marker(map);
                                marker.setPosition(punto);
                                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                                marker.setTitle(titulo != null ? titulo : "Incidencia");
                                marker.setSnippet("Categoría: " + (categoria != null ? categoria : "N/A"));

                                map.getOverlays().add(marker);
                            } catch (NumberFormatException ignored) {
                            }
                        }
                    }
                    map.invalidate();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error al cargar datos: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }
}