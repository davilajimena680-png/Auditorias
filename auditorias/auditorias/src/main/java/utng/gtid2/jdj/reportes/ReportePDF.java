package utng.gtid2.jdj.reportes;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import utng.gtid2.jdj.modelo.Evaluacion;
import utng.gtid2.jdj.modelo.Hallazgo;
import utng.gtid2.jdj.modelo.PlanAccion;

import java.util.List;
import java.util.Map;

/**
* Genera el reporte final de auditoria en formato PDF: portada, resumen
* de cumplimiento, matriz de evaluaciones, hallazgos y plan de accion.
*/
public class ReportePDF {
    
    public void generar(String rutaSalida, String empresa, String auditor,String estatusAuditoria,
                        double porcentajeGlobal, Map<String, Integer> resumenCumplimiento,
                        List<Evaluacion> evaluaciones, List<Hallazgo> hallazgos,
                        List<PlanAccion> planes, String rutaImagenGrafica) throws Exception {

        try (PdfWriter writer = new PdfWriter(rutaSalida);
             PdfDocument pdfDoc = new PdfDocument(writer);
             Document doc = new Document(pdfDoc)) {
             doc.add(new Paragraph("Reporte de Auditoría de Igualdad Laboral y No Discriminación").setBold().setFontSize(16).setTextAlignment(TextAlignment.CENTER));
             doc.add(new Paragraph(" "));
             doc.add(new Paragraph("Empresa auditada: " + empresa));
             doc.add(new Paragraph("Auditor responsable: " + auditor));
             doc.add(new Paragraph("Estatus de la auditoría: " + estatusAuditoria));
             doc.add(new Paragraph("Porcentaje global de cumplimiento (ponderado): " + porcentajeGlobal + " %") .setBold());
             doc.add(new Paragraph(""));

             // ----- Resumen de cumplimiento -----//
             doc.add(new Paragraph(" Resumen de cumplimiento").setBold().setFontSize(13));
             Table tablaResumen = new Table(UnitValue.createPercentArray(new float[]{3, 2})).useAllAvailableWidth();
             tablaResumen.addHeaderCell(celdaEncabezado("Estado"));
             tablaResumen.addHeaderCell(celdaEncabezado("Total de criterios"));
             resumenCumplimiento.forEach((estado, total) -> {
                 tablaResumen.addCell(estado);
                 tablaResumen.addCell(String.valueOf(total));
             });
             doc.add(tablaResumen);
             doc.add(new Paragraph(""));

             // ----- Grafica (imagen exportada desde JavaFX) -----//
             if (rutaImagenGrafica != null && !rutaImagenGrafica.isBlank()) {
                 doc.add(new Paragraph("Distribución gráfica deres<dos").setBold().setFontSize(13));
                 Image img = new Image(com.itextpdf.io.image.ImageDataFactory.create(rutaImagenGrafica));
                 img.setWidth(UnitValue.createPercentValue(80));
                 img.setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.CENTER);
                 doc.add(img);
                 doc.add(new Paragraph(""));
                }

             // ----- Matriz de evaluaciones -----//
             doc.add(new Paragraph("Matriz de evaluación por criterio").setBold().setFontSize(13));
             Table tablaEval = new Table(UnitValue.createPercentArray(new float[]{3, 5, 2, 4})).useAllAvailableWidth();
             tablaEval.addHeaderCell(celdaEncabezado("Categoría")); 
             tablaEval.addHeaderCell(celdaEncabezado("Criterio"));
             tablaEval.addHeaderCell(celdaEncabezado("Cumplimiento"));
             tablaEval.addHeaderCell(celdaEncabezado("Observaciones"));

             for (Evaluacion e : evaluaciones) {
                 tablaEval.addCell(e.getNombreCategoria());
                 tablaEval.addCell(e.getDescripcionCriterio());
                 Cell celdaCump = new Cell().add(new Paragraph(e.getCumplimiento()));
                 if ("Cumple".equals(e.getCumplimiento())) celdaCump.setBackgroundColor(new
                 com.itextpdf.kernel.colors.DeviceRgb(198, 239, 206));
                 else if ("Parcial".equals(e.getCumplimiento()))
                 celdaCump.setBackgroundColor(new com.itextpdf.kernel.colors.DeviceRgb(255, 235, 156));
                 else celdaCump.setBackgroundColor(new
                 com.itextpdf.kernel.colors.DeviceRgb(255, 199, 206));
                 tablaEval.addCell(celdaCump);
                 tablaEval.addCell(e.getObservaciones() != null ? e.getObservaciones() : "");
                }
             doc.add(tablaEval);
             doc.add(new Paragraph(""));

             // ----- Hallazgos -----//
             doc.add(new Paragraph("Hallazgos detectados").setBold().setFontSize(13));
             Table tablaHallazgos = new Table(UnitValue.createPercentArray(new float[]{3, 2,5})).useAllAvailableWidth();
             tablaHallazgos.addHeaderCell(celdaEncabezado("Tipo"));
             tablaHallazgos.addHeaderCell(celdaEncabezado("Riesgo"));
             tablaHallazgos.addHeaderCell(celdaEncabezado("Descripción"));
             for (Hallazgo h : hallazgos) {
                 tablaHallazgos.addCell(h.getTipo());
                 tablaHallazgos.addCell(h.getNivelRiesgo());
                 tablaHallazgos.addCell(h.getDescripcion());
                }
             doc.add(tablaHallazgos);
             doc.add(new Paragraph(" "));

             // ----- Plan de accion -----//
             doc.add(new Paragraph("Plan de acción").setBold().setFontSize(13));
             Table tablaPlanes = new Table(UnitValue.createPercentArray(new float[]{4, 3, 3}))
             .useAllAvailableWidth();
             tablaPlanes.addHeaderCell(celdaEncabezado("Responsable"));
             tablaPlanes.addHeaderCell(celdaEncabezado("Fecha compromiso"));
             tablaPlanes.addHeaderCell(celdaEncabezado("Estatus"));
             for (PlanAccion p : planes) {
                 tablaPlanes.addCell(p.getResponsable());
                 tablaPlanes.addCell(p.getFechaCompromiso() != null ?
                 p.getFechaCompromiso().toString() : "Sin fecha");
                 tablaPlanes.addCell(p.getEstatus());
                }
             doc.add(tablaPlanes);
            }
        }
        
        private Cell celdaEncabezado(String texto) {
                Cell c = new Cell().add(new Paragraph(texto).setBold());
                c.setBackgroundColor(ColorConstants.DARK_GRAY);
                c.setFontColor(ColorConstants.WHITE);
                return c;
        }
}