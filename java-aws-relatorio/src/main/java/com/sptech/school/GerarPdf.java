package com.sptech.school;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GerarPdf {

    public static byte[] gerar(List<Modelo> modelos) throws IOException {

        PdfFont fontNormal = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        PdfFont fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

        List<Modelo> modelosOrdenados = new ArrayList<>(modelos);

        modelosOrdenados.sort(Comparator.comparing(Modelo::healthscore));

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(output);

        PdfDocument pdf = new PdfDocument(writer);

        Document document = new Document(pdf);

        adicionarTitulo(document, fontNormal, fontBold);

        adicionarResumo(document, modelosOrdenados, fontNormal, fontBold);

        adicionarTabela(document, modelosOrdenados, fontNormal, fontBold);

        document.close();

        return output.toByteArray();
    }

    private static void adicionarTitulo(Document document, PdfFont fontNormal, PdfFont fontBold) {

        Paragraph titulo = new Paragraph("Relatório dos Modelos")
                        .setFont(fontBold)
                        .setFontSize(18)
                        .setTextAlignment(TextAlignment.CENTER);

        document.add(titulo);

        document.add(new Paragraph("Gerado em: "
                                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))).setFont(fontNormal));

        document.add(new Paragraph(" "));
    }

    private static void adicionarResumo(Document document, List<Modelo> modelos, PdfFont fontNormal, PdfFont fontBold) {

        Modelo melhor = modelos.stream().max(Comparator.comparing(Modelo::healthscore))
                        .orElse(null);

        Modelo pior = modelos.stream().min(Comparator.comparing(Modelo::healthscore))
                        .orElse(null);

        document.add(new Paragraph("Resumo").setFont(fontBold));

        document.add(new Paragraph("Total de modelos: " + modelos.size()).setFont(fontNormal));

        if (melhor != null) {

            document.add(new Paragraph("Melhor modelo: "
                                    + melhor.nome()
                                    + " ("
                                    + String.format("%.0f", melhor.healthscore())
                                    + ")").setFont(fontNormal));
        }

        if (pior != null) {

            document.add(new Paragraph("Pior modelo: "
                                    + pior.nome()
                                    + " ("
                                    + String.format("%.0f", pior.healthscore())
                                    + ")").setFont(fontNormal));
        }

        document.add(new Paragraph(" "));
    }

    private static void adicionarTabela(Document document, List<Modelo> modelos, PdfFont fontNormal, PdfFont fontBold) {

        document.add(new Paragraph("Lista de Health Score").setFont(fontBold));

        Table table = new Table(UnitValue.createPercentArray(new float[]{4, 1, 1, 1, 1}));

        table.useAllAvailableWidth();

        adicionarCabecalho(table, fontBold);

        modelos.forEach(modelo -> adicionarLinha(table, modelo, fontNormal));

        document.add(table);
    }

    private static void adicionarCabecalho(Table table, PdfFont fontBold) {

        table.addHeaderCell(criarHeaderCell("Modelo", fontBold));

        table.addHeaderCell(criarHeaderCell("CPU", fontBold));

        table.addHeaderCell(criarHeaderCell("RAM", fontBold));

        table.addHeaderCell(criarHeaderCell("Rede", fontBold));

        table.addHeaderCell(criarHeaderCell("Geral", fontBold));
    }

    private static Cell criarHeaderCell(String texto, PdfFont fontBold) {

        return new Cell().add(new Paragraph(texto).setFont(fontBold)).setTextAlignment(TextAlignment.CENTER);
    }

    private static void adicionarLinha(Table table, Modelo modelo, PdfFont fontNormal) {

        table.addCell(new Cell().add(new Paragraph(modelo.nome()).setFont(fontNormal)));

        table.addCell(criarCellScore(modelo.healthscore_cpu(), fontNormal));

        table.addCell(criarCellScore(modelo.healthscore_ram(), fontNormal));

        table.addCell(criarCellScore(modelo.healthscore_rede(), fontNormal));

        table.addCell(criarCellScore(modelo.healthscore(), fontNormal));
    }

    private static Cell criarCellScore(Double score, PdfFont fontNormal) {

        return new Cell().add(new Paragraph(String.format("%.0f", score)).setFont(fontNormal)).setTextAlignment(TextAlignment.CENTER).setBackgroundColor(obterCor(score));
    }

    private static Color obterCor(Double score) {

        if (score >= 80) {
            return ColorConstants.GREEN;
        }

        if (score >= 60) {
            return new DeviceRgb(255, 255, 0);
        }

        return ColorConstants.RED;
    }
}