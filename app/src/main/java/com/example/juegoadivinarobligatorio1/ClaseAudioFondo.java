package com.example.juegoadivinarobligatorio1;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.juegoadivinarobligatorio1.R;

import java.util.Random;

//HE CREADO UNA CLASE PARA MANEJAR LA MUSICA DE FONDO DEL JUEGO
public class ClaseAudioFondo
{

    private MediaPlayer mediaPlayer;
    private int[] audioFiles = {
            R.raw.musica1,
            R.raw.musica2,
            R.raw.musica3,
            R.raw.musica4
    };

    private Random random = new Random();

    // LA MUSICA QUE SONARA DE FONDO ES ALEATORIA
    public void iniciarAudioAleatorio(Context context)
    {
        int randomAudio = audioFiles[random.nextInt(audioFiles.length)];
        mediaPlayer = MediaPlayer.create(context, randomAudio);
        mediaPlayer.setLooping(true); //LOS AUDIOS DURAN 10 MIN O ASÍ NO CREO Q SEA NECESARIO REPETIR
        //PERO EN CASO DE QUE LLEGUEMOS AL FINAL DEL AUDIO, EN LUGAR DE QUEDARNOS SIN SONIDO SE INICIA DE NUEVO
        mediaPlayer.start();
        mediaPlayer.setVolume(0.4f, 0.4f);
    }

    // ESTE MÉTODO DETIENE EL AUDIO CUANDO YO SE LO INDIQUE (AL MOMENTO DE EMPEZAR LA PARTIDA EN ACTIVITY 4)
    public void detenerAudio()
    {
        if (mediaPlayer != null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    // EL AUDIO SE PUEDE PAUSAR PARA CONTINUAR TRAS LAS PUNTUACIONES
    public void pausarAudio()
    {
        if (mediaPlayer != null && mediaPlayer.isPlaying())
        {
            mediaPlayer.pause();
        }
    }

    // EL AUDIO SE REANUDA
    public void reanudarAudio()
    {
        if (mediaPlayer != null && !mediaPlayer.isPlaying())
        {
            mediaPlayer.start();
        }
    }
}
