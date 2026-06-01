package com.sptech.school;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Modelo(
        Integer id_modelo,
        String nome,
        Double healthscore_cpu,
        Double healthscore_ram,
        Double healthscore_rede,
        Double healthscore
) {
}
