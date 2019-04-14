package br.com.project.listener;

import java.io.Serializable;

import org.hibernate.envers.RevisionListener;

import br.com.framework.utils.UtilFramework;
import br.com.project.model.classes.Entidade;
import br.com.project.model.classes.InformacaoRevisao;

public class CustomListener implements RevisionListener, Serializable{

	private static final long serialVersionUID = 1L;

	@Override
	public void newRevision(Object revisionEntity) {
		InformacaoRevisao informacaoRevisao = (InformacaoRevisao) revisionEntity;
		Long codUser = UtilFramework.getThreadLocal().get();
		
		Entidade entidade = new Entidade();
		
		if(codUser != null && codUser !=0L) {
			entidade.setEnt_codigo(codUser);
			informacaoRevisao.setEntidade(entidade);
		}
		
	}

}
