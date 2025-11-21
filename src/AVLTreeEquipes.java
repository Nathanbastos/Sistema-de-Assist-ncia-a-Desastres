package com.resgate.app;

// Árvore AVL para armazenar equipes de resgate.
// Mantém árvore balanceada automaticamente.
public class AVLTreeEquipes {

    // Nó interno da AVL contendo uma equipe.
    private class Node {
        EquipeResgate equipe;
        int height;       // altura do nó (conceito base da AVL)
        Node left, right; // filhos

        Node(EquipeResgate e) {
            this.equipe = e;
            this.height = 1; // nós novos têm altura 1
        }
    }

    private Node root;

    // ---------- Funções auxiliares de AVL ----------

    // Retorna altura do nó (0 se nulo)
    private int h(Node n){ return n==null ? 0 : n.height; }

    // Atualiza altura do nó baseado na maior altura dos filhos
    private void update(Node n){ n.height = 1 + Math.max(h(n.left), h(n.right)); }

    // Fator de balanceamento: FB = altura esquerda - altura direita
    // Deve estar entre -1 e +1 para estar equilibrado.
    private int bal(Node n){ return n==null ? 0 : h(n.left) - h(n.right); }

    // ---------- Rotações (correção de desequilíbrio) ----------

    // Rotação simples à direita (caso LL)
    private Node rRight(Node y){
        Node x = y.left;
        Node t2 = x.right;

        x.right = y;
        y.left = t2;

        update(y); 
        update(x);
        return x; // nova raiz da subárvore
    }

    // Rotação simples à esquerda (caso RR)
    private Node rLeft(Node x){
        Node y = x.right;
        Node t2 = y.left;

        y.left = x;
        x.right = t2;

        update(x);
        update(y);
        return y;
    }

    // ---------- Inserção recursiva ----------

    private Node insert(Node n, EquipeResgate e){

        // Caso base: chegou em posição nula para inserir
        if(n == null) return new Node(e);

        // Decide se vai para esquerda ou direita
        int cmp = e.compararCom(n.equipe);

        if(cmp < 0) n.left = insert(n.left, e);
        else if(cmp > 0) n.right = insert(n.right, e);
        else return n; // ignorar duplicados

        // Atualiza altura após inserção recursiva
        update(n);

        // Calcula fator de balanceamento
        int b = bal(n);

        // ---------- Verifica os 4 casos de rotação ----------

        // Caso LL: desbalanceado à esquerda e inserido à esquerda
        if(b > 1 && e.compararCom(n.left.equipe) < 0)
            return rRight(n);

        // Caso RR: desbalanceado à direita e inserido à direita
        if(b < -1 && e.compararCom(n.right.equipe) > 0)
            return rLeft(n);

        // Caso LR: inserção cruzada, requer rotação dupla
        if(b > 1 && e.compararCom(n.left.equipe) > 0){
            n.left = rLeft(n.left);
            return rRight(n);
        }

        // Caso RL: inserção cruzada, rotação dupla ao contrário
        if(b < -1 && e.compararCom(n.right.equipe) < 0){
            n.right = rRight(n.right);
            return rLeft(n);
        }

        return n; // retornando nó já balanceado
    }

    public void inserir(EquipeResgate e){
        root = insert(root, e);
    }

    // ---------- Impressão In-Order (ordem crescente de ID) ----------

    private void inOrder(Node n){
        if(n != null){
            inOrder(n.left);
            System.out.println(n.equipe);
            inOrder(n.right);
        }
    }

    public void imprimirInOrder(){
        inOrder(root);
    }
}
