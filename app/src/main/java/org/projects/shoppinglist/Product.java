package org.projects.shoppinglist;

/**
 * Created by user on 18-05-2017.
 */

public class Product {


    public Product() {

    }

    private String name;
    private int number;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }



    @Override
    public String toString(){

        return name + " " + " " + number;

    }
}
