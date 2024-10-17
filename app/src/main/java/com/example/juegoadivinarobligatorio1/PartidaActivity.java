package com.example.juegoadivinarobligatorio1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;

import java.text.Normalizer;
import java.util.Random;

public class PartidaActivity extends AppCompatActivity {


    //TODOS LOS ATRIBUTOS Q NECESITAMOS, EL JUGADOR TRAIDO, EL NIVEL TRAIDO Y LA ULTIMA PUNTUACION
    //GUARDADA EN EL SHAREDPREF, CREO Q NO NECESITARÉ MÁS DE MOMENTO
    TextView nombreJugador, nivelSeleccionado, ultimaPuntuacion;
    EditText respuestaJugador; //EL EDIT DONDE RESPONDERÁ EL JUGADOR (MAS ADELANTE LO PASAREMOS TOLOWERCASE X SI ACASO)
    Button jugarPartida, volver, salir;
    ImageView imagenJuego; //DONDE IRÁ LA IMAGEN RECOGIDA ALEATORIAMENTE DE LOS ARRAY



    SharedPreferences sp;
    SharedPreferences.Editor editor;

    // LAS CREDENCIALES DE SP
    private static final String keyNombre = "user_prefs";
    private static final String keyId = "username"; //NO SÉ SI ME HACE FALTA EL keyId DE MOMENTO LO DEJO TRAIDO
    private static final String keyPuntuacion = "score";

    private String nivel;
    private String nombre;

    // AQUÍ TENDRÉ LOS ARRAYS DE IMÁGENES (COMO ME GUSTA LA FÍSICA SERÁN RELACIONADAS LAS IMAGENES CON ESTO)
    //EN CUANTO A LOS SONIDOS, SERÁN DE ANIMALES POR LA DIFICULTAD QUE CONLLEVARÍA ENTENDER LOS SONIDOS EN FÍSICA

    //MI JUEGO ESTÁ HECHO PARA EXPERTOS EN FÍSICA EN IMÁGENES.
    //SITUADAS EN LA CARPETA DRAWABLE


    //PRIMERO IRÁN LAS PREGUNTAS PARA CADA DIFICULTAD Y LUEGO LAS RESPUESTAS DEBAJO
    private int[] imagenesMuyFacil = {
            R.drawable.masa,
            R.drawable.velocidad,
            R.drawable.aceleracion,
            R.drawable.fuerza,
            R.drawable.energia
    };
    private String[] respuestasImagenesMuyFacil = {"masa", "velocidad", "aceleracion", "fuerza", "energia"};

    private int[] imagenesFacil = {
            R.drawable.gravedad,
            R.drawable.ley_newton,
            R.drawable.circuito,
            R.drawable.ondas,
            R.drawable.electricidad
    };
    private String[] respuestasImagenesFacil = {"gravedad", "ley de newton", "circuito", "ondas", "electricidad"};

    private int[] imagenesDificil = {
            R.drawable.relatividad,
            R.drawable.entropia,
            R.drawable.fisica_cuantica,
            R.drawable.boson_higgs,
            R.drawable.superposicion
    };
    private String[] respuestasImagenesDificil = {"relatividad", "entropia", "fisica cuantica", "boson de higgs", "superposicion"};

    // LOS ARRAY DE SONIDOS VAN A SER DE ANIMALES, LO CUÁL FACILITA AL JUGADOR PODER ADQUIRIR MUCHA PUNTUACIÓN AQUÍ
    //ESTARÁN SITUADAS EN LA CARPETA RAW QUE TENGO QUE CREAR MANUALMENTE
    private int[] sonidosMuyFacil = {
            R.raw.gato,
            R.raw.perro,
            R.raw.vaca,
            R.raw.oveja,
            R.raw.pajaro
    };
    private String[] respuestasSonidosMuyFacil = {"gato", "perro", "vaca", "oveja", "pajaro"};

    private int[] sonidosFacil = {
            R.raw.leon,
            R.raw.elefante,
            R.raw.tigre,
            R.raw.lobo,
            R.raw.mono
    };
    private String[] respuestasSonidosFacil = {"leon", "elefante", "tigre", "lobo", "mono"};

    private int[] sonidosDificil = {
            R.raw.ballena,
            R.raw.delfin,
            R.raw.murcielago,
            R.raw.camello,
            R.raw.rinoceronte
    };
    private String[] respuestasSonidosDificil = {"ballena", "delfin", "murcielago", "camello", "rinoceronte"};

