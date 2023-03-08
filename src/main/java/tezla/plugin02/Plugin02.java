package tezla.plugin02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public final class Plugin02 extends JavaPlugin implements CommandExecutor {

    ConsoleCommandSender mycmd = Bukkit.getConsoleSender();

    @Override
    public void onEnable() {
        mycmd.sendMessage("§bEl plugin ha sido iniciado");

        // Registramos nuestro comando
        getCommand("chatgpt").setExecutor(this);
    }

    @Override
    public void onDisable() {
        mycmd.sendMessage("§cEl plugin ha sido desactivado");
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        JSONParser parser = new JSONParser();
        if (cmd.getName().equalsIgnoreCase("chatgpt")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                String palabras = String.join(" ", args);

                // Hacemos la solicitud a la API
                try {
                    URL url = new URL("https://api.openai.com/v1/completions");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("Authorization", "Bearer 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");
                    conn.setDoOutput(true);

                    String jsonInputString = "{\"model\":\"text-davinci-003\",\"prompt\":\"" + palabras + "\",\"max_tokens\":30,\"temperature\":0}";


                    try (OutputStream os = conn.getOutputStream()) {
                        byte[] input = jsonInputString.getBytes("utf-8");
                        os.write(input, 0, input.length);
                    }

                    try (BufferedReader br = new BufferedReader(
                            new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                        StringBuilder response = new StringBuilder();
                        String responseLine = null;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                        System.out.println(response.toString());
                        Object obj = parser.parse(response.toString());
                        JSONObject json = (JSONObject) obj;
                        JSONArray choices = (JSONArray) json.get("choices");
                        JSONObject choice = (JSONObject) choices.get(0);
                        String text = (String) choice.get("text");

// Mostramos el valor de "text" en el chat
                        player.sendMessage("§a"+palabras+"  §f"+text);
                    }


                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
            } else {
                sender.sendMessage("Este comando solo se puede ejecutar desde el juego.");
            }
        }
        return true;
    }
}
