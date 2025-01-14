package telran.game;

import java.time.LocalDate;
import java.util.*;
import org.json.*;
import telran.net.NetworkClient;

public class BullsCowsNetProxy implements BullsCowsService {
    NetworkClient client;

    public BullsCowsNetProxy(NetworkClient client) {
        this.client = client;
    }

    @Override
    public long createGame() {
        return Long.parseLong(client.sendAndReceive("createGame", ""));
    }

    @Override
    public List<Long> getListJoinebleGames(String username) {
        String res = client.sendAndReceive("getListJoinebleGames", username);
        JSONArray jsonArray = new JSONArray(res);
        return jsonArray.toList().stream().map(o -> Long.parseLong(o.toString())).toList();
    }

    @Override
    public List<Long> getListStartebleGames(String username) {
        String res = client.sendAndReceive("getListStartebleGames", username);
        JSONArray jsonArray = new JSONArray(res);
        return jsonArray.toList().stream().map(o -> Long.parseLong(o.toString())).toList();
    }

    @Override
    public void joinToGame(String username, long gameId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        jsonObject.put("gameId", gameId);
        client.sendAndReceive("joinToGame", jsonObject.toString());
    }

    @Override
    public void login(String username) {
        client.sendAndReceive("login", username);
    }

    @Override
    public List<MoveResult> makeMove(String username, long gameId, String sequece) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        jsonObject.put("gameId", gameId);
        jsonObject.put("sequece", sequece);
        String res = client.sendAndReceive("makeMove", jsonObject.toString());
        JSONArray jsonArray = new JSONArray(res);
        JSONObject[] jsonObjects = jsonArray.toList().toArray(JSONObject[]::new);
        return Arrays.stream(jsonObjects)
                .map(j -> new MoveResult(j.getString("sequece"), Integer.parseInt(j.getString("bulls")), Integer.parseInt(j.getString("cows"))))
                        .toList();
    }

    @Override
    public void register(String username, LocalDate birthdate) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        jsonObject.put("birthdate", birthdate);
        client.sendAndReceive("register", jsonObject.toString());
    }

    @Override
    public void startGame(String username, long gameId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        jsonObject.put("gameId", gameId);
        client.sendAndReceive("startGame", jsonObject.toString());
    }

}
