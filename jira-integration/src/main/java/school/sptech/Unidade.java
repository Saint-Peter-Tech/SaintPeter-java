package school.sptech;

public class Unidade {

    private Integer id;
    private String nome;

    public Unidade(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}
