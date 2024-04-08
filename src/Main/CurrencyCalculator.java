package Main;

import java.util.Map;
import GUI.GUI;
import Utils.Utils;
import Utils.Data.Calculations;
import Utils.Data.ExchangeRateFetcher;
import javax.swing.JComboBox;

public class CurrencyCalculator {

    public static void main(String[] args) {
        GUI.drawGUI();

        // Erstellt eine Dropdown Liste der Currency 
        // spezifisch fuer einen String -> vermeidet somit Fehler
        JComboBox<String> dropdown = new JComboBox<>();

        for (Map.Entry<String, String> currency : Utils.getAllCurrencies()) {
         String isoCode = currency.getKey();
         String currencyName = currency.getValue();

         dropdown.addItem(currencyName + " (" + isoCode + ")");
         }

        // TODO: für @Jonas

        // Variablen brauchen noch einen User input
        // am besten nochmal gemeinsam klaeren, wie wir das durchsetzen
        String baseCurrency = "USD";
        String targetCurrency = "EUR";
        double amount = 10.00;
        
         Calculations.convertCurrencies(baseCurrency, targetCurrency, amount);
         ExchangeRateFetcher.getLastFetchTime();
    }
}