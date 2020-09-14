import javax.swing.*;
import org.json.*;
import java.io.*;
import java.util.Iterator;

public class OrderPanel extends JPanel{

    private JButton sendOrder;

    public OrderPanel() {

        sendOrder = new JButton("Invia Ordine");
        
        JFrame frame = new JFrame("Ordini");
        frame.setSize(500, 500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
    }

    public void send() {
    }

    private void init(){
        JSONObject obj = new JSONObject(read("conf/menu.json"));

        JSONArray arr = obj.getJSONArray("menu");
        for(int i = 0;i < arr.length();i++){

            JSONObject curr = arr.getJSONObject(i);
            String key = curr.keys().next();
            add(new JLabel(key));

            JSONArray temp = curr.getJSONArray(key);
            for(int j = 0; j < temp.length(); j++){
                add(new JButton(temp.getString(j)));
            }
        }
    }

    public String read(String file) {

        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e1) {
            System.err.println("File " + file + " not found.");
        }

        String st;
        try {
            while ((st = br.readLine()) != null) {
                sb.append(st);

            }
        } catch (IOException e) {
            System.err.println("Error while reading file " + file);
        }

        return sb.toString();
    }
    
}