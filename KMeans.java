import helpers.Distancia;
import java.io.IOException;
import java.util.*;

public class KMeans {
    //blood = transfusion
    //iris
    //liver = bupa


    public static void main(String[] args) throws IOException {
        System.out.println("Ingrese cantidad de grupos a generar: ");
        Scanner scanner = new Scanner(System.in);
        int k = scanner.nextInt();
        int maxIteraciones = 100;
        Dataset dataset = new Dataset("src/iris.data");
        Distancia distancia = new Distancia();
        LinkedList<Float[]> ys = new LinkedList<>();
        HashMap<Float[], LinkedList<Float[]>> grupos = new HashMap<>();

        //Calcular distancias con respecto a cada punto centroide
        int x = 0;
        while(true){
            System.out.println("\nIteracion " + x);
            if(x == 0){
                //Obtener k patrones aleatorios del conjunto
                for(int i = 0; i < k; i++){
                    int index = (int) (Math.random() * dataset.getConjunto().size() + 0);
                    ys.add(dataset.getConjunto().get(index));
                    grupos.put(dataset.getConjunto().get(index), new LinkedList<>());
                }

                System.out.print("Y ->");
                for(int i = 0; i < ys.size(); i++){
                    System.out.print("\t");
                    System.out.print(Arrays.toString(ys.get(i)));
                }

                calcularDistanciasAsignacionGrupos(dataset, ys, distancia, grupos);
            } else {
                LinkedList<Float[]> antiguosYs = ys;
                ys = reAsignacionCentroides(grupos, antiguosYs);
                System.out.print("Y ->");
                for(int i = 0; i < ys.size(); i++){
                    System.out.print("\t");
                    System.out.print(Arrays.toString(ys.get(i)));
                }
                System.out.print("\nY anteriores ->");
                for(int i = 0; i < antiguosYs.size(); i++){
                    System.out.print("\t");
                    System.out.print(Arrays.toString(antiguosYs.get(i)));
                }
                if(statusConvergencia(antiguosYs, ys)){
                    ys = antiguosYs;
                    System.out.println();
                    System.out.printf("\nConvergencia en la iteracion %d", x);
                    break;
                }
                grupos = new HashMap<>();
                for(int i = 0; i < ys.size(); i++){
                    grupos.put(ys.get(i), new LinkedList<>());
                }
                calcularDistanciasAsignacionGrupos(dataset, ys, distancia, grupos);
            }
            x++;
            if(x >= maxIteraciones)
                break;
        }
        System.out.println();
        for(int i = 0; i < ys.size(); i++){
            System.out.printf("\nCentroide %d [%.2f, %.2f, %.2f, %.2f] contiene %d patrones", i, ys.get(i)[0], ys.get(i)[1], ys.get(i)[2], ys.get(i)[3], grupos.get(ys.get(i)).size());
        }
        System.out.println();
        //imprimimos la lista de cada grupo
        for(int i = 0; i < ys.size(); i++){
            System.out.printf("\nCentroide %d [%.2f, %.2f, %.2f, %.2f] :", i, ys.get(i)[0], ys.get(i)[1], ys.get(i)[2], ys.get(i)[3]);
            for(int j = 0; j < grupos.get(ys.get(i)).size(); j++){
                System.out.printf("\n(%d) [%.2f, %.2f, %.2f, %.2f]", j+1, grupos.get(ys.get(i)).get(j)[0], grupos.get(ys.get(i)).get(j)[1], grupos.get(ys.get(i)).get(j)[2], grupos.get(ys.get(i)).get(j)[3]);
            }
        }
    }

    public static HashMap<Float[], LinkedList<Float[]>> calcularDistanciasAsignacionGrupos(Dataset dataset, LinkedList<Float[]> ys, Distancia distancia, HashMap<Float[], LinkedList<Float[]>> grupos){
        //HashMap<Float[], LinkedList<Float[]>> grupos = new HashMap<>();
        for(int i = 0; i < dataset.getConjunto().size(); i++){
            LinkedList<Distancia> listaDistancias = new LinkedList<>();
            for(int j = 0; j < ys.size(); j++){
                float dis = distancia.calcularDistancia(dataset.getConjunto().get(i), ys.get(j));
                listaDistancias.add(new Distancia(ys.get(j), dis));
            }

            //Ordenamos las distancias para ubicar la menor
            Collections.sort(listaDistancias, new Comparator<Distancia>() {
                @Override
                public int compare(Distancia o1, Distancia o2) {
                    return o1.getDistancia().compareTo(o2.getDistancia());
                }
            });
            grupos.get(listaDistancias.getFirst().getCentroide()).add(dataset.getConjunto().get(i));
        }
        return grupos;
    }

    public static LinkedList<Float[]> reAsignacionCentroides(HashMap<Float[], LinkedList<Float[]>> grupos, LinkedList<Float[]> y){
        LinkedList<Float[]> ys = new LinkedList<>();
        for(int i = 0; i < grupos.size(); i++){
            Float[] nuevo = new Float[y.getFirst().length];
            for(int d = 0; d < y.getFirst().length; d++){
                int counter = 0;
                float sumatoria = 0.0f;
                for(int j = 0; j < grupos.get(y.get(i)).size(); j++){
                    sumatoria += grupos.get(y.get(i)).get(j)[d];
                    counter++;
                }
                sumatoria = sumatoria / counter;
                nuevo[d] = sumatoria;
            }
            ys.add(nuevo);
        }
        return ys;
    }

    public static boolean statusConvergencia(LinkedList<Float[]> antiguo, LinkedList<Float[]> actual){
        int counter = 0;
        for(int i = 0; i < antiguo.size(); i++){
            if(Arrays.equals(antiguo.get(i), actual.get(i)))
                counter++;
        }
        if(counter == antiguo.size())
            return true;
        return false;
    }
}
