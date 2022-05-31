package com.example.geocoder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class WeatherResult implements Serializable {
    @Expose
    @SerializedName("response")
    private Response response;

    public WeatherResult(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }

    @Override
    public String toString() {
        return "WeatherResult{" +
                "response=" + response +
                '}';
    }

    public static class Response implements Serializable {
        @Expose
        @SerializedName("body")
        private body body;

        @Expose
        @SerializedName("header")
        private Header header;

        public body getBody() {
            return body;
        }

        public Header getHeader() {
            return header;
        }

        @Override
        public String toString() {
            return "Response{" +
                    "body=" + body +
                    ", header=" + header +
                    '}';
        }
    }

    public static class body implements Serializable {
        @Expose
        @SerializedName("totalCount")
        private int totalcount;

        @Expose
        @SerializedName("numOfRows")
        private int numofrows;

        @Expose
        @SerializedName("pageNo")
        private int pageno;

        @Expose
        @SerializedName("items")
        private items items;

        @Expose
        @SerializedName("dataType")
        private String dataType;

        public int getTotalcount() {
            return totalcount;
        }

        public int getNumofrows() {
            return numofrows;
        }

        public int getPageno() {
            return pageno;
        }

        public items getItems() {
            return items;
        }

        public String getDataType() {
            return dataType;
        }

        @Override
        public String toString() {
            return "Body{" +
                    "totalcount=" + totalcount +
                    ", numofrows=" + numofrows +
                    ", pageno=" + pageno +
                    ", items=" + items +
                    ", dataType=" + dataType +
                    '}';
        }
    }

    public static class items implements Serializable {
        @Expose
        @SerializedName("item")
        private ArrayList<Item> item;

        public ArrayList<Item> getItem() {
            return item;
        }

        @Override
        public String toString() {
            return "items{" +
                    "item=" + item +
                    '}';
        }
    }

    public static class Item implements Serializable {
        @Expose
        @SerializedName("ny")
        private int ny;

        @Expose
        @SerializedName("nx")
        private int nx;

        @Expose
        @SerializedName("obsrValue")
        private String obsrValue;

        @Expose
        @SerializedName("category")
        private String category;

        @Expose
        @SerializedName("baseTime")
        private int basetime;

        @Expose
        @SerializedName("baseDate")
        private int baseDate;

        public int getNy() {
            return ny;
        }

        public int getNx() {
            return nx;
        }

        public String getObsrValue() {
            return obsrValue;
        }

        public String getCategory() {
            return category;
        }

        public int getBasetime() {
            return basetime;
        }

        public int getBaseDate() {
            return baseDate;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "ny=" + ny +
                    ", nx=" + nx +
                    ", obsrValue='" + obsrValue + '\'' +
                    ", category='" + category + '\'' +
                    ", basetime=" + basetime +
                    ", baseDate=" + baseDate +
                    '}';
        }
    }

    public static class Header implements Serializable {
        @Expose
        @SerializedName("resultMsg")
        private String resultmsg;

        @Expose
        @SerializedName("resultCode")
        private String resultcode;

        public String getResultmsg() {
            return resultmsg;
        }

        public String getResultcode() {
            return resultcode;
        }

        @Override
        public String toString() {
            return "Header{" +
                    "resultmsg='" + resultmsg + '\'' +
                    ", resultcode='" + resultcode + '\'' +
                    '}';
        }
    }

}
