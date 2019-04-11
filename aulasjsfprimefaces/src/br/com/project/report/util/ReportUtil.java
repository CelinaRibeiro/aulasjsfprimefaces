package br.com.project.report.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.stereotype.Component;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 * 
 * @author Celina
 *
 */
@Component
public class ReportUtil implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private static final String UNDERLINE = "_";
	private static final String PONTO = ".";
	private String SEPARATOR = File.separator;
	
	private static final String FOLDER_RELATORIOS = "/relatorios";
	private static final String SUBREPORT_DIR = "SUBREPORT_DIR";
	
	private static final String EXTESION_ODS = "ods";
	private static final String EXTESION_XLS = "xls";
	private static final String EXTESION_HTML = "html";
	private static final String EXTESION_PDF = "pdf";
	
	private static final int RELATORIO_PDF = 1;
	private static final int RELATORIO_EXCEL = 2;
	private static final int RELATORIO_HTML = 3;
	private static final int RELATORIO_PLANILHA_OPEN_OFICE = 4;
	
	private StreamedContent arquivoRetorno = null;
	private String caminhoArquivoRelatorio = null;
	private JRExporter tipoArquivoExportado = null;
	private String extensaoArquivoExportado = "";
	private String caminhoSubreport_dir = "";
	private File arquivoGerado = null;
	
	
	public StreamedContent geraRelatorio(List<?> listDataBeanColletionReport,
			HashMap parametrosRelatorio, String nomeRelatorioJasper, 
			String nomeRelatorioSaida, int tipoRelatorio) throws Exception {
		
		/*Cria a lista de collectionDataSource de beans que carregam os dados para os relatórios*/
		JRBeanCollectionDataSource jrbcds = new JRBeanCollectionDataSource(listDataBeanColletionReport);
		
		/*Fornece o caminho físico até a pasta que contém os relatórios compilados .jasper*/
		FacesContext context = FacesContext.getCurrentInstance();
		context.responseComplete();
		ServletContext scontext = (ServletContext) context.getExternalContext().getContext();
		
		String caminhoRelatorio = scontext.getRealPath(FOLDER_RELATORIOS);
		//Exemplo ->  c:/aplicaçao/relatorios/rel_bairro.jasper
		File file = new File(caminhoRelatorio + SEPARATOR + nomeRelatorioJasper + PONTO + "jasper");
		
		if(caminhoRelatorio == null
				|| (caminhoRelatorio != null && caminhoRelatorio.isEmpty())
				|| !file.exists()){
			caminhoRelatorio = this.getClass().getResource(FOLDER_RELATORIOS).getPath();
			SEPARATOR ="";
		}
		
		/*caminho para imagens*/
		parametrosRelatorio.put("REPORT_PARAMETERS_IMG", caminhoRelatorio);
		
		/*Caminho completo até o relatório compilado indicado*/
		String caminhoArquivoJasper = caminhoRelatorio + SEPARATOR + nomeRelatorioJasper + PONTO + "jasper";
		
		/*Faz o carregamento do relatório indicado*/
		JasperReport relatorioJasper = (JasperReport) JRLoader.loadObjectFromFile(caminhoArquivoJasper);
		
		/*Seta parâmetros SUBREPORT_DIR como caminho físico para sub-reports */
		caminhoSubreport_dir = caminhoRelatorio + SEPARATOR;
		parametrosRelatorio.put(SUBREPORT_DIR, caminhoSubreport_dir);
		
		/*Carrega o arquivo compilado para a memória*/
		JasperPrint impressoraJasper = JasperFillManager.
				fillReport(relatorioJasper, parametrosRelatorio, jrbcds);
		
		
		switch (tipoRelatorio) {
		case RELATORIO_PDF:
			tipoArquivoExportado = new JRPdfExporter();
			extensaoArquivoExportado = EXTESION_PDF;
			break;
			
		case RELATORIO_EXCEL:
			tipoArquivoExportado = new JRXlsExporter();
			extensaoArquivoExportado = EXTESION_XLS;
			break;
			
		case RELATORIO_HTML:
			tipoArquivoExportado = new JRHtmlExporter();
			extensaoArquivoExportado = EXTESION_HTML;
			break;	
			
		case RELATORIO_PLANILHA_OPEN_OFICE:
			tipoArquivoExportado = new JROdtExporter();
			extensaoArquivoExportado = EXTESION_ODS;
			break;

		default:
			tipoArquivoExportado = new JRPdfExporter();
			extensaoArquivoExportado = EXTESION_PDF;
			break;
		}
		
		nomeRelatorioSaida += UNDERLINE + DateUtils.getDateAtualReportName();
		
		/*Caminho do relatório exportado*/
		caminhoArquivoRelatorio = caminhoRelatorio + SEPARATOR + nomeRelatorioSaida + PONTO + extensaoArquivoExportado;
		
		/*Cria novo file exportado*/
		arquivoGerado = new File(caminhoArquivoRelatorio);
		
		/*Preparar a impressão*/
		tipoArquivoExportado.setParameter(JRExporterParameter.JASPER_PRINT, impressoraJasper);
		
		/*Nome do arquivo a ser impresso/exportado*/
		tipoArquivoExportado.setParameter(JRExporterParameter.OUTPUT_FILE, arquivoGerado);
		
		/*Executa a exportação*/
		tipoArquivoExportado.exportReport();
		
		/*Remove o arquivo do servidor após ser feito o download*/
		arquivoGerado.deleteOnExit();
		
		/*Cria o inputstream para ser usado pelo Primefaces*/
		InputStream conteudoRelatorio = new FileInputStream(arquivoGerado);
		
		/*Faz o retorno para a aplicação*/
		arquivoRetorno = new DefaultStreamedContent(conteudoRelatorio, "application/"+extensaoArquivoExportado,
				nomeRelatorioSaida + PONTO+extensaoArquivoExportado);
		
		return arquivoRetorno;
			
	}
	
}
