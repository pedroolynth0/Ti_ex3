package service;

import java.util.Scanner;
import java.time.LocalDate;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import dao.TarefaDAO;
import model.Tarefa;
import spark.Request;
import spark.Response;


public class TarefaService {

	private TarefaDAO tarefaDAO = new TarefaDAO();
	private String form;
	private final int FORM_INSERT = 1;
	private final int FORM_DETAIL = 2;
	private final int FORM_UPDATE = 3;
	private final int FORM_ORDERBY_ID = 1;
	private final int FORM_ORDERBY_DESCRICAO = 2;
	private final int FORM_ORDERBY_PROGRESSO = 3;
	
	
	public TarefaService() {
		makeForm();
	}

	
	public void makeForm() {
		makeForm(FORM_INSERT, new Tarefa(), FORM_ORDERBY_DESCRICAO);
	}

	
	public void makeForm(int orderBy) {
		makeForm(FORM_INSERT, new Tarefa(), orderBy);
	}

	
	public void makeForm(int tipo, Tarefa tarefa, int orderBy) {
		String nomeArquivo = "form.html";
		form = "";
		try{
			Scanner entrada = new Scanner(new File(nomeArquivo));
		    while(entrada.hasNext()){
		    	form += (entrada.nextLine() + "\n");
		    }
		    entrada.close();
		}  catch (Exception e) { System.out.println(e.getMessage()); }
		
		String umTarefa = "";
		if(tipo != FORM_INSERT) {
			umTarefa += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
			umTarefa += "\t\t<tr>";
			umTarefa += "\t\t\t<td align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;<a href=\"/tarefa/list/1\">Nova Tarefa</a></b></font></td>";
			umTarefa += "\t\t</tr>";
			umTarefa += "\t</table>";
			umTarefa += "\t<br>";			
		}
		
		if(tipo == FORM_INSERT || tipo == FORM_UPDATE) {
			String action = "/tarefa/";
			String name, descricao, buttonLabel;
			if (tipo == FORM_INSERT){
				action += "insert";
				name = "Inserir Tarefa";
				descricao = "estudar geometria";
				buttonLabel = "Inserir";
			} else {
				action += "update/" + tarefa.getID();
				name = "Atualizar Tarefa (ID " + tarefa.getID() + ")";
				descricao = tarefa.getDescricao();
				buttonLabel = "Atualizar";
			}
			umTarefa += "\t<form class=\"form--register\" action=\"" + action + "\" method=\"post\" id=\"form-add\">";
			umTarefa += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
			umTarefa += "\t\t<tr>";
			umTarefa += "\t\t\t<td colspan=\"3\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;" + name + "</b></font></td>";
			umTarefa += "\t\t</tr>";
			umTarefa += "\t\t<tr>";
			umTarefa += "\t\t\t<td colspan=\"3\" align=\"left\">&nbsp;</td>";
			umTarefa += "\t\t</tr>";
			umTarefa += "\t\t<tr>";
			umTarefa += "\t\t\t<td>&nbsp;Descrição: <input class=\"input--register\" type=\"text\" name=\"descricao\" value=\""+ descricao +"\"></td>";
			umTarefa += "\t\t\t<td>Progresso: <input class=\"input--register\" type=\"text\" name=\"progresso\" value=\""+ tarefa.getProgresso() +"\"></td>";
			umTarefa += "\t\t\t<td>Prioridade: <input class=\"input--register\" type=\"text\" name=\"prioridade\" value=\""+ tarefa.getPrioridade() +"\"></td>";
			umTarefa += "\t\t</tr>";
			umTarefa += "\t\t<tr>";
			umTarefa += "\t\t\t<td>&nbsp;Data de Inicio: <input class=\"input--register\" type=\"text\" name=\"dataInicio\" value=\""+ tarefa.getDataInicio().toString() + "\"></td>";
			umTarefa += "\t\t\t<td>Data de entrega: <input class=\"input--register\" type=\"text\" name=\"dataEntrega\" value=\""+ tarefa.getDataEntrega().toString() + "\"></td>";
			umTarefa += "\t\t\t<td align=\"center\"><input type=\"submit\" value=\""+ buttonLabel +"\" class=\"input--main__style input--button\"></td>";
			umTarefa += "\t\t</tr>";
			umTarefa += "\t</table>";
			umTarefa += "\t</form>";		
		} else if (tipo == FORM_DETAIL){
			umTarefa += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
			umTarefa += "\t\t<tr>";
			umTarefa += "\t\t\t<td colspan=\"3\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;Detalhar Tarefa (ID " + tarefa.getID() + ")</b></font></td>";
			umTarefa += "\t\t</tr>";
			umTarefa += "\t\t<tr>";
			umTarefa += "\t\t\t<td colspan=\"3\" align=\"left\">&nbsp;</td>";
			umTarefa += "\t\t</tr>";
			umTarefa += "\t\t<tr>";
			umTarefa += "\t\t\t<td>&nbsp;Descrição: "+ tarefa.getDescricao() +"</td>";
			umTarefa += "\t\t\t<td>Progresso: "+ tarefa.getProgresso() +"</td>";
			umTarefa += "\t\t\t<td>Prioridade: "+ tarefa.getPrioridade() +"</td>";
			umTarefa += "\t\t</tr>";
			umTarefa += "\t\t<tr>";
			umTarefa += "\t\t\t<td>&nbsp;Data de inicio: "+ tarefa.getDataInicio().toString() + "</td>";
			umTarefa += "\t\t\t<td>Data de entrega: "+ tarefa.getDataEntrega().toString() + "</td>";
			umTarefa += "\t\t\t<td>&nbsp;</td>";
			umTarefa += "\t\t</tr>";
			umTarefa += "\t</table>";		
		} else {
			System.out.println("ERRO! Tipo não identificado " + tipo);
		}
		form = form.replaceFirst("<UM-TAREFA>", umTarefa);
		
		String list = new String("<table width=\"80%\" align=\"center\" bgcolor=\"#f3f3f3\">");
		list += "\n<tr><td colspan=\"6\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;Relação de Tarefas</b></font></td></tr>\n" +
				"\n<tr><td colspan=\"6\">&nbsp;</td></tr>\n" +
    			"\n<tr>\n" + 
        		"\t<td><a href=\"/tarefa/list/" + FORM_ORDERBY_ID + "\"><b>ID</b></a></td>\n" +
        		"\t<td><a href=\"/tarefa/list/" + FORM_ORDERBY_DESCRICAO + "\"><b>Descrição</b></a></td>\n" +
        		"\t<td><a href=\"/tarefa/list/" + FORM_ORDERBY_PROGRESSO + "\"><b>Progresso</b></a></td>\n" +
        		"\t<td width=\"100\" align=\"center\"><b>Detalhar</b></td>\n" +
        		"\t<td width=\"100\" align=\"center\"><b>Atualizar</b></td>\n" +
        		"\t<td width=\"100\" align=\"center\"><b>Excluir</b></td>\n" +
        		"</tr>\n";
		
		List<Tarefa> tarefas;
		if (orderBy == FORM_ORDERBY_ID) {                 	tarefas = tarefaDAO.getOrderByID();
		} else if (orderBy == FORM_ORDERBY_DESCRICAO) {		tarefas = tarefaDAO.getOrderByDescricao();
		} else if (orderBy == FORM_ORDERBY_PROGRESSO) {			tarefas = tarefaDAO.getOrderByProgresso();
		} else {											tarefas = tarefaDAO.get();
		}

		int i = 0;
		String bgcolor = "";
		for (Tarefa p : tarefas) {
			bgcolor = (i++ % 2 == 0) ? "#fff5dd" : "#dddddd";
			list += "\n<tr bgcolor=\""+ bgcolor +"\">\n" + 
            		  "\t<td>" + p.getID() + "</td>\n" +
            		  "\t<td>" + p.getDescricao() + "</td>\n" +
            		  "\t<td>" + p.getProgresso() + "</td>\n" +
            		  "\t<td align=\"center\" valign=\"middle\"><a href=\"/tarefa/" + p.getID() + "\"><img src=\"/image/detail.png\" width=\"20\" height=\"20\"/></a></td>\n" +
            		  "\t<td align=\"center\" valign=\"middle\"><a href=\"/tarefa/update/" + p.getID() + "\"><img src=\"/image/update.png\" width=\"20\" height=\"20\"/></a></td>\n" +
            		  "\t<td align=\"center\" valign=\"middle\"><a href=\"javascript:confirmarDeleteTarefa('" + p.getID() + "', '" + p.getDescricao() + "', '" + p.getProgresso() + "');\"><img src=\"/image/delete.png\" width=\"20\" height=\"20\"/></a></td>\n" +
            		  "</tr>\n";
		}
		list += "</table>";		
		form = form.replaceFirst("<LISTAR-TAREFA>", list);				
	}
	
	
	public Object insert(Request request, Response response) {
		String descricao = request.queryParams("descricao");
		float progresso = Float.parseFloat(request.queryParams("progresso"));
		int prioridade = Integer.parseInt(request.queryParams("prioridade"));
		LocalDateTime dataInicio = LocalDateTime.parse(request.queryParams("dataInicio"));
		LocalDate dataEntrega = LocalDate.parse(request.queryParams("dataEntrega"));
		
		String resp = "";
		
		Tarefa tarefa = new Tarefa(-1, descricao, progresso, prioridade, dataInicio, dataEntrega);
		
		if(tarefaDAO.insert(tarefa) == true) {
            resp = "Tarefa (" + descricao + ") inserida!";
            response.status(201); // 201 Created
		} else {
			resp = "Tarefa (" + descricao + ") não inserida!";
			response.status(404); // 404 Not found
		}
			
		makeForm();
		return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
	}

	
	public Object get(Request request, Response response) {
		int id = Integer.parseInt(request.params(":id"));		
		Tarefa tarefa = (Tarefa) tarefaDAO.get(id);
		
		if (tarefa != null) {
			response.status(200); // success
			makeForm(FORM_DETAIL, tarefa, FORM_ORDERBY_DESCRICAO);
        } else {
            response.status(404); // 404 Not found
            String resp = "Tarefa " + id + " não encontrada.";
    		makeForm();
    		form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");     
        }

		return form;
	}

	
	public Object getToUpdate(Request request, Response response) {
		int id = Integer.parseInt(request.params(":id"));		
		Tarefa tarefa = (Tarefa) tarefaDAO.get(id);
		
		if (tarefa != null) {
			response.status(200); // success
			makeForm(FORM_UPDATE, tarefa, FORM_ORDERBY_DESCRICAO);
        } else {
            response.status(404); // 404 Not found
            String resp = "Tarefa " + id + " não encontrada.";
    		makeForm();
    		form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");     
        }

		return form;
	}
	
	
	public Object getAll(Request request, Response response) {
		int orderBy = Integer.parseInt(request.params(":orderby"));
		makeForm(orderBy);
	    response.header("Content-Type", "text/html");
	    response.header("Content-Encoding", "UTF-8");
		return form;
	}			
	
	public Object update(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
		Tarefa tarefa = tarefaDAO.get(id);
        String resp = "";       

        if (tarefa != null) {
        	tarefa.setDescricao(request.queryParams("descricao"));
        	tarefa.setProgresso(Float.parseFloat(request.queryParams("progresso")));
        	tarefa.setPrioridade(Integer.parseInt(request.queryParams("prioridade")));
        	tarefa.setDataInicio(LocalDateTime.parse(request.queryParams("dataInicio")));
        	tarefa.setDataEntrega(LocalDate.parse(request.queryParams("dataEntrega")));
        	tarefaDAO.update(tarefa);
        	response.status(200); // success
            resp = "Tarefa (ID " + tarefa.getID() + ") atualizada!";
        } else {
            response.status(404); // 404 Not found
            resp = "Tarefa (ID \" + tarefa.getId() + \") não encontrada!";
        }
		makeForm();
		return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
	}

	
	public Object delete(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Tarefa tarefa = tarefaDAO.get(id);
        String resp = "";       

        if (tarefa != null) {
            tarefaDAO.delete(id);
            response.status(200); // success
            resp = "Tarefa (" + id + ") excluída!";
        } else {
            response.status(404); // 404 Not found
            resp = "Tarefa (" + id + ") não encontrada!";
        }
		makeForm();
		return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
	}
}