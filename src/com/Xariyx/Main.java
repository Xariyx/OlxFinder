package com.Xariyx;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);

        String lookout = howdy(scanner);
        int minPrice = setMinimalPrice(scanner);
        int maxPrice = setMaximalPrice(scanner, minPrice);

        long startTime = System.currentTimeMillis();

        ArrayList<Offer> offers = new ArrayList<>();

        for (int page = 1; page <= 25; page++) {


            String url = "https://www.olx.pl/oferty" +
                    "/q-" + lookout.replace(" ", "-") +
                    "/?search%5Bfilter_float_price%3Afrom%5D=" + minPrice +
                    "&search%5Bfilter_float_price%3Ato%5D=" + maxPrice +
                    "/?page=" + page;
            Document doc = Jsoup.connect(url).get();
            Elements elementOffers = doc.getElementsByClass("wrap");

            if (page == 1) {
                offers.add(new Offer(elementOffers.first()));
            }

            for (Element elementOffer : elementOffers) {
                Offer newOffer = new Offer(elementOffer);
                boolean isAlreadyIn = false;
                for (Offer offer : offers) {
                    if (newOffer.getUrl().equalsIgnoreCase(offer.getUrl())) {
                        isAlreadyIn = true;
                        break;
                    }
                }

                if (newOffer.getPrice() < minPrice) {
                    continue;
                }

                if (newOffer.getPrice() > maxPrice) {
                    continue;
                }

                if (!isAlreadyIn) {
                    offers.add(newOffer);
                }


            }

        }

        sortOffersByPrice(offers);

        int x = 0;
        for (Offer offer : offers) {

            x++;
            System.out.println("Title: " + offer.getTitle());
            System.out.println("Link: " + offer.getUrl());
            System.out.println("Price: " + offer.getPrice());
            System.out.println("Location: " + offer.getLocation());
            System.out.println("Date: " + offer.getDate());
            System.out.println(" ");
            System.out.println("===============================================");
            System.out.println(" ");
        }


        long endTime = System.currentTimeMillis();
        System.out.println("Took " + (endTime - startTime) + " ms");
        System.out.println("Listed " + x + " offers");

    }

    private static void sortOffersByPrice(ArrayList<Offer> offers) {


        boolean sorted = false;
        Offer temp;
        while (!sorted) {
            sorted = true;
            for (int i = 0; i < offers.size() - 1; i++) {
                if (offers.get(i).getPrice() < offers.get(i + 1).getPrice()) {
                    temp = offers.get(i);
                    offers.set(i, offers.get(i + 1));
                    offers.set(i + 1, temp);
                    sorted = false;
                }
            }
        }
    }

    private static String howdy(Scanner scanner) {
        System.out.println("Welcome in OLX Onion Finder!");
        System.out.println("Offers will be listed from cheapest");
        System.out.println(" ");
        System.out.println("Write what are you looking for: ");
        return scanner.nextLine();

    }

    private static int setMinimalPrice(Scanner scanner) {
        System.out.println("Write minimal price that you're looking for: ");
        String minimalPrice = scanner.nextLine();
        int minPrice;
        try {
            minPrice = Integer.parseInt(minimalPrice);
        } catch (NumberFormatException exception) {
            System.out.println("Write number, not string!");

            return setMinimalPrice(scanner);
        }

        return minPrice;
    }

    private static int setMaximalPrice(Scanner scanner, int minPrice) {
        System.out.println("Write max price that you're looking for: ");
        String maximalPrice = scanner.nextLine();
        int maxPrice;
        try {
            maxPrice = Integer.parseInt(maximalPrice);
        } catch (NumberFormatException exception) {
            System.out.println("Write number, not string!");
            return setMaximalPrice(scanner, minPrice);
        }

        if (maxPrice < minPrice) {
            System.out.println("Maximal price can't be lower than minimal!");
            return setMaximalPrice(scanner, minPrice);
        }
        return maxPrice;
    }

}
