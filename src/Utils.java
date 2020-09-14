import org.json.*;
import java.util.*;

public class Utils {

    public static Ordine parse(String json){

        JSONObject obj = new JSONObject(json);

        String nome = (String) obj.get("nome");
        String tavolo = (String) obj.get("tavolo");
        HashMap<String, Integer> map = new HashMap<>();

        JSONArray piatti = obj.getJSONArray("consumazioni");

        for(int i = 0; i < piatti.length(); i++){

            String piatto = piatti.getJSONObject(i).getString("nome");
            int quantita = piatti.getJSONObject(i).getInt("numero");

            map.put(piatto, quantita);

        }

        return new Ordine(nome, tavolo, map);
    }

    public static String build(Ordine ordine) {

        JSONObject obj = new JSONObject().put("nome", ordine.getNome()).put("tavolo", ordine.getTavolo());

        for (Map.Entry<String, Integer> entry : ordine.getOrdine().entrySet()) {
            String key = entry.getKey();
            int value = entry.getValue();

            obj.append("consumazioni", new JSONObject().put(key, value));
        }

        return obj.toString();
    }
}