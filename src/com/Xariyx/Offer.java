package com.Xariyx;

import org.jsoup.nodes.Element;

public class Offer {

    private float price;
    private String title;
    private String location;
    private String date;
    private String url;


    public Offer(Element element) {

        getOfferInfo(element);


    }

    private void getOfferInfo(Element element) {
        float price;
        String title;
        String location;
        String date;
        String[] url;

        title = element
                .getElementsByClass("link").first()
                .getElementsByTag("strong").first()
                .html();

        url = element
                .getElementsByClass("link").first()
                .attr("href").split("\\.html");



        String tempPrice = element
                .getElementsByClass("price").first()
                .getElementsByTag("strong").first()
                .html()
                .replace(" zł", "")
                .replace(',', '.')
                .replace(" ","");

        if(tempPrice.equalsIgnoreCase("zamienię")){
            price = -1;
        }
        else if(tempPrice.equalsIgnoreCase("za darmo")){
            price = 0;

        }
        else {
            try {
                price = Float.parseFloat(tempPrice);
            } catch (NumberFormatException exception) {
                System.out.println("Exception at " + url[0]+".html");
                price = 0;
            }
        }

        location = element
                .getElementsByClass("breadcrumb x-normal").get(1)
                .getElementsByTag("span").first()
                .text();

        date = element
                .getElementsByClass("breadcrumb x-normal").get(2)
                .getElementsByTag("span").first()
                .text();



        this.url = url[0]+".html";
        this.price = price;
        this.title = title;
        this.location = location;
        this.date = date;

    }

    public float getPrice() {
        return price;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }

}
