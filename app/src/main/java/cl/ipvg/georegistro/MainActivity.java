package cl.ipvg.georegistro;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    Button button, button2, button3;

    SensorManager sensorManager;
    Sensor sensorProximidad;

    long ultimoAcceso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);

        if (button != null) {
            button.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, RegistroActivity.class)));
        }
        if (button2 != null) {
            button2.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ListadoActivity.class)));
        }
        if (button3 != null) {
            button3.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MapaActivity.class)));
        }

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            sensorProximidad = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        }

        if (sensorProximidad == null) {
            Toast.makeText(this, "Sensor de proximidad no disponible.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorProximidad != null && sensorManager != null) {
            sensorManager.registerListener(this, sensorProximidad, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
        super.onPause();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            float distancia = event.values[0];

            if (distancia < sensorProximidad.getMaximumRange()) {
                long tiempoActual = System.currentTimeMillis();
                if (ultimoAcceso + 2000 > tiempoActual) {
                    return;
                }
                ultimoAcceso = tiempoActual;

                Toast.makeText(this, "Gesto detectado: Abriendo Mapa...", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, MapaActivity.class));
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}