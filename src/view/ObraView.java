package view;

import controller.ObraController;
import controller.EngenheiroController;
import model.Obra;
import model.Status;
import model.Engenheiro;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Scanner;

public class ObraView {
	
    private static final Scanner input = new Scanner(System.in);
    private static ObraController obraController;
    private static EngenheiroController engenheiroController;

    public static void configurarControladores(ObraController oController, EngenheiroController eController) {
        obraController = oController;
        engenheiroController = eController;
    }

    public static void exibirMenu() {
        int opcao;

        do {
            System.out.println("\nMenu de Obras");
            System.out.println("Escolha uma opção: ");
            System.out.println("1 - Cadastrar Obra");
            System.out.println("2 - Editar Obra");
            System.out.println("3 - Remover Obra");
            System.out.println("4 - Listar Obras");
            System.out.println("5 - Buscar por Código");
            System.out.println("0 - Voltar");
            opcao = Integer.parseInt(input.nextLine());

            switch (opcao) {
                case 1 -> cadastrarObra();
                case 2 -> editarObra();
                case 3 -> removerObra();
                case 4 -> listarObras();
                case 5 -> buscarObra();
                case 0 -> System.out.println("Retornar ao menu principal");
                default -> System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    private static void cadastrarObra() {
        try {
            System.out.print("Código da obra: ");
            int codigo = Integer.parseInt(input.nextLine());

            System.out.print("Nome da obra: ");
            String nome = input.nextLine();

            System.out.print("Localização: ");
            String local = input.nextLine();

            System.out.print("Orçamento: ");
            double orcamento = Double.parseDouble(input.nextLine());

            System.out.print("Data de início (YYYY-MM-DD): ");
            LocalDate dataInicio = LocalDate.parse(input.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE);

            System.out.print("Data de entrega prevista (YYYY-MM-DD): ");
            LocalDate dataPrevista = LocalDate.parse(input.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE);

            System.out.print("CREA do engenheiro responsável: ");
            String crea = input.nextLine();

            Engenheiro engenheiro = engenheiroController.buscarPorCREA(crea);

            Obra obra = new Obra(codigo, nome, local, orcamento, Status.EM_ANDAMENTO, dataInicio, dataPrevista);

            System.out.println("Digite as etapas da obra (digite 'fim' para encerrar):");
            String etapa;
            while (!(etapa = input.nextLine()).equalsIgnoreCase("fim")) {
                obra.adicionarEtapa(etapa);
            }

            obraController.cadastrarObra(obra, engenheiro);
            System.out.println("Obra cadastrada com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao cadastrar: " + e.getMessage());
        }
    }

    private static void editarObra() {
        try {
            System.out.print("Código da obra: ");
            int codigo = Integer.parseInt(input.nextLine());

            System.out.print("Escolha o campo para editar: ");
            System.out.println("1 - Editar nome");
            System.out.println("2 - Editar localização");
            System.out.println("3 - Editar orçamento");
            System.out.println("4 - Editar data de entrega prevista");
            int campo = Integer.parseInt(input.nextLine());

            switch (campo) {
                case 1 -> {
                    System.out.print("Novo nome: ");
                    obraController.editarNome(codigo, input.nextLine());
                }
                case 2 -> {
                    System.out.print("Nova localização: ");
                    obraController.editarLocalizacao(codigo, input.nextLine());
                }
                case 3 -> {
                    System.out.print("Novo orçamento: ");
                    obraController.editarOrcamento(codigo, Double.parseDouble(input.nextLine()));
                }
                case 4 -> {
                    System.out.print("Nova data de entrega prevista (YYYY-MM-DD): ");
                    LocalDate novaData = LocalDate.parse(input.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE);
                    obraController.editarDataEntregaPrevista(codigo, novaData);
                }
                default -> System.out.println("Opção inválida!");
            }

            System.out.println("Obra atualizada com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao atualizar: " + e.getMessage());
        }
    }

    private static void removerObra() {
        try {
            System.out.print("Código da obra a remover: ");
            int codigo = Integer.parseInt(input.nextLine());
            obraController.removerObra(codigo);
            System.out.println("Obra removida com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao remover obra: " + e.getMessage());
        }
    }

    private static void listarObras() {
        Map<Integer, Obra> obras = obraController.listarObras();

        if (obras.isEmpty()) {
            System.out.println("Nenhuma obra cadastrada.");
        } else {
            System.out.println("\nLista de Obras:");
            for (Obra o : obras.values()) {
                System.out.printf("- [%d] %s (%s) | Local: %s | Orçamento: R$ %.2f%n",
                        o.getCodigo(), o.getNome(), o.getStatus(), o.getLocalizacao(), o.getOrcamento());
            }
        }
    }

    private static void buscarObra() {
        try {
            System.out.print("Código da obra: ");
            int codigo = Integer.parseInt(input.nextLine());
            Obra o = obraController.buscarObraPorCodigo(codigo);

            System.out.printf("[%d] %s | Status: %s%n", o.getCodigo(), o.getNome(), o.getStatus());
            System.out.println("Localização: " + o.getLocalizacao());
            System.out.println("Orçamento: R$ " + o.getOrcamento());
            System.out.println("Data de início: " + o.getDataInicio());
            System.out.println("Entrega prevista: " + o.getDataEntregaPrevista());

            if (!o.getEtapas().isEmpty()) {
                System.out.println("Etapas:");
                for (String etapa : o.getEtapas()) {
                    System.out.println("  - " + etapa);
                }
                System.out.println("Etapa atual: " + o.getEtapaAtual());
            }

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}

