package model;

public enum Status {
    PENDENTE("Pendente"),
    EM_ANDAMENTO("Em Andamento"),
    ATRASADA("Atrasada"),
    CONCLUIDA("Concluída"),
    CANCELADA("Cancelada");

    private String descricao;

    Status(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
