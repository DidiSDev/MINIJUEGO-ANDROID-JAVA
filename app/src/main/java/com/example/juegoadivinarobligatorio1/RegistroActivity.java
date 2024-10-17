package com.example.juegoadivinarobligatorio1;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegistroActivity extends AppCompatActivity {

    EditText cajaNombre, cajaPass;
    Button finalizarRegistro, volver;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    // LAS CLAVES DEL SHARED, DE NUEVO
    private static final String keyNombre = "user_prefs";
    private static final String keyId = "username";
    private static final String keyPass = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);



        // COGEMOS IDS
        cajaNombre = findViewById(R.id.cajaIdRegistro);
        cajaPass = findViewById(R.id.cajaPassRegistro);
        finalizarRegistro = findViewById(R.id.finalizarRegistro);
        volver=findViewById(R.id.volver);

        // INICIALIZAMOS SP
        sp = getSharedPreferences(keyNombre, Context.MODE_PRIVATE);
        //Context.MODE_PRIVATE ES UNA COINSTANTE QUE INDICA QUE EL ARCHIVO DE PREFERENCIAS QUE ESTÁS
        // CREANDO O ACCEDIENDO SOLO PUEDE SER ACCEDIDO POR ESTA APLICACIÓN
        editor = sp.edit(); //PERMITE EDITAR EL SP

        // LISTENER ALOS BOTONES
        finalizarRegistro.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                realizarRegistro();
            }
        });

        volver.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                finish();
            }
        });
    }


    private void realizarRegistro()
    {
        String nombre = cajaNombre.getText().toString();
        String password = cajaPass.getText().toString();

        //CAMPOS RELLENADOS OBLIGATORIOS
        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Obligatorio rellenar todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // GUARDO DATOS EN SHAREDP
        editor.putString(keyId, nombre);
        editor.putString(keyPass, password);
        editor.apply();

        Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();

        // VOLVEMOS AL LOGIN
        finish();
    }


}
