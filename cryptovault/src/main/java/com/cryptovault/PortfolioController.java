package com.cryptovault;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PortfolioController {

    private final AssetDAO assetDAO;
    private final PortfolioDAO portfolioDAO;

    public PortfolioController(AssetDAO assetDAO, PortfolioDAO portfolioDAO) {
        this.assetDAO = assetDAO;
        this.portfolioDAO = portfolioDAO;
    }

    @GetMapping("/")
    public String redirectHome() {
        return "redirect:/portfolio?id=1";
    }

    @GetMapping("/portfolio")
    public String showPortfolio(@RequestParam(name = "id", defaultValue = "1") Integer portfolioId, Model model) {
        
        // 1. Fetch Dynamic Nav
        model.addAttribute("portfolios", portfolioDAO.getByUser(1));

        // 2. Fetch Assets and Calculate Value
        List<Asset> assets = new ArrayList<>();
        double totalValue = 0.0;
        try {
            assets = assetDAO.getByPortfolioId(portfolioId);
            for (Asset a : assets) {
                double price = (a.getCurrentPrice() > 0) ? a.getCurrentPrice() : a.getAveragePurchasePrice();
                totalValue += a.getQuantity() * price;
            }
        } catch (Exception e) { e.printStackTrace(); }

        // 3. Line Chart Labels & Values
        List<String> chartLabels = new ArrayList<>();
        List<Double> chartValues = new ArrayList<>();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd");

        for (int i = 6; i >= 0; i--) {
            chartLabels.add(today.minusDays(i).format(formatter));
            if (totalValue > 0) {
                chartValues.add(totalValue * (0.97 + (Math.random() * 0.06)));
            } else {
                chartValues.add(0.0);
            }
        }

        // 4. Pie Chart Data
        List<String> pieLabels = new ArrayList<>();
        List<Double> pieValues = new ArrayList<>();
        for (Asset a : assets) {
            pieLabels.add(a.getSymbol());
            pieValues.add(a.getQuantity() * a.getAveragePurchasePrice());
        }

        // 5. Send to Model
        model.addAttribute("assets", assets);
        model.addAttribute("portfolioId", portfolioId);
        model.addAttribute("message", "CryptoVault Terminal");
        model.addAttribute("totalValue", String.format("%.2f", totalValue));
        model.addAttribute("chartLabels", chartLabels);
        model.addAttribute("chartValues", chartValues);
        model.addAttribute("pieLabels", pieLabels);
        model.addAttribute("pieValues", pieValues);

        return "home";
    }

    @PostMapping("/executeTrade")
    public String executeTrade(@RequestParam(name = "portfolioId") Integer portfolioId,
                               @RequestParam(name = "symbol") String symbol,
                               @RequestParam(name = "type") String type,
                               @RequestParam(name = "quantity") double quantity,
                               @RequestParam(name = "price") double price) {
        try {
            double finalQty = type.equalsIgnoreCase("SELL") ? -quantity : quantity;
            assetDAO.addOrUpdateAsset(portfolioId, symbol, "Digital Asset", finalQty, price, "CRYPTO");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/portfolio?id=" + portfolioId;
    }
}