package school.sptech;

import java.util.ArrayList;
import java.util.List;

public class Empresa {
    private Integer id;
    private String nome;
    private String key;
    private List<Unidade> unidades = new ArrayList<>();

    public Empresa(Integer id, String nome, String key, List<Unidade> unidades) {
        this.id = id;
        this.nome = nome;
        this.key = key;
        this.unidades = unidades;
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
}
