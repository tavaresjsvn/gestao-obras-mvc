package model;

public enum UnidadeMedida {
	
    METRO("m"),
    METRO_QUADRADO("m²"),
    METRO_CUBICO("m³"),
    QUILOGRAMA("kg"),
    GRAMA("g"),
    LITRO("L"),
    UNIDADE("unid.");

    private String simbolo;

    UnidadeMedida(String simbolo) {
        this.simbolo = simbolo;
    }

    public String getSimbolo() {
        return simbolo;
    }

    @Override
    public String toString() {
        return simbolo;
    }

}
