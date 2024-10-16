package com.example.juegoadivinarobligatorio1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //LO PRIMERISIMO LOS ATRIBUTOS
    EditText cajaNombre, cajaPass;
    Button login, registrar;
    CheckBox recordar;

    //INSTANCIAR SHAREDPREFERENCES
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    //CLAVES QUE NECESITARÁ EL POROGRAMA  GUARDAR EN EL SP, PARA LOGIN, NOMBRE, CONTRASEÑA Y RECORDAR
    //MÁS TARDE A PARTIR DE SELECCIÓN NIVEL AÑADIREMOS keyPuntuacion.

    //DIEGO IMOPRTANTE: ES BUENA PRÁCTICA USAR ESTA NOMENCLATURA EN SP PARA CUANDO TRABAJE, EN INGLÉS "user_prefs"
    //"username" "password", ETC.
    private static final String keyNombre="user_prefs";
    private static final String keyId="username";
    private static final String keyPass="password";
    private static final String keyRecordar = "remember";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //IMAGENES
        ImageView imagen = findViewById(R.id.imagen);
        imagen.setImageResource(R.drawable.imagen);

        //ID's Y BOTONES ESCUCHADORES
        cajaNombre=findViewById(R.id.cajaId);
        cajaPass=findViewById(R.id.cajaPass);
        login=findViewById(R.id.login);
        registrar=findViewById(R.id.registrar);
        recordar=findViewById(R.id.checkbox);

        //ESCUCHADORES
        login.setOnClickListener(this);
        registrar.setOnClickListener(this);

        //SP Q LO INICIALIZO YO MANUALMENTE
        sp=getSharedPreferences(keyNombre, Context.MODE_PRIVATE);
        editor=sp.edit();
        checkRememberedLogin();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onClick(View v)
    {


        switch (v.getId())
        {
            case R.id.login:realizarLogin();
                break;
            case R.id.registrar:
                // AQUÍ LANZO EL INTENT Q ME LLEVA A LA SEGUNDA ACTIVIDAD DE REGISTRO Y AHÍ MANEJO LAS COSAS
                Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
                startActivity(intent);
                break;


        }
    }

    //MÉTODO QUE VERIFICA EL CHECKBOX SI RECUERDA O NO
    private void checkRememberedLogin()
    {
        boolean recordarUsuario = sp.getBoolean(keyRecordar, false);
        if (recordarUsuario)
        {
            cajaNombre.setText(sp.getString(keyId, ""));
            cajaPass.setText(sp.getString(keyPass, ""));
            recordar.setChecked(true); //MARCAMOS CHECKBOX SI PULSÓ RECORDAR
        }
        else
        {
            recordar.setChecked(false); //DESMARCAMOS SI NO
        }
    }
    private void realizarLogin()
    {
        String nombre = cajaNombre.getText().toString();
        String password = cajaPass.getText().toString();

        // CAMPOS OBLIGATORIOS RELLENAR
        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Debe rellenar todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // VERIFICAMOS SI YA HAY UN USUARIO REGISTRADO
        String nombreGuardado = sp.getString(keyId, null);
        String passGuardada = sp.getString(keyPass, null);

        // VERIFICAMOS CAMPOS RECOGIDOS
        if (nombreGuardado == null || passGuardada == null)
        {
            // NULL ES Q NO HAY USUARIO REGISTRADO
            Toast.makeText(this, "No hay ningún usuario registrado. Regístrate primero.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            // COMPARACIÓN DE LAS CREDENCIALES REGISTRADAS AHORA CON LAS ALMACENADAS
            if (nombreGuardado.equals(nombre) && passGuardada.equals(password))
            {
                //MUESTRO UN MENSAJITO PERSONALIZADO AL USER
                Toast.makeText(this, "Bienvenid@ " + nombre + ". Te has conectado", Toast.LENGTH_SHORT).show();

                // GUARDAMOS EL ESTADO RECORDAR EN SHAREDPREFERENCES
                if (recordar.isChecked())
                {
                    editor.putBoolean(keyRecordar, true);
                    editor.putString(keyId, nombre);
                    editor.putString(keyPass, password);
                    editor.apply(); // EL MÉTODO .APPLY(); APLICA CAMBIOS AL SP
                }
                else
                {
                    // SI NO SE SELECCIONÓ RECORDAR, LIMPIAMOS CREDENCIALES
                    editor.putBoolean(keyRecordar, false);
                    editor.remove(keyId);
                    editor.remove(keyPass);
                    editor.apply();
                }

                // LIMPIAMOS LOS CAMPOS TRAS LOGARSE
                cajaNombre.setText("");
                cajaPass.setText("");

                // INTENT PA IR A SELECCIÓN DE NIVEL
                Intent intent = new Intent(MainActivity.this, SeleccionNivelActivity.class);
                intent.putExtra("nombreUsuario", nombre);
                //IMPORTANTE RECORDAR QUE: nombreUsuario PUEDO LLAMARLO COMO ME DE LA GANA, PUES AL RECOGER
                //ESTA CADENA DE TEXTO EN LA SIGUIENTE ACTIVIDAD, EL PROGRAMA ENTENDERÁ QUE AL USAR "nombreUsuario"
                //EN REALIDAD ME ESTOY REFIRIENDO AL nombre GUARDADO EN ESA VARIABLE.

                //DE LA SIGUIENTE FORMA:
                //nombre = getIntent().getStringExtra("nombreUsuario");
               //TENGO QUE PREGUNTAR UNA DUDA A ISA DE SI ES UN BOOLEANO PARA QUÉ LE ENVÍO TAMBIÉN TRUE O FALSE ¿?¿?¿?¿?
                //?¿¿?¿?¿?¿?¿?

                startActivity(intent); // -> NOS VAMOS AL SIGUIENTE

            }
            else
            {
                //SIMPLEMENTE CONTROLAMOS CREDENCIALES ERRONEAS
                Toast.makeText(this, "¡Error de credenciales!", Toast.LENGTH_SHORT).show();
            }
        }

    }
}