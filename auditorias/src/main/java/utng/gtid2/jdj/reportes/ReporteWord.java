package utng.gtid2.jdj.reportes;

import utng.gtid2.jdj.modelo.Hallazgo;
import utng.gtid2.jdj.modelo.PlanAccion;

import org.apache.poi.xwpf.usermodel.*;
import org.apache.poi.util.Units;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.util.List;
import java.util.Map;
/**
* Genera el informe formal de auditoria en Word: portada, resumen ejecutivo,
* hallazgos y plan de accion con responsables y fechas compromiso.
*/
public class ReporteWord {
    public void generar(String rutaSalida,
                        String empresa,
                        String auditor,
                        String estatusAuditoria,
                        double porcentajeGlobal,
                        Map<String, Integer> resumenCumplimiento,
                        List<Hallazgo> hallazgos,
                        List<PlanAccion> planes,
                        String rutaImagenGrafica) throws Exception {

        try (XWPFDocument documento = new XWPFDocument();
        FileOutputStream out = new FileOutputStream(rutaSalida)) {

            // ----- Titulo -----
            XWPFParagraph titulo = documento.createParagraph();
            titulo.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun runTitulo = titulo.createRun();
            runTitulo.setText("Informe de Auditoría de Igualdad Laboral y No Discriminación");
            runTitulo.setBold(true);
            runTitulo.setFontSize(16);
            agregarParrafo(documento, "Empresa auditada: " + empresa, false);
            agregarParrafo(documento, "Auditor responsable: " + auditor, false);
            agregarParrafo(documento, "Estatus de la auditoría: " + estatusAuditoria, false);
            agregarParrafo(documento, "Porcentaje global de cumplimiento (ponderado): " + porcentajeGlobal + " %", true);
            documento.createParagraph();

            // ----- Resumen ejecutivo -----
            agregarEncabezadoSeccion(documento, "Resumen de cumplimiento");
            XWPFTable tablaResumen = documento.createTable(resumenCumplimiento.size() + 1, 2);
            tablaResumen.getRow(0).getCell(0).setText("Estado");
            tablaResumen.getRow(0).getCell(1).setText("Total de criterios");
            int fila = 1;

            for (Map.Entry<String, Integer> entrada : resumenCumplimiento.entrySet()) {
                tablaResumen.getRow(fila).getCell(0).setText(entrada.getKey());
                tablaResumen.getRow(fila).getCell(1).setText(String.valueOf(entrada.getValue()));
                fila++;
            }
            documento.createParagraph();

            // ----- Grafica -----
            if (rutaImagenGrafica != null && !rutaImagenGrafica.isBlank()) {
                agregarEncabezadoSeccion(documento, "Distribución gráfica de resultados");
                XWPFParagraph parrafoImg = documento.createParagraph();
                XWPFRun runImg = parrafoImg.createRun();
                try (FileInputStream fis = new FileInputStream(rutaImagenGrafica)) {
                    runImg.addPicture(fis, XWPFDocument.PICTURE_TYPE_PNG, rutaImagenGrafica, Units.toEMU(400), Units.toEMU(260));
                }
                documento.createParagraph();
            }

            // ----- Hallazgos y plan de accion -----
            agregarEncabezadoSeccion(documento, "Hallazgos y plan de acción");
            XWPFTable tabla = documento.createTable(hallazgos.size() + 1, 5);
            XWPFTableRow encabezado = tabla.getRow(0);
            encabezado.getCell(0).setText("Tipo");
            encabezado.getCell(1).setText("Riesgo");
            encabezado.getCell(2).setText("Descripción");
            encabezado.getCell(3).setText("Responsable");
            encabezado.getCell(4).setText("Fecha compromiso / Estatus");
            
            for (int i = 0; i < hallazgos.size(); i++) {
                Hallazgo h = hallazgos.get(i);
                XWPFTableRow filaTabla = tabla.getRow(i + 1);
                filaTabla.getCell(0).setText(h.getTipo());
                filaTabla.getCell(1).setText(h.getNivelRiesgo());
                filaTabla.getCell(2).setText(h.getDescripcion());

                PlanAccion planAsociado = planes.stream().filter(p -> p.getIdHallazgo() == h.getIdHallazgo()).findFirst().orElse(null);

                if (planAsociado != null) {
                    filaTabla.getCell(3).setText(planAsociado.getResponsable());
                    String fechaEstatus = (planAsociado.getFechaCompromiso() != null ? planAsociado.getFechaCompromiso().toString() : "Sin fecha") + " / " + planAsociado.getEstatus();
                    filaTabla.getCell(4).setText(fechaEstatus);

                } else {
                        filaTabla.getCell(3).setText("Sin asignar");
                        filaTabla.getCell(4).setText("Sin plan de acción");
                }
            }
            documento.write(out);
        }
    }
        
    private void agregarParrafo(XWPFDocument documento, String texto, boolean negrita) {
                XWPFParagraph p = documento.createParagraph();
                XWPFRun r = p.createRun();
                r.setText(texto);
                r.setBold(negrita);
    }

    private void agregarEncabezadoSeccion(XWPFDocument documento, String texto) {
                XWPFParagraph p = documento.createParagraph();
                XWPFRun r = p.createRun();
                r.setText(texto);
                r.setBold(true);
                r.setFontSize(13);
    }
}