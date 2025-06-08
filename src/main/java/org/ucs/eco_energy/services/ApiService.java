package org.ucs.eco_energy.services;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ucs.eco_energy.models.Device;


public class ApiService {

    private String apiKey = "AIzaSyALz2t0NF2Jrr2-EiuuDYB_ZPU7-cowdR0";
    private static final String MODEL_ID = "gemini-2.0-flash";

    public String GetTip(List<Device> devices) {
        if (devices == null || devices.isEmpty()) {
            return "Nenhum dispositivo fornecido para análise.";
        }

        StringBuilder promptBuilder = new StringBuilder("Com base nas seguintes informações dos dispositivos, forneça uma dica útil (de um jeito engraçado e leve) em uma frase curta:\n\n");

        for (Device device : devices) {
            promptBuilder.append("Nome do dispositivo: ").append(device.getName()).append("\n");
            promptBuilder.append("Gasto estimado do dispositivo no último mês: ").append(device.getPower() + "kwH - " + "Tempo de uso por dia: " +device.getTimeUse() ).append("\n");
            promptBuilder.append("\n");
        }

        String prompt = promptBuilder.toString();

        System.out.println("Prompt gerado para a API (HTTP sem lib externa - curl): \n" + prompt);

        try {
            return SendPromptHttp(prompt);
        } catch (IOException e) {
            System.err.println("Erro ao chamar a API Gemini (HTTP sem lib externa - curl): " + e.getMessage());
            return "Ocorreu um erro ao obter a dica (HTTP sem lib externa - curl).";
        }
    }

    private String SendPromptHttp(String prompt) throws IOException {
        if (apiKey == null || apiKey.isEmpty()) {
            return "Chave de API não configurada.";
        }

        String endpointUrl = String.format(
                "https://generativelanguage.googleapis.com/v1beta/models/%s:generateContent?key=%s",
                MODEL_ID, apiKey
        );

        String requestBody = String.format(
                "{\"contents\": [{\"parts\": [{\"text\": \"%s\"}]}]}",
                prompt.replace("\"", "\\\"")
        );

        try {
            URL url = new URI(endpointUrl).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = requestBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            } catch (IOException e) {
                StringBuilder errorResponse = new StringBuilder();
                try (BufferedReader errBr = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "utf-8"))) {
                    String errorLine = null;
                    while ((errorLine = errBr.readLine()) != null) {
                        errorResponse.append(errorLine.trim());
                    }
                } catch (Exception ignored) {
                }
                System.err.println("Erro na requisição HTTP para a API: Status Code = " + connection.getResponseCode() + ", Mensagem: " + errorResponse.toString());
                return String.format("Erro ao conectar com a API Gemini (HTTP sem lib externa - curl). Status: %d, Mensagem: %s",
                        connection.getResponseCode(), errorResponse.toString());
            }

            return TratarResposta(response.toString());

        } catch (Exception e) {
            System.err.println("Erro ao enviar requisição HTTP: " + e.getMessage());
            throw new IOException("Erro ao enviar requisição HTTP", e);
        }
    }

    public String TratarResposta(String jsonResponse) {
        if (jsonResponse == null || jsonResponse.isEmpty()) {
            return "Resposta da API vazia.";
        }

        Pattern pattern = Pattern.compile("\"text\":\\s*\"([^\"]*)\"");
        Matcher matcher = pattern.matcher(jsonResponse);

        StringBuilder dicaBuilder = new StringBuilder();
        while (matcher.find()) {
            dicaBuilder.append(matcher.group(1)).append("\n");
        }

        if (dicaBuilder.length() > 0) {
            return dicaBuilder.toString().trim();
        } else {
            return "Nenhuma dica encontrada na resposta.";
        }
    }
}