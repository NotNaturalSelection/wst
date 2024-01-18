package org.example.common;

public class Item {
    private String name;
    private String description;
    private int price;
    private int level;
    private int power;

    public Item(String name, String description, int price, int level, int power) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.level = level;
        this.power = power;
    }

    public Item() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", level=" + level +
                ", power=" + power +
                '}';
    }
}
