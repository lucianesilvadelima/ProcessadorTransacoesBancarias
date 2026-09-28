public class Transacao {
    String agencia;
    String conta;
    String banco;
    String titular;
    String tipo; // "saque" ou "deposito"
    double valor;
    String dataHora;

    public Transacao(String titular, String tipo, double valor, String dataHora) {
        this.agencia = agencia;
        this.conta = conta;
        this.banco = banco;
        this.titular = titular;
        this.tipo = tipo;
        this.valor = valor;
        this.dataHora = dataHora;
    }

}