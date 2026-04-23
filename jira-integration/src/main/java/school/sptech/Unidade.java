package school.sptech;

import java.net.URL;

public class Unidade {

    private Integer id;
    private String nome;
    private final String url;

    public Unidade(Integer id, String nome, String url) {
        this.url = url;
        this.nome = nome;
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getUrl() {
        return url;
    }
}
