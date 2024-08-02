import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TCPClient {
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    public void start(String serverIp, int serverPort) throws IOException {
        socket = new Socket(serverIp, serverPort);
        input = new DataInputStream(socket.getInputStream());
        output = new DataOutputStream(socket.getOutputStream());

        System.out.println("[C1] Conectado ao servidor " + serverIp + ":" + serverPort);

        Thread sendThread = new Thread(new SendHandler());
        Thread receiveThread = new Thread(new ReceiveHandler());

        sendThread.start();
        receiveThread.start();

        try {
            sendThread.join();
            receiveThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String serverIp = "localhost";
        int serverPort = 6666;
        try {
            TCPClient client = new TCPClient();
            client.start(serverIp, serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class SendHandler implements Runnable {
        @Override
        public void run() {
            Scanner scanner = new Scanner(System.in);
            try {
                while (true) {
                    String message = scanner.nextLine();
                    output.writeUTF(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ReceiveHandler implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    String message = input.readUTF();
                    System.out.println("[C2] Mensagem recebida: " + message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
