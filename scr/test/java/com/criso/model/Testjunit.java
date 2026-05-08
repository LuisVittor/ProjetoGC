package com.criso.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe principal que agrupa todos os testes do modelo financeiro de forma COMPLETA.
 * Cada classe aninhada testa cenários comuns, casos de borda e diferentes lógicas.
 */
@DisplayName("Testes Completos do Modelo Financeiro")
class SistemaFinanceiroCompletoTest {

    // --- Testes para a Classe Caixa ---
    @Nested
    @DisplayName("Testes Completos da Classe Caixa")
    class CaixaNestedTest {
        private Caixa caixa;

        @BeforeEach
        void setUp() {
            caixa = new Caixa("Conta Corrente", new BigDecimal("1000.00"));
        }

        @Test
        @DisplayName("Construtor deve inicializar nome e saldos corretamente")
        void testConstrutor() {
            assertEquals("Conta Corrente", caixa.getNome());
            // Usar compareTo(BigDecimal.ZERO) == 0 para comparar BigDecimals é a prática recomendada
            assertEquals(0, new BigDecimal("1000.00").compareTo(caixa.getSaldoInicial()));
            assertEquals(0, new BigDecimal("1000.00").compareTo(caixa.getSaldoAtual()));
        }

        @Test
        @DisplayName("Adicionar valor positivo deve aumentar o saldo atual")
        void testAdicionarValorPositivo() {
            caixa.adicionarValor(new BigDecimal("250.50"));
            assertEquals(0, new BigDecimal("1250.50").compareTo(caixa.getSaldoAtual()));
        }

        @Test
        @DisplayName("Remover valor positivo deve diminuir o saldo atual")
        void testRemoverValorPositivo() {
            caixa.removerValor(new BigDecimal("300.00"));
            assertEquals(0, new BigDecimal("700.00").compareTo(caixa.getSaldoAtual()));
        }
        
        @Test
        @DisplayName("Adicionar BigDecimal.ZERO não deve alterar o saldo")
        void testAdicionarValorZero() {
            caixa.adicionarValor(BigDecimal.ZERO);
            assertEquals(0, new BigDecimal("1000.00").compareTo(caixa.getSaldoAtual()));
        }
        
        @Test
        @DisplayName("Adicionar valor negativo deve diminuir o saldo")
        void testAdicionarValorNegativo() {
            caixa.adicionarValor(new BigDecimal("-100.00"));
            assertEquals(0, new BigDecimal("900.00").compareTo(caixa.getSaldoAtual()));
        }
        
        @Test
        @DisplayName("Remover valor negativo deve aumentar o saldo")
        void testRemoverValorNegativo() {
            caixa.removerValor(new BigDecimal("-100.00"));
            assertEquals(0, new BigDecimal("1100.00").compareTo(caixa.getSaldoAtual()));
        }

        @Test
        @DisplayName("Adicionar valor nulo deve lançar NullPointerException")
        void testAdicionarValorNulo() {
            assertThrows(NullPointerException.class, () -> caixa.adicionarValor(null));
        }
        
        @Test
        @DisplayName("Remover valor nulo deve lançar NullPointerException")
        void testRemoverValorNulo() {
            assertThrows(NullPointerException.class, () -> caixa.removerValor(null));
        }

        @Test
        @DisplayName("SetNome deve atualizar o nome do caixa")
        void testSetNome() {
            caixa.setNome("Poupança");
            assertEquals("Poupança", caixa.getNome());
        }
    }

    // --- Testes para a Classe Categoria (já é simples e completa) ---
    @Nested
    @DisplayName("Testes da Classe Categoria")
    class CategoriaNestedTest {
        @Test
        @DisplayName("Construtor e Getters devem funcionar corretamente")
        void testConstrutorEGetters() {
            Categoria categoria = new Categoria("Alimentação", "#FF0000");
            assertEquals("Alimentação", categoria.getNome());
            assertEquals("#FF0000", categoria.getCor());
        }

