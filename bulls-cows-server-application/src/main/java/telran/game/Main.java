package telran.game;

import telran.net.*;
import static telran.game.ServerConfigProperties.*;
import java.util.Scanner;;

public class Main {
    public static void main(String[] args) {
        BullsCowsService service = new BullsCowsServiceImpl();
        Protocol protocol = new ProtocolBullsCows(service);
        TcpServer server = new TcpServer(protocol, PORT, BAD_RESPONSES, REQUEST_PER_SECOND, TOTAL_TIMEOUT, N_THREADS);
        Thread threadTcpServer = new Thread(server);
        threadTcpServer.start();
        Scanner scanner = new Scanner(System.in); 
        while (true) {
            System.out.print("To shutdown server input \"shutdown\":");
            String command = scanner.nextLine();
            if (command.equals("shutdown")) {
                server.shutdown();
                break;
            }
        }
        scanner.close();
    }
}