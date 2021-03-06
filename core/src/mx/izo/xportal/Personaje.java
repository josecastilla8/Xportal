package mx.izo.xportal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;


public class Personaje {

    public static final float VELOCIDAD_Y = -4f;   // Velocidad de caída
    //ya no sera estático
    //public static final float VELOCIDAD_X = 2;     // Velocidad horizontal
    public static float VELOCIDAD_X = 2;     // Velocidad horizontal

    private Sprite sprite, spriteSalto, sprite1;  // Sprite cuando no se mueve

    // Animación
    private Animation animacion;    // Caminando
    private Animation animarSalto;    //Saltando
    private float timerAnimacion;   // tiempo para calcular el frame
    private boolean banderaPosicion=false;

    // Estados del personaje
    private EstadoMovimiento estadoMovimiento;
    private EstadoSalto estadoSalto;

    // SALTO del personaje
    private float V0 = 80;     // Velocidad inicial del salto
    private static final float G = 9.81f;
    private static final float G_2 = G / 2;   // Gravedad
    private float yInicial;         // 'y' donde inicia el salto
    private float tiempoVuelo;       // Tiempo que estará en el aire
    private float tiempoSalto;// Tiempo actual de vuelo

    private TextureRegion texturaSaltoF;
    private TextureRegion pers;
    float x;


    private boolean normal = true;

    //constructor que se usara en el minigame dos
    public Personaje(Texture textura){
        sprite = new Sprite(textura);
        sprite1= new Sprite(textura);
        pers = new TextureRegion(textura);
        estadoMovimiento = EstadoMovimiento.INICIANDO;
        estadoSalto = EstadoSalto.EN_PISO;
        timerAnimacion = 0;
        normal = false;
    }

    /*
    Constructor del personaje, recibe una imagen con varios frames, (ver imagen marioSprite.png)
     */
    public Personaje(Texture textura, Texture texturaSaltos) {
        // Lee la textura como región
        TextureRegion texturaCompleta = new TextureRegion(textura);
        TextureRegion texturaSalto = new TextureRegion(texturaSaltos);
        // La divide en frames de 16x32 (ver marioSprite.png)
        //TextureRegion[][] texturaPersonaje = texturaCompleta.split(16,32);
        TextureRegion[][] texturaPersonaje = texturaCompleta.split(82, 138);
        TextureRegion[][] texturaPersonaje2 = texturaCompleta.split(82, 138);
        TextureRegion[][] texturaSaltar = texturaSalto.split(57, 96);


        // Crea la animación con tiempo de 0.25 segundos entre frames.
        animacion = new Animation(0.25f, texturaPersonaje[0][0],texturaPersonaje[0][1], texturaPersonaje[0][2], texturaPersonaje[0][3],texturaPersonaje[0][4], texturaPersonaje[0][5],texturaPersonaje[0][6],texturaPersonaje[0][7]);
        //animarSalto = new Animation(0.25f, texturaPersonaje[0][5],
               // texturaPersonaje[0][2], texturaPersonaje[0][1]);

        // Animación infinita
        animacion.setPlayMode(Animation.PlayMode.LOOP);
        //animarSalto.setPlayMode(Animation.PlayMode.LOOP);
        //texturaSaltoF = new TextureRegion(texturaSaltar[0][5]);
        // Inicia el timer que contará tiempo para saber qué frame se dibuja
        timerAnimacion = 0;
        // Crea el sprite cuando para el personaje quieto (idle)
        sprite = new Sprite(texturaPersonaje[0][2]);    // quieto
        sprite1= new Sprite (texturaPersonaje2 [0][6]);
        estadoMovimiento = EstadoMovimiento.INICIANDO;
        estadoSalto = EstadoSalto.EN_PISO;
    }

