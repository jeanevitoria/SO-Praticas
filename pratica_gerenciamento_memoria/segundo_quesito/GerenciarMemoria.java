package pratica_gerenciamento_memoria.segundo_quesito;

import java.util.*;

public class GerenciarMemoria {
    private int tamanhoMemoriaFisica;
    private int tamanhoMemoriaVirtual;
    private int tamanhoPagina;
    private Queue<Pagina> memoriaFisica; // Usar Queue para memória física (FIFO)
    private Queue<Pagina> memoriaVirtual; // Usar Queue para memória virtual (FIFO)
    private Set<Processo> processosAlocados; // Para rastrear os processos já alocados
    private int totalPageMisses; // Contador de page misses

    public GerenciarMemoria(int tamanhoMemoriaFisica, int tamanhoMemoriaVirtual, int tamanhoPagina) {
        this.tamanhoMemoriaFisica = tamanhoMemoriaFisica;
        this.tamanhoMemoriaVirtual = tamanhoMemoriaVirtual;
        this.tamanhoPagina = tamanhoPagina;
        this.totalPageMisses = 0;

        // Inicializando as memórias como filas (FIFO)
        memoriaFisica = new LinkedList<>();
        memoriaVirtual = new LinkedList<>();
        processosAlocados = new HashSet<>();
    }

    public void adicionarProcesso() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nome do processo: ");
        String nomeProcesso = scanner.nextLine();
        System.out.print("Tamanho do processo (MB): ");
        int tamanhoProcesso = scanner.nextInt();
        scanner.nextLine(); // Limpar o buffer após nextInt()

        // Gerar um id único para o processo
        int idProcesso = gerarIdUnico();

        Processo processo = new Processo(nomeProcesso, idProcesso, tamanhoProcesso);

        // Calcular o número de páginas necessárias
        int numPaginas = (int) Math.ceil((double) tamanhoProcesso / tamanhoPagina);

        // Verificar se o processo já foi alocado
        if (processosAlocados.contains(processo)) {
            System.out.println("O processo já foi alocado.");
            return;
        }

        // Verificar se há espaço suficiente para alocar o processo
        if (!alocarProcessoNaMemoriaFisica(processo, numPaginas)) {
            // Se não for possível alocar na memória física, mover processos para a memória
            // virtual
            System.out.println("Não há memória suficiente para alocar o processo.");
        } else {
            System.out.println("Processo alocado com sucesso na memória física.");
        }

