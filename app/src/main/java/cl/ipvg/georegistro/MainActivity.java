package cl.ipvg.georegistro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Button Button1 = findViewById(R.id.button);
        Button Button2 = findViewById(R.id.button2);
        Button Button3 = findViewById(R.id.button3);

        Button1.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, RegistroActivity.class)));
        Button2.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ListadoActivity.class)));
        Button3.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MapaActivity.class)));


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}