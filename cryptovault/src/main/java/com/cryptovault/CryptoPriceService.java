package com.cryptovault;

import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CryptoPriceService {

    private final AssetDAO assetDAO;
    private final RestTemplate restTemplate;

    public CryptoPriceService(AssetDAO assetDAO) {
        this.assetDAO = assetDAO;
        this.restTemplate = new RestTemplate();
    }

    // Runs automatically every 60,000 milliseconds (60 seconds)
    @Scheduled(fixedRate = 60000)
    public void syncLivePrices() {
        try {
            // CoinGecko public simple price endpoint for our main assets
            String url = "https://api.coingecko.com/api/v3/simple/price?ids=bitcoin,solana,ethereum,dogecoin,binancecoin&vs_currencies=usd";
            
            // Fetch raw JSON and map it cleanly
            @SuppressWarnings("unchecked")
            Map<String, Map<String, Object>> response = restTemplate.getForObject(url, Map.class);

            if (response != null) {
                // Safely extract the number and convert to primitive double to match AssetDAO perfectly
                if (response.containsKey("bitcoin")) {
                    assetDAO.updateAssetPrice("BTC", ((Number) response.get("bitcoin").get("usd")).doubleValue());
                }
                if (response.containsKey("solana")) {
                    assetDAO.updateAssetPrice("SOL", ((Number) response.get("solana").get("usd")).doubleValue());
                }
                if (response.containsKey("ethereum")) {
                    assetDAO.updateAssetPrice("ETH", ((Number) response.get("ethereum").get("usd")).doubleValue());
                }
                if (response.containsKey("dogecoin")) {
                    assetDAO.updateAssetPrice("DOGE", ((Number) response.get("dogecoin").get("usd")).doubleValue());
                }
                if (response.containsKey("binancecoin")) {
                    assetDAO.updateAssetPrice("BNB", ((Number) response.get("binancecoin").get("usd")).doubleValue());
                }

                System.out.println("[MARKET SYNC] Live prices updated successfully from CoinGecko.");
            }
        } catch (Exception e) {
            System.err.println("[MARKET SYNC ERROR] Failed to fetch live rates. Retrying in 60s... (" + e.getMessage() + ")");
        }
    }
}