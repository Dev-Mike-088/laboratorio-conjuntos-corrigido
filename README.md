# Laboratório de Conjuntos

## Introdução

Este projeto foi desenvolvido com o objetivo de aplicar, de forma prática, os conceitos de Teoria dos Conjuntos, Conjuntos Numéricos e lógica de controle de acesso em sistemas computacionais. A proposta da atividade foi unir fundamentos matemáticos com programação em Java, evidenciando como operações como união, interseção, diferença, pertinência e inclusão podem ser implementadas e validadas em um contexto real de desenvolvimento.

A estrutura do trabalho foi organizada em três partes, cada uma com foco em um tema específico, porém todas conectadas pela mesma base conceitual: representação e manipulação de conjuntos para resolver problemas concretos.

---

## Objetivo geral

O objetivo deste laboratório foi demonstrar, de maneira didática e aplicada, como os conjuntos podem ser utilizados para:

- representar relações entre elementos e grupos;
- classificar números conforme seu conjunto numérico;
- modelar permissões e acessos de usuários em sistemas reais.

Além disso, a atividade foi pensada para reforçar o raciocínio lógico, a organização do código e a validação dos resultados obtidos, comparando a implementação manual com os recursos nativos da linguagem Java.

---

## Entregáveis do projeto

### 1. Parte 1 — Teoria dos Conjuntos

Na primeira parte, o programa implementa manualmente as operações entre conjuntos, sem depender diretamente dos métodos nativos da linguagem para a lógica principal. O usuário informa dois conjuntos A e B e pode realizar:

- união;
- interseção;
- diferença;
- diferença simétrica;
- pertinência;
- inclusão;
- cardinalidade;
- complemento em relação a um universo U.

Esse módulo foi desenvolvido para evidenciar a lógica por trás das operações matemáticas, comparando os resultados obtidos pela implementação manual com a solução nativa do Java para verificar a correção.

### 2. Parte 2 — Conjuntos Numéricos

A segunda parte do projeto trata da classificação de números em diferentes conjuntos numéricos, como:

- ℕ (naturais);
- ℤ (inteiros);
- ℚ (racionais);
- 𝕀 (irracionais);
- ℝ (reais).

O programa aceita entradas em formatos diversos, incluindo:

- inteiros;
- frações;
- decimais exatos;
- dízimas periódicas;
- raízes quadradas.

Também foram incluídos recursos extras, como:

- Crivo de Eratóstenes para listar números primos até um valor informado;
- cálculo dos divisores de um número inteiro.

A convenção adotada no projeto foi: 0 pertence ao conjunto dos naturais, ou seja, ℕ = {0, 1, 2, 3, ...}.

### 3. Parte 3 — Controle de Acesso por Perfis

A terceira parte aplica a teoria dos conjuntos em um problema real de autorização e autenticação. Cada perfil é tratado como um conjunto de permissões, permitindo verificar:

- se uma permissão pertence a um perfil;
- se um perfil está contido em outro;
- a união de permissões entre diferentes perfis;
- se um usuário com múltiplos perfis pode acessar determinada funcionalidade.

Essa parte mostra como conceitos matemáticos fundamentais se tornam extremamente úteis em sistemas de segurança e controle de acesso.

---

## Estrutura do repositório

- [Parte1Conjuntos.java](Parte1Conjuntos.java): operações de conjuntos implementadas manualmente
- [Parte2ConjuntosNumericos.java](Parte2ConjuntosNumericos.java): classificação numérica e cálculos auxiliares
- [Parte3ControleAcesso.java](Parte3ControleAcesso.java): mini sistema de controle de acesso por perfis
- [logs/](logs): arquivos com exemplos e cenários de execução
- [README.md](README.md): documentação do projeto

---

## Requisitos

Para executar corretamente o projeto, é necessário ter instalado:

- Java JDK 9 ou superior;
- terminal ou prompt de comando;
- ambiente com acesso ao compilador Java.

---

## Como executar

### 1. Compilar os arquivos

No diretório do projeto, execute:

```bash
javac *.java
```

Ou, se preferir, compile cada arquivo individualmente:

```bash
javac Parte1Conjuntos.java
javac Parte2ConjuntosNumericos.java
javac Parte3ControleAcesso.java
```

### 2. Executar cada parte

```bash
java Parte1Conjuntos
```

```bash
java Parte2ConjuntosNumericos
```

```bash
java Parte3ControleAcesso
```

---

## Funcionamento geral do projeto

O projeto foi pensado para ser didático e intuitivo. Cada parte oferece um menu interativo no console, permitindo ao usuário inserir valores e observar imediatamente os resultados das operações realizadas.

A organização do código foi feita em blocos bem definidos, incluindo:

- leitura de dados;
- operações matemáticas e de conjunto;
- validação dos resultados;
- exibição em console;
- cenários de teste e exemplos de execução.

---

## Validação e testes

O projeto foi validado por meio de compilação dos arquivos Java e execução básica dos programas. A verificação confirmou que os programas compilam corretamente e que os menus de interação iniciam sem erros.

Comando usado para validação:

```bash
javac *.java
```

A partir dessa etapa, cada programa pode ser executado individualmente para testar as funcionalidades do laboratório em exemplos reais.

---

## Logs e cenários de execução

A pasta [logs/](logs) contém exemplos de execução e cenários de teste para as diferentes partes do sistema. Esses arquivos ajudam a validar o comportamento esperado e demonstram a aplicação dos conceitos em situações como:

- conjunto vazio;
- conjuntos iguais;
- conjuntos disjuntos;
- pertinência e inclusão;
- elementos repetidos;
- cenários de controle de acesso por perfis.

---

## Considerações finais

Este laboratório foi uma oportunidade de conectar teoria e prática de forma clara e objetiva. O que, em princípio, poderia parecer apenas uma abordagem matemática abstrata, mostrou-se extremamente útil quando aplicado à programação, especialmente na parte de controle de acesso, onde a lógica de conjuntos é essencial para decidir permissões e autorizações.

Ao final, o trabalho evidencia que conceitos como pertinência, inclusão e união possuem aplicação direta em sistemas reais e são fundamentais para a construção de soluções organizadas, seguras e funcionais.

---

## Autores

- Mikael Machado Souza
- Daniel Plínio Henrique da Silva

---

## Disciplina

Matemática Discreta/ Matemática para computação