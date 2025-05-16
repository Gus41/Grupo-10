package model;

public enum Category {
    ELETRONICS("Eletronicos"),
    ELETRODOMESTICOS("Eletrodomésticos"),
    OTHERS("Outros");         
    private final String description;

   
    private Category(String description) {
        this.description = description;
    }

    
    public String getDescription() {
        return this.description;
    }

        @Override
    public String toString() {
        return this.description;
    }
}