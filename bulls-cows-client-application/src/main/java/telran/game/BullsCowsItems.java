package telran.game;

import java.time.LocalDate;
import java.util.List;

import telran.view.*;

public class BullsCowsItems {
    static BullsCowsService service;
    static String loginUsername = "anonimus";

    public static Item[] getItems(BullsCowsService service) {
		BullsCowsItems.service = service;
		Item[] items = {
				Item.of("Register", BullsCowsItems::register),
				Item.of("Login", BullsCowsItems::login),
				Item.of("Create game", BullsCowsItems::createGame),
                Item.of("Join game", BullsCowsItems::joinGame),
				Item.of("Start game", BullsCowsItems::startGame)
				//Item.of("Play game", BullsCowsItems::playGame),
		};
		return items;
	}

    static void register(InputOutput io) {
		String username = io.readString("Enter username");
        String birthdateStr = io.readString("Enter birthdate");
		service.register(username, LocalDate.parse(birthdateStr));
		io.writeLine("Gamer %s is registered on server".formatted(username));
	}

    static void login(InputOutput io) {
		String username = io.readString("Enter username");
		service.login(username);
        loginUsername = username;
		io.writeLine("Gamer %s login to the server".formatted(username));
	}

    static void createGame(InputOutput io) {
        long gameId = service.createGame();
        io.writeLine("Game %d is created".formatted(gameId));
    }

    static void joinGame(InputOutput io) {
        List<Long> games = service.getListJoinebleGames(loginUsername);
        if (games.isEmpty()) {
            io.writeLine("There are no games to join");
        } else {
            io.writeString("Games wich gamer could join: %s".formatted(games.toString()));
            long gameId = io.readLong("Enter game id", "Wrong game id");
            service.joinToGame(loginUsername, gameId);
            io.writeLine("Gamer %s joined to game %d".formatted(loginUsername, gameId));
        }
    }

    static void startGame(InputOutput io) {
        List<Long> games = service.getListStartebleGames(loginUsername);
        io.writeString("Games wich could be started: %s".formatted(games.toString()));
        if (games.isEmpty()) {
            io.writeLine("There are no games to start");
        } else {
            long gameId = io.readLong("Enter game id", "Wrong game id");
            service.startGame(loginUsername, gameId);
            io.writeLine("Game %d is started".formatted(gameId));
        }
    }
}
