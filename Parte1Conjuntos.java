import java.util.*;

/**
 * PARTE 1 - TEORIA DOS CONJUNTOS (BÁSICO)
 *
 * Programa de menu: o usuário informa dois conjuntos A e B e escolhe operações.
 *
 * DESAFIO DO ENUNCIADO:
 *  - As operações são implementadas "na mão", com laços (for) e condicionais (if),
 *    SEM usar addAll, retainAll, removeAll, contains, containsAll ou size().
 *  - Depois, cada resultado é comparado com a solução nativa da linguagem para validar.
 *
 * Organização do arquivo:
 *  SEÇÃO 1 - Variáveis globais e método main (menu)
 *  SEÇÃO 2 - Leitura de dados (conjuntos e inteiros)
 *  SEÇÃO 3 - Operações MANUAIS (laços + condicionais)
 *  SEÇÃO 4 - Operações NATIVAS (usadas somente para validar)
 *  SEÇÃO 5 - Telas de cada opção do menu
 *  SEÇÃO 6 - Formatação e comparação manual x nativa
 */
public class Parte1Conjuntos {

    // =====================================================================
    // SEÇÃO 1 - VARIÁVEIS GLOBAIS E MENU PRINCIPAL
    // =====================================================================

    static Scanner scanner = new Scanner(System.in);

    // Conjunto universo U: é informado uma única vez e reutilizado nos complementos.
    // Enquanto for null, significa que o usuário ainda não informou U.
    static Set<Integer> U = null;

    public static void main(String[] args) {
        System.out.println("=== PARTE 1 - TEORIA DOS CONJUNTOS ===");

        // O TreeSet descarta repetidos e mantém os elementos ordenados.
        Set<Integer> A = lerConjunto("A");
        Set<Integer> B = lerConjunto("B");

        boolean executando = true;

        while (executando) {
            System.out.println("\n--- MENU ---");
            System.out.println("1  - União (A ∪ B)");
            System.out.println("2  - Interseção (A ∩ B)");
            System.out.println("3  - Diferença (A - B)");
            System.out.println("4  - Diferença (B - A)");
            System.out.println("5  - Diferença simétrica (A Δ B)");
            System.out.println("6  - Verificar pertinência (x ∈ A, x ∈ B)");
            System.out.println("7  - Verificar inclusão entre A e B (⊆ e ⊂)");
            System.out.println("8  - Verificar inclusão de um conjunto X em A (X ⊆ A, X ⊂ A)");
            System.out.println("9  - Mostrar cardinalidade (|A| e |B|)");
            System.out.println("10 - Informar/alterar o universo U");
            System.out.println("11 - Calcular complemento de A (e de B) em relação a U");
            System.out.println("12 - Mostrar os conjuntos");
            System.out.println("0  - Sair");
            System.out.print("Escolha uma opção: ");

            int opcao = lerInteiro();

            switch (opcao) {
                case 1:
                    mostrarComparacao("A ∪ B", uniao(A, B), uniaoNativa(A, B));
                    break;
                case 2:
                    mostrarComparacao("A ∩ B", intersecao(A, B), intersecaoNativa(A, B));
                    break;
                case 3:
                    mostrarComparacao("A - B", diferenca(A, B), diferencaNativa(A, B));
                    break;
                case 4:
                    mostrarComparacao("B - A", diferenca(B, A), diferencaNativa(B, A));
                    break;
                case 5:
                    mostrarComparacao("A Δ B", diferencaSimetrica(A, B), diferencaSimetricaNativa(A, B));
                    break;
                case 6:
                    verificarPertinencia(A, B);
                    break;
                case 7:
                    verificarInclusaoAB(A, B);
                    break;
                case 8:
                    verificarInclusaoX(A);
                    break;
                case 9:
                    mostrarCardinalidade(A, B);
                    break;
                case 10:
                    U = lerConjunto("U (universo)");
                    System.out.println("U = " + formatar(U));
                    break;
                case 11:
                    mostrarComplemento(A, B);
                    break;
                case 12:
                    mostrarConjuntos(A, B);
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

    /**
     * Lê um conjunto elemento por elemento.
     * Elementos repetidos são descartados (conferindo com laço antes de inserir)
     * e o usuário é avisado. Quantidade 0 gera o conjunto vazio.
     */
    static Set<Integer> lerConjunto(String nome) {
        Set<Integer> conjunto = new TreeSet<>(); // TreeSet só guarda e ordena; a lógica de repetidos é nossa

        System.out.print("Quantos elementos terá o conjunto " + nome + "? ");
        int quantidade = lerQuantidade();

        for (int i = 0; i < quantidade; i++) {
            System.out.print("Digite o " + (i + 1) + "º elemento de " + nome + ": ");
            int elemento = lerInteiro();

            if (contem(conjunto, elemento)) {
                System.out.println("  (" + elemento + " já está em " + nome + ", repetido descartado)");
            } else {
                conjunto.add(elemento);
            }
        }

        System.out.println(nome + " = " + formatar(conjunto));
        return conjunto;
    }

    /** Lê um inteiro qualquer, repetindo a pergunta enquanto a entrada for inválida. */
    static int lerInteiro() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Digite um número inteiro válido: ");
            }
        }
    }

