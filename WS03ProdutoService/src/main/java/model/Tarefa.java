package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Tarefa {
	private int id;
	private String descricao;
	private float progresso;
	private int prioridade;
	private LocalDateTime dataInicio;	
	private LocalDate dataEntrega;
	
	public Tarefa() {
		id = -1;
		descricao = "";
		progresso = 0.00F;
		prioridade = 0;
		dataInicio = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
		dataEntrega = LocalDate.now().plusWeeks(1); 
	}

	public Tarefa(int id, String descricao, float progresso, int prioridade, LocalDateTime inicio, LocalDate v) {
		setId(id);
		setDescricao(descricao);
		setProgresso(progresso);
		setPrioridade(prioridade);
		setDataInicio(inicio);
		setDataEntrega(v);
	}		
	
	public int getID() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public float getProgresso() {
		return progresso;
	}

	public void setProgresso(float progresso) {
		this.progresso = progresso;
	}

	public int getPrioridade() {
		return prioridade;
	}
	
	public void setPrioridade(int prioridade) {
		this.prioridade = prioridade;
	}
	
	public LocalDate getDataEntrega() {
		return dataEntrega;
	}

	public LocalDateTime getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(LocalDateTime dataInicio) {
		// Pega a Data Atual
		LocalDateTime agora = LocalDateTime.now();
		// Garante que a data de inicio não pode ser futura
		if (agora.compareTo(dataInicio) >= 0)
			this.dataInicio = dataInicio;
	}

	public void setDataEntrega(LocalDate dataEntrega) {
		// a data de inicio deve ser anterior a data de entrega.
		if (getDataInicio().isBefore(dataEntrega.atStartOfDay()))
			this.dataEntrega = dataEntrega;
	}
	// APAGAR DEPOIS -----------------------------------------------------------------------------------------------------------
	public boolean emValidade() {
		return LocalDateTime.now().isBefore(this.getDataEntrega().atTime(23, 59));
	}


	/**
	 * Método sobreposto da classe Object. É executado quando um objeto precisa
	 * ser exibido na forma de String.
	 */
	@Override
	public String toString() {
		return "Tarefa: " + descricao + "   Progresso: " + progresso + "%   Prioridade.: " + prioridade + "   Inicio da Tarefa: "
				+ dataInicio  + "   Entrega da Tarefa: " + dataEntrega;
	}
	
	@Override
	public boolean equals(Object obj) {
		return (this.getID() == ((Tarefa) obj).getID());
	}	
}