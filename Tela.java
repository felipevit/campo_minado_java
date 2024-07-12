package com.mycompany.tela;

// NOME DO ALUNO: FELIPE FILLA VITORINO
//GRR: 20230947
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.Scanner;

public class Tela extends JFrame {

    private int NUM_LINHAS;
    private int NUM_COLUNAS;
    private int NUM_MINES;
    private Celula[][] celulas;
    private JButton[][] botoes;
    private int marcadas = 0;
    private JButton botaoReiniciar;

    public Tela(int numLinhas, int numColunas, int numMinas) {
        NUM_LINHAS = numLinhas;
        NUM_COLUNAS = numColunas;
        NUM_MINES = numMinas;
        celulas = new Celula[NUM_LINHAS][NUM_COLUNAS];
        botoes = new JButton[NUM_LINHAS][NUM_COLUNAS];
        criarTela();
        inicializarCelulas();
        colocarMinas();
        calcularNumero();
    }

    private void reiniciarJogo() {
        marcadas = 0; 
        inicializarCelulas();
        colocarMinas();
        calcularNumero();
        atualizarTela();
    }

    private void colocarMinas() {
        Random rand = new Random();
        int qnt = 0;

        while (qnt < NUM_MINES) {
            int linha = rand.nextInt(NUM_LINHAS);
            int coluna = rand.nextInt(NUM_COLUNAS);

            if (!celulas[linha][coluna].isMina()) {
                celulas[linha][coluna].setMina(true);
                qnt++;
            }
        }
    }

    private void inicializarCelulas() {
        for (int i = 0; i < NUM_LINHAS; i++) {
            for (int j = 0; j < NUM_COLUNAS; j++) {
                celulas[i][j] = new Celula();
            }
        }
    }

    private void calcularNumero() {
        for (int linha = 0; linha < NUM_LINHAS; linha++) {
            for (int coluna = 0; coluna < NUM_COLUNAS; coluna++) {
                if (!celulas[linha][coluna].isMina()) {
                    int count = 0;
                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1; j <= 1; j++) {
                            int r = linha + i;
                            int c = coluna + j;
                            if (r >= 0 && r < NUM_LINHAS && c >= 0 && c < NUM_COLUNAS && celulas[r][c].isMina()) {
                                count++;
                            }
                        }
                    }
                    celulas[linha][coluna].setNumero(count);
                }
            }
        }
    }

    private void criarTela() {
        setTitle("Campo Minado");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        JPanel painelSuperior = new JPanel();
        botaoReiniciar = new JButton("Reiniciar Jogo");
        botaoReiniciar.addActionListener(e -> reiniciarJogo());
        painelSuperior.add(botaoReiniciar);
        add(painelSuperior, BorderLayout.NORTH);

        JPanel painelGrid = new JPanel();
        painelGrid.setLayout(new GridLayout(NUM_LINHAS, NUM_COLUNAS));

        for (int i = 0; i < NUM_LINHAS; i++) {
            for (int j = 0; j < NUM_COLUNAS; j++) {
                botoes[i][j] = new JButton();
                botoes[i][j].setFont(new Font("Arial", Font.PLAIN, 10));
                botoes[i][j].addMouseListener(new Click(i, j));
                botoes[i][j].setBackground(Color.gray);
                painelGrid.add(botoes[i][j]);
            }
        }
        
        add(painelGrid, BorderLayout.CENTER);
    }

    private void atualizarTela() {
        for (int i = 0; i < NUM_LINHAS; i++) {
            for (int j = 0; j < NUM_COLUNAS; j++) {
                botoes[i][j].setEnabled(true);
                botoes[i][j].setText("");
                botoes[i][j].setBackground(Color.gray);
                celulas[i][j].setAberta(false);
            }
        }
    }

    private void abrirCelula(int linha, int coluna) {
        if (celulas[linha][coluna].isAberta()) {
            return;
        }

        celulas[linha][coluna].setAberta(true);
        botoes[linha][coluna].setEnabled(false);

        if (celulas[linha][coluna].isMina()) {
            botoes[linha][coluna].setText("M");
            revelarMinas();
            JOptionPane.showMessageDialog(this, "Game Over!");
            reiniciarJogo();
            return;
        }
        botoes[linha][coluna].setText(String.format("%d", celulas[linha][coluna].getNumero()));
        botoes[linha][coluna].setBackground(Color.LIGHT_GRAY);
    }

    private void marcarCelula(int linha, int coluna) {
        if (!celulas[linha][coluna].isAberta() && !(botoes[linha][coluna].getText().equals("?"))) {
            botoes[linha][coluna].setText("?");
            if (celulas[linha][coluna].isMina()) {
                marcadas++;
            }
            if (marcadas == NUM_MINES) {
                JOptionPane.showMessageDialog(this, "Você venceu!");
                reiniciarJogo();
            }
        }
    }

    private void revelarMinas() {
        for (int i = 0; i < NUM_LINHAS; i++) {
            for (int j = 0; j < NUM_COLUNAS; j++) {
                if (celulas[i][j].isMina()) {
                    botoes[i][j].setBackground(Color.RED);
                }
            }
        }
    }

    private class Click extends MouseAdapter {
        private int linha;
        private int coluna;

        public Click(int linha, int coluna) {
            this.linha = linha;
            this.coluna = coluna;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                abrirCelula(linha, coluna);
            } else if (SwingUtilities.isRightMouseButton(e)) {
                marcarCelula(linha, coluna);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Scanner scanner = new Scanner(System.in);
            int linhas = 0;
            int colunas = 0;
            int numMinas = 0;
            int escolha = 0;

            System.out.println("Escolha o nível de dificuldade:");
            System.out.println("1. Fácil");
            System.out.println("2. Médio");
            System.out.println("3. Difícil");

            try {
                escolha = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Entrada inválida. Por favor, insira um número entre 1 e 3.");
                scanner.close();
                return;
            }

            switch (escolha) {
                case 1:
                    linhas = 9;
                    colunas = 9;
                    numMinas = 10;
                    break;
                case 2:
                    linhas = 16;
                    colunas = 16;
                    numMinas = 40;
                    break;
                case 3:
                    linhas = 30;
                    colunas = 16;
                    numMinas = 99;
                    break;
                default:
                    System.out.println("Opção inválida. Por favor, escolha um número entre 1 e 3.");
                    scanner.close();
                    return;
            }

            scanner.close();
            Tela jogo = new Tela(linhas, colunas, numMinas);
            jogo.setVisible(true);
        });
    }
}
