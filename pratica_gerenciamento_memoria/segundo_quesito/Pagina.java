package pratica_gerenciamento_memoria.segundo_quesito;

import java.util.Objects;
class Pagina {
    private int id;
    private Processo processo;

    public Pagina(int i, Processo processo) {
        this.id = i;
        this.processo = processo;
    }

    public int getId() {
        return id;
    }

    public Processo getProcesso() {
        return processo;
    }

    public void setProcesso(Processo processo) {
        this.processo = processo;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Pagina pagina = (Pagina) obj;
        return id == pagina.id && processo.getId() == pagina.processo.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, processo.getId());
    }
}

