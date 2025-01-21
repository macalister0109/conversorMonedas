package cl.mjamett;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.Scanner;

public class Conversor {
    private static final String API_KEY = "3685354afd78d0f5a1a576fe";
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);


        System.out.print("Ingresa la moneda de origen (por ejemplo, USD): ");
        String fromCurrency = scanner.nextLine().toUpperCase();

        System.out.print("Ingresa la moneda de destino (por ejemplo, EUR): ");
        String toCurrency = scanner.nextLine().toUpperCase();

        System.out.print("Ingresa la cantidad a convertir: ");
        double amount = scanner.nextDouble();


        try {
            double convertedAmount = convertCurrency(fromCurrency, toCurrency, amount);
            System.out.printf("%.2f %s equivale a %.2f %s%n",
                    amount, fromCurrency, convertedAmount, toCurrency);
        } catch (Exception e) {
            System.out.println("Error al convertir moneda: " + e.getMessage());
        }

        scanner.close();
    }

    public static double convertCurrency(String fromCurrency, String toCurrency, double amount) throws Exception {
        // Construir la URL de la API
        String url = BASE_URL + API_KEY + "/latest/" + fromCurrency;


        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


        JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();


        if (!jsonResponse.has("conversion_rates")) {
            throw new Exception("Respuesta inválida de la API: " + jsonResponse);
        }


        JsonObject conversionRates = jsonResponse.getAsJsonObject("conversion_rates");
        if (!conversionRates.has(toCurrency)) {
            throw new Exception("Moneda de destino no soportada.");
        }

        double rate = conversionRates.get(toCurrency).getAsDouble();


        return amount * rate;
    }
}


