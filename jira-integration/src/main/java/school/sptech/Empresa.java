package school.sptech;

import java.util.ArrayList;
import java.util.List;

public class Empresa {
    private Integer id;
    private String nome;
    private String key;
    private List<Unidade> unidades = new ArrayList<>();
    private final String url;

    public Empresa(Integer id, String nome, String key, List<Unidade> unidades, String url) {
        this.url = url;
        this.unidades = unidades;
        this.key = key;
        this.nome = nome;
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getKey() {
        return key;
    }

    public List<Unidade> getUnidades() {
        return unidades;
    }

    public String getUrl() {
        return url;
    }
}