        @Test
        @DisplayName("Setters devem atualizar os valores")
        void testSettersDeveAtualizarValores() {
            Categoria categoria = new Categoria("Lazer", "#00FF00");
            categoria.setNome("Transporte");
            categoria.setCor("#0000FF");
            assertEquals("Transporte", categoria.getNome());
            assertEquals("#0000FF", categoria.getCor());
        }
    }
    
    // --- Testes para a Classe Despesa ---
    @Nested
    @DisplayName("Testes Completos da Classe Despesa")
    class DespesaNestedTest {
        private Caixa caixaPagador, caixaReceptor;

        @BeforeEach
        void setUp() {
            caixaPagador = new Caixa("Carteira", new BigDecimal("500.00"));
            caixaReceptor = new Caixa("Investimento", new BigDecimal("2000.00"));
        }

        @Test
        @DisplayName("Construtor (nome, valor, data, status, caixaPaga) deve debitar do caixa")
        void testConstrutorSimples() {
            new Despesa("Almoço", new BigDecimal("75.50"), LocalDate.now(), StatusDespesa.Pago, caixaPagador);
            assertEquals(0, new BigDecimal("424.50").compareTo(caixaPagador.getSaldoAtual()));
        }
        
        @Test
        @DisplayName("Construtor (nome, desc, valor, data, status, caixaPaga) deve debitar do caixa")
        void testConstrutorComDescricao() {
            new Despesa("Aluguel", "Mensal", new BigDecimal("300"), LocalDate.now(), StatusDespesa.Pago, caixaPagador);
            assertEquals(0, new BigDecimal("200.00").compareTo(caixaPagador.getSaldoAtual()));
        }

        @Test
        @DisplayName("Construtor de transferência deve debitar de um e creditar em outro")
        void testConstrutorTransferencia() {
            new Despesa("Aporte", new BigDecimal("100"), LocalDate.now(), StatusDespesa.Pago, caixaPagador, caixaReceptor);
            assertEquals(0, new BigDecimal("400.00").compareTo(caixaPagador.getSaldoAtual()));
            assertEquals(0, new BigDecimal("2100.00").compareTo(caixaReceptor.getSaldoAtual()));
        }
        
         @Test
        @DisplayName("Construtor de transferência com descrição deve funcionar")
        void testConstrutorTransferenciaComDescricao() {
            new Despesa("Aporte", "Investimento mensal", new BigDecimal("50"), LocalDate.now(), StatusDespesa.Pago, caixaPagador, caixaReceptor);
            assertEquals(0, new BigDecimal("450.00").compareTo(caixaPagador.getSaldoAtual()));
            assertEquals(0, new BigDecimal("2050.00").compareTo(caixaReceptor.getSaldoAtual()));
        }
        
        @Test
        @DisplayName("SetStatus deve alterar o status da despesa")
        void testSetStatus() {
            Despesa despesa = new Despesa("Conta de Luz", new BigDecimal("150"), LocalDate.now(), StatusDespesa.Pendente, caixaPagador);
            assertEquals(StatusDespesa.Pendente, despesa.getStatus());
            despesa.setStatus(StatusDespesa.Pago);
            assertEquals(StatusDespesa.Pago, despesa.getStatus());
        }
        
        @Test
        @DisplayName("Getters devem retornar os valores corretos")
        void testGetters() {
            Despesa despesa = new Despesa("Cinema", "Ingresso", new BigDecimal("50"), LocalDate.now(), StatusDespesa.Pago, caixaPagador, caixaReceptor);
            assertNotNull(despesa.getId());
            assertEquals("Cinema", despesa.getNome());
            assertEquals("Ingresso", despesa.getDescricao());
            assertEquals(0, new BigDecimal("50").compareTo(despesa.getValor()));
            assertEquals(LocalDate.now(), despesa.getData());
            assertEquals(caixaPagador, despesa.getCaixaQuePaga());
            assertEquals(caixaReceptor, despesa.getCaixaQueRecebe());
        }
    }
    
    // --- Testes para a Classe Receita ---
    @Nested
    @DisplayName("Testes Completos da Classe Receita")
    class ReceitaNestedTest {
        private Caixa caixaReceptor, caixaPagador;

