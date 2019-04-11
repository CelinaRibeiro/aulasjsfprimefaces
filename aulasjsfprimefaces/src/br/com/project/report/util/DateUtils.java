package br.com.project.report.util;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils implements Serializable{

	private static final long serialVersionUID = 1L;
	
	//formato de data para adicionar no nome do relatório
	public static String getDateAtualReportName() {
		DateFormat df = new SimpleDateFormat("ddMMyyyy");
		return df.format(Calendar.getInstance().getTime());
	}
	
	//formato de data para gravar no bc
	public static String formatDateSql(Date data) {
		StringBuffer retorno = new StringBuffer();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		retorno.append("'");
		retorno.append(df.format(data));
		retorno.append("'");
		return retorno.toString();
	}
	
	//formato de data para gravar no bc sem utilizar aspas simples
		public static String formatDateSqlSimple(Date data) {
			StringBuffer retorno = new StringBuffer();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			retorno.append(df.format(data));
			return retorno.toString();
		}

}
