package view;

import controller.EngenheiroController;
import model.Engenheiro;
import model.Especialidade;
import model.Obra;
import java.util.Map;
import java.util.Scanner;

public class EngenheiroView {
	
    private static final Scanner input = new Scanner(System.in);
    private static EngenheiroController engenheiroController;

    public static void configurarController(EngenheiroController controller) {
        engenheiroController = controller;
    }

    public static void exibirMenu() {
        int opcao;

        do {
            System.out.println("\nMenu de Engenheiros: ");
            System.out.println("Escolha uma opção: ");
            System.out.println("1 - Cadastrar Engenheiro");
            System.out.println("2 - Editar Engenheiro");
            System.out.println("3 - Remover Engenheiro");
            System.out.println("4 - Listar Engenheiros");
            System.out.println("5 - Buscar por CREA");
            System.out.print("0 - Voltar");
            opcao = Integer.parseInt(input.nextLine());

            switch (opcao) {
                case 1 -> cadastrarEngenheiro();
                case 2 -> editarEngenheiro();
                case 3 -> removerEngenheiro();
                case 4 -> listarEngenheiros();
                case 5 -> buscarEngenheiro();
                case 0 -> System.out.println("Retornar ao menu principal");
                default -> System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    private static void cadastrarEngenheiro() {
        try {
            System.out.print("Registro CREA: ");
            String crea = input.nextLine();

            System.out.print("Nome: ");
            String nome = input.nextLine();

            System.out.print("Especialidade - Opções:\n"
            		+ "(digite conforme o apresentado abaixo)"
            		+ "ESTRUTURAL\n"
            		+ "HIDRAULICA\n"
            		+ "ELETRICA\n"
            		+ "GEOTECNICA\n"
            		+ "ARQUITETURA\n"
            		+ "OUTRA");
            Especialidade especialidade = Especialidade.valueOf(input.nextLine().toUpperCase());

            Engenheiro engenheiro = new Engenheiro(crea, nome, especialidade);
            engenheiroController.cadastrarEngenheiro(engenheiro);
            System.out.println("Engenheiro cadastrado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar: " + e.getMessage());
        }
    }

    private static void editarEngenheiro() {
        try {
            System.out.print("CREA do engenheiro a editar: ");
            String crea = input.nextLine();

            System.out.println("Escolha o campo para editar: ");
            System.out.println("1 - Editar nome");
            System.out.println("2 - Editar especialidade");
            System.out.print("3 - Editar registro CREA");
            int campo = Integer.parseInt(input.nextLine());

            switch (campo) {
                case 1 -> {
                    System.out.print("Novo nome: ");
                    String novoNome = input.nextLine();
                    engenheiroController.editarNomeEngenheiro(crea, novoNome);
                }
                case 2 -> {
                    System.out.print("Nova especialidade - Opções:\n"
                    		+ "(digite conforme o apresentado abaixo)\n"
                    		+ "ESTRUTURAL\n"
                    		+ "HIDRAULICA\n"
                    		+ "ELETRICA\n"
                    		+ "GEOTECNICA\n"
                    		+ "ARQUITETURA\n"
                    		+ "OUTRA");
                    Especialidade novaEsp = Especialidade.valueOf(input.nextLine().toUpperCase());
                    engenheiroController.editarEspecialidadeEngenheiro(crea, novaEsp);
                }
                case 3 -> {
                    System.out.print("Novo CREA: ");
                    String novoCrea = input.nextLine();
                    engenheiroController.editarRegistroCREA(crea, novoCrea);
                }
                default -> System.out.println("Opção inválida.");
            }

            System.out.println("Engenheiro atualizado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao atualizar: " + e.getMessage());
        }
    }

    private static void removerEngenheiro() {
        try {
            System.out.print("CREA do engenheiro a remover: ");
            String crea = input.nextLine();
            engenheiroController.removerEngenheiro(crea);
            System.out.println("Engenheiro removido com sucessoa!");
        } catch (Exception e) {
            System.out.println("Erro ao remover: " + e.getMessage());
        }
    }

    private static void listarEngenheiros() {
        Map<String, Engenheiro> mapa = engenheiroController.listarEngenheirosCadastrados();

        if (mapa.isEmpty()) {
            System.out.println("Nenhum engenheiro cadastrado.");
        } else {
            System.out.println("\nLista de Engenheiros:");
            for (Engenheiro e : mapa.values()) {
                System.out.printf("- [%s] %s (%s)\n", e.getRegistroCREA(), e.getNome(), e.getEspecialidade());
            }
        }
    }

    private static void buscarEngenheiro() {
        try {
            System.out.print("CREA do engenheiro: ");
            String crea = input.nextLine();
            Engenheiro e = engenheiroController.buscarPorCREA(crea);
            System.out.printf("[%s] %s (%s)%n", e.getRegistroCREA(), e.getNome(), e.getEspecialidade());

            if (!e.getObrasAssociadas().isEmpty()) {
                System.out.println("Obras associadas:");
                for (Obra o : e.getObrasAssociadas().values()) {
                    System.out.printf("- [%d] %s (%s)%n", o.getCodigo(), o.getNome(), o.getStatus());
                }
            }

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}