        @BeforeEach
        void setUp() {
            caixaReceptor = new Caixa("Conta Principal", new BigDecimal("1500.00"));
            caixaPagador = new Caixa("Cliente", new BigDecimal("1000.00"));
        }

        @Test
        @DisplayName("Construtor (nome, valor, data, caixaRecebe) deve creditar no caixa")
        void testConstrutorSimples() {
            new Receita("Salário", new BigDecimal("300.00"), LocalDate.now(), caixaReceptor);
            assertEquals(0, new BigDecimal("1800.00").compareTo(caixaReceptor.getSaldoAtual()));
        }
        
        @Test
        @DisplayName("Construtor (desc, nome, valor, data, caixaRecebe) deve creditar no caixa")
        void testConstrutorComDescricao() {
            new Receita("Adiantamento", "Adiantamento Quinzenal", new BigDecimal("150"), LocalDate.now(), caixaReceptor);
            assertEquals(0, new BigDecimal("1650.00").compareTo(caixaReceptor.getSaldoAtual()));
        }

        @Test
        @DisplayName("Construtor de transferência deve creditar em um e debitar de outro")
        void testConstrutorTransferencia() {
            new Receita("Pagamento", new BigDecimal("200"), LocalDate.now(), caixaReceptor, caixaPagador);
            assertEquals(0, new BigDecimal("1700.00").compareTo(caixaReceptor.getSaldoAtual()));
            assertEquals(0, new BigDecimal("800.00").compareTo(caixaPagador.getSaldoAtual()));
        }
        
        @Test
        @DisplayName("Construtor de transferência com descrição deve funcionar")
        void testConstrutorTransferenciaComDescricao() {
             new Receita("Pagamento", "Serviço prestado", new BigDecimal("250"), LocalDate.now(), caixaReceptor, caixaPagador);
            assertEquals(0, new BigDecimal("1750.00").compareTo(caixaReceptor.getSaldoAtual()));
            assertEquals(0, new BigDecimal("750.00").compareTo(caixaPagador.getSaldoAtual()));
        }
        
         @Test
        @DisplayName("Getters devem retornar os valores corretos")
        void testGetters() {
            Receita receita = new Receita("Venda", "Venda de produto", new BigDecimal("120"), LocalDate.now(), caixaReceptor, caixaPagador);
            assertNotNull(receita.getId());
            assertEquals("Venda de produto", receita.getNome());
            assertEquals("Venda", receita.getDescricao());
            assertEquals(0, new BigDecimal("120").compareTo(receita.getValor()));
            assertEquals(LocalDate.now(), receita.getData());
            assertEquals(caixaReceptor, receita.getCaixaQueRecebe());
            assertEquals(caixaPagador, receita.caixaQuePaga);
        }
    }

    // --- Testes para a Classe Meta ---
    @Nested
    @DisplayName("Testes Completos da Classe Meta")
    class MetaNestedTest {
        @Test
        @DisplayName("Meta não atingida deve iniciar como 'Em Andamento'")
        void testConstrutorStatusEmAndamento() {
            Meta meta = new Meta("Viagem", new BigDecimal("5000"), new BigDecimal("1000"));
            assertEquals(StatusMeta.EmAndamento, meta.getStatus());
            assertEquals(0, BigDecimal.ZERO.compareTo(meta.getSaldoAtual()), "Saldo atual da meta deve iniciar em zero");
        }

        @Test
        @DisplayName("Meta já atingida no construtor deve iniciar como 'Concluida'")
        void testConstrutorStatusConcluida() {
            Meta meta = new Meta("Carro", new BigDecimal("20000"), new BigDecimal("25000"));
            assertEquals(StatusMeta.Concluida, meta.getStatus());
        }

        @Test
        @DisplayName("Adicionar valor sem atingir a meta deve manter status 'Em Andamento'")
        void testAdicionarValorSemAtingir() {
            Meta meta = new Meta("Notebook", new BigDecimal("4000"), BigDecimal.ZERO);
            meta.adicionarValor(new BigDecimal("3999.99"));
            assertEquals(StatusMeta.EmAndamento, meta.getStatus());
        }

