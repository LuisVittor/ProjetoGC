package com.criso.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.criso.persistencia.Serializador;

import java.time.LocalDate;
import java.time.YearMonth;

/**
 * Representa uma carteira financeira, contendo caixas, transações, categorias e metas.
 */
public class Carteira implements Serializable {
    /** Identificador único da carteira */
    UUID id;
    /** Lista de caixas da carteira */
    ArrayList<Caixa> caixas = new ArrayList<>();
    /** Lista de transações da carteira */
    ArrayList<Transacao> transacoes = new ArrayList<>();
    /** Lista de categorias da carteira */
    ArrayList<Categoria> categorias = new ArrayList<>();
    /** Lista de metas da carteira */
    ArrayList<Meta> metas = new ArrayList<>();

    /**
     * Cria uma nova carteira financeira.
     */
    public Carteira() {
        this.id = UUID.randomUUID();
    }

    /**
     * Adiciona um caixa à carteira.
     * @param caixa Caixa a ser adicionada
     */
    public void adicionarCaixa(Caixa caixa) {
        caixas.add(caixa);
    }
    /**
     * Remove um caixa da carteira.
     * @param caixa Caixa a ser removida
     */
    public void removerCaixa(Caixa caixa) {
        caixas.remove(caixa);
    }
    /**
     * Adiciona uma transação à carteira.
     * @param transacao Transação a ser adicionada
     */
    public void adicionarTransacao(Transacao transacao) {
        transacoes.add(transacao);
    }
    /**
     * Remove uma transação da carteira.
     * @param transacao Transação a ser removida
     */
    public void removerTransacao(Transacao transacao) {transacoes.remove(transacao);}
    /**
     * Adiciona uma categoria à carteira.
     * @param categoria Categoria a ser adicionada
     */
    public void adicionarCategoria(Categoria categoria) {categorias.add(categoria);}
    /**
     * Remove uma categoria da carteira.
     * @param categoria Categoria a ser removida
     */
    public void removerCategoria(Categoria categoria) {categorias.remove(categoria);}
    /**
     * Adiciona uma meta à carteira.
     * @param meta Meta a ser adicionada
     */
    public void adicionarMeta(Meta meta) {metas.add(meta);}
    /**
     * Remove uma meta da carteira.
     * @param meta Meta a ser removida
     */
    public void removerMeta(Meta meta) {metas.remove(meta);}

    /**
     * Retorna o identificador da carteira.
     * @return UUID da carteira
     */
    public UUID getId() {
        return id;
    }

    /**
     * Retorna a lista de caixas da carteira.
     * @return lista de caixas
     */
    public ArrayList<Caixa> getCaixas() {
        return caixas;
    }

    /**
     * Retorna a lista de transações da carteira.
     * @return lista de transações
     */
    public ArrayList<Transacao> getTransacoes() {
        return transacoes;
    }

    /**
     * Retorna a lista de categorias da carteira.
     * @return lista de categorias
     */
    public ArrayList<Categoria> getCategorias() {
        return categorias;
    }

    /**
     * Retorna a lista de metas da carteira.
     * @return lista de metas
     */
    public ArrayList<Meta> getMetas() {
        return metas;
    }

    /**
     * Atualiza uma meta existente na carteira.
     * @param metaAtualizada Meta com dados atualizados
     */
    public void atualizarMeta(Meta metaAtualizada) {
        for (int i = 0; i < metas.size();) {
            Meta metaExistente = metas.get(i);
                metaExistente.setNome(metaAtualizada.getNome());
                metaExistente.setSaldoAlvo(metaAtualizada.getSaldoAlvo());
                metaExistente.setSaldoAtual(metaAtualizada.getSaldoAtual());
                metaExistente.setStatus(metaAtualizada.getStatus());
                Serializador.serializar(this);
                System.out.println("Meta '" + metaExistente.getNome() + "' atualizada e carteira serializada.");
                return;
        }
        System.out.println("Erro: Meta com ID " + metaAtualizada.getId() + " não encontrada para atualização.");
    }

    /**
     * Atualiza uma transação existente na carteira.
     * @param transacaoAtualizada transacao com dados atualizados
     */
    public void atualizarTransacao(Transacao transacaoAtualizada) {
        for (int i = 0; i < transacoes.size(); i++) {
            if (transacoes.get(i).getId().equals(transacaoAtualizada.getId())) { 
                transacoes.set(i, transacaoAtualizada);
                Serializador.serializar(this);
                System.out.println("Transação '" + transacaoAtualizada.getNome() + "' atualizada e carteira serializada.");
                return;
            }
        }
        System.out.println("Erro: Transação com ID " + transacaoAtualizada.getId() + " não encontrada para atualização.");
    }

    /**
     * Calcula o saldo total de todos os caixas da carteira.
     * @return saldo total
     */
    public BigDecimal getSaldoTotal() {
        ArrayList<Caixa> caixas = this.getCaixas();
        BigDecimal total = BigDecimal.valueOf(0);
        for (Caixa caixa: caixas){
            total = total.add(caixa.getSaldoAtual());
        }
        return total;
    }

    /**
     * Calcula o total de receitas do mês atual.
     * @return total de receitas
     */
    public BigDecimal getTotalReceitasMes() {
        YearMonth mesAtual = YearMonth.now();
        BigDecimal totalReceitas = BigDecimal.ZERO;

        for (Transacao transacao : transacoes) {
            if (transacao instanceof Receita) {
                LocalDate dataTransacao = transacao.getData();
                YearMonth mesTransacao = YearMonth.from(dataTransacao);

                if (mesTransacao.equals(mesAtual)) {
                    totalReceitas = totalReceitas.add(transacao.getValor());
                }
            }
        }

        return totalReceitas;
    }

    /**
     * Calcula o total de despesas do mês atual.
     * @return total de despesas
     */
    public BigDecimal getTotalDespesasMes() {
        YearMonth mesAtual = YearMonth.now();
        BigDecimal totalDespesas = BigDecimal.ZERO;

        for (Transacao transacao : transacoes) {
            if (transacao instanceof Despesa) {
                LocalDate dataTransacao = transacao.getData();
                YearMonth mesTransacao = YearMonth.from(dataTransacao);

                if (mesTransacao.equals(mesAtual)) {
                    totalDespesas = totalDespesas.add(transacao.getValor());
                }
            }
        }

        return totalDespesas;
    }

    /**
     * Lista as transações mais recentes da carteira.
     * @param numTransacoesExibidas Número de transações a exibir
     * @return lista de transações recentes
     */
    public List<Transacao> listarTransacoesRecentes(int numTransacoesExibidas) {
        if (transacoes.isEmpty()) {
            return new ArrayList<>();
        }
        List<Transacao> transacoesOrdenadas = new ArrayList<>(transacoes);
        transacoesOrdenadas.sort((t1, t2) -> t2.getData().compareTo(t1.getData()));
        int tamanhoLista = transacoesOrdenadas.size();
        int numTransacoes = Math.min(numTransacoesExibidas, tamanhoLista);
        return transacoesOrdenadas.subList(0, numTransacoes);
    }
}
