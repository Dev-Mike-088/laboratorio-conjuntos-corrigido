import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * PARTE 2 - CONJUNTOS NUMÉRICOS (INTERMEDIÁRIO)
 *
 * Classificador de números: dado um número (inteiro, fração, decimal ou raiz),
 * o programa informa a quais conjuntos ele pertence (ℕ, ℤ, ℚ, 𝕀, ℝ) e JUSTIFICA a classificação.
 *
 * O que o programa faz (conforme o enunciado):
 *  - Fração lida no formato a/b (ex.: 3/4, -5/2, 6/3).
 *  - Decimal lido como TEXTO (sem passar por double, para não perder precisão).
 *  - Identifica dízima periódica (ex.: 0,(3)  2,(7)  1,2(5)  0,(9)) e calcula a fração geratriz.
 *  - Raiz quadrada: exata (√9 = 3) ou irracional (√2).
 *  - BÔNUS: primos de ℕ até n (crivo de Eratóstenes) e divisores de um número.
 *
 * CONVENÇÃO ADOTADA (declarar também no README): ℕ = {0, 1, 2, 3, ...}, ou seja, o zero pertence a ℕ.
 *
 * Requer Java 9 ou superior (usa BigInteger.sqrt()).
 *
 * Organização do arquivo:
 *  SEÇÃO 1 - Constantes, variáveis globais e menu principal
 *  SEÇÃO 2 - Leitura de dados
 *  SEÇÃO 3 - Classificadores (um para cada tipo de entrada)
 *  SEÇÃO 4 - Cálculos auxiliares (simplificação de fração e justificativa)
 *  SEÇÃO 5 - Exibição do resultado (formato do gabarito)
 *  SEÇÃO 6 - Bônus: crivo de Eratóstenes e divisores
 */
public class Parte2ConjuntosNumericos {

    // =====================================================================
    // SEÇÃO 1 - CONSTANTES, VARIÁVEIS GLOBAIS E MENU PRINCIPAL
    // =====================================================================

    static Scanner scanner = new Scanner(System.in);

    // Símbolos dos conjuntos, definidos em um único lugar.
    // Se o console não exibir bem algum símbolo, basta trocar aqui (ex.: "N", "Z", "Q", "I", "R").
    static final String SIMB_N = "ℕ";
    static final String SIMB_Z = "ℤ";
    static final String SIMB_Q = "ℚ";
    static final String SIMB_I = "𝕀";
    static final String SIMB_R = "ℝ";

    // Fração no formato a/b: dois inteiros (com sinal opcional) separados por "/".
    static final Pattern PADRAO_FRACAO = Pattern.compile("^([+-]?\\d+)\\s*/\\s*([+-]?\\d+)$");

    // Decimal exato: sinal opcional, parte inteira e, opcionalmente, vírgula/ponto com casas decimais.
    static final Pattern PADRAO_DECIMAL = Pattern.compile("^([+-]?)(\\d+)(?:[.,](\\d+))?$");

    // Dízima periódica: sinal, parte inteira, vírgula/ponto, parte NÃO periódica (pode ser vazia) e período entre parênteses.
    // Ex.: 0,(3)  -> inteira=0, nãoPeriódica="", período=3
    //      1,2(5) -> inteira=1, nãoPeriódica=2,  período=5
    static final Pattern PADRAO_DIZIMA = Pattern.compile("^([+-]?)(\\d+)[.,](\\d*)\\((\\d+)\\)$");

    // Limite do crivo de Eratóstenes, para não estourar a memória (o crivo usa um vetor de tamanho n+1).
    static final int LIMITE_CRIVO = 10_000_000;

