package br.com.framework.utils;

import java.io.Serializable;

import org.springframework.stereotype.Component;


/*
 * @author Celina
 */

@Component
public class UtilFramework implements Serializable{

	private static final long serialVersionUID = 1L;
	
	//threadLocal � para n�o haver concorr�ncia
	private static ThreadLocal<Long> threadLocal = new ThreadLocal<Long>();
	
	public synchronized static ThreadLocal<Long> getThreadLocal(){
		return threadLocal;
	}

}