    /** Lê uma quantidade de elementos, aceitando apenas valores >= 0. */
    static int lerQuantidade() {
        int quantidade = lerInteiro();
        while (quantidade < 0) {
            System.out.print("A quantidade não pode ser negativa. Digite novamente: ");
            quantidade = lerInteiro();
        }
        return quantidade;
    }

    // =====================================================================
    // SEÇÃO 3 - OPERAÇÕES MANUAIS (laços + condicionais, sem métodos prontos)
    // =====================================================================

    /** Pertinência: percorre S e verifica se x aparece. Substitui S.contains(x). */
    static boolean contem(Set<Integer> S, int x) {
        for (int e : S) {
            if (e == x) {
                return true;
            }
        }
        return false;
    }

    /** Cardinalidade |S|: conta os elementos com um contador. Substitui S.size(). */
    static int cardinalidade(Set<Integer> S) {
        int contador = 0;
        for (int e : S) {
            contador++;
        }
        return contador;
    }

    /** União A ∪ B: todos os elementos de A e todos os de B (sem repetir). */
    static Set<Integer> uniao(Set<Integer> A, Set<Integer> B) {
        Set<Integer> resultado = new TreeSet<>();
        for (int e : A) {
            if (!contem(resultado, e)) {
                resultado.add(e);
            }
        }
        for (int e : B) {
            if (!contem(resultado, e)) {
                resultado.add(e);
            }
        }
        return resultado;
    }

    /** Interseção A ∩ B: elementos de A que também estão em B. */
    static Set<Integer> intersecao(Set<Integer> A, Set<Integer> B) {
        Set<Integer> resultado = new TreeSet<>();
        for (int e : A) {
            if (contem(B, e)) {
                resultado.add(e);
            }
        }
        return resultado;
    }

    /** Diferença A - B: elementos de A que NÃO estão em B. */
    static Set<Integer> diferenca(Set<Integer> A, Set<Integer> B) {
        Set<Integer> resultado = new TreeSet<>();
        for (int e : A) {
            if (!contem(B, e)) {
                resultado.add(e);
            }
        }
        return resultado;
    }

    /** Diferença simétrica A Δ B: elementos que estão em exatamente um dos dois conjuntos. */
    static Set<Integer> diferencaSimetrica(Set<Integer> A, Set<Integer> B) {
        Set<Integer> resultado = new TreeSet<>();
        for (int e : A) {
            if (!contem(B, e)) {
                resultado.add(e);
            }
        }
        for (int e : B) {
            if (!contem(A, e)) {
                resultado.add(e);
            }
        }
        return resultado;
    }

    /** Complemento de S em relação a U: elementos de U que NÃO estão em S. */
    static Set<Integer> complemento(Set<Integer> U, Set<Integer> S) {
        return diferenca(U, S); // complemento é a diferença U - S
    }

    /** Inclusão X ⊆ Y: todo elemento de X precisa estar em Y. */
    static boolean subconjunto(Set<Integer> X, Set<Integer> Y) {
        for (int e : X) {
            if (!contem(Y, e)) {
                return false; // achou um elemento de X fora de Y
            }
        }
        return true; // vale também para X vazio (∅ ⊆ qualquer conjunto)
    }

    /** Inclusão própria X ⊂ Y: X ⊆ Y e X ≠ Y (como X ⊆ Y, ser diferente = ter menos elementos). */
    static boolean subconjuntoProprio(Set<Integer> X, Set<Integer> Y) {
        return subconjunto(X, Y) && cardinalidade(X) != cardinalidade(Y);
    }

    // =====================================================================
    // SEÇÃO 4 - OPERAÇÕES NATIVAS (somente para VALIDAR as versões manuais)
    // =====================================================================

    static Set<Integer> uniaoNativa(Set<Integer> A, Set<Integer> B) {
        Set<Integer> r = new TreeSet<>(A);
        r.addAll(B);
        return r;
    }

    static Set<Integer> intersecaoNativa(Set<Integer> A, Set<Integer> B) {
        Set<Integer> r = new TreeSet<>(A);
        r.retainAll(B);
        return r;
    }

    static Set<Integer> diferencaNativa(Set<Integer> A, Set<Integer> B) {
        Set<Integer> r = new TreeSet<>(A);
        r.removeAll(B);
        return r;
    }

    static Set<Integer> diferencaSimetricaNativa(Set<Integer> A, Set<Integer> B) {
        Set<Integer> r = uniaoNativa(A, B);
        r.removeAll(intersecaoNativa(A, B));
        return r;
    }

    static Set<Integer> complementoNativo(Set<Integer> U, Set<Integer> S) {
        Set<Integer> r = new TreeSet<>(U);
        r.removeAll(S);
        return r;
    }

