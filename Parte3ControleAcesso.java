import java.util.*;

/**
 * PARTE 3 - INTEGRAÇÃO (AVANÇADO)
 * Mini-projeto escolhido: opção 2 - CONTROLE DE ACESSO POR PERFIS.
 *
 * Ideia central: cada perfil é um CONJUNTO de permissões.
 *  - "O perfil pode fazer X?"            -> pertinência:  X ∈ perfil
 *  - "O perfil A cabe dentro do B?"      -> inclusão:     A ⊆ B
 *  - "O que um usuário com vários perfis pode fazer?" -> união: P1 ∪ P2 ∪ ...
 *
 * Observação: o Desafio de "não usar operadores prontos" é da Parte 1. Aqui o enunciado não o exige,
 * por isso usamos contains, containsAll e addAll da linguagem.
 *
 * Organização do arquivo:
 *  SEÇÃO 1 - Dados globais e menu principal
 *  SEÇÃO 2 - Leitura e normalização de dados
 *  SEÇÃO 3 - Operações de conjuntos (pertinência, inclusão, união)
 *  SEÇÃO 4 - Telas de cada opção do menu
 *  SEÇÃO 5 - Perfis do gabarito e cenários (Esperado x Obtido)
 *  SEÇÃO 6 - Formatação e comparação
 */
public class Parte3ControleAcesso {

    // =====================================================================
    // SEÇÃO 1 - DADOS GLOBAIS E MENU PRINCIPAL
    // =====================================================================

    // Cada perfil (nome) guarda o conjunto de permissões que ele possui.
    // TreeMap mantém os perfis em ordem alfabética (não precisa ordenar na hora de listar).
    static Map<String, Set<String>> perfis = new TreeMap<>();

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== PARTE 3 - CONTROLE DE ACESSO POR PERFIS ===");

        int opcao;

        do {
            System.out.println("\n--- MENU ---");
            System.out.println("1  - Criar perfil");
            System.out.println("2  - Adicionar permissão a um perfil");
            System.out.println("3  - Remover permissão de um perfil");
            System.out.println("4  - Mostrar permissões de um perfil");
            System.out.println("5  - Verificar se um perfil possui uma permissão (x ∈ perfil)");
            System.out.println("6  - Verificar subconjunto entre dois perfis (A ⊆ B)");
            System.out.println("7  - União de perfis (P1 ∪ P2 ∪ ...)");
            System.out.println("8  - Verificar permissão de um usuário com vários perfis (x ∈ união)");
            System.out.println("9  - Mostrar todos os perfis");
            System.out.println("10 - Mostrar quem possui uma permissão");
            System.out.println("11 - Carregar os perfis do gabarito (leitor, editor, admin, financeiro)");
            System.out.println("12 - Executar os cenários do gabarito (Esperado x Obtido)");
            System.out.println("0  - Sair");
            System.out.print("Escolha uma opção: ");

            opcao = lerInteiro();

            switch (opcao) {
                case 1:
                    criarPerfil();
                    break;
                case 2:
                    adicionarPermissao();
                    break;
                case 3:
                    removerPermissao();
                    break;
                case 4:
                    mostrarPermissoes();
                    break;
                case 5:
                    verificarPermissao();
                    break;
                case 6:
                    verificarSubconjunto();
                    break;
                case 7:
                    mostrarUniao();
                    break;
                case 8:
                    verificarPermissaoUsuario();
                    break;
                case 9:
                    mostrarTodos();
                    break;
                case 10:
                    mostrarQuemPossui();
                    break;
                case 11:
                    carregarGabarito();
                    break;
                case 12:
                    executarCenarios();
                    break;
                case 0:
                    System.out.println("Programa encerrado.");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }

        } while (opcao != 0);

