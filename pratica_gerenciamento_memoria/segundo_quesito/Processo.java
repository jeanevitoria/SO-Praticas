package pratica_gerenciamento_memoria.segundo_quesito;

class Processo {
    private String nome;
    private int id;
    private int tamanho;

    public Processo(String nome, int id, int tamanho) {
        this.nome = nome;
        this.id = id;
        this.tamanho = tamanho;
    }

    public String getNome() {
        return nome;
    }

    public int getId() {
        return id;
    }

    public int getTamanho() {
        return tamanho;
    }
}