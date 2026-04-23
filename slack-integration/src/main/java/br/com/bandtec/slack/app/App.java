package br.com.bandtec.slack.app;

import br.com.bandtec.slack.config.Slack;
import java.io.IOException;
import org.json.JSONObject;

/**
 *
 * @author Diego Brito <diego.lima@bandtec.com.br>
 */
public class App {

    public static void main(String[] args) throws IOException, InterruptedException {
        
        JSONObject json = new JSONObject();
        
        json.put("text", "ALERTA! :siren:\nUnidade Jardins registrou consumo de rede acima de 85%!");
        
        Slack.sendMessage(json);
    }
}
