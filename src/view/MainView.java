package view;

import controller.EngenheiroController;
import controller.MaterialController;
import controller.ObraController;
import persistence.EngenheiroRepository;
import persistence.MaterialRepository;
import persistence.ObraRepository;

import java.util.Scanner;

public class MainView {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
   
        EngenheiroRepository engenheiroRepo = new EngenheiroRepository();
        MaterialRepository materialRepo = new MaterialRepository();
        ObraRepository obraRepo = new ObraRepository();

        EngenheiroController engenheiroController = new EngenheiroController(engenheiroRepo, null);
        MaterialController materialController = new MaterialController(materialRepo);
        ObraController obraController = new ObraController(obraRepo, engenheiroController);

        engenheiroController.setObraController(obraController);

        EngenheiroView.configurarController(engenheiroController);
        MaterialView.configurarController(materialController);
        ObraView.configurarControladores(obraController, engenheiroController);

        int opcao;
        do {
            System.out.println("\nSistema de Gestão de Obras Civis");
            System.out.println("Escolha uma opção: ");
            System.out.println("1 - Gerenciar Engenheiros");
            System.out.println("2 - Gerenciar Materiais");
            System.out.println("3 - Gerenciar Obras");
            System.out.println("0 - Sair");
            opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1 -> EngenheiroView.exibirMenu();
                case 2 -> MaterialView.exibirMenu();
                case 3 -> ObraView.exibirMenu();
                case 0 -> System.out.println("Sair");
                default -> System.out.println("Opção inválida!");
            }

        } while (opcao != 0);
    }
}
