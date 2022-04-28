package dao;

import model.Tarefa;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


public class TarefaDAO extends DAO {	
	public TarefaDAO() {
		super();
		conectar();
	}
	
	
	public void finalize() {
		close();
	}
	
	
	public boolean insert(Tarefa tarefa) {
		boolean status = false;
		try {
			String sql = "INSERT INTO tarefa (descricao, progresso, prioridade, datainicio, dataentrega) "
		               + "VALUES ('" + tarefa.getDescricao() + "', "
		               + tarefa.getProgresso() + ", " + tarefa.getPrioridade() + ", ?, ?);";
			PreparedStatement st = conexao.prepareStatement(sql);
		    st.setTimestamp(1, Timestamp.valueOf(tarefa.getDataInicio()));
			st.setDate(2, Date.valueOf(tarefa.getDataEntrega()));
			st.executeUpdate();
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}

	
	public Tarefa get(int id) {
		Tarefa tarefa = null;
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM tarefa WHERE id="+id;
			ResultSet rs = st.executeQuery(sql);	
	        if(rs.next()){            
	        	 tarefa = new Tarefa(rs.getInt("id"), rs.getString("descricao"), (float)rs.getDouble("progresso"), 
	                				   rs.getInt("prioridade"), 
	        			               rs.getTimestamp("datainicio").toLocalDateTime(),
	        			               rs.getDate("dataentrega").toLocalDate());
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return tarefa;
	}
	
	
	public List<Tarefa> get() {
		return get("");
	}

	
	public List<Tarefa> getOrderByID() {
		return get("id");		
	}
	
	
	public List<Tarefa> getOrderByDescricao() {
		return get("descricao");		
	}
	
	
	public List<Tarefa> getOrderByProgresso() {
		return get("progresso");		
	}
	
	
	private List<Tarefa> get(String orderBy) {
		List<Tarefa> tarefas = new ArrayList<Tarefa>();
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM tarefa" + ((orderBy.trim().length() == 0) ? "" : (" ORDER BY " + orderBy));
			ResultSet rs = st.executeQuery(sql);	           
	        while(rs.next()) {	            	
	        	Tarefa p = new Tarefa(rs.getInt("id"), rs.getString("descricao"), (float)rs.getDouble("progresso"), 
	        			                rs.getInt("prioridade"),
	        			                rs.getTimestamp("datainicio").toLocalDateTime(),
	        			                rs.getDate("dataentrega").toLocalDate());
	            tarefas.add(p);
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return tarefas;
	}
	
	
	public boolean update(Tarefa tarefa) {
		boolean status = false;
		try {  
			String sql = "UPDATE tarefa SET descricao = '" + tarefa.getDescricao() + "', "
					   + "progresso = " + tarefa.getProgresso() + ", " 
					   + "prioridade = " + tarefa.getPrioridade() + ","
					   + "datainicio = ?, " 
					   + "dataentrega = ? WHERE id = " + tarefa.getID();
			PreparedStatement st = conexao.prepareStatement(sql);
		    st.setTimestamp(1, Timestamp.valueOf(tarefa.getDataInicio()));
			st.setDate(2, Date.valueOf(tarefa.getDataEntrega()));
			st.executeUpdate();
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
	
	
	public boolean delete(int id) {
		boolean status = false;
		try {  
			Statement st = conexao.createStatement();
			st.executeUpdate("DELETE FROM tarefa WHERE id = " + id);
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
}