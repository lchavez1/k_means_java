import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author AndrésEspinalJiménez
 */

public class Dataset {
    private LinkedList<String> etiqueta;
    private HashMap<Integer, LinkedList<Float[]>> patrones;
    private LinkedList<Float[]> conjunto;

    private HashMap<Integer, LinkedList<Float[]>> entrenamiento;
    private HashMap<Integer, LinkedList<Float[]>> prueba;

    private float pEntrenamiento = 0.90f;
    private float pPrueba = 1 - pEntrenamiento;

    public Dataset(String rutaArchivo) throws FileNotFoundException, IOException{
        BufferedReader lector = new BufferedReader(new FileReader(new File(rutaArchivo)));
        String linea = "";
        this.etiqueta = new LinkedList<>();
        this.patrones =  new HashMap<>();

        while((linea = lector.readLine()) != null){
            String[] sPatron = linea.split(",");

            if(!this.etiqueta.contains(sPatron[sPatron.length -1])){
                this.etiqueta.add(sPatron[sPatron.length -1]);
                this.patrones.put(this.etiqueta.indexOf(sPatron[sPatron.length -1]), new LinkedList<>());
            }
            Float[] tmp = new Float[sPatron.length - 1];
            for(Integer i = 0; i < sPatron.length - 1; i++)
                tmp[i] = Float.valueOf(sPatron[i]);
            this.patrones.get(this.etiqueta.indexOf(sPatron[sPatron.length -1])).add(tmp);
        }

        divisionEntranamientoPrueba();
        this.conjunto = new LinkedList<>();
        AgruparPatrones();
    }

    public LinkedList<String> getEtiquetas(){

        for( int i = 0; i < this.etiqueta.size(); i++){
            if(this.etiqueta.get(i).equals("") || this.etiqueta.get(i) == null){
                this.etiqueta.remove(i);
                //System.out.println("Elimino una etiqueta vacia");
            }
        }
        return this.etiqueta;

    }

    public HashMap<Integer, LinkedList<Float[]>> getPatrones(){
        for(int i = 0; i < this.patrones.size(); i++){
            if(this.patrones.get(i) == null || this.patrones.get(i).getFirst().length == 0){
                this.patrones.remove(i);
                //System.out.println("Se elimino un patron vacio");
            }
        }
        return this.patrones;

    }

    public void divisionEntranamientoPrueba(){
        Random numAleatorio = new Random();
        this.entrenamiento = new HashMap<>();
        LinkedList<Float[]> list = new LinkedList<>();
        //float porcentajeEntrenamientoXClase = pEntrenamiento/this.getEtiquetas().size();
        for(int i = 0; i < this.getEtiquetas().size(); i++){
            float nPatrones = pEntrenamiento * this.getPatrones().get(i).size();
            nPatrones = (int) nPatrones;
            for(int j = 0; j < nPatrones; j++){
                int index = numAleatorio.nextInt(this.getPatrones().get(i).size()-1+1) + 0;
                list.add(this.getPatrones().get(i).get(index));
            }
            this.entrenamiento.put(i, list);
            list = new LinkedList<>();
        }

        this.prueba = new HashMap<>();
        list = new LinkedList<>();
        //float porcentajePruebaXClase = pPrueba/this.getEtiquetas().size();
        for(int i = 0; i < this.getEtiquetas().size(); i++){
            float nPatrones = pPrueba * this.getPatrones().get(i).size();
            nPatrones = (int) nPatrones;
            for(int j = 0; j < nPatrones; j++){
                int index = numAleatorio.nextInt(this.getPatrones().get(i).size()-1+1) + 0;
                list.add(this.getPatrones().get(i).get(index));
            }
            this.prueba.put(i, list);
            list = new LinkedList<>();
        }
    }

    public HashMap<Integer, LinkedList<Float[]>> getEntrenamiento(){
        return this.entrenamiento;
    }

    public HashMap<Integer, LinkedList<Float[]>> getPrueba(){
        return this.prueba;
    }

    public void AgruparPatrones(){
        for(int i = 0; i < this.getPatrones().size(); i++){
            for(int j = 0; j < this.getPatrones().get(i).size(); j++){
                this.conjunto.add(this.patrones.get(i).get(j));
            }
        }
    }

    public LinkedList<Float[]> getConjunto(){
        return this.conjunto;
    }

    /*public static void main(String[] args) throws FileNotFoundException, IOException{
        Dataset ds = new Dataset("src/iris.data");

        System.out.println(ds.getPrueba().get(0).size());
        System.out.println(ds.getEntrenamiento().get(0).size());
        System.out.println(ds.getPrueba().get(1).size());
        System.out.println(ds.getEntrenamiento().get(1).size());

        System.out.println(ds.getPatrones().get(0).size());
        System.out.println( ds.getPatrones().get(1).size());



    }*/

}