        // Marcar o processo como alocado
        processosAlocados.add(processo);
    }

    private boolean alocarProcessoNaMemoriaFisica(Processo processo, int numPaginas) {
        int paginasAlocadas = 0;

        // Tenta alocar o processo na memória física
        for (int i = 0; i < numPaginas; i++) {
            // Cria uma nova página para o processo com um identificador único para a página
            Pagina pagina = new Pagina(i, processo); // Usando id único para a página

            // Verifica se a página já está na memória física (se houver page miss)
            boolean paginaNaMemoria = false;
            for (Pagina p : memoriaFisica) {
                if (p.getProcesso().equals(processo) && p.getId() == pagina.getId()) {
                    paginaNaMemoria = true;
                    break;
                }
            }

            if (!paginaNaMemoria) {
                // Page miss, aumenta o contador
                totalPageMisses++;
                System.out.println(
                        "Page miss: Página " + pagina.getId() + " não encontrada na memória física. Carregando...");
            }

            // Se a memória física estiver cheia, removemos o processo mais antigo (FIFO)
            while (memoriaFisica.size() + numPaginas - paginasAlocadas > (tamanhoMemoriaFisica / tamanhoPagina)) {
                // Remove a página mais antiga
                Pagina paginaRemovida = memoriaFisica.poll();
                // Verifica se a página removida pertence ao processo que será movido para a
                // memória virtual
                if (paginaRemovida.getProcesso() != null) {
                    Processo processoRemovido = paginaRemovida.getProcesso();
                    // Cria uma lista temporária para armazenar as páginas a serem movidas
                    List<Pagina> paginasParaMover = new ArrayList<>();
                    // Move todas as páginas desse processo para a memória virtual
                    System.out.println("Movendo todas as páginas do processo " + processoRemovido.getNome()
                            + " para a memória virtual.");
                    for (Pagina paginaRemovidaDoProcesso : memoriaFisica) {
                        if (paginaRemovidaDoProcesso.getProcesso().equals(processoRemovido)) {
                            paginasParaMover.add(paginaRemovidaDoProcesso); // Armazena a página para remoção
                        }
                    }
                    // Após a iteração, remove as páginas que devem ser movidas para a memória
                    // virtual
                    for (Pagina paginaParaMover : paginasParaMover) {
                        memoriaFisica.remove(paginaParaMover);
                        memoriaVirtual.add(paginaParaMover);
                        System.out.println("Página " + paginaParaMover.getId() + " movida para a memória virtual.");
                    }
                }
            }

            // Aloca a página do novo processo na memória física
            memoriaFisica.add(pagina);
            paginasAlocadas++;
        }

        return paginasAlocadas == numPaginas;
    }

    public void exibirEstadoMemoria() {
        System.out.println("Estado da Memória Física:");
        for (Pagina pagina : memoriaFisica) {
            System.out.println("Página " + pagina.getId() + ": Processo " + pagina.getProcesso().getNome());
        }

        System.out.println("Estado da Memória Virtual:");
        for (Pagina pagina : memoriaVirtual) {
            System.out.println("Página " + pagina.getId() + ": Processo " + pagina.getProcesso().getNome());
        }

        System.out.println("\nDetalhes da memória:");
        int paginasLivresFisica = (tamanhoMemoriaFisica / tamanhoPagina) - memoriaFisica.size();
        System.out.println("Páginas livres na memória física: " + paginasLivresFisica);

        // Cálculo da fragmentação interna
        int fragmentacaoInterna = 0;
        Set<Processo> processosCalculados = new HashSet<>();

        for (Pagina pagina : memoriaFisica) {
            Processo processo = pagina.getProcesso();

            // Verifica se o processo já foi calculado
            if (!processosCalculados.contains(processo)) {
                processosCalculados.add(processo);

                // Calcula a fragmentação interna apenas para a última página do processo
                int tamanhoRestante = processo.getTamanho() % tamanhoPagina;

                if (tamanhoRestante != 0) {
                    fragmentacaoInterna += tamanhoPagina - tamanhoRestante;
                }
            }
        }

        System.out.println("Fragmentação interna: " + fragmentacaoInterna + " MB");

        System.out.println("Total de Page Misses: " + totalPageMisses);
    }

    public void exibirEstatisticas() {
        System.out.println("Total de Page Misses: " + totalPageMisses);
    }

    private int gerarIdUnico() {
        return (int) (Math.random() * 100000); // Gerar um id aleatório para o processo
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Defina o tamanho da memória física (MB): ");
        int tamanhoMemoriaFisica = scanner.nextInt();
        System.out.print("Defina o tamanho da memória virtual (MB): ");
        int tamanhoMemoriaVirtual = scanner.nextInt();
        System.out.print("Defina o tamanho das páginas (MB): ");
        int tamanhoPagina = scanner.nextInt();

        GerenciarMemoria gerenciarMemoria = new GerenciarMemoria(tamanhoMemoriaFisica, tamanhoMemoriaVirtual,
                tamanhoPagina);

        int opcao;
        do {
            System.out.println("\n1 - Adicionar Processo");
            System.out.println("2 - Exibir Estado da Memória");
            System.out.println("3 - Exibir Estatísticas");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    gerenciarMemoria.adicionarProcesso();
                    break;
                case 2:
                    gerenciarMemoria.exibirEstadoMemoria();
                    break;
                case 3:
                    gerenciarMemoria.exibirEstatisticas();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }
}
