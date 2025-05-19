package view;
import controller.MaterialController;
import model.Material;
import model.UnidadeMedida;
import java.util.Map;
import java.util.Scanner;

public class MaterialView {
	
    private static final Scanner input = new Scanner(System.in);
    private static MaterialController materialController;

    public static void configurarController(MaterialController controller) {
        materialController = controller;
    }

    public static void exibirMenu() {
        int opcao;

        do {
            System.out.println("\nMenu de Materiais:");
            System.out.println("Escolha uma opção: ");
            System.out.println("1 - Cadastrar Material");
            System.out.println("2 - Editar Material");
            System.out.println("3 - Remover Material");
            System.out.println("4 - Listar Materiais");
            System.out.println("5 - Buscar Material por Código");
            System.out.print("0 - Voltar");
            opcao = Integer.parseInt(input.nextLine());

            switch (opcao) {
                case 1 -> cadastrarMaterial();
                case 2 -> editarMaterial();
                case 3 -> removerMaterial();
                case 4 -> listarMateriais();
                case 5 -> buscarMaterial();
                case 0 -> System.out.println("Retornar ao menu principal");
                default -> System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    private static void cadastrarMaterial() {
        try {
        	
            System.out.print("Código: ");
            int codigo = Integer.parseInt(input.nextLine());

            System.out.print("Nome: ");
            String nome = input.nextLine();

            System.out.println("Unidade de Medida - Opções\n"
            		+ "(digite conforme o apresentado abaixo)"
            		+ " METRO\n" 
            		+ " METRO_QUADRADO\n" 
            		+ " METRO_CUBICO\n" 
            		+ " QUILOGRAMA\n" 
            		+ " GRAMA\n" 
            		+ " LITRO\n" 
            		+ " UNIDADE: ");
            UnidadeMedida unidade = UnidadeMedida.valueOf(input.nextLine().toUpperCase());
            
            System.out.print("Quantidade: ");
            int quantidade = Integer.parseInt(input.nextLine());

            
            Material m = new Material(codigo, nome, quantidade, unidade);
            materialController.adicionarMaterial(m);

            System.out.println("Material cadastrado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar: " + e.getMessage());
        }
    }

    private static void editarMaterial() {
        try {
            System.out.print("Código do material: ");
            int codigo = Integer.parseInt(input.nextLine());

            System.out.print("Escolha o campo para editar: ");
            System.out.println("1 - Editar nome");
            System.out.println("2 - Editar quantidade");
            System.out.println("3 - Editar unidade");
            int campo = Integer.parseInt(input.nextLine());

            switch (campo) {
                case 1 -> {
                    System.out.print("Novo nome: ");
                    String nome = input.nextLine();
                    materialController.editarNome(codigo, nome);
                }
                case 2 -> {
                    System.out.print("Nova quantidade: ");
                    int qtd = Integer.parseInt(input.nextLine());
                    materialController.editarQuantidade(codigo, qtd);
                }
                case 3 -> {
                    System.out.print(" Nova Unidade de Medida - Opções\n"
                    		+ "(digite conforme o apresentado abaixo)"
                    		+ " METRO\n " 
                    		+ " METRO_QUADRADO\n " 
                    		+ " METRO_CUBICO\n " 
                    		+ " QUILOGRAMA\n " 
                    		+ " GRAMA\n " 
                    		+ " LITRO\n " 
                    		+ " UNIDADE:  ");
                    UnidadeMedida unidade = UnidadeMedida.valueOf(input.nextLine().toUpperCase());
                    materialController.editarUnidade(codigo, unidade);
                }
                default -> System.out.println("Opção inválida!");
            }

            System.out.println("Material atualizado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao atualizar: " + e.getMessage());
        }
    }

    private static void removerMaterial() {
        try {
            System.out.print("Código: ");
            int codigo = Integer.parseInt(input.nextLine());
            materialController.removerMaterial(codigo);
            System.out.println("Material removido com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao remover: " + e.getMessage());
        }
    }

    private static void listarMateriais() {
        Map<Integer, Material> materiais = materialController.listarMateriaisCadastrados();
        if (materiais.isEmpty()) {
            System.out.println("Nenhum material cadastrado.");
        } else {
            System.out.println("\nLista de Materiais:");
            for (Material m : materiais.values()) {
                System.out.printf("- [%d] %s (%d %s)\n", m.getCodigo(), m.getNome(), m.getQuantidade(), m.getUnidadeMedida());
            }
        }
    }

    private static void buscarMaterial() {
        try {
            System.out.print("Código: ");
            int codigo = Integer.parseInt(input.nextLine());
            Material m = materialController.buscarMaterialPorCodigo(codigo);
            System.out.printf("Encontrado: [%d] %s (%d %s)\n", m.getCodigo(), m.getNome(), m.getQuantidade(), m.getUnidadeMedida());
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}