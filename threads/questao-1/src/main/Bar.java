import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import model.GarsomClass;


public class Bar {


    // Parâmetros
    public static final int NUM_CLIENTES = 1; // a quantidade de clientes presentes no estabelecimento
    public static final int NUM_GARCONS = 1; // a quantidade de garçons que estão trabalhando
    public static final int CAPACIDADE_GARCONS = 1; // a capacidade de atendimento dos garçons
    public static final int NUM_RODADAS = 2; // o número de rodadas que serão liberadas no bar

    // Variáveis
    public static final Queue<Integer> filaPedidos = new LinkedList<>();
    public static final Queue<Integer> filaPedidosPreparo = new LinkedList<>();
    public static final Object Lock = new Object();
    public static boolean barAberto = true;
    public static int clientesAtendidos = 0;
    public static int clientesCriados = 0;
    public static int garcomsCriados = 0;
    public static final Semaphore garconsDisponiveis = new Semaphore(NUM_GARCONS);
    public static final Semaphore preparoBartender = new Semaphore(1);
    public static int rodada = 1;

    //Garçons
    public static List<GarsomClass> garcoms = new ArrayList<>();

    public static void main(final String[] args) {
        
        for (int i = 0; i < NUM_GARCONS; i++) {
            garcoms.add(new GarsomClass(i,CAPACIDADE_GARCONS)); 
            System.err.println("\n  Garçom " + (i + 1) + " está aguardando no salão.");
            new Thread(new GarcomThread(i,CAPACIDADE_GARCONS)).start();
            garcomsCriados +=1;
        }
        
        new Thread(new RodadaController()).start();
        
        for (int i = 0; i < NUM_CLIENTES; i++) {
            new Thread(new ClienteThread(i)).start();
            clientesCriados += 1;
        }
    }
}