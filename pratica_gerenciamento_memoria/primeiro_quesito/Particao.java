public class Particao {
    private int tamanho;
    private Processo processoAlocado;

    public Particao(int tamanho) {
        this.tamanho = tamanho;
        this.processoAlocado = null;  // Nenhum processo alocado inicialmente
    }

    // Método para alocar um processo na partição
    public void alocarProcesso(Processo processo) {
        if (estaDisponivel()) {
            this.processoAlocado = processo;
        }
    }

    // Método para remover um processo da partição
    public void removerProcesso() {
        this.processoAlocado = null;
    }

    // Verifica se a partição está disponível (sem processo alocado)
    public boolean estaDisponivel() {
        return processoAlocado == null;
    }

    // Retorna o processo alocado na partição
    public Processo getProcesso() {
        return processoAlocado;
    }

    // Retorna o tamanho da partição
    public int getTamanho() {
        return tamanho;
    }

    // Imprime o estado da partição
    public void imprimirEstadoParticao() {
        if (estaDisponivel()) {
            System.out.println("Partição Livre (" + tamanho + " MB)");
        } else {
            System.out.println("Partição Ocupada por: " + processoAlocado.getNome() + " (ID: " + processoAlocado.getId() + ")");
        }
    }
}
