package com.example.mypastryshop;
public class cake_menu_class {
    private String menu_name;
    private String menu_price;
    private int menu_picture;

    public cake_menu_class(String menu_name, String menu_price, int menu_picture) {
        this.menu_name = menu_name;
        this.menu_price = menu_price;
        this.menu_picture = menu_picture;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public String getMenu_price() {
        return menu_price;
    }

    public int getMenu_picture(){
        return menu_picture;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public void setMenu_price(String menu_price) {
        this.menu_price = menu_price;
    }

    public void setMenu_picture(int menu_picture) {
        this.menu_picture = menu_picture;
    }
}