        @Test
        @DisplayName("Adicionar valor que atinge EXATAMENTE a meta deve mudar status para 'Concluida'")
        void testAdicionarValorAtingindoExatamente() {
            Meta meta = new Meta("Notebook", new BigDecimal("4000"), BigDecimal.ZERO);
            meta.adicionarValor(new BigDecimal("4000"));
            assertEquals(StatusMeta.Concluida, meta.getStatus());
        }
        
         @Test
        @DisplayName("Adicionar valor que ultrapassa a meta deve mudar status para 'Concluida'")
        void testAdicionarValorUltrapassando() {
            Meta meta = new Meta("Notebook", new BigDecimal("4000"), BigDecimal.ZERO);
            meta.adicionarValor(new BigDecimal("5000"));
            assertEquals(StatusMeta.Concluida, meta.getStatus());
        }

        @Test
        @DisplayName("Remover valor de meta concluída deve reverter status para 'Em Andamento'")
        void testRemoverValorRevertendoStatus() {
            Meta meta = new Meta("Emergência", new BigDecimal("10000"), BigDecimal.ZERO);
            meta.adicionarValor(new BigDecimal("10000")); // Atinge a meta
            assertEquals(StatusMeta.Concluida, meta.getStatus());
            meta.removerValor(new BigDecimal("0.01"));      // Fica abaixo da meta
            assertEquals(StatusMeta.EmAndamento, meta.getStatus());
        }

        @Test
        @DisplayName("SetSaldoAtual com valor nulo deve lançar IllegalArgumentException")
        void testSetSaldoAtualNulo() {
             Meta meta = new Meta("Teste", BigDecimal.TEN, BigDecimal.ZERO);
             assertThrows(IllegalArgumentException.class, () -> meta.setSaldoAtual(null));
        }
        
        @Test
        @DisplayName("SetSaldoAtual com valor negativo deve ser definido como ZERO")
        void testSetSaldoAtualNegativo() {
             Meta meta = new Meta("Teste", BigDecimal.TEN, BigDecimal.ZERO);
             meta.setSaldoAtual(new BigDecimal("-100"));
             assertEquals(0, BigDecimal.ZERO.compareTo(meta.getSaldoAtual()));
        }
        
        @Test
        @DisplayName("SetStatus deve alterar o status manualmente")
        void testSetStatus() {
            Meta meta = new Meta("Teste", BigDecimal.TEN, BigDecimal.ZERO);
            meta.setStatus(StatusMeta.Cancelada);
            assertEquals(StatusMeta.Cancelada, meta.getStatus());
        }
    }

    // --- Testes para a Classe Carteira ---
    @Nested
    @DisplayName("Testes Completos da Classe Carteira")
    class CarteiraNestedTest {
        private Carteira carteira;
        private Caixa caixa1, caixa2;

        @BeforeEach
        void setUp() {
            carteira = new Carteira();
            caixa1 = new Caixa("Conta A", new BigDecimal("1000.00"));
            caixa2 = new Caixa("Conta B", new BigDecimal("500.00"));
            carteira.adicionarCaixa(caixa1);
            carteira.adicionarCaixa(caixa2);
        }

        @Test
        @DisplayName("GetSaldoTotal deve retornar a soma correta dos saldos dos caixas")
        void testGetSaldoTotal() {
            assertEquals(0, new BigDecimal("1500.00").compareTo(carteira.getSaldoTotal()));
        }
        
        @Test
        @DisplayName("GetSaldoTotal com nenhum caixa deve retornar ZERO")
        void testGetSaldoTotalSemCaixas() {
            Carteira carteiraVazia = new Carteira();
            assertEquals(0, BigDecimal.ZERO.compareTo(carteiraVazia.getSaldoTotal()));
        }