    public static void main(String[] args) {
        System.out.println("=== PARTE 2 - CLASSIFICADOR DE CONJUNTOS NUMÉRICOS ===");
        System.out.println("Convenção adotada: " + SIMB_N + " = {0, 1, 2, 3, ...} (o zero pertence a " + SIMB_N + ").");

        boolean executando = true;

        while (executando) {
            System.out.println("\n--- MENU ---");
            System.out.println("1 - Informar um número inteiro");
            System.out.println("2 - Informar uma fração (formato a/b, ex.: 3/4)");
            System.out.println("3 - Informar um decimal (exato ou dízima periódica, ex.: 0,75  1,2(5))");
            System.out.println("4 - Informar uma raiz quadrada (ex.: 2 para √2)");
            System.out.println("5 - Bônus: primos de " + SIMB_N + " até n (crivo de Eratóstenes)");
            System.out.println("6 - Bônus: divisores de um número");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");

            int opcao = lerInteiro();

            switch (opcao) {
                case 1:
                    classificarInteiro();
                    break;
                case 2:
                    classificarFracao();
                    break;
                case 3:
                    classificarDecimal();
                    break;
                case 4:
                    classificarRaiz();
                    break;
                case 5:
                    listarPrimos();
                    break;
                case 6:
                    listarDivisores();
                    break;
                case 0:
                    executando = false;
                    System.out.println("Programa encerrado.");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    // =====================================================================
    // SEÇÃO 2 - LEITURA DE DADOS
    // =====================================================================

    /** Lê um int (opção do menu ou valor de n), repetindo a pergunta enquanto a entrada for inválida. */
    static int lerInteiro() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Digite um número inteiro válido: ");
            }
        }
    }

