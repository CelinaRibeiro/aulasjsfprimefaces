package teste.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Calendar;

import org.junit.Test;

import br.com.project.report.util.DateUtils;

public class TesteData {

	@Test
	public void testData() {
		try {
			assertEquals("11042019", DateUtils.getDateAtualReportName());
			
			assertEquals("'2019-03-25'", DateUtils.formatDateSql(Calendar.getInstance().getTime()));
			
			assertEquals("2019-03-25", DateUtils.formatDateSqlSimple(Calendar.getInstance().getTime()));
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

	}

}