        @Test
        @DisplayName("GetTotalReceitasMes deve somar apenas receitas do mês corrente")
        void testGetTotalReceitasMes() {
            carteira.adicionarTransacao(new Receita("Salário", new BigDecimal("2500"), LocalDate.now(), caixa1));
            carteira.adicionarTransacao(new Receita("Bônus", new BigDecimal("300"), LocalDate.now(), caixa2));
            carteira.adicionarTransacao(new Receita("Antigo", new BigDecimal("500"), LocalDate.now().minusMonths(1), caixa1));
            carteira.adicionarTransacao(new Despesa("Aluguel", new BigDecimal("1000"), LocalDate.now(), StatusDespesa.Pago, caixa1)); // Deve ser ignorado

            assertEquals(0, new BigDecimal("2800").compareTo(carteira.getTotalReceitasMes()));
        }

        @Test
        @DisplayName("GetTotalDespesasMes deve somar apenas despesas do mês corrente")
        void testGetTotalDespesasMes() {
            carteira.adicionarTransacao(new Despesa("Aluguel", new BigDecimal("1200"), LocalDate.now(), StatusDespesa.Pago, caixa1));
            carteira.adicionarTransacao(new Despesa("Supermercado", new BigDecimal("700"), LocalDate.now(), StatusDespesa.Pago, caixa2));
            carteira.adicionarTransacao(new Despesa("Antiga", new BigDecimal("100"), LocalDate.now().minusMonths(2), StatusDespesa.Pago, caixa1));
            carteira.adicionarTransacao(new Receita("Salário", new BigDecimal("3000"), LocalDate.now(), caixa1)); // Deve ser ignorada

            assertEquals(0, new BigDecimal("1900").compareTo(carteira.getTotalDespesasMes()));
        }
        
        @Test
        @DisplayName("GetTotalReceitasMes sem transações deve retornar ZERO")
        void testGetTotalReceitasMesVazio() {
             assertEquals(0, BigDecimal.ZERO.compareTo(carteira.getTotalReceitasMes()));
        }
        
        @Test
        @DisplayName("GetTotalDespesasMes sem transações deve retornar ZERO")
        void testGetTotalDespesasMesVazio() {
             assertEquals(0, BigDecimal.ZERO.compareTo(carteira.getTotalDespesasMes()));
        }

        @Test
        @DisplayName("ListarTransacoesRecentes deve retornar lista ordenada por data")
        void testListarTransacoesRecentesOrdenacao() {
            Transacao t1 = new Receita("T1", BigDecimal.TEN, LocalDate.now().minusDays(2), caixa1);
            Transacao t2 = new Despesa("T2", BigDecimal.ONE, LocalDate.now(), StatusDespesa.Pago, caixa2);
            Transacao t3 = new Receita("T3", BigDecimal.ZERO, LocalDate.now().minusDays(1), caixa1);
            carteira.adicionarTransacao(t1);
            carteira.adicionarTransacao(t2);
            carteira.adicionarTransacao(t3);
            
            List<Transacao> recentes = carteira.listarTransacoesRecentes(3);
            
            assertEquals(t2, recentes.get(0), "T2 deveria ser a primeira (mais recente)");
            assertEquals(t3, recentes.get(1), "T3 deveria ser a segunda");
            assertEquals(t1, recentes.get(2), "T1 deveria ser a terceira (mais antiga)");
        }
        
        @Test
        @DisplayName("ListarTransacoesRecentes deve respeitar o limite solicitado")
        void testListarTransacoesRecentesLimite() {
            carteira.adicionarTransacao(new Receita("T1", BigDecimal.TEN, LocalDate.now().minusDays(2), caixa1));
            carteira.adicionarTransacao(new Despesa("T2", BigDecimal.ONE, LocalDate.now(), StatusDespesa.Pago, caixa2));
            
            List<Transacao> recentes = carteira.listarTransacoesRecentes(1);
            assertEquals(1, recentes.size());
            assertEquals("T2", recentes.get(0).getNome());
        }
        
        @Test
        @DisplayName("ListarTransacoesRecentes com carteira vazia deve retornar lista vazia")
        void testListarTransacoesRecentesCarteiraVazia() {
            Carteira carteiraVazia = new Carteira();
            List<Transacao> recentes = carteiraVazia.listarTransacoesRecentes(5);
            assertTrue(recentes.isEmpty());
        }
    }
}