package tezla.plugin02;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public final class Plugin02 extends JavaPlugin implements CommandExecutor {

    ConsoleCommandSender mycmd = Bukkit.getConsoleSender();

    @Override
    public void onEnable() {
        mycmd.sendMessage("§bEl plugin ha sido iniciado");

        // Registramos nuestro comando
        getCommand("saludar").setExecutor(this);
    }

    @Override
    public void onDisable() {
        mycmd.sendMessage("§cEl plugin ha sido desactivado");
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        JSONParser parser = new JSONParser();
        if (cmd.getName().equalsIgnoreCase("saludar")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                // Hacemos la solicitud a la API
                try {
                    URL url = new URL("https://jsonplaceholder.typicode.com/todos/1");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();

                    // Leemos la respuesta de la API
                    Scanner scanner = new Scanner(url.openStream());
                    String response = scanner.useDelimiter("\\Z").next();
                    scanner.close();

                    // Parseamos el JSON y obtenemos el valor del campo "title"
                    Object obj = parser.parse(response);
                    JSONObject json = (JSONObject) obj;
                    String title = (String) json.get("title");

                    // Mostramos el valor del campo "title" en el chat
                    player.sendMessage(title);
                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
            } else {
                sender.sendMessage("Este comando solo se puede ejecutar desde el juego.");
            }
            return true;
        }
        return false;
    }}