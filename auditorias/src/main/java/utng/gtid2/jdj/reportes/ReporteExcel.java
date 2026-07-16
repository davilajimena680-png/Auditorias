package utng.gtid2.jdj.reportes;

import utng.gtid2.jdj.modelo.Evaluacion;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;

import java.util.List;
import java.util.Map;
/**
* Genera la matriz de evaluacion en Excel con formato condicional tipo semaforo
* y una segunda hoja con el resumen de cumplimiento por categoria (solo datos,
* sin grafica nativa de Excel, para evitar el mensaje de "contenido no valido"
* que a veces genera Apache POI al insertar graficas XDDF).
*/
public class ReporteExcel {

    public void generar(String rutaSalida,
                        String empresa,
                        List<Evaluacion> evaluaciones,
                        Map<String, Double> porcentajePorCategoria) throws Exception {

        try (XSSFWorkbook libro = new XSSFWorkbook();
            FileOutputStream out = new FileOutputStream(rutaSalida)) {
            CellStyle estiloEncabezado = crearEstiloEncabezado(libro);
            CellStyle verde = estiloConColor(libro, IndexedColors.LIGHT_GREEN);
            CellStyle amarillo = estiloConColor(libro, IndexedColors.LIGHT_YELLOW);
            CellStyle rojo = estiloConColor(libro, IndexedColors.ROSE);

            // ---------- Hoja 1: Matriz de evaluacion ----------
            Sheet hojaMatriz = libro.createSheet("Matriz de evaluación");
            Row titulo = hojaMatriz.createRow(0);
            Cell celdaTitulo = titulo.createCell(0);
            celdaTitulo.setCellValue("Matriz de evaluación - " + empresa);
            hojaMatriz.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
            Row encabezado = hojaMatriz.createRow(2);
            String[] columnas = {"Categoría", "Criterio", "Cumplimiento", "Observaciones"};

            for (int i = 0; i < columnas.length; i++) {
                Cell celda = encabezado.createCell(i);
                celda.setCellValue(columnas[i]);
                celda.setCellStyle(estiloEncabezado);
            }
            
            int fila = 3;

            for (Evaluacion e : evaluaciones) {
                Row r = hojaMatriz.createRow(fila++);
                r.createCell(0).setCellValue(e.getNombreCategoria());
                r.createCell(1).setCellValue(e.getDescripcionCriterio());
                Cell celdaCump = r.createCell(2);
                celdaCump.setCellValue(e.getCumplimiento());
                switch (e.getCumplimiento()) {
                    case "Cumple" : celdaCump.setCellStyle(verde);
                    case "Parcial" : celdaCump.setCellStyle(amarillo);
                    default : celdaCump.setCellStyle(rojo);
                }
                r.createCell(3).setCellValue(e.getObservaciones() != null ? e.getObservaciones() : "");
            }
                
            for (int i = 0; i < columnas.length; i++) hojaMatriz.autoSizeColumn(i);

                // ---------- Hoja 2: Resumen por categoria (solo datos) ----------
                Sheet hojaResumen = libro.createSheet("Resumen por categoría");
                Row encResumen = hojaResumen.createRow(0);
                encResumen.createCell(0).setCellValue("Categoría");
                encResumen.createCell(1).setCellValue("% Cumplimiento");
                encResumen.getCell(0).setCellStyle(estiloEncabezado);
                encResumen.getCell(1).setCellStyle(estiloEncabezado);

                int filaResumen = 1;

                for (Map.Entry<String, Double> entrada : porcentajePorCategoria.entrySet()) {
                    Row r = hojaResumen.createRow(filaResumen++);
                    r.createCell(0).setCellValue(entrada.getKey());
                    Cell celdaPct = r.createCell(1);
                    celdaPct.setCellValue(entrada.getValue());
                    if (entrada.getValue() >= 80) celdaPct.setCellStyle(verde);
                    else if (entrada.getValue() >= 50) celdaPct.setCellStyle(amarillo);
                    else celdaPct.setCellStyle(rojo);
                }
                hojaResumen.autoSizeColumn(0);
                hojaResumen.autoSizeColumn(1);
                libro.write(out);
            }
    }

    private CellStyle crearEstiloEncabezado(XSSFWorkbook libro) {
        CellStyle estilo = libro.createCellStyle();
        Font fuente = libro.createFont();
        fuente.setBold(true);
        fuente.setColor(IndexedColors.WHITE.getIndex());
        estilo.setFont(fuente);
        estilo.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        estilo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return estilo;
    }

    private CellStyle estiloConColor(XSSFWorkbook libro, IndexedColors color) {
        CellStyle estilo = libro.createCellStyle();
        estilo.setFillForegroundColor(color.getIndex());
        estilo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return estilo;
    }
}