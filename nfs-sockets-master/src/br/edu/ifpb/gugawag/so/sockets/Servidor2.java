package br.edu.ifpb.gugawag.so.sockets;

import java.nio.file.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor2 {

    public static void main(String[] args) throws IOException {
        System.out.println("== Servidor ==");

        // Configurando o servidor para escutar na porta 7001
        ServerSocket serverSocket = new ServerSocket(7001);
        Socket socket = serverSocket.accept();

        // Obtendo os canais de entrada e saída
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        DataInputStream dis = new DataInputStream(socket.getInputStream());

        // Laço principal para processar comandos do cliente
        while (true) {
            // Lendo o comando enviado pelo cliente
            String comando = dis.readUTF();
            String resposta = "";

            // Processando o comando recebido
            if (comando.equals("1")) {  // Verificar existência de arquivo
                resposta = "Digite o caminho do arquivo para verificar a existência:";
                dos.writeUTF(resposta);
                String caminho = dis.readUTF();
                Path path = Paths.get(caminho);
                if (Files.exists(path)) {
                    resposta = "Arquivo encontrado!";
                } else {
                    resposta = "Arquivo não encontrado!";
                }

            } else if (comando.equals("2")) {  // Criar arquivo
                resposta = "Digite o nome do arquivo a ser criado:";
                dos.writeUTF(resposta);
                String caminho = dis.readUTF();
                Path path = Paths.get(caminho);
                System.out.println(caminho);
                try {
                    Files.createFile(path);
                    resposta = "Arquivo criado com sucesso!";
                } catch (IOException e) {
                    resposta = "Falha ao criar arquivo: " + e.getMessage();
                }

            } else if (comando.equals("3")) {  // Criar diretório
                resposta = "Digite o nome do diretório a ser criado:";
                dos.writeUTF(resposta);
                String caminho = dis.readUTF();
                Path path = Paths.get(caminho);
                try {
                    Files.createDirectory(path);
                    resposta = "Diretório criado com sucesso!";
                } catch (IOException e) {
                    resposta = "Falha ao criar diretório: " + e.getMessage();
                }

            } else if (comando.equals("4")) {  // Excluir arquivo
                resposta = "Digite o caminho do arquivo a ser excluído:";
                dos.writeUTF(resposta);
                String caminho = dis.readUTF();
                Path path = Paths.get(caminho);
                try {
                    Files.delete(path);
                    resposta = "Arquivo excluído com sucesso!";
                } catch (IOException e) {
                    resposta = "Falha ao excluir arquivo: " + e.getMessage();
                }

            } else if (comando.equals("5")) {  // Listar arquivos no diretório
                resposta = "listar os arquivos:";
           
           
                Path path = Paths.get("documentos");
                if (Files.isDirectory(path)) {
                    try {
                        // Listando os arquivos e diretórios no caminho fornecido
                        StringBuilder listaArquivos = new StringBuilder();
                        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
                            for (Path entry : stream) {
                                listaArquivos.append(entry.getFileName()).append("\n");
                            }
                        }
                        if (listaArquivos.length() > 0) {
                            resposta = "Arquivos no diretório:\n" + listaArquivos.toString();
                        } else {
                            resposta = "Nenhum arquivo encontrado no diretório.";
                        }
                    } catch (IOException e) {
                        resposta = "Erro ao listar arquivos: " + e.getMessage();
                    }
                } else {
                    resposta = "Caminho fornecido não é um diretório válido.";
                }

            } else if (comando.equals("6")) {  // Sair
                resposta = "Conexão encerrada.";
                dos.writeUTF(resposta);
                break;
            } else {
                resposta = "Comando inválido.";
            }

            // Enviando a resposta de volta para o cliente
            dos.writeUTF(resposta);
        }

        // Fechando os fluxos e o socket
        dos.close();
        dis.close();
        socket.close();
        serverSocket.close();
    }
}