        scanner.close();
    }

    // =====================================================================
    // SEÇÃO 2 - LEITURA E NORMALIZAÇÃO DE DADOS
    // =====================================================================

    /** Lê um inteiro (opção do menu), repetindo a pergunta enquanto a entrada for inválida. */
    static int lerInteiro() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Digite um número inteiro válido: ");
            }
        }
    }

    /**
     * Padroniza um texto: remove espaços das pontas e converte para minúsculas.
     * Necessário porque pertinência e inclusão dependem de igualdade exata do texto:
     * sem isso, "Editor", "editor" e "editor " seriam elementos diferentes.
     */
    static String normalizar(String texto) {
        return texto.trim().toLowerCase(Locale.ROOT);
    }

    /** Lê um texto (nome de perfil ou de permissão) já normalizado, recusando texto vazio. */
    static String lerTexto(String pergunta) {
        while (true) {
            System.out.print(pergunta);
            String texto = normalizar(scanner.nextLine());
            if (!texto.isEmpty()) {
                return texto;
            }
            System.out.println("O texto não pode ser vazio.");
        }
    }

    /** Pede o nome de um perfil e confere se existe. Retorna o nome, ou null se não existir. */
    static String lerPerfilExistente(String pergunta) {
        String nome = lerTexto(pergunta);
        if (!perfis.containsKey(nome)) {
            System.out.println("Perfil não encontrado: " + nome);
            return null;
        }
        return nome;
    }

    /**
     * Lê vários perfis em uma linha, separados por vírgula (ex.: editor, financeiro).
     * Descarta repetidos e confere se todos existem e se há pelo menos "minimo" perfis.
     * Retorna a lista de nomes, ou null se algo estiver errado.
     */
    static List<String> lerListaPerfis(int minimo) {
        System.out.print("Digite os perfis separados por vírgula (ex.: editor, financeiro): ");
        String linha = scanner.nextLine();

        Set<String> nomes = new LinkedHashSet<>(); // mantém a ordem digitada e descarta repetidos
        for (String parte : linha.split(",")) {
            String nome = normalizar(parte);
            if (!nome.isEmpty()) {
                nomes.add(nome);
            }
        }

        List<String> inexistentes = new ArrayList<>();
        for (String nome : nomes) {
            if (!perfis.containsKey(nome)) {
                inexistentes.add(nome);
            }
        }
        if (!inexistentes.isEmpty()) {
            System.out.println("Perfil(is) não encontrado(s): " + inexistentes);
            return null;
        }
        if (nomes.size() < minimo) {
            System.out.println("Informe pelo menos " + minimo + " perfil(is) diferente(s).");
            return null;
        }
        return new ArrayList<>(nomes);
    }

    // =====================================================================
    // SEÇÃO 3 - OPERAÇÕES DE CONJUNTOS
    // =====================================================================

    /** Pertinência: x ∈ S. */
    static boolean pertence(String x, Set<String> S) {
        return S.contains(x);
    }

    /** Inclusão: X ⊆ Y (toda permissão de X também está em Y). */
    static boolean subconjunto(Set<String> X, Set<String> Y) {
        return Y.containsAll(X);
    }

    /**
     * União dos perfis informados: P1 ∪ P2 ∪ ... (sem permissões repetidas).
     * Recebe o mapa como parâmetro para servir tanto ao programa quanto aos cenários do gabarito.
     */
    static Set<String> uniaoDePerfis(List<String> nomes, Map<String, Set<String>> mapa) {
        Set<String> uniao = new TreeSet<>();
        for (String nome : nomes) {
            uniao.addAll(mapa.get(nome));
        }
        return uniao;
    }

    // =====================================================================
    // SEÇÃO 4 - TELAS DE CADA OPÇÃO DO MENU
    // =====================================================================

    /** Opção 1: cria um perfil vazio (∅). */
    static void criarPerfil() {
        String nome = lerTexto("Digite o nome do perfil: ");

        if (perfis.containsKey(nome)) {
            System.out.println("Esse perfil já existe.");
        } else {
            perfis.put(nome, new TreeSet<>());
            System.out.println("Perfil criado com sucesso: " + nome + " = ∅");
        }
    }

    /** Opção 2: adiciona uma permissão ao conjunto do perfil. */
    static void adicionarPermissao() {
        String nome = lerPerfilExistente("Digite o nome do perfil: ");
        if (nome == null) {
            return;
        }

        String permissao = lerTexto("Digite a permissão: ");

        if (perfis.get(nome).add(permissao)) {
            System.out.println("Permissão adicionada. " + nome + " = " + formatar(perfis.get(nome)));
        } else {
            System.out.println("O perfil já possui essa permissão.");
        }
    }

    /** Opção 3: remove uma permissão do perfil. */
    static void removerPermissao() {
        String nome = lerPerfilExistente("Digite o nome do perfil: ");
        if (nome == null) {
            return;
        }

        String permissao = lerTexto("Digite a permissão que deseja remover: ");

        if (perfis.get(nome).remove(permissao)) {
            System.out.println("Permissão removida. " + nome + " = " + formatar(perfis.get(nome)));
        } else {
            System.out.println("Essa permissão não está no perfil.");
        }
    }

    /** Opção 4: mostra o conjunto de permissões de um perfil. */
    static void mostrarPermissoes() {
        String nome = lerPerfilExistente("Digite o nome do perfil: ");
        if (nome == null) {
            return;
        }

        System.out.println(nome + " = " + formatar(perfis.get(nome)));
    }

    /** Opção 5: pertinência de uma permissão no conjunto de UM perfil (x ∈ perfil). */
    static void verificarPermissao() {
        String nome = lerPerfilExistente("Digite o nome do perfil: ");
        if (nome == null) {
            return;
        }

        String permissao = lerTexto("Digite a permissão: ");
        Set<String> conjunto = perfis.get(nome);

        if (pertence(permissao, conjunto)) {
            System.out.println("SIM. " + permissao + " ∈ " + formatar(conjunto) + ", logo o perfil " + nome + " possui essa permissão.");
        } else {
            System.out.println("NÃO. " + permissao + " ∉ " + formatar(conjunto) + ", logo o perfil " + nome + " não possui essa permissão.");
        }
    }

    /** Opção 6: inclusão entre dois perfis, nos dois sentidos (A ⊆ B e B ⊆ A). */
    static void verificarSubconjunto() {
        String a = lerPerfilExistente("Digite o perfil A: ");
        if (a == null) {
            return;
        }
        String b = lerPerfilExistente("Digite o perfil B: ");
        if (b == null) {
            return;
        }

        Set<String> permissoesA = perfis.get(a);
        Set<String> permissoesB = perfis.get(b);

        System.out.println(a + " = " + formatar(permissoesA));
        System.out.println(b + " = " + formatar(permissoesB));
        System.out.println(a + " ⊆ " + b + " ? " + formatar(subconjunto(permissoesA, permissoesB)));
        System.out.println(b + " ⊆ " + a + " ? " + formatar(subconjunto(permissoesB, permissoesA)));
    }

    /** Opção 7: mostra a união das permissões de dois ou mais perfis. */
    static void mostrarUniao() {
        List<String> nomes = lerListaPerfis(2);
        if (nomes == null) {
            return;
        }

        for (String nome : nomes) {
            System.out.println(nome + " = " + formatar(perfis.get(nome)));
        }
        System.out.println(String.join(" ∪ ", nomes) + " = " + formatar(uniaoDePerfis(nomes, perfis)));
    }

    /**
     * Opção 8: um usuário que acumula perfis tem como permissões a UNIÃO deles.
     * Verifica se a permissão pertence a essa união e mostra o motivo em notação de conjuntos.
     */
    static void verificarPermissaoUsuario() {
        List<String> nomes = lerListaPerfis(1);
        if (nomes == null) {
            return;
        }

        String permissao = lerTexto("Digite a permissão: ");
        Set<String> uniao = uniaoDePerfis(nomes, perfis);

        System.out.println("Perfis do usuário: " + nomes);
        System.out.println("Permissões do usuário (união) = " + formatar(uniao));

        if (pertence(permissao, uniao)) {
            System.out.println("SIM. " + permissao + " ∈ " + formatar(uniao) + ", logo o usuário pode.");
        } else {
            System.out.println("NÃO. " + permissao + " ∉ " + formatar(uniao) + ", logo o usuário não pode.");
        }
    }

    /** Opção 9: lista todos os perfis (já em ordem alfabética, por causa do TreeMap). */
    static void mostrarTodos() {
        if (perfis.isEmpty()) {
            System.out.println("Nenhum perfil cadastrado.");
            return;
        }

        System.out.println("\n--- PERFIS CADASTRADOS ---");
        for (Map.Entry<String, Set<String>> entrada : perfis.entrySet()) {
            System.out.println(entrada.getKey() + " = " + formatar(entrada.getValue()));
        }
    }

    /** Opção 10: lista os perfis cujo conjunto contém a permissão. */
    static void mostrarQuemPossui() {
        String permissao = lerTexto("Digite a permissão que deseja procurar: ");

        boolean encontrou = false;

        for (Map.Entry<String, Set<String>> entrada : perfis.entrySet()) {
            if (pertence(permissao, entrada.getValue())) {
                System.out.println(entrada.getKey() + " possui a permissão " + permissao + ".");
                encontrou = true;
            }
        }

        if (!encontrou) {
            System.out.println("Nenhum perfil possui essa permissão.");
        }
    }

    // =====================================================================
    // SEÇÃO 5 - PERFIS DO GABARITO E CENÁRIOS (ESPERADO x OBTIDO)
    // =====================================================================

    /** Cria no mapa recebido os quatro perfis do gabarito (substitui os de mesmo nome). */
    static void carregarGabaritoEm(Map<String, Set<String>> mapa) {
        mapa.put("leitor", conjunto("ler"));
        mapa.put("editor", conjunto("ler", "editar"));
        mapa.put("admin", conjunto("ler", "editar", "excluir", "gerir_usuarios"));
        mapa.put("financeiro", conjunto("ver_relatorios"));
    }

    /** Opção 11: carrega os perfis do gabarito no programa, para uso manual nas demais opções. */
    static void carregarGabarito() {
        carregarGabaritoEm(perfis);
        System.out.println("Perfis do gabarito carregados (os de mesmo nome foram substituídos):");
        mostrarTodos();
    }

    /**
     * Opção 12: executa 3 cenários com os resultados ESPERADOS (calculados à mão, escritos no código)
     * e compara com o que o programa OBTÉM. Usa um mapa próprio, sem alterar os perfis do usuário.
     */
    static void executarCenarios() {
        Map<String, Set<String>> g = new TreeMap<>();
        carregarGabaritoEm(g);

        System.out.println("\nPerfis usados nos cenários:");
        for (Map.Entry<String, Set<String>> entrada : g.entrySet()) {
            System.out.println("  " + entrada.getKey() + " = " + formatar(entrada.getValue()));
        }

        // CENÁRIO 1: união de dois perfis com permissão em comum.
        System.out.println("\n--- CENÁRIO 1: leitor + editor ---");
        System.out.println("À mão: {ler} ∪ {editar, ler} = {editar, ler}");
        mostrarComparacao("leitor ∪ editor",
                conjunto("ler", "editar"),
                uniaoDePerfis(Arrays.asList("leitor", "editor"), g));

        // CENÁRIO 2: união de perfis sem permissões em comum e teste de permissão sobre a união.
        System.out.println("\n--- CENÁRIO 2: editor + financeiro, esse usuário pode excluir? ---");
        System.out.println("À mão: {editar, ler} ∪ {ver_relatorios} = {editar, ler, ver_relatorios}; excluir ∉ união, logo NÃO");
        Set<String> uniao2 = uniaoDePerfis(Arrays.asList("editor", "financeiro"), g);
        mostrarComparacao("editor ∪ financeiro",
                conjunto("ler", "editar", "ver_relatorios"),
                uniao2);
        mostrarComparacao("excluir ∈ (editor ∪ financeiro) ?",
                false,
                pertence("excluir", uniao2));

        // CENÁRIO 3: inclusão entre perfis.
        System.out.println("\n--- CENÁRIO 3: subconjuntos em relação a admin ---");
        System.out.println("À mão: {editar, ler} ⊆ {editar, excluir, gerir_usuarios, ler} é verdadeiro;"
                + " {ver_relatorios} ⊆ admin é falso (ver_relatorios ∉ admin)");
        mostrarComparacao("editor ⊆ admin ?",
                true,
                subconjunto(g.get("editor"), g.get("admin")));
        mostrarComparacao("financeiro ⊆ admin ?",
                false,
                subconjunto(g.get("financeiro"), g.get("admin")));
    }

    /** Monta um conjunto ordenado a partir dos elementos informados. */
    static Set<String> conjunto(String... elementos) {
        return new TreeSet<>(Arrays.asList(elementos));
    }

    // =====================================================================
    // SEÇÃO 6 - FORMATAÇÃO E COMPARAÇÃO
    // =====================================================================

    /** Imprime o valor esperado (calculado à mão), o obtido pelo programa e se são iguais. */
    static void mostrarComparacao(String rotulo, Object esperado, Object obtido) {
        System.out.println(rotulo);
        System.out.println("   Esperado: " + formatar(esperado) + " | Obtido: " + formatar(obtido)
                + " | Confere: " + (esperado.equals(obtido) ? "SIM" : "NÃO"));
    }

    /** Converte valores para texto: conjuntos como {a, b}, vazio como ∅, booleanos como Verdadeiro/Falso. */
    static String formatar(Object valor) {
        if (valor instanceof Set) {
            Set<?> conjunto = (Set<?>) valor;
            if (conjunto.isEmpty()) {
                return "∅";
            }
            StringBuilder texto = new StringBuilder();
            for (Object e : conjunto) {
                if (texto.length() > 0) {
                    texto.append(", ");
                }
                texto.append(e);
            }
            return "{" + texto + "}";
        }
        if (valor instanceof Boolean) {
            return ((Boolean) valor) ? "Verdadeiro" : "Falso";
        }
        return String.valueOf(valor);
    }
}