    public Personaje(Texture textura, Texture texturaSaltos, Texture texturaI) {
        // Lee la textura como región
        TextureRegion texturaCompleta = new TextureRegion(textura);
        TextureRegion texturaIzq = new TextureRegion(texturaI);
        TextureRegion texturaSalto = new TextureRegion(texturaSaltos);
        // La divide en frames de 16x32 (ver marioSprite.png)
        //TextureRegion[][] texturaPersonaje = texturaCompleta.split(16,32);
        TextureRegion[][] texturaPersonaje = texturaCompleta.split(82, 138);
        TextureRegion[][] texturaPersonaje2 = texturaIzq.split(82, 138);
        //TextureRegion[][] texturaSaltar = texturaSalto.split(57, 96);


        // Crea la animación con tiempo de 0.25 segundos entre frames.
        animacion = new Animation(0.10f, texturaPersonaje[0][0],texturaPersonaje[0][1], texturaPersonaje[0][2], texturaPersonaje[0][3],texturaPersonaje[0][4], texturaPersonaje[0][5],texturaPersonaje[0][6],texturaPersonaje[0][7]);
        //animarSalto = new Animation(0.25f, texturaPersonaje[0][5],
        // texturaPersonaje[0][2], texturaPersonaje[0][1]);

        // Animación infinita
        animacion.setPlayMode(Animation.PlayMode.LOOP);
        //animarSalto.setPlayMode(Animation.PlayMode.LOOP);
        //texturaSaltoF = new TextureRegion(texturaSaltar[0][5]);
        // Inicia el timer que contará tiempo para saber qué frame se dibuja
        timerAnimacion = 0;
        // Crea el sprite cuando para el personaje quieto (idle)
        sprite = new Sprite(texturaPersonaje[0][2]);    // quieto
        sprite1= new Sprite(texturaPersonaje2 [0][1]);
        estadoMovimiento = EstadoMovimiento.INICIANDO;
        estadoSalto = EstadoSalto.EN_PISO;
    }

    // Dibuja el personaje
    public void render(SpriteBatch batch) {
        switch (estadoMovimiento) {

            case MOV_DERECHA:
            case MOV_IZQUIERDA:
                if(normal) {
                    // Incrementa el timer para calcular el frame que se dibuja
                    timerAnimacion += Gdx.graphics.getDeltaTime();
                    // Obtiene el frame que se debe mostrar (de acuerdo al timer)
                    TextureRegion region = animacion.getKeyFrame(timerAnimacion);
                    // Dirección correcta
                    if (estadoMovimiento == EstadoMovimiento.MOV_IZQUIERDA) {
                        if (!region.isFlipX()) {
                            region.flip(true, false);
                        }
                    } else {
                        if (region.isFlipX()) {
                            region.flip(true, false);
                        }
                    }
                    // Dibuja el frame en las coordenadas del sprite
                    batch.draw(region, sprite.getX(), sprite.getY());
                    break;
                }else{
                    batch.draw(pers,sprite.getX(),sprite.getY());
                    break;
                }
            case INICIANDO:
            case QUIETO:
                if(banderaPosicion){
                    sprite1.setPosition(this.getX(),this.getY());
                    sprite1.draw(batch);
                }
                else{
                    sprite.draw(batch);
                } // Dibuja el sprite
                break;

        }
       /* switch (estadoSalto) {
            case SUBIENDO:
            case BAJANDO:
            case CAIDA_LIBRE:
                //timerAnimacion += Gdx.graphics.getDeltaTime();
                //TextureRegion region = animarSalto.getKeyFrame(timerAnimacion);
                timerAnimacion = 0;
                sprite.setRegion(texturaSaltoF);
                sprite.draw(batch);
                break;

        }*/

    }

    // Actualiza el sprite, de acuerdo al estadoMovimiento
    public void actualizar() {
        // Ejecutar movimiento horizontal
        float nuevaX = sprite.getX();
        switch (estadoMovimiento) {
            case MOV_DERECHA:
                // Prueba que no salga del mundo
                nuevaX += VELOCIDAD_X;
                if (nuevaX <= PantallaJuego.ANCHO_MAPA - sprite.getWidth()) {
                    sprite.setX(nuevaX);
                }
                break;
            case MOV_IZQUIERDA:
                // Prueba que no salga del mundo
                nuevaX -= VELOCIDAD_X;
                if (nuevaX >= 0) {
                    sprite.setX(nuevaX);
                }
                break;
        }
    }

    public void verificarCaida(TiledMap mapa) {
        boolean hayCeda = leerCeldaAbajo(mapa);
        if (!hayCeda) {
            estadoSalto = EstadoSalto.BAJANDO;
        }
    }

