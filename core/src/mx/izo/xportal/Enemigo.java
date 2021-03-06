package mx.izo.xportal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;

/**
 * Created by isain on 10/25/2016.
 */
public class Enemigo {
    private Sprite sprite;
    private int vidas = 5;


    //animacion
    private Animation animacion;    // Caminando
    private float timerAnimacion;   // tiempo para calcular el frame





    public Enemigo(Texture textura) {
        TextureRegion texturaEnemigo = new TextureRegion(textura);

        sprite = new Sprite(texturaEnemigo);

        TextureRegion[][] texturaPersonaje = texturaEnemigo.split(269,134);
        animacion = new Animation(0.20f,texturaPersonaje[0][5], texturaPersonaje[0][4], texturaPersonaje[0][3],texturaPersonaje[0][2], texturaPersonaje[0][1],texturaPersonaje[0][0],
                texturaPersonaje[0][1], texturaPersonaje[0][2],texturaPersonaje[0][3], texturaPersonaje[0][4]);
        // Animación infinita
        animacion.setPlayMode(Animation.PlayMode.LOOP);
        // Inicia el timer que contará tiempo para saber qué frame se dibuja
        timerAnimacion = 0;
        // Crea el sprite cuando para el personaje quieto (idle)
        sprite = new Sprite(texturaPersonaje[0][0]);    // quieto

    }

    public void render(SpriteBatch batch) {

        timerAnimacion += Gdx.graphics.getDeltaTime();
        // Obtiene el frame que se debe mostrar (de acuerdo al timer)
        TextureRegion region = animacion.getKeyFrame(timerAnimacion);
        batch.draw(region, sprite.getX(), sprite.getY());
    }

    public void setPosicion(float x, float y) {
        sprite.setPosition(x, y);
    }

    public int getVidas(){
        return vidas;
    }

    public void setVidas(int vidas){
        this.vidas = vidas;
    }

    public float getX() {
        return sprite.getX();
    }

    public float getY() {
        return sprite.getY();
    }

    public Sprite getSprite() {
        return sprite;
    }
}
