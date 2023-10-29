package theatricalplays;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StatementPrinter {

    public String print(Invoice invoice, Map<String, Play> plays) {
        int totalAmount = 0;
        int volumeCredits = 0;
        StringBuilder result = new StringBuilder();
        result.append("Statement for ").append(invoice.getCustomer()).append("\n");

        NumberFormat frmt = NumberFormat.getCurrencyInstance(Locale.US);

        for (Performance perf : invoice.getPerformances()) {
            Play play = plays.get(perf.getPlayID());
            int thisAmount = calculateAmount(play.getType(), perf.getAudience());

            // add volume credits
            volumeCredits += Math.max(perf.getAudience() - 30, 0);
            // add extra credit for every ten comedy attendees
            if ("comedy".equals(play.getType())) volumeCredits += Math.floor(perf.getAudience() / 5);

            // print line for this order
            result.append("  ").append(play.getName()).append(": ").append(frmt.format(thisAmount / 100))
                    .append(" (").append(perf.getAudience()).append(" seats)\n");
            totalAmount += thisAmount;
        }
        result.append("Amount owed is ").append(frmt.format(totalAmount / 100)).append("\n");
        result.append("You earned ").append(volumeCredits).append(" credits\n");

        return result.toString();
    }

    private int calculateAmount(String playType, int audience) {
        int baseAmount = 0;
        switch (playType) {
            case "tragedy":
                baseAmount = 40000;
                if (audience > 30) {
                    baseAmount += 1000 * (audience - 30);
                }
                break;
            case "comedy":
                baseAmount = 30000;
                if (audience > 20) {
                    baseAmount += 10000 + 500 * (audience - 20);
                }
                baseAmount += 300 * audience;
                break;
            default:
                throw new Error("unknown type: " + playType);
        }
        return baseAmount;
    }
}