    static boolean subconjuntoNativo(Set<Integer> X, Set<Integer> Y) {
        return Y.containsAll(X);
    }

    static boolean subconjuntoProprioNativo(Set<Integer> X, Set<Integer> Y) {
        return Y.containsAll(X) && !X.equals(Y);
    }

    // =====================================================================
    // SEÇÃO 5 - TELAS DE CADA OPÇÃO DO MENU
    // =====================================================================

    /** Opção 6: pertinência de um elemento x em A e em B (manual x nativa). */
    static void verificarPertinencia(Set<Integer> A, Set<Integer> B) {
        System.out.print("Digite um elemento para verificar: ");
        int x = lerInteiro();

        mostrarComparacao(x + " ∈ A ?", contem(A, x), A.contains(x));
        mostrarComparacao(x + " ∈ B ?", contem(B, x), B.contains(x));
    }

    /** Opção 7: inclusão entre A e B, nos dois sentidos (manual x nativa). */
    static void verificarInclusaoAB(Set<Integer> A, Set<Integer> B) {
        mostrarComparacao("A ⊆ B ?", subconjunto(A, B), subconjuntoNativo(A, B));
        mostrarComparacao("A ⊂ B ?", subconjuntoProprio(A, B), subconjuntoProprioNativo(A, B));
        mostrarComparacao("B ⊆ A ?", subconjunto(B, A), subconjuntoNativo(B, A));
        mostrarComparacao("B ⊂ A ?", subconjuntoProprio(B, A), subconjuntoProprioNativo(B, A));
    }

    /** Opção 8: testa um conjunto X informado pelo usuário contra A (ex.: {3, 4} ⊆ A). */
    static void verificarInclusaoX(Set<Integer> A) {
        Set<Integer> X = lerConjunto("X");

        mostrarComparacao("X ⊆ A ?", subconjunto(X, A), subconjuntoNativo(X, A));
        mostrarComparacao("X ⊂ A ?", subconjuntoProprio(X, A), subconjuntoProprioNativo(X, A));
    }

    /** Opção 9: cardinalidade de A e de B (contador manual x size()). */
    static void mostrarCardinalidade(Set<Integer> A, Set<Integer> B) {
        mostrarComparacao("|A|", cardinalidade(A), A.size());
        mostrarComparacao("|B|", cardinalidade(B), B.size());
    }

    /**
     * Opção 11: complemento de A (e de B) em relação a U.
     * Se U ainda não foi informado, ele é pedido uma única vez e guardado.
     */
    static void mostrarComplemento(Set<Integer> A, Set<Integer> B) {
        if (U == null) {
            U = lerConjunto("U (universo)");
        }

        System.out.println("Universo U = " + formatar(U));

        // O complemento só é bem definido quando o conjunto está contido em U.
        if (!subconjunto(A, U)) {
            System.out.println("Aviso: A não está contido em U (A ⊄ U); o complemento considera apenas os elementos de U.");
        }
        if (!subconjunto(B, U)) {
            System.out.println("Aviso: B não está contido em U (B ⊄ U); o complemento considera apenas os elementos de U.");
        }

        mostrarComparacao("Complemento de A em U", complemento(U, A), complementoNativo(U, A));
        mostrarComparacao("Complemento de B em U", complemento(U, B), complementoNativo(U, B));
    }

    /** Opção 12: exibe A e B. */
    static void mostrarConjuntos(Set<Integer> A, Set<Integer> B) {
        System.out.println("A = " + formatar(A));
        System.out.println("B = " + formatar(B));
    }

    // =====================================================================
    // SEÇÃO 6 - FORMATAÇÃO E COMPARAÇÃO MANUAL x NATIVA
    // =====================================================================

    /**
     * Imprime o resultado da implementação manual, o da solução nativa e se são iguais.
     * Funciona para conjuntos, booleanos e inteiros (Set.equals compara o conteúdo).
     */
    static void mostrarComparacao(String rotulo, Object manual, Object nativo) {
        System.out.println(rotulo + " = " + formatar(manual));
        System.out.println("   Manual: " + formatar(manual) + " | Nativa: " + formatar(nativo)
                + " | Confere: " + (manual.equals(nativo) ? "SIM" : "NÃO"));
    }

    /** Converte valores para texto: conjuntos como {1, 2}, vazio como ∅, booleanos como Verdadeiro/Falso. */
    static String formatar(Object valor) {
        if (valor instanceof Set) {
            Set<?> conjunto = (Set<?>) valor;
            if (conjunto.isEmpty()) {
                return "∅";
            }
            String texto = "";
            for (Object e : conjunto) {
                if (!texto.isEmpty()) {
                    texto += ", ";
                }
                texto += e;
            }
            return "{" + texto + "}";
        }
        if (valor instanceof Boolean) {
            return ((Boolean) valor) ? "Verdadeiro" : "Falso";
        }
        return String.valueOf(valor);
    }
}
