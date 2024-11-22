package com.example.model;

public class PetFactory {
    public static Pet createPet(String type, String name) {
        switch (type.toLowerCase()) {
            case "mole":

                return new Mole(name);

            case "bear":
                return new Bear(name);

            case "cat":
                return new Cat(name);

            default:
                throw new IllegalArgumentException("Invalid pet type: " + type);
        }
    }
}
