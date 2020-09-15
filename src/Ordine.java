import java.util.*;

public class Ordine {

    private String nome;
    private String tavolo;
    private HashMap<String, Integer> ordine;

    public Ordine(){
        this.nome = "SENZA NOME";
        this.tavolo = "X";
        this.ordine = new HashMap<>();
    }

    public Ordine(String nome, String tavolo){
        this.nome = nome;
        this.tavolo = tavolo;

        this.ordine = new HashMap<>();
    }

    public Ordine(String nome, String tavolo, HashMap<String, Integer> ordine){
        this.nome = nome;
        this.tavolo = tavolo;
        this.ordine = ordine;
    }

    public String getNome(){
        return this.nome;
    }

    public String getTavolo(){
        return this.tavolo;
    }

    public HashMap<String, Integer> getOrdine() {
        return this.ordine;
    }

    public void add(String key){

        if (this.ordine.containsKey(key)){
            int amount = this.ordine.get(key);

            this.ordine.put(key, amount + 1);
        }
        else {
            this.ordine.put(key, 1);
        }

        System.out.println("Adding " + key + " to the order");
    }
}