    /** Lê um long, repetindo a pergunta enquanto a entrada for inválida. */
    static long lerLong() {
        while (true) {
            try {
                return Long.parseLong(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Digite um número inteiro válido: ");
            }
        }
    }

    /** Lê um inteiro de qualquer tamanho (BigInteger), repetindo a pergunta enquanto for inválido. */
    static BigInteger lerBigInteger() {
        while (true) {
            try {
                return new BigInteger(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Digite um número inteiro válido: ");
            }
        }
    }

    // =====================================================================
    // SEÇÃO 3 - CLASSIFICADORES (um para cada tipo de entrada)
    // =====================================================================

    /** Opção 1: número inteiro. Todo inteiro n é a fração n/1, logo é racional. */
    static void classificarInteiro() {
        System.out.print("Digite o número inteiro: ");
        BigInteger numero = lerBigInteger();

        String justificativa = "Todo inteiro n pode ser escrito como n/1. "
                + justificarValor(numero, BigInteger.ONE);

        exibirRacional(numero.toString(), "número inteiro", null, justificativa, numero, BigInteger.ONE);
    }

    /** Opção 2: fração lida em uma única entrada no formato a/b. */
    static void classificarFracao() {
        System.out.print("Digite a fração no formato a/b (ex.: 3/4): ");
        String texto = scanner.nextLine().trim();

        Matcher m = PADRAO_FRACAO.matcher(texto);
        if (!m.matches()) {
            System.out.println("Formato inválido. Use a/b com números inteiros, ex.: 3/4 ou -5/2.");
            return;
        }

        BigInteger a = new BigInteger(m.group(1));
        BigInteger b = new BigInteger(m.group(2));

        if (b.signum() == 0) {
            System.out.println("O denominador não pode ser zero.");
            return;
        }

        BigInteger[] r = reduzir(a, b); // r[0] = numerador, r[1] = denominador (sempre > 0)
        String simplificada = r[1].equals(BigInteger.ONE) ? r[0].toString() : r[0] + "/" + r[1];

        // Só mostra "Simplificando" quando a fração realmente muda (ex.: 6/3 = 2); senão avisa que já é irredutível.
        String passoSimplificacao = (a + "/" + b).equals(simplificada)
                ? "A fração já está na forma irredutível. "
                : "Simplificando: " + a + "/" + b + " = " + simplificada + ". ";

        String justificativa = "Razão de dois inteiros com denominador diferente de zero é um número racional. "
                + passoSimplificacao
                + justificarValor(r[0], r[1]);

        exibirRacional(texto, "fração", null, justificativa, r[0], r[1]);
    }

    /**
     * Opção 3: decimal. O número é lido como TEXTO (não como double) para não perder precisão.
     * Primeiro testa se é dízima periódica (tem parênteses); se não for, trata como decimal exato.
     */
    static void classificarDecimal() {
        System.out.print("Digite o decimal (ex.: 0,75 ou dízima como 1,2(5)): ");
        String texto = scanner.nextLine().trim();

        // Caso 1: dízima periódica
        Matcher dizima = PADRAO_DIZIMA.matcher(texto);
        if (dizima.matches()) {
            classificarDizima(texto, dizima);
            return;
        }

        // Caso 2: decimal exato (finito)
        Matcher exato = PADRAO_DECIMAL.matcher(texto);
        if (!exato.matches()) {
            System.out.println("Decimal inválido. Exemplos: 0,75  -2,5  0,(3)  1,2(5).");
            return;
        }

        boolean negativo = exato.group(1).equals("-");
        String inteira = exato.group(2);
        String decimais = exato.group(3) == null ? "" : exato.group(3);

        // Decimal exato com k casas = (todos os dígitos como inteiro) / 10^k
        BigInteger numerador = new BigInteger(inteira + decimais);
        BigInteger denominador = BigInteger.TEN.pow(decimais.length());
        if (negativo) {
            numerador = numerador.negate();
        }

        BigInteger[] r = reduzir(numerador, denominador);

        String tipo;
        String justificativa;
        if (decimais.isEmpty()) {
            tipo = "número inteiro";
            justificativa = "Não há casas decimais: o número é inteiro. ";
        } else {
            tipo = "decimal exato";
            justificativa = "Decimal exato (finito) com " + decimais.length() + " casa(s) decimal(is) equivale à fração "
                    + numerador + "/" + denominador + ", logo é racional. ";
        }
        justificativa += justificarValor(r[0], r[1]);

        exibirRacional(texto, tipo, null, justificativa, r[0], r[1]);
    }

    /**
     * Dízima periódica e fração geratriz.
     *
     * Regra da geratriz:
     *   numerador   = (parte inteira + não periódica + período) - (parte inteira + não periódica)
     *   denominador = tantos 9 quanto os dígitos do período, seguidos de tantos 0 quanto os dígitos da parte não periódica
     *
     * Exemplo 1,2(5): numerador = 125 - 12 = 113; denominador = 9 seguido de um 0 = 90; geratriz = 113/90.
     */
    static void classificarDizima(String texto, Matcher m) {
        boolean negativo = m.group(1).equals("-");
        String inteira = m.group(2);
        String naoPeriodica = m.group(3); // pode ser vazia
        String periodo = m.group(4);

        BigInteger comPeriodo = new BigInteger(inteira + naoPeriodica + periodo);
        BigInteger semPeriodo = new BigInteger(inteira + naoPeriodica);

        BigInteger numerador = comPeriodo.subtract(semPeriodo);
        BigInteger denominador = BigInteger.TEN.pow(periodo.length()).subtract(BigInteger.ONE) // 99...9
                .multiply(BigInteger.TEN.pow(naoPeriodica.length()));                          // seguido de 00...0
        if (negativo) {
            numerador = numerador.negate();
        }

        BigInteger[] r = reduzir(numerador, denominador);

        // Mostra a geratriz bruta e, se for diferente, a forma simplificada (ex.: 9/9 = 1  ou  3/9 = 1/3)
        String bruta = numerador + "/" + denominador;
        String simplificada = r[1].equals(BigInteger.ONE) ? r[0].toString() : r[0] + "/" + r[1];
        String geratriz = bruta.equals(simplificada) ? bruta : bruta + " = " + simplificada;

        String tipo = "dízima periódica (parte não periódica: "
                + (naoPeriodica.isEmpty() ? "nenhuma" : naoPeriodica) + "; período: " + periodo + ")";

        String justificativa = "Toda dízima periódica pode ser escrita como fração (a geratriz), logo é racional. "
                + "Cálculo: (" + comPeriodo + " - " + semPeriodo + ") / " + denominador + " = " + bruta + ". "
                + justificarValor(r[0], r[1]);

        exibirRacional(texto, tipo, geratriz, justificativa, r[0], r[1]);
    }

    /** Opção 4: raiz quadrada de um inteiro. Exata (quadrado perfeito) vira inteiro; não exata é irracional. */
    static void classificarRaiz() {
        System.out.print("Digite o número que está dentro da raiz (ex.: 2 para √2): ");
        BigInteger n = lerBigInteger();

        if (n.signum() < 0) {
            System.out.println("√" + n + ": raiz quadrada de número negativo não é real, "
                    + "logo não pertence a nenhum dos conjuntos pedidos (" + SIMB_N + ", " + SIMB_Z + ", "
                    + SIMB_Q + ", " + SIMB_I + ", " + SIMB_R + ").");
            return;
        }

        BigInteger raiz = n.sqrt(); // parte inteira da raiz, calculada sem usar double (sem perda de precisão)
        boolean quadradoPerfeito = raiz.multiply(raiz).equals(n);
        String entrada = "√" + n;

        if (quadradoPerfeito) {
            // Raiz exata: o resultado é um inteiro não negativo.
            String justificativa = n + " = " + raiz + "² (quadrado perfeito), logo √" + n + " = " + raiz + ". "
                    + justificarValor(raiz, BigInteger.ONE);
            exibirRacional(entrada, "raiz exata (√" + n + " = " + raiz + ")", null, justificativa, raiz, BigInteger.ONE);
        } else {
            // Raiz não exata: irracional (não pode ser escrita como fração).
            BigInteger proximo = raiz.add(BigInteger.ONE);
            String justificativa = n + " não é quadrado perfeito (está entre " + raiz + "² = " + raiz.multiply(raiz)
                    + " e " + proximo + "² = " + proximo.multiply(proximo) + "). "
                    + "A raiz quadrada de um inteiro que não é quadrado perfeito é irracional: "
                    + "não pode ser escrita como fração. Pertence a " + SIMB_I + " e " + SIMB_R
                    + ", mas não a " + SIMB_N + ", " + SIMB_Z + " nem " + SIMB_Q + ".";
            exibir(entrada, "raiz não exata", null, justificativa, false, false, false, true, true);
        }
    }

    // =====================================================================
    // SEÇÃO 4 - CÁLCULOS AUXILIARES (simplificação de fração e justificativa)
    // =====================================================================

    /**
     * Simplifica a fração num/den dividindo pelo mdc e deixa o denominador positivo.
     * Retorna {numerador, denominador}. Ex.: 6/3 -> {2, 1};  3/-6 -> {-1, 2};  0/5 -> {0, 1}.
     */
    static BigInteger[] reduzir(BigInteger num, BigInteger den) {
        if (den.signum() < 0) {
            num = num.negate();
            den = den.negate();
        }
        BigInteger mdc = num.gcd(den);
        return new BigInteger[] { num.divide(mdc), den.divide(mdc) };
    }

    /**
     * Texto de justificativa sobre onde o valor (já simplificado num/den, den > 0) se encaixa.
     * Vale para todo número racional: se o denominador é 1 o valor é inteiro (e ℕ depende do sinal).
     */
    static String justificarValor(BigInteger num, BigInteger den) {
        if (den.equals(BigInteger.ONE)) {
            if (num.signum() > 0) {
                return "O valor é o inteiro " + num + " (positivo): pertence a " + SIMB_N + ", " + SIMB_Z + ", "
                        + SIMB_Q + " e " + SIMB_R + ", e não é irracional.";
            }
            if (num.signum() == 0) {
                return "O valor é 0: pertence a " + SIMB_N + " (convenção desta atividade: " + SIMB_N
                        + " inclui o zero), " + SIMB_Z + ", " + SIMB_Q + " e " + SIMB_R + ", e não é irracional.";
            }
            return "O valor é o inteiro " + num + " (negativo): pertence a " + SIMB_Z + ", " + SIMB_Q + " e "
                    + SIMB_R + ", mas não a " + SIMB_N + "; também não é irracional.";
        }
        return "O valor " + num + "/" + den + " não é inteiro (fração irredutível com denominador diferente de 1): "
                + "pertence a " + SIMB_Q + " e " + SIMB_R + ", mas não a " + SIMB_N + ", " + SIMB_Z + " nem a "
                + SIMB_I + " (todo racional deixa de ser irracional).";
    }

    // =====================================================================
    // SEÇÃO 5 - EXIBIÇÃO DO RESULTADO (formato do gabarito)
    // =====================================================================

    /**
     * Exibe o resultado de um número RACIONAL já simplificado (num/den, den > 0).
     * Regras: ℕ se for inteiro e não negativo; ℤ se for inteiro; ℚ e ℝ sempre; 𝕀 nunca.
     */
    static void exibirRacional(String entrada, String tipo, String geratriz, String justificativa,
                               BigInteger num, BigInteger den) {
        boolean inteiro = den.equals(BigInteger.ONE);
        boolean natural = inteiro && num.signum() >= 0; // o zero pertence a ℕ nesta atividade

        exibir(entrada, tipo, geratriz, justificativa, natural, inteiro, true, false, true);
    }

    /**
     * Exibe a saída no formato do gabarito:
     * Entrada, Tipo, (Fração geratriz), Justificativa, Pertence a, Não pertence a.
     * Os cinco booleanos indicam a pertinência a ℕ, ℤ, ℚ, 𝕀 e ℝ, nesta ordem.
     */
    static void exibir(String entrada, String tipo, String geratriz, String justificativa,
                       boolean n, boolean z, boolean q, boolean i, boolean r) {
        List<String> pertence = new ArrayList<>();
        List<String> naoPertence = new ArrayList<>();

        (n ? pertence : naoPertence).add(SIMB_N);
        (z ? pertence : naoPertence).add(SIMB_Z);
        (q ? pertence : naoPertence).add(SIMB_Q);
        (i ? pertence : naoPertence).add(SIMB_I);
        (r ? pertence : naoPertence).add(SIMB_R);

        System.out.println();
        System.out.println("Entrada: " + entrada);
        System.out.println("Tipo: " + tipo);
        if (geratriz != null) {
            System.out.println("Fração geratriz: " + geratriz);
        }
        System.out.println("Justificativa: " + justificativa);
        System.out.println("Pertence a: " + String.join(", ", pertence));
        System.out.println("Não pertence a: " + (naoPertence.isEmpty() ? "nenhum" : String.join(", ", naoPertence)));
    }

    // =====================================================================
    // SEÇÃO 6 - BÔNUS: CRIVO DE ERATÓSTENES E DIVISORES
    // =====================================================================

    /**
     * Opção 5: todos os primos de ℕ até n pelo crivo de Eratóstenes.
     * Ideia: marca como composto todo múltiplo de cada número ainda não marcado; o que sobra é primo.
     */
    static void listarPrimos() {
        System.out.print("Digite n (listar os primos de " + SIMB_N + " até n): ");
        int n = lerInteiro();

        if (n < 2) {
            System.out.println("Não há primos menores ou iguais a " + n + ".");
            return;
        }
        if (n > LIMITE_CRIVO) {
            System.out.println("Valor muito grande para o crivo. Use n até " + LIMITE_CRIVO + ".");
            return;
        }

        boolean[] composto = new boolean[n + 1]; // composto[k] == true significa que k NÃO é primo

        // Só é preciso testar i até a raiz de n: todo composto <= n tem fator primo <= √n.
        for (int i = 2; (long) i * i <= n; i++) {
            if (!composto[i]) {
                for (int j = i * i; j <= n; j += i) { // começa em i*i (os menores múltiplos já foram marcados)
                    composto[j] = true;
                }
            }
        }

        List<Integer> primos = new ArrayList<>();
        for (int k = 2; k <= n; k++) {
            if (!composto[k]) {
                primos.add(k);
            }
        }

        System.out.println("Primos até " + n + " (quantidade: " + primos.size() + "): " + primos);
    }

    /** Opção 6: divisores positivos de um número natural n >= 1. */
    static void listarDivisores() {
        System.out.print("Digite um número natural (maior ou igual a 1): ");
        long n = lerLong();

        if (n < 1) {
            System.out.println("Informe um número natural maior ou igual a 1.");
            return;
        }

        // Cada divisor i <= √n forma um par com n/i; o TreeSet mantém tudo ordenado e sem repetir (ex.: 6 em 36).
        TreeSet<Long> divisores = new TreeSet<>();
        for (long i = 1; i <= n / i; i++) {
            if (n % i == 0) {
                divisores.add(i);
                divisores.add(n / i);
            }
        }

        System.out.println("Divisores de " + n + " (quantidade: " + divisores.size() + "): " + divisores);
    }
}
