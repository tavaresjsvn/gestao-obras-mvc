package model;

public enum Especialidade {

    ESTRUTURAL("Estrutural"),
    HIDRAULICA("Hidráulica"),
    ELETRICA("Elétrica"),
    GEOTECNICA("Geotécnica"),
    ARQUITETURA("Arquitetura"),
    OUTRA("Outra");

    private final String descricao;

    Especialidade(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }

    public static Especialidade fromDescricao(String descricao) {
        for (Especialidade esp : values()) {
            if (esp.getDescricao().equalsIgnoreCase(descricao.trim())) {
                return esp;
            }
        }
        throw new IllegalArgumentException("Especialidade inválida: " + descricao);
    }
}
