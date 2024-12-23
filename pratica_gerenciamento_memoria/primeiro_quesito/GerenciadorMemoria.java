import java.util.*;

public class GerenciadorMemoria {
    private int memoriaMaxima;
    private List<Particao> particoes;
    private Queue<Processo> memoriaSecundaria;
    private List<Processo> processosAlocados;

    public GerenciadorMemoria(int memoriaMaxima, int numeroParticoes, int tamanhoParticao) {
        this.memoriaMaxima = memoriaMaxima;
        this.particoes = new ArrayList<>();
        this.memoriaSecundaria = new LinkedList<>();
        this.processosAlocados = new ArrayList<>();
        for (int i = 0; i < numeroParticoes; i++) {
            particoes.add(new Particao(tamanhoParticao));
        }
    }

    public void alocarProcesso(Processo processo) {
        if (processosAlocados.contains(processo)) {
            return;
        }

        int memoriaNecessaria = processo.getTamanho();
        List<Particao> particoesAlocadas = new ArrayList<>();

        for (int i = 0; i < particoes.size(); i++) {
            Particao particao = particoes.get(i);

            if (particao.estaDisponivel() && memoriaNecessaria > 0) {
                int espacoDisponivel = particao.getTamanho();

                if (espacoDisponivel <= memoriaNecessaria) {
                    particao.alocarProcesso(processo);
                    memoriaNecessaria -= espacoDisponivel;
                    particoesAlocadas.add(particao);
                } else if (espacoDisponivel >= memoriaNecessaria) {
                    particao.alocarProcesso(processo);
                    memoriaNecessaria = 0;
                    particoesAlocadas.add(particao);
                    break;
                }
            }

            if (memoriaNecessaria == 0) {
                break;
            }
        }

        if (memoriaNecessaria == 0) {
            processosAlocados.add(processo);
        } else {
            realizarSwapping(processo);
        }
    }

    public void realizarSwapping(Processo processo) {
        Random rand = new Random();

        List<Particao> particoesOcupadas = new ArrayList<>();
        for (Particao particao : particoes) {
            if (!particao.estaDisponivel()) {
                particoesOcupadas.add(particao);
            }
        }

        if (!particoesOcupadas.isEmpty()) {
            Particao particaoAleatoria = particoesOcupadas.get(rand.nextInt(particoesOcupadas.size()));
            Processo processoDesalocado = particaoAleatoria.getProcesso();

            for (Particao particao : particoes) {
                if (particao.getProcesso() != null && particao.getProcesso().equals(processoDesalocado)) {
                    particao.removerProcesso();
                }
            }

            memoriaSecundaria.add(processoDesalocado);
            System.out.println("Processo " + processoDesalocado.getNome() + " (ID: " + processoDesalocado.getId() + ") foi movido para a memória secundária.");
        }

        alocarProcesso(processo);
    }

    public void imprimirEstadoMemoria() {
        System.out.println("\nEstado atual da memória:");
        int espacoTotalLivre = 0;
        int espacoTotalOcupado = 0;
        int fragmentacaoInterna = 0;
        int fragmentacaoExterna = 0;

        for (Particao particoes : particoes) {
            particoes.imprimirEstadoParticao();
            if (particoes.estaDisponivel()) {
                espacoTotalLivre += particoes.getTamanho();
            } else {
                espacoTotalOcupado += particoes.getTamanho();
                Processo processo = particoes.getProcesso();
                if (processo != null && processo.getTamanho() < particoes.getTamanho()) {
                    fragmentacaoInterna += (particoes.getTamanho() - processo.getTamanho());
                }
            }
        }

        for (int i = 0; i < particoes.size(); i++) {
            Particao particao = particoes.get(i);
            if (particao.estaDisponivel()) {
                fragmentacaoExterna += particao.getTamanho();
            }
        }

        System.out.println("Capacidade total da memória: " + memoriaMaxima);
        System.out.println("Espaço total livre: " + espacoTotalLivre);
        System.out.println("Espaço total ocupado: " + espacoTotalOcupado);
        System.out.println("Fragmentação interna: " + fragmentacaoInterna);
        System.out.println("Fragmentação externa: " + fragmentacaoExterna);
        System.out.println("\nProcessos na memória secundária:");
        if (memoriaSecundaria.isEmpty()) {
            System.out.println("Nenhum processo na memória secundária.");
        } else {
            for (Processo p : memoriaSecundaria) {
                System.out.println(p.getNome() + " (ID: " + p.getId() + ")");
            }
        }
    }
}
