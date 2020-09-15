import javax.swing.*;
import org.json.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.*;

public class OrderPanel extends JPanel {

    private JButton sendOrder, newOrder;
    private Ordine currentOrder;

    private static ServerSocket server;
    private static ArrayList<Socket> clients;

    public OrderPanel() {

        currentOrder = new Ordine();

        try {
            server = new ServerSocket(36135);
            clients = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
        }

        init();

        JFrame frame = new JFrame("Ordini");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.pack();
    }

    private void init() {

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        int lastx = 0;
        int lasty = 0;

        JSONObject obj = new JSONObject(read("conf/menu.json"));

        JSONArray arr = obj.getJSONArray("menu");
        for (int i = 0; i < arr.length(); i++) {

            JSONObject curr = arr.getJSONObject(i);
            String key = curr.keys().next();


            JSONArray temp = curr.getJSONArray(key);
            c.fill = GridBagConstraints.HORIZONTAL;
            c.insets = new Insets(10,10,10,10);
            c.gridx = 0;
            c.gridwidth = temp.length();
            c.gridy = i + lasty;
            add(new JLabel(key), c);
            lasty++;
            for (int j = 0; j < temp.length(); j++) {

                JButton b = new JButton(temp.getString(j));

                b.addActionListener(event -> {
                    currentOrder.add(b.getText());
                });

                c.fill = GridBagConstraints.HORIZONTAL;
                c.gridwidth = 1;
                c.gridx = j;
                c.gridy = i+lasty;
                add(b, c);
            }
        }

        lasty += arr.length();

        sendOrder = new JButton("Invia Ordine");
        sendOrder.addActionListener(event -> {

            String order = Utils.build(currentOrder);

            send(order, clients);
            System.out.println("Sending order " + order);
            sendOrder.setEnabled(false);
        });


        newOrder = new JButton("Nuovo Ordine");
        newOrder.addActionListener(event -> {

            JFrame popup = new JFrame("Nuovo ordine");
            JPanel temp = new JPanel();
            JTextField nome = new JTextField(20);
            JTextField tavolo = new JTextField(10);
            JButton ok = new JButton("OK");
            JButton cancel = new JButton("Cancel");

            ok.addActionListener(evt -> {
                currentOrder = new Ordine(nome.getText(), tavolo.getText());
                sendOrder.setEnabled(true);
                popup.dispose();
            });

            cancel.addActionListener(evt -> popup.dispose());

            temp.add(nome);
            temp.add(tavolo);
            temp.add(ok);
            temp.add(cancel);

            popup.add(temp);
            popup.pack();
            popup.setVisible(true);
        });

        lasty++;
        c.gridx = 0;
        c.gridy = lasty;
        add(sendOrder, c);
        c.gridx = 1;
        c.gridy = lasty;
        add(newOrder, c);

        int MAX_CONN = 1;
        while (true) {
            try {
                server.setSoTimeout(1000);
                Socket newSocket = server.accept();
                clients.add(newSocket);
                System.out.println("Adding new connection with " + newSocket.getInetAddress() + ", port: " + newSocket.getLocalPort());

                if(clients.size() == MAX_CONN) break;
            } catch (SocketTimeoutException e) {
            } catch (IOException e) {
                System.out.println("Connection refused!");
            }
        }


        changeFont(this, new Font("Verdana", Font.BOLD, 20));

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

    public void send(String str, ArrayList<Socket> clients) {

        for (Socket client : clients) {

            try {
                OutputStream out = client.getOutputStream();

                out.write(str.getBytes(), 0, str.length());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

        new OrderPanel();
    }

    public static void changeFont ( Component component, Font font ) {
        component.setFont ( font );
        if ( component instanceof Container )
        {
            for ( Component child : ( ( Container ) component ).getComponents () )
            {
                changeFont ( child, font );
            }
        }
    }
}