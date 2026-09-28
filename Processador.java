import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Classe principal do projeto de Processador de Transações Bancárias.
 * Objetivo: ler um arquivo CSV com transações, agrupar por titular,
 * ordenar por data/hora, remover duplicatas e calcular o saldo final.
 */
public class Processador {

    public static void main(String[] args) throws Exception {

        // Lista para armazenar todas as transações lidas do arquivo
        // Estrutura escolhida: ArrayList → inserção O(1) amortizado
        List<Transacao> lista = new ArrayList<>();//Usei Arraylist para armazenar as transacoes

        BufferedReader br = new BufferedReader(
                new FileReader("C:\\Users\\Luciane\\IdeaProjects\\TransacoesBancarias\\operacoes_bancarias_exemplo.csv"));

        String linha = br.readLine(); // pula o cabeçalho

        // Lê cada linha do CSV e cria objetos Transacao
        while ((linha = br.readLine()) != null) {
            String[] dados = linha.split(",");

            String titular = dados[3];
            String tipo = dados[4];
            String dataHora = dados[5];
            double valor = Double.parseDouble(dados[6]);

            lista.add(new Transacao(titular, tipo, valor, dataHora));
        }
        br.close();

        // Agrupamento por titular usando HashMap → busca/inserção O(1)
        Map<String, List<Transacao>> agrupado = new HashMap<>();

        for (Transacao t : lista) {

            //verificar se a chave existe
            if (!agrupado.containsKey(t.titular)) {
                agrupado.put(t.titular, new ArrayList<>());
            }
            //adicionar a transcao na lista do titular
            agrupado.get(t.titular) .add(t);
        }

        // Ordenação por data/hora dentro de cada titular
        // Collections.sort usa algoritmo TimSort → O(n log n)
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        for (List<Transacao> transacoes : agrupado.values()) {
            transacoes.sort(Comparator.comparing(t -> LocalDateTime.parse(t.dataHora, formatter)));
        }

        // Processamento e cálculo de saldo final
        for (String titular : agrupado.keySet()) {

            // HashSet para eliminar duplicatas → busca/inserção O(1)
            Set<String> duplicadas = new HashSet<>();
            double saldo = 0;

            for (Transacao t : agrupado.get(titular)) {

                // Chave única para detectar duplicatas
                String chave = t.titular + t.tipo + t.valor + t.dataHora;

                if (!duplicadas.contains(chave)) {
                    duplicadas.add(chave);

                    // Atualiza saldo conforme tipo de operação
                    if (t.tipo.equalsIgnoreCase("deposito")) {
                        saldo += t.valor;
                    } else if (t.tipo.equalsIgnoreCase("saque")) {
                        saldo -= t.valor;
                    }
                }
            }

            System.out.printf("%s - Saldo final: %.2f%n", titular, saldo);
        }
    }
}

