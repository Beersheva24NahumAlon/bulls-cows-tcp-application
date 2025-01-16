package telran.game;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;

import telran.net.*;
import telran.view.*;

public class Main { 

    public static void main(String[] args) {
        InputOutput io = new StandardInputOutput();
        String host = args.length == 1 ? args[0] : "localhost"; 
        NetworkClient client = new TcpClient(host, 5000);
        BullsCowsService game = new BullsCowsNetProxy(client);
        Item[] items = BullsCowsItems.getItems(game);
        items = addExitItem(items, client);
        Menu menu = new Menu("Bulls and Cows game", items);
        menu.perform(io);
        io.writeLine("Application is finished");
    }

    private static Item[] addExitItem(Item[] items, NetworkClient client) {
        Item[] res = Arrays.copyOf(items, items.length + 1);
        res[res.length - 1] = Item.of("Exit", io -> {
            try {
                if (client instanceof Closeable closeable) {
                    closeable.close();
                }
                io.writeString("Session closed correctly");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, true);
        return res;
    }
}