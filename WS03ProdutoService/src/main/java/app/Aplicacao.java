package app;

import static spark.Spark.*;
import service.TarefaService;


public class Aplicacao {
	
	private static TarefaService tarefaService = new TarefaService();
	
    public static void main(String[] args) {
        port(6793);
        
        staticFiles.location("/public");
        
        post("/tarefa/insert", (request, response) -> tarefaService.insert(request, response));

        get("/tarefa/:id", (request, response) -> tarefaService.get(request, response));
        
        get("/tarefa/list/:orderby", (request, response) -> tarefaService.getAll(request, response));

        get("/tarefa/update/:id", (request, response) -> tarefaService.getToUpdate(request, response));
        
        post("/tarefa/update/:id", (request, response) -> tarefaService.update(request, response));
           
        get("/tarefa/delete/:id", (request, response) -> tarefaService.delete(request, response));

             
    }
}