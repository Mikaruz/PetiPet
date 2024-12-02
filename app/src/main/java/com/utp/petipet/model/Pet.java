package com.utp.petipet.model;

public class Pet {
    public String id;
    public String name;
    public String description;
    public String specie;
    public String gender;
    public int age;
    public String imageUrl;
    public String userId;

    public Pet() {
    }

    public Pet( String name, String description, String specie, String gender, int age, String imageUrl, String userId) {
        this.name = name;
        this.description = description;
        this.specie = specie;
        this.gender = gender;
        this.age = age;
        this.imageUrl = imageUrl;
        this.userId = userId;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpecie() {
        return specie;
    }

    public void setSpecie(String specie) {
        this.specie = specie;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}