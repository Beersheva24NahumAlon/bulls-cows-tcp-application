package telran.game;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import telran.view.*;

public class BullsCowsItems {
    static BullsCowsService service;
    static String currentGamer = "anonimus";
    static long currentGame = -1;

    public static Item[] getItems(BullsCowsService service) {
        BullsCowsItems.service = service;
        Item[] items = {
                Item.of("Register", BullsCowsItems::register),
                Item.of("Login", BullsCowsItems::login),
                Item.of("Create game", BullsCowsItems::createGame),
                Item.of("Join game", BullsCowsItems::joinGame),
                Item.of("Start game", BullsCowsItems::startGame),
                Item.of("Play game", BullsCowsItems::playGame)
        };
        return items;
    }

    static void register(InputOutput io) {
        String username = io.readString("Enter username:");
        String birthdateStr = io.readString("Enter birthdate:");
        service.register(username, LocalDate.parse(birthdateStr));
        io.writeLine("Gamer %s is registered on server.".formatted(username));
    }

    static void login(InputOutput io) {
        String username = io.readString("Enter username:");
        service.login(username);
        currentGamer = username;
        io.writeLine("Gamer %s login to the server.".formatted(username));
    }

    static void createGame(InputOutput io) {
        long gameId = service.createGame();
        io.writeLine("Game %d is created.".formatted(gameId));
    }

    static void joinGame(InputOutput io) {
        List<Long> games = service.getListJoinebleGames(currentGamer);
        if (games.isEmpty()) {
            io.writeLine("There are no games to join.");
        } else {
            io.writeString("Games you can join: %s".formatted(games.toString()));
            long gameId = Long.parseLong(io.readStringOptions("Enter game id:", "Wrong game id", longListToOptions(games)));
            service.joinToGame(currentGamer, gameId);
            io.writeLine("Gamer %s joined to game %d.".formatted(currentGamer, gameId));
        }
    }

    static void startGame(InputOutput io) {
        List<Long> games = service.getListStartebleGames(currentGamer);
        if (games.isEmpty()) {
            io.writeLine("There are no games to start.");
        } else {
            io.writeString("Games you can start: %s".formatted(games.toString()));
            long gameId = Long.parseLong(io.readStringOptions("Enter game id:", "Wrong game id", longListToOptions(games)));
            service.startGame(currentGamer, gameId);
            io.writeLine("Game %d is started.".formatted(gameId));
        }
    }

    static void playGame(InputOutput io) {
        List<Long> games = service.getListPlaybleGames(currentGamer);
        if (games.isEmpty()) {
            io.writeLine("There are no games to play.");
        } else {
            io.writeString("Games you can play: %s".formatted(games.toString()));
            //long gameId = io.readLong("Enter game id:", "Wrong game id");
            long gameId = Long.parseLong(io.readStringOptions("Enter game id:", "Wrong game id", longListToOptions(games)));
            currentGame = gameId;
            Menu menu = new Menu("Playing game", getPlayGameItems());
            menu.perform(io);
        }
    }

    private static HashSet<String> longListToOptions(List<Long> list) {
        return new HashSet<>(list.stream().map(l -> l.toString()).toList());
    }

    static Item[] getPlayGameItems() {
        Item[] items = {
            Item.of("Make a move", BullsCowsItems::makeMove),
            Item.of("Exit the game", BullsCowsItems::exitGame, true)
        };
        return items;
    }

    static void makeMove(InputOutput io) {
        String sequence = io.readString("Enter sequence:");
        //checkSequence
        List<MoveResult> resList = service.makeMove(currentGamer, currentGame, sequence);
        io.writeString("Sequence Bulls Cows");
        resList.stream().forEach(r -> {
            io.writeString("%8s %4d  %4d".formatted(r.sequence(), r.bulls(), r.cows()));
            if (r.bulls() == 4) {
                io.writeString("You are winner. Congratulations");
            }
        });
    }

    static void exitGame(InputOutput io) {
        currentGame = -1;
    }
}
