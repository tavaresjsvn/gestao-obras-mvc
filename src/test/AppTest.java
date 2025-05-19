package test;

import controller.EngenheiroController;
import controller.MaterialController;
import controller.ObraController;
import model.*;
import persistence.EngenheiroRepository;
import persistence.MaterialRepository;
import persistence.ObraRepository;

import java.time.LocalDate;

public class AppTest {

    public static void main(String[] args) {

        // Repositórios
        EngenheiroRepository engRepo = new EngenheiroRepository();
        MaterialRepository matRepo = new MaterialRepository();
        ObraRepository obraRepo = new ObraRepository();

        // Controladores
        EngenheiroController engController = new EngenheiroController(engRepo, null);
        ObraController obraController = new ObraController(obraRepo, engController);
        engController.setObraController(obraController);
        MaterialController matController = new MaterialController(matRepo);

        System.out.println("Testes do Sistema");

        // Cadastrar engenheiros
        Engenheiro e1 = new Engenheiro("12345-5/PE", "Carlos Silva", Especialidade.ESTRUTURAL);
        Engenheiro e2 = new Engenheiro("67890-3/SP", "Marina Rocha", Especialidade.ELETRICA);
        Engenheiro e3 = new Engenheiro("00000-2/RJ", "CREA inválido", Especialidade.ARQUITETURA);

        cadastrarEngenheiro(engController, e1);
        cadastrarEngenheiro(engController, e2);
        cadastrarEngenheiro(engController, e3);

        // Cadastrar materiais
        Material cimento = new Material(10, "Cimento CP-V", 200, UnidadeMedida.QUILOGRAMA);
        Material ferro = new Material(11, "Ferro 3/8", 150, UnidadeMedida.METRO);
        cadastrarMaterial(matController, cimento);
        cadastrarMaterial(matController, ferro);

        // Criar obra válida
        Obra obra1 = new Obra(1, "Edifício Solaris", "Recife", 750000, Status.EM_ANDAMENTO, LocalDate.now(), LocalDate.now().plusDays(30));
        obra1.adicionarEtapa("Terraplenagem");
        obra1.adicionarEtapa("Fundação");
        obra1.adicionarEtapa("Estrutura");

        cadastrarObra(obraController, obra1, e1); // engenheiro responsável

        // Criar obra atrasada
        Obra obra2 = new Obra(2, "Galpão Logístico", "São Paulo", 300000, Status.EM_ANDAMENTO, LocalDate.now().minusDays(50), LocalDate.now().minusDays(10));
        obra2.adicionarEtapa("Planejamento");
        obra2.adicionarEtapa("Execução");
        cadastrarObra(obraController, obra2, e2);

        // Testar avanço de etapa
        try {
            obra1.avancarEtapa();
            System.out.println("Etapa avançada: agora em " + obra1.getEtapaAtual());
        } catch (Exception ex) {
            System.out.println("Erro ao avançar etapa: " + ex.getMessage());
        }

        // Testar obra sem etapas
        Obra obraInvalida = new Obra(3, "Sem Etapas Inc.", "RJ", 150000, Status.EM_ANDAMENTO, LocalDate.now(), LocalDate.now().plusDays(20));
        cadastrarObra(obraController, obraInvalida, e1); // deve falhar

        // Verificar atraso e status atualizado
        obraController.verificarObrasAtrasadas();
        System.out.println("Obra 2 status atual: " + obra2.getStatus()); // deve virar ATRASADA

        // Alocar mais um engenheiro na obra
        try {
            obra1.alocarEngenheiro(e2);
            System.out.println("Engenheiro adicional alocado à obra 1.");
        } catch (Exception ex) {
            System.out.println("Falha ao alocar engenheiro adicional: " + ex.getMessage());
        }

        // Remover engenheiro e atualizar nas obras
        try {
            engController.removerEngenheiro(e2.getRegistroCREA());
            System.out.println("Engenheiro " + e2.getNome() + " removido com sucesso!");
        } catch (Exception ex) {
            System.out.println("Erro ao remover engenheiro: " + ex.getMessage());
        }

        // Buscar obra por código
        try {
            Obra buscada = obraController.buscarObraPorCodigo(1);
            System.out.println("Obra encontrada: " + buscada.getNome() + " | Etapa atual: " + buscada.getEtapaAtual());
        } catch (Exception e) {
            System.out.println("Obra não encontrada.");
        }

        System.out.println("Testes Finalizados");
    }

    private static void cadastrarEngenheiro(EngenheiroController controller, Engenheiro e) {
        try {
            controller.cadastrarEngenheiro(e);
            System.out.println("Engenheiro cadastrado: " + e.getNome());
        } catch (Exception ex) {
            System.out.println("Falha ao cadastrar engenheiro " + e.getNome() + ": " + ex.getMessage());
        }
    }

    private static void cadastrarMaterial(MaterialController controller, Material m) {
        try {
            controller.adicionarMaterial(m);
            System.out.println("Material cadastrado: " + m.getNome());
        } catch (Exception ex) {
            System.out.println("Falha ao cadastrar material " + m.getNome() + ": " + ex.getMessage());
        }
    }

    private static void cadastrarObra(ObraController controller, Obra o, Engenheiro responsavel) {
        try {
            controller.cadastrarObra(o, responsavel);
            System.out.println("Obra cadastrada: " + o.getNome());
        } catch (Exception ex) {
            System.out.println("Falha ao cadastrar obra " + o.getNome() + ": " + ex.getMessage());
        }
    }
}

