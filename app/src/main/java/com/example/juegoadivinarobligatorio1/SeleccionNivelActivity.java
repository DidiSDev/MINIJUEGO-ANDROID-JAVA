package com.example.juegoadivinarobligatorio1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SeleccionNivelActivity extends AppCompatActivity {
    //ATRIBUTOS
    TextView nombreUsuario, puntuacionUsuario;
    Button nivelFacil, nivelMuyFacil, nivelDificil, salir;
    //ATR SHP
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    //KEYS SHP
    private static final String keyNombre = "user_prefs";
    private static final String keyId = "username";
    private static final String keyPuntuacion = "score";

    //AQUÍ GUARDARÉ LO Q RECOJO DEL INTENT DEL MAINACTIVITY.JAVA CON .GETINTENT()
    private String nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_nivel);

        //ID's
        nombreUsuario = findViewById(R.id.nombreUsuario);
        puntuacionUsuario = findViewById(R.id.puntuacionUsuario);
        nivelFacil = findViewById(R.id.nivelFacil);
        nivelMuyFacil = findViewById(R.id.nivelMuyFacil);
        nivelDificil = findViewById(R.id.nivelDificil);
        salir=findViewById(R.id.salir);

        // MODO PRIVADO SIEMPRE POR BUENA PRÁCTICA DE SEGURIDAD
        sp = getSharedPreferences(keyNombre, Context.MODE_PRIVATE);

        //RECOJO "nombreUsuario" QUE ES COMO LO HE LLAMADO AL ENVIARLO AL INTENT EN LA ACTIVIDAD
        //ANTERIOR
        nombre = getIntent().getStringExtra("nombreUsuario");

        //MUESTRO UN MENMSAJE PERSONALIZADO DEPENDIENDO DEL NOMBRE QUE TENGA EL USUARIO
        nombreUsuario.setText("Bienvenid@, " + nombre);

        //MÉTODO QUE ACTUALIZA LA PUNTUACIÓN
        actualizarPuntuacion();

        //EN ESTA ACTIVIDAD VOY A HACER LISTENERS INDIVIDUALES
        //DIEGO RECUERDA SIEMPRE... nombre.setOnClickListener(LE PASAMOS new View.OnClickListener()
        //Y ABRIMOS {public void onClick(View v){LO QUE QUEREMOS QUE OCURRA}}
        nivelMuyFacil.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                seleccionarNivel("Muy Facil");
            }
        });

        nivelFacil.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                seleccionarNivel("Facil");
            }
        });

        nivelDificil.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                seleccionarNivel("Dificil");
            }
        });

        salir.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                salirDelPrograma();
            }
        });
    }
    private void salirDelPrograma()
    {
        finishAffinity(); //-> FINALIZA LAS ACTIVIDADES ANTES DE CERRARSE EL PROGRAMA
        System.exit(0);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        actualizarPuntuacion();
    }

    private void actualizarPuntuacion()
    {
        //PREGUNTO AL SP SI HAY ALGUN NOMBRE GUARDADO, SI NO LO HAY ENTONCES SERÁ NULL
        //A ESTAS ALTURAS DEL PROGRAMA OBLIGATORIAMENTE TENEMOS QUE LLEGAR AQUÍ CON NOMBRE
        //ASÍ QUE EN keyId HABRÁ GUARDADO UN NOMBRE SÍ O SÍ.
        String nombreGuardado = sp.getString(keyId, null);

        //ESTABLECEMOS LA PUNTUACIÓN DE ESTA NUEVA PARTIDA A 0 Y SE LA MOSTRAMOS AL USUARIO
        int puntuacion = sp.getInt(keyPuntuacion, 0);

        if (nombreGuardado != null && nombreGuardado.equals(nombre))
        {
            puntuacionUsuario.setText("Puntuación actual: " + puntuacion);
        }
        else
        {
            //EN ESTE ELSE NUNCA VA A ENTRAR PERO SI HAY ALGÚN FALLO DEL PROGRAMA PODRÍA HACERLO
            //ASÍ QUE MOSTRAMOS EL MENSAJE EN ESTE CASO TAMBIÉN
            puntuacionUsuario.setText("Puntuación actual: Sin puntuación");
        }
    }

    private void seleccionarNivel(String nivel)
    {
        //LA PUNTUACIÓN A 0 ANTES DE EMPEZAR CADA NUEVA PARTIDA, DADO QUE EN PARTIDAACTIVITY VOY
        //A GUARDAR LA ÚLTIMA PUNTUACIÓN QUE EL USUARIO TUVO. NO LA MEJOR, OJO, SINO LA ÚLTIMA
        editor = sp.edit(); //PERMITIMOS EDITAR AL SP COMO SIEMPRE
        editor.putInt(keyPuntuacion, 0); //ENVIAMOS 0 AL SP Y APLICAMOS
        editor.apply(); //ALPICAMOS

        //UNA VEZ ELEGIDO EL NIVEL, NOS VAMOS AL SIGUIENTE ACTIVITY EN FUNCIÓN DEL BOTÓN QUE HAYA ELEGIDO
        //EN ESTE CASO, TRAEMOS EN EL CONSTRUCTOR EL PARÁMETRO "nivel" QUE CONTIENE EL BOTÓN PULSADO POR EL USUARIO
        //AL ELEGIR EL NIVEL


        //IMPORTANTE TAMBIEN LE TENGO Q MANDAR EL NOMBRE, QUE LUAGO GUARDARÉ SU PUNTUACIÓN NO?
        //LO ENVÍO POR SI ACASO PROVISIONALMENTE
        Intent intent = new Intent(SeleccionNivelActivity.this, PartidaActivity.class);
        intent.putExtra("nombreUsuario", nombre);
        intent.putExtra("nivel", nivel);
        startActivity(intent); //INICIAMOS PARTIDA, POR FIN
    }
}
