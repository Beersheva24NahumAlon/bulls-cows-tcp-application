package telran.game;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import telran.net.*;

@SuppressWarnings("unused")
public class ProtocolBullsCows implements Protocol {
    BullsCowsService service;

    public ProtocolBullsCows(BullsCowsService service) {
        this.service = service;
    }

    @Override
    public Response getResponse(Request request) {
        String type = request.requestType();
        String data = request.requestData();
        Response response = null;
        try {
            Method method = ProtocolBullsCows.class.getDeclaredMethod(type, String.class);
            method.setAccessible(true);
            response = (Response) method.invoke(this, data);
        } catch (NoSuchMethodException e) {
            response = new Response(ResponseCode.WRONG_TYPE, "Wrong type of command to server");
        } catch (InvocationTargetException e) {
            Throwable causeExc = e.getCause();
            String msg = causeExc == null ? e.getMessage() : causeExc.getMessage();
            response = new Response(ResponseCode.WRONG_DATA, msg);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    
    private Response register(String data) {
        JSONObject jsonObject = new JSONObject(data);
        String username = jsonObject.getString("username");
        LocalDate birthdate = LocalDate.parse(jsonObject.getString("birthdate"));
        service.register(username, birthdate);
        return new Response(ResponseCode.OK, "");
    }

    private Response login(String data) {
        String username = data;
        service.login(username);
        return new Response(ResponseCode.OK, "");
    }

    private Response createGame(String data) {
        long gameId = service.createGame();
        return new Response(ResponseCode.OK, String.valueOf(gameId));
    }

    private Response joinToGame(String data) {
        JSONObject jsonObject = new JSONObject(data);
        String username = jsonObject.getString("username");
        long gameId = jsonObject.getLong("gameId");
        service.joinToGame(username, gameId);
        return new Response(ResponseCode.OK, "");
    }

    private Response getListJoinebleGames(String data) {
        String username = data;
        List<Long> games = service.getListJoinebleGames(username);
        JSONArray jsonArray = new JSONArray(games);
        return new Response(ResponseCode.OK, jsonArray.toString());
    }

    private Response startGame(String data) {
        JSONObject jsonObject = new JSONObject(data);
        String username = jsonObject.getString("username");
        long gameId = jsonObject.getLong("gameId");
        service.startGame(username, gameId);
        return new Response(ResponseCode.OK, "");
    }

    private Response getListStartebleGames(String data) {
        String username = data;
        List<Long> games = service.getListStartebleGames(username);
        JSONArray jsonArray = new JSONArray(games);
        return new Response(ResponseCode.OK, jsonArray.toString());
    }
}