    private int imagenActual;
    private int sonidoActual;
    private String respuestaCorrecta;
    private int puntuacionActual = 0;
    private int puntuacionAnterior=0;

    private MediaPlayer mediaPlayer;

    //INTERRUPTOR PARA INDICAR SI ESTAMOS USANDO IMÁGENES O SONIDOS
    private boolean usandoImagenes = true;

    // QUIERO QUE EN SU VERSIÓN 1.0 EL JUEGO SOLAMENTE PERMITA 5 PREGUNTAS, CON ACIERTOS Y FALLOS PERO MÁXIMO 5.
    //POSTERIORMENTE IMPLEMENTARÉ MÁS DATOS A LOS ARRAY Y POR ENDE MÁS PREGUNTAS POR PARTIDA
    private int numPreguntas = 0;
    private final int totalPreguntas = 5;
    private int numCorrectas = 0; //ALMACENO PORQUE AL FINAL QUIERO MOSTRAR AL USUARIO UNA ESPECIE DE JOptionPane QUE MOSTRARÁ LAS QUE HA ACERTADO SOBRE EL TOTAL
    //PARA MAYOR INTERACTIVIDAD DE LA APLICACIÓN CON EL USUARIO

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partida); //DEBAJO LAS ID'S DIEGO QUE SINO NO FUNCIOINA


        nombreJugador = findViewById(R.id.nombreJugador);
        nivelSeleccionado = findViewById(R.id.nivelSeleccionado);
        ultimaPuntuacion = findViewById(R.id.ultimaPuntuacion);
        respuestaJugador = findViewById(R.id.respuestaJugador);
        jugarPartida = findViewById(R.id.jugarPartida);
        volver = findViewById(R.id.volver);
        salir = findViewById(R.id.salir);
        imagenJuego = findViewById(R.id.imagenJuego);

        //COMO SIEMPRE EN PRIVATE
        sp = getSharedPreferences(keyNombre, Context.MODE_PRIVATE);

        // NOS TRAÍAMOS EL NOMBRE Y EL NIVEL DE LA ACTIVIDAD ANTERIOR, RECOGEMOS AQUÍ
        nombre = getIntent().getStringExtra("nombreUsuario");
        nivel = getIntent().getStringExtra("nivel");

        //MOSTRAMOS AL J8GUADOR SU NOMBRE Y EL NIVEL DE DIFICULTAD ELEGIDO, POR SI DESEA CAMBIARLO EN EL BOTÓN "Volver" O "Salir"
        nombreJugador.setText("Jugador: " + nombre);
        nivelSeleccionado.setText("Nivel: " + nivel);

        //RECUPERAMOS LA ÚLTIMA PUNTUACIÓN GUARDADA SI ES QUE LA HAY, DE NO HABERLA, TENDREMOS 0 COMO VALO RPOR DEFECTO
        puntuacionActual = sp.getInt(getKeyPuntuacion(), 0);

        //MOSTRAMOS EN EL TEXTVIEW
        ultimaPuntuacion.setText("Última Puntuación: " + puntuacionActual);

        //AHORA ESTABLEZCO LA PUNTUACIÓN ACTUAL A 0 Y USO OTRA VARIABLE QUE COMPARE AL FINAL
        puntuacionAnterior=puntuacionActual;
        puntuacionActual=0;

        //LISTENER PARA EL BOTÓN "Jugar Partida"
        jugarPartida.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                jugarPartida();
            }
        });

        //TENEMOS QUE OFRECER LA POSIBILIDAD DE VOLVER A LA ELECCIÓN DEL NIVEL
        volver.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if (mediaPlayer != null)
                {
                    mediaPlayer.release();
                    //POR QUÉ HAGO ESTO? PORQUE A VECES CUANDO VOLVÍA A LA VENTANA ANTERIOR Y SE ESTABA REPRODUCIENDO UN SONIDO, EL SONIDO SEGUÍA REPRODUCIÉNDOSE.
                    //ESTO PREGUNTA SI HAY UN SONIDO REPRODUCIÉNDOSE EN LOS PROCESOS, EN CASO DE HABERLO, .release() ES UN MÉTODO QUE LO FINALIZA. ASÍ PODEMOS
                    //VOLVER A LA ACTIVIDAD NIVEL SIN QUE SUENE NADA
                }
                finish();  //ESTE MÉTODO DE JAVA NOS DEVUELVE A LA ACTIVIDAD ANTERIOR
            }
        });

        //LLAMO A SALIR DE LA PARTIDA
        salir.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                salirPartida();
            }
        });

        //MÉTODO Q ELIGE AUTOMÁTICAMENTE IMAGEN O SONIDO
        seleccionarElementoSegunNivel();

    }


    //LE METO AQUÍ DEBAJO Q ES LARGO
    private void seleccionarElementoSegunNivel()
    {

        //INSTANCIO EL RANDOM QUE LO VOY A USAR VARIAS VECES
        Random random = new Random();
        int indice; //EL ÍNDICE ELIGE IMAGENES O SONIDOS DEL ARRAY

        // VOY A USAR UN RANDOM BOOLEANO PARA QUE ELIJA TRUE O FALSE
        usandoImagenes = random.nextBoolean();

        if (usandoImagenes)
        {
            // SI SALE TRUE ENTRAMOS AQUÍ Y JUGAREMOS IMÁGENES
            imagenJuego.setVisibility(View.VISIBLE);  //ESTO OBLIGA AL ImageView A MOSTRAR LA IMAGEN

            if (mediaPlayer != null)
            {
                mediaPlayer.release();  //LIBERAMOS SONIDO
            }


            //EL ÍNDICE ALEATORIO SELECCIONARÁ AUTOMÁTICAMENTE UNA IMAGEN DE ENTRE LAS QUE EXISTEN
            if (nivel.equals("Muy Facil"))
            {
                indice = random.nextInt(imagenesMuyFacil.length);
                imagenActual = imagenesMuyFacil[indice];
                respuestaCorrecta = respuestasImagenesMuyFacil[indice];
            }
            else if (nivel.equals("Facil"))
            {
                indice = random.nextInt(imagenesFacil.length);
                imagenActual = imagenesFacil[indice];
                respuestaCorrecta = respuestasImagenesFacil[indice];
            }
            else if (nivel.equals("Dificil"))
            {
                indice = random.nextInt(imagenesDificil.length);
                imagenActual = imagenesDificil[indice];
                respuestaCorrecta = respuestasImagenesDificil[indice];
            }

            // LA PINTAMOS
            imagenJuego.setImageResource(imagenActual);
        }
        else
        {
            //SI EL RANDOM BOOLEANO ME DICE QUE !usandoImagenes ENTRAMOS EN ESTE ELSE, DE SONIDOS
            //LO PRIMERO QUE HACEMOS ES QUITAR EL ImageView PORQUE NO VA A MOSTRARSE NINGUNA IMAGEN
            imagenJuego.setVisibility(View.INVISIBLE);

            if (nivel.equals("Muy Facil"))
            {
                indice = random.nextInt(sonidosMuyFacil.length);
                sonidoActual = sonidosMuyFacil[indice];
                respuestaCorrecta = respuestasSonidosMuyFacil[indice];
            } else if (nivel.equals("Facil"))
            {
                indice = random.nextInt(sonidosFacil.length);
                sonidoActual = sonidosFacil[indice];
                respuestaCorrecta = respuestasSonidosFacil[indice];
            } else if (nivel.equals("Dificil"))
            {
                indice = random.nextInt(sonidosDificil.length);
                sonidoActual = sonidosDificil[indice];
                respuestaCorrecta = respuestasSonidosDificil[indice];
            }

            // AQUÍ REPRODUCIMOS EL SONIDO
            if (mediaPlayer != null)
            {
                mediaPlayer.release();
            }
            mediaPlayer = MediaPlayer.create(this, sonidoActual); //LE PASAMOS EL CONTEXTO Y EL SONIDO ELEGIDO
            mediaPlayer.start();
        }
    }

    private void jugarPartida()
    {
        //PASAMOS LO QUE RESPONDA TOLOWERCASE PORQUE LAS RESPUESTAS SON SOLAMENTE EN MINÚSCULAS
        String respuesta = respuestaJugador.getText().toString().toLowerCase().trim(); //AUNQUE ESTA LÍNEA DE CÓDIGO ES INÚTIL PORQUE NORMALIZAMOS LAS RESPUESTAS

        //TÉCNICA QUE ME DIÓ CHATGPT MUY ÚTIL PARA EVITAR SI EL USUARIO INTRODUCE O NO ACENTOS O ESPACIOS SIN QUERER O CARÁCTERES ESPECIALES EN LA RESPUESTA
        //POR TANTO, NORMALIZAMOS TANTO LA CADENA DE LA RESPUESTA CORRECTA DEL ARRAY COMO LA QUE INTRODUCE EL USUARIO, QUE SE LO PASAMOS POR PARÁMETRO AL MÉTODO
        String respuestaUsuarioNormalizada = normalizarTexto(respuesta);
        String respuestaCorrectaNormalizada = normalizarTexto(respuestaCorrecta);


        //CONTADOR1
        numPreguntas++;

        if (respuestaUsuarioNormalizada.equals(respuestaCorrectaNormalizada)) {
            Toast.makeText(this, "¡Respuesta correcta!", Toast.LENGTH_SHORT).show();

            //CONTADOR2
            numCorrectas++;

            //CADA NIVEL DARÁ UNA PUNTUACIÓN POR ACIERTO, USO LA DEL HACKATON
            int puntosGanados = 0;
            if (nivel.equals("Muy Facil"))
            {
                puntosGanados = 10;
            } else if (nivel.equals("Facil"))
            {
                puntosGanados = 20;
            } else if (nivel.equals("Dificil"))
            {
                puntosGanados = 30;
            }
            puntuacionActual=puntuacionActual+ puntosGanados;

            //GUARDAMOS EN SP
            editor = sp.edit();
            editor.putInt(getKeyPuntuacion(), puntuacionActual);
            editor.apply();

            //SE LA MOSTRAMOS
            ultimaPuntuacion.setText("Puntuación actual: " + puntuacionActual);
        } else {
            Toast.makeText(this, "Respuesta incorrecta", Toast.LENGTH_SHORT).show();
        }

        // SI LLEGAMOS A 5 PREGUNTAS, YA MOSTRAMOS PUNTUACIÓN
        if (numPreguntas >= totalPreguntas)
        {
            mostrarPuntuacionFinal();
        }
        else
        {
            //SINO, SEGUIMOS JUGANDO, LIMPIAMOS CAJITA DE RESPUESTA Y ELEGIMOS IMAGEN O SONIDO
            respuestaJugador.setText("");
            seleccionarElementoSegunNivel();
        }
    }
    private void mostrarPuntuacionFinal()
    {
        // GUARDAMOS LA PUNTUACIÓN EN SP, QUE HABRÑÁ QUE MOSTRAR EN EL FUTURO
        editor = sp.edit();
        editor.putInt(getKeyPuntuacion(), puntuacionActual);
        editor.apply(); //SIN ESTO A VECES NO SE GUARDA, ACUÉRDATE SIEMPRE DIEGO

        String mensajeFinal;
        // CREEAMOS UN MENSAJE QUE QUEREMOS MOSTRAR AL USUARIO
        if (puntuacionActual>puntuacionAnterior)
        {
            mensajeFinal = "ANTERIOR PUNTUACIÓN: "+puntuacionAnterior+"\n\n¡ENHORABUENA! Has superado tu anterior marca con los siguientes resultados:\n" + "Respuestas correctas: " + numCorrectas + " de " + totalPreguntas + "\n\n" + "Puntuación final: " + puntuacionActual;
        }
        else
        {
            mensajeFinal = "ANTERIOR PUNTUACIÓN: "+puntuacionAnterior+"\n\nTus resultados no han superado tu marca anterior :(\n" + "Respuestas correctas: " + numCorrectas + " de " + totalPreguntas + "\n\n" + "Puntuación final: " + puntuacionActual;

        }

        // COMO QUERIA ALGO PARECIDO A JOPTIONPANE DE SWING VOY A USAR UN ALERTDIALOG,
        // UNA VENTANA QUE TENGA QUE CERRAR EL USUARIO EN LUGAR DE UN MENSAJE TEMPORAL QUE SE QUITA SÓLO
        //POR SI EL USUARIO DESEA GUARDAR LAS PUNTUACIONES EN UN FOLIO CON SU LAPICERO DE CONFIANZA, QUE LE PUEDA DAR TIEMPO.

        //LE PASAMOS THIS, LE DAMOS TITULO, LE METEMOS EL MENSAJE QUE HEMOS GUARDADO ANTES EN STRING, LE DAMOS A QUE NO PUEDA CANCELARSE, PONEMOS UN BOTÓN ACEPTAR PARA QUE TRAS PULSARSE SE TERMINE LA PARTIDA
        new AlertDialog.Builder(this).setTitle("¡FIN DE LA PARTIDA!").setMessage(mensajeFinal).setCancelable(false).setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // ELIMINAMOS SONIDO SI SIGUE REPRODUCIÉNDOSE
                        if (mediaPlayer != null) {
                            mediaPlayer.release();
                        }
                        finish();
                    }
                }).show(); //-> EL .show(); ES OBLIGATORIO AL FINAL DE LOS ALERTDIALOG SINO NO SE MUESTRA NADA Y SÍ EJECUTA EL PROCESO QUEMANDO RECURSOS
    }

    // ESTE ES EL MÉTODO QUE NORMALIZA TEXTO, CON LOWERCASE Y ELIMINA ESPACIOS Y ACENTOS
    private String normalizarTexto(String texto)
    {
        texto = texto.toLowerCase();
        //LOS ACENTOS LOS ELIMINO MANUALMENTE
        eliminarAcentosManualmente(texto);
        //ME GUSTARÍA CONTROLAR TAMBIÉN QUE POR EJEMPLO, SI RESPUESTA ES "ley de newton"
        // Y EL USUARIO RESOPNDE "ley newton" TAMBIÉN FUESE CORRECTA
        //ASÍ QUE PARA ELLO VOY A CONTROLARLO MANUALMENTE CON UN MÉTODO QUE ME ELIMINE ARTÍCULOS COMO "de, la, el"...
        texto = eliminarPalabrasIrrelevantes(texto); //ESTE LO CREO MANUALMENTE PARA ELIMINAR ARTÍCULOS INNECESARIOS
        return texto.trim();//ELIMINA ESPACIOS
    }
    private String eliminarAcentosManualmente(String texto)
    {
        texto = texto.replace("á", "a");
        texto = texto.replace("é", "e");
        texto = texto.replace("í", "i");
        texto = texto.replace("ó", "o");
        texto = texto.replace("ú", "u");
        texto = texto.replace("Á", "a");
        texto = texto.replace("É", "e");
        texto = texto.replace("Í", "i");
        texto = texto.replace("Ó", "o");
        texto = texto.replace("Ú", "u");

        return texto;
    }

    private void salirPartida()
    {
        //MODULARIZAMOS, PORQUE RESULTA QUE HABÍA QUE MOSTRAR LA PUNTUACIÓN ANTES DE PULSAR ESTE BOTÓN
        //CON LO QUE YA NO QUIERO QUE SALGA DEL PROGRAMA AUTOMÁTICAMENTE
        mostrarPuntuacionFinal();
    }

    private String eliminarPalabrasIrrelevantes(String texto)
    {
        //ESTO ES EN PPIO LAS QUE VOY A ELIMINAR DE LA RESPUESTA DEL USUARIO Y DE LA DEL ARRAY DE RESPUESTAS
        String[] palabrasIrrelevantes={"de", "la", "el", "los", "las", "y", "a"};

        String[]palabras=texto.split(" ");

        //VOY A USAR UN StringBuilder PARA ELIMINARLAS, PORQUE UNA CADENA PODRÁ CONTENER VARIOS ARTÍCULOS, NO SOLAMENTE UNO...
        StringBuilder resultado=new StringBuilder();

        //RECORROC ON FOREACH
        for (String palabra : palabras)
        {
            if (!java.util.Arrays.asList(palabrasIrrelevantes).contains(palabra))
            {
                if (resultado.length()>0)
                {
                    resultado.append(" "); //INTERCAMBIAMOS EN CASO DE QUE LA PALABRA TRAIDA EN EL CONSTRUCTOR CONTENGA "de", "la", etc... Y METEMOS UN ESPACIO EN SU LUGAR
                    //Y ADEMÁS ESTO SOLAMENTE OCURIRRÁ SI HAY MÁS DE UNA PALABRA EN LA RESPUESTA, SINO AQUÍ NO ENTRA
                }
                resultado.append(palabra); //AÑADO LA PALABRA AL RESULTADO FINAL
            }
        }

        //DEVOLVEMOS LA CADENA SIN EL ARTICULO
        return resultado.toString();

    }

    @Override
    protected void onPause()
    {
        super.onPause();
        //LIBERAMOS SONIDO
        if (mediaPlayer != null)
        {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    // RECOGERMOS LA PUNTUACIÓN DEL USUARIO
    private String getKeyPuntuacion()
    {
        return keyPuntuacion + "_" + nombre;
    }
}
