package com.gugawag.so.ipc;

/**
 * Time-of-day server listening to port 6013.
 *
 * @author Silberschatz, Gagne, and Galvin. Pequenas alterações feitas por Gustavo Wagner (gugawag@gmail.com)
 * Operating System Concepts  - Ninth Edition
 * Copyright John Wiley & Sons - 2013.
 */

import java.net.*;
import java.io.*;
import java.util.Date;
import java.util.Scanner;

public class DateServer {

    public static void main(String[] args) {
        try {
            ServerSocket sock = new ServerSocket(6013);

            System.out.println("=== Servidor iniciado ===\n");
            // escutando por conexões
            while (true) {
                Socket client = sock.accept();
                Thread thread = new Thread(() -> {
                    try {
                        DataOutputStream dos = new DataOutputStream(client.getOutputStream());
                        DataInputStream dis = new DataInputStream(client.getInputStream());

                        while (true) {
                            System.out.println("Servidor recebeu comunicação do IP: " + client.getInetAddress() + "-" + client.getPort());
                            PrintWriter pout = new PrintWriter(dos, true);
                            pout.println(new Date().toString() + "- Boa noite, cliente!");

                            BufferedReader bin = new BufferedReader(new InputStreamReader(dis));
                            String line = bin.readLine(); // Leitura do comando do cliente
                            System.out.println("O cliente me disse: " + line);

                            // Se o comando for 'menu', mostra as opções
                            if (line.equals("menu")) {
                                mostrarMenu(dos);
                            } else {
                                // Caso o cliente envie um comando para manipulação de arquivos
                                executarComando(line, dos);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                thread.start();
            }
        } catch (IOException ioe) {
            System.err.println(ioe);
        }
    }

    // Função para mostrar o menu de opções
    public static void mostrarMenu(DataOutputStream dos) throws IOException {
        String menu = "\nMenu de Operações com Arquivos:\n" +
                "1 - Listar arquivos\n" +
                "2 - Excluir arquivo\n" +
                "3 - Renomear arquivo\n" +
                "4 - Criar arquivo\n" +
                "5 - Sair\n";
        dos.writeUTF(menu);
    }

    // Função para executar o comando recebido do cliente
    public static void executarComando(String comando, DataOutputStream dos) throws IOException {
        switch (comando) {
            case "1":
                listarArquivos(dos);
                break;
            case "2":
                excluirArquivo(dos);
                break;
            case "3":
                renomearArquivo(dos);
                break;
            case "4":
                criarArquivo(dos);
                break;
            case "5":
                dos.writeUTF("Saindo...");
                break;
            default:
                dos.writeUTF("Comando inválido.");
        }
    }

    // Função para listar arquivos no diretório atual
    public static void listarArquivos(DataOutputStream dos) throws IOException {
        File dir = new File(".");
        String[] arquivos = dir.list();
        if (arquivos != null) {
            StringBuilder lista = new StringBuilder("Arquivos no diretório:\n");
            for (String arquivo : arquivos) {
                lista.append(arquivo).append("\n");
            }
            dos.writeUTF(lista.toString());
        } else {
            dos.writeUTF("Não foi possível listar os arquivos.");
        }
    }

    // Função para excluir um arquivo
    public static void excluirArquivo(DataOutputStream dos) throws IOException {
        Scanner teclado = new Scanner(System.in);
        dos.writeUTF("Digite o nome do arquivo para excluir:");
        String arquivo = teclado.nextLine();
        File file = new File(arquivo);
        if (file.exists()) {
            if (file.delete()) {
                dos.writeUTF("Arquivo '" + arquivo + "' excluído com sucesso.");
            } else {
                dos.writeUTF("Erro ao excluir o arquivo.");
            }
        } else {
            dos.writeUTF("Arquivo '" + arquivo + "' não encontrado.");
        }
    }

    // Função para renomear um arquivo
    public static void renomearArquivo(DataOutputStream dos) throws IOException {
        Scanner teclado = new Scanner(System.in);
        dos.writeUTF("Digite o nome do arquivo a ser renomeado:");
        String arquivoAntigo = teclado.nextLine();
        File fileAntigo = new File(arquivoAntigo);

        if (fileAntigo.exists()) {
            dos.writeUTF("Digite o novo nome para o arquivo:");
            String arquivoNovo = teclado.nextLine();
            File fileNovo = new File(arquivoNovo);

            if (fileAntigo.renameTo(fileNovo)) {
                dos.writeUTF("Arquivo renomeado de '" + arquivoAntigo + "' para '" + arquivoNovo + "'.");
            } else {
                dos.writeUTF("Erro ao renomear o arquivo.");
            }
        } else {
            dos.writeUTF("Arquivo '" + arquivoAntigo + "' não encontrado.");
        }
    }

    // Função para criar um novo arquivo
    public static void criarArquivo(DataOutputStream dos) throws IOException {
        Scanner teclado = new Scanner(System.in);
        dos.writeUTF("Digite o nome do novo arquivo a ser criado:");
        String nomeArquivo = teclado.nextLine();
        File novoArquivo = new File(nomeArquivo);

        if (novoArquivo.exists()) {
            dos.writeUTF("Arquivo já existe.");
        } else {
            boolean criado = novoArquivo.createNewFile();
            if (criado) {
                dos.writeUTF("Arquivo '" + nomeArquivo + "' criado com sucesso.");
            } else {
                dos.writeUTF("Erro ao criar o arquivo.");
            }
        }
    }
}
