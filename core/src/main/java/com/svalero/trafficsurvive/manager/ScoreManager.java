package com.svalero.trafficsurvive.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;

import java.util.Comparator;

import static com.svalero.trafficsurvive.util.Constants.GAME_NAME;

public class ScoreManager {

    private static final Preferences prefs = Gdx.app.getPreferences(GAME_NAME + "_scores");

    // Guarda una nueva puntuación
    public static void saveScore(String name, int score) {
        // Guardamos usando el tiempo actual en milisegundos como clave única para que no se machaquen
        prefs.putInteger(name + "_" + System.currentTimeMillis(), score);
        prefs.flush();
    }

    // Devuelve las 10 mejores puntuaciones
    public static Array<String> getTop10Scores() {
        Array<ScoreInput> allScores = new Array<>();

        // Leemos todos los registros guardados en las preferencias
        for (String key : prefs.get().keySet()) {
            int score = prefs.getInteger(key);
            // Limpiamos la clave quitándole los milisegundos para recuperar el nombre limpio
            String name = key.substring(0, key.lastIndexOf("_"));
            allScores.add(new ScoreInput(name, score));
        }

        // Ordenamos puntuación
        allScores.sort(new Comparator<ScoreInput>() {
            @Override
            public int compare(ScoreInput s1, ScoreInput s2) {
                return Integer.compare(s1.score, s2.score);
            }
        });

        // Preparamos el formato de texto para la interfaz
        Array<String> top10 = new Array<>();
        int limit = Math.min(allScores.size, 10);
        for (int i = 0; i < limit; i++) {
            ScoreInput entry = allScores.get(i);
            top10.add((i + 1) + ". " + entry.name + " - " + entry.score + " pts");
        }

        return top10;
    }

    private static class ScoreInput {
        String name;
        int score;
        public ScoreInput(String name, int score) {
            this.name = name;
            this.score = score;
        }
    }
}
