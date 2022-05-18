package helpers;

import java.util.Comparator;

public class Distancia {

    private Float[] centroide;
    private Float distancia;

    public Distancia() {
    }

    public Distancia(Float[] centroide, Float distancia) {
        this.centroide = centroide;
        this.distancia = distancia;
    }

    public Float getDistancia() {
        return distancia;
    }

    public Float[] getCentroide() {
        return centroide;
    }

    public Float calcularDistancia(Float[] x, Float[] y){
        float distancia = 0.0f;
        for(int i = 0; i < x.length; i++){
            distancia += Math.pow(x[i] - y[i], 2);
        }
        distancia = (float) Math.sqrt(distancia);
        return distancia;
    }
}