    private boolean leerCeldaAbajo(TiledMap mapa) {
        TiledMapTileLayer capa = (TiledMapTileLayer) mapa.getLayers().get(0);
        int x = (int) ((sprite.getX()) / 57);
        int y = (int) ((sprite.getX() + VELOCIDAD_Y) / 96);
        TiledMapTileLayer.Cell celdaA = capa.getCell(x, y);
        if (celdaA != null) {
            Object tipo = celdaA.getTile().getProperties().get("tipo");
            if (!"esPiso".equals(tipo)) {
                celdaA = null;
            }
        }
        TiledMapTileLayer.Cell celdaDer = capa.getCell(x + 1, y);
        if(celdaDer != null){
            Object tipo = celdaDer.getTile().getProperties().get("tipo");
        if (!"esPiso".equals(tipo)) {
            celdaDer = null;
        }
        }
       return celdaA!=null || celdaDer!= null;
    }




    public void caer() {
        timerAnimacion=0;
        if(sprite.getY()>sprite.getScaleY()) {
            sprite.setY(sprite.getY() + VELOCIDAD_Y);
        }
    }

    // Actualiza la posición en 'y', está saltando
    public void actualizarSalto(TiledMap mapa) {
        timerAnimacion=0;
        tiempoSalto += 10*Gdx.graphics.getDeltaTime();
        float y= V0 *tiempoSalto -G_2 *tiempoSalto*tiempoSalto;
        if(tiempoSalto > tiempoVuelo/2){
            estadoSalto=EstadoSalto.EN_PISO;
        }
        if(estadoSalto==EstadoSalto.SUBIENDO){
            sprite.setY(yInicial+y);
        }
        else if (estadoSalto==EstadoSalto.BAJANDO){
            boolean hayCelda =leerCeldaAbajo(mapa);
            if(hayCelda) {
                estadoSalto = EstadoSalto.EN_PISO;
            }
            else {
                sprite.setY(sprite.getY()+VELOCIDAD_Y);
            }
        }
        if(y<0){
            sprite.setY(yInicial);
            estadoSalto=EstadoSalto.EN_PISO;
        }
    }
        // Ejecutar movimiento vertical      /*
      //  float y = V0 * tiempoSalto - G_2 * tiempoSalto * tiempoSalto;  // Desplazamiento desde que inició el salto
        //if (tiempoSalto > tiempoVuelo / 2) { // Llegó a la altura máxima?
            // Inicia caída
          //  estadoSalto = EstadoSalto.BAJANDO;

        //tiempoSalto += 10 * Gdx.graphics.getDeltaTime();  // Actualiza tiempo
        //sprite.setY(yInicial + y);    // Actualiza posición
        //if (y < 0) {
            // Regresó al piso
           // sprite.setY(yInicial);  // Lo deja donde inició el salto
        //estadoSalto = EstadoSalto.EN_PISO;  // Ya no está saltando



    // Accesor de la variable sprite
    public Sprite getSprite() {
        return sprite;
    }

    // Accesores para la posición
    public float getX() {
        return sprite.getX();
    }

    public float getY() {
        return sprite.getY();
    }

    public void setPosicion(float x, int y) {
        sprite.setPosition(x,y);
    }

    // Accesor del estadoMovimiento
    public EstadoMovimiento getEstadoMovimiento() {
        return estadoMovimiento;
    }

    // Modificador del estadoMovimiento
    public void setEstadoMovimiento(EstadoMovimiento estadoMovimiento) {
        this.estadoMovimiento = estadoMovimiento;
    }

    public void setEstadoSalto(EstadoSalto estadoSalto) {
        this.estadoSalto = estadoSalto;
    }

    public void setVelocidadX(float vel){
        this.VELOCIDAD_X = vel;
    }

    public void setBanderaPosicion(boolean bandera){
        this.banderaPosicion=bandera;
    }

    public void setSalto(float velocidad ){
        this.V0 = velocidad;
    }

    // Inicia el salto
    public void saltar() {
        if (estadoSalto==EstadoSalto.EN_PISO) {
            tiempoSalto = 0;
            tiempoSalto=0;
            yInicial = sprite.getY();
            estadoSalto = EstadoSalto.SUBIENDO;
            tiempoVuelo = 2 * V0 / G;

        }
    }

    public EstadoSalto getEstadoSalto() {
        return estadoSalto;
    }

    public enum EstadoMovimiento {
        INICIANDO,
        QUIETO,
        MOV_IZQUIERDA,
        MOV_DERECHA
    }

    public enum EstadoSalto {
        EN_PISO,
        SUBIENDO,
        BAJANDO,
        CAIDA_LIBRE // Cayó de una orilla
    }
}
