package com.gugawag.so.ipc;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class DateClient {

    public static void main(String[] args) {
        try {
            // Conecta ao servidor na porta 6013 (pode ser alterado para o IP do servidor se necessário)
            Socket sock = new Socket("localhost", 6013);
            DataOutputStream dos = new DataOutputStream(sock.getOutputStream());
            DataInputStream dis = new DataInputStream(sock.getInputStream());
            BufferedReader bin = new BufferedReader(new InputStreamReader(dis));

            System.out.println("=== Cliente iniciado ===\n");

            // Recebe a saudação inicial do servidor
            String welcomeMessage = bin.readLine();
            System.out.println("O servidor me disse: " + welcomeMessage);

            // Envia uma mensagem inicial para o servidor
            dos.writeUTF("Marcio Jose da Silva");

            // Recebe e exibe a resposta do servidor
            String response = bin.readLine();
            System.out.println("O servidor me disse: " + response);

            // Loop para interação contínua
            while (true) {
                // Envia o comando "menu" para o servidor para mostrar as opções
                dos.writeUTF("menu");

                // Recebe o menu de operações com arquivos
                String menu = bin.readLine();
                System.out.println("Menu de operações do servidor:\n" + menu);

                // Solicita ao usuário que escolha uma opção
                Scanner teclado = new Scanner(System.in);
                System.out.print("Escolha uma opção (1-5): ");
                String opcao = teclado.nextLine();

                // Envia a opção escolhida para o servidor
                dos.writeUTF(opcao);

                // Processa a resposta do servidor
                String resultado = bin.readLine();
                System.out.println("Resposta do servidor: " + resultado);

                // Se a opção foi para excluir, renomear ou criar um arquivo, o cliente deve enviar mais informações
                if (opcao.equals("2")) { // Excluir arquivo
                    System.out.print("Digite o nome do arquivo a excluir: ");
                    String nomeArquivo = teclado.nextLine();
                    dos.writeUTF(nomeArquivo); // Envia o nome do arquivo para excluir
                    resultado = bin.readLine();
                    System.out.println("Resposta do servidor: " + resultado);
                } else if (opcao.equals("3")) { // Renomear arquivo
                    System.out.print("Digite o nome do arquivo a renomear: ");
                    String arquivoAntigo = teclado.nextLine();
                    dos.writeUTF(arquivoAntigo); // Envia o nome do arquivo para renomear

                    System.out.print("Digite o novo nome para o arquivo: ");
                    String arquivoNovo = teclado.nextLine();
                    dos.writeUTF(arquivoNovo); // Envia o novo nome
                    resultado = bin.readLine();
                    System.out.println("Resposta do servidor: " + resultado);
                } else if (opcao.equals("4")) { // Criar arquivo
                    System.out.print("Digite o nome do novo arquivo: ");
                    String nomeArquivo = teclado.nextLine();
                    dos.writeUTF(nomeArquivo); // Envia o nome do arquivo para criar
                    resultado = bin.readLine();
                    System.out.println("Resposta do servidor: " + resultado);
                } else if (opcao.equals("5")) { // Sair
                    System.out.println("Encerrando o cliente...");
                    break; // Sai do loop e encerra o cliente
                }
            }

            // Fecha a conexão com o servidor
            sock.close();

        } catch (IOException ioe) {
            System.err.println(ioe);
        }
    }
}
