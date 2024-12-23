import java.util.*;

public class Principal {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            // Definir o tamanho máximo da memória
            System.out.print("Digite o tamanho máximo da memória (em MB): ");
            int memoriaMaxima = scanner.nextInt();

            // Definir o número de partições
            System.out.print("Digite o número de partições: ");
            int numeroParticoes = scanner.nextInt();

            // Definir o tamanho das partições (mesmo tamanho para todas)
            System.out.print("Digite o tamanho de cada partição (em MB): ");
            int tamanhoParticao = scanner.nextInt();

            // Inicializando o Gerenciador de Memória
            GerenciadorMemoria gerenciadorMemoria = new GerenciadorMemoria(memoriaMaxima, numeroParticoes, tamanhoParticao);

            List<Processo> processos = new ArrayList<>();
            scanner.nextLine(); // Consumir a quebra de linha

            while (true) {
                System.out.println("\n--- MENU ---");
                System.out.println("1. Criar Processo");
                System.out.println("2. Alocar Processos");
                System.out.println("3. Exibir Estado da Memória");
                System.out.println("4. Sair");
                System.out.print("Escolha uma opção: ");
                int opcao = scanner.nextInt();
                scanner.nextLine(); // Consumir a quebra de linha

                if (opcao == 1) {
                    // Criar processo
                    System.out.print("Digite o nome do processo: ");
                    String nome = scanner.nextLine();
                    System.out.print("Digite o tamanho do processo (em MB): ");
                    int tamanho = scanner.nextInt();
                    int id = new Random().nextInt(1000); // Gerar um ID aleatório para o processo

                    Processo processo = new Processo(nome, id, tamanho);
                    processos.add(processo);
                    System.out.println("Processo " + nome + " criado com sucesso.");
                } else if (opcao == 2) {
                    // Alocar processos
                    System.out.println("\nIniciando a alocação...");
                    for (Processo processo : processos) {
                        gerenciadorMemoria.alocarProcesso(processo);
                    }
                } else if (opcao == 3) {
                    // Exibir o estado da memória
                    gerenciadorMemoria.imprimirEstadoMemoria();
                } else if (opcao == 4) {
                    // Sair
                    System.out.println("Saindo...");
                    break;
                } else {
                    System.out.println("Opção inválida! Tente novamente.");
                }
            }

            System.out.print("\nDeseja repetir o processo (S/N)? ");
            String repetir = scanner.nextLine();
            if (!repetir.equalsIgnoreCase("S")) {
                break;
            }
        }
        scanner.close();
    }
}
