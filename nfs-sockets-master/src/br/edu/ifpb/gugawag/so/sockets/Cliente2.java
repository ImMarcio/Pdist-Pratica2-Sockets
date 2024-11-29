package br.edu.ifpb.gugawag.so.sockets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Cliente2 {

    public static void main(String[] args) throws IOException {
        System.out.println("== Cliente ==");

        // Configurando o socket para se conectar ao servidor
        Socket socket = new Socket("127.0.0.1", 7001);

        // Obtendo os canais de entrada e saída
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        DataInputStream dis = new DataInputStream(socket.getInputStream());

        // Criando scanner para entrada do usuário
        Scanner teclado = new Scanner(System.in);

        // Laço principal para interação com o servidor
        while (true) {
            // Exibindo opções de comando para o usuário
            System.out.println("Escolha um comando:");
            System.out.println("1. Verificar existência de arquivo");
            System.out.println("2. Criar arquivo");
            System.out.println("3. Criar diretório");
            System.out.println("4. Excluir arquivo");
            System.out.println("5. Listar arquivos no diretório");
            System.out.println("6. Sair");

            // Lendo o comando do usuário
            String comando = teclado.nextLine();

            // Enviando o comando para o servidor
            dos.writeUTF(comando);

            if (comando.equals("1")) {  // Verificar existência de arquivo
                // Recebendo a resposta do servidor
                String resposta = dis.readUTF();
                System.out.println("Servidor: " + resposta);

                // Lendo o caminho do arquivo a ser verificado
                String caminhoArquivo = teclado.nextLine();
                dos.writeUTF(caminhoArquivo);  // Enviando o caminho do arquivo ao servidor

                // Recebendo a resposta sobre a existência do arquivo
                resposta = dis.readUTF();
                System.out.println("Servidor: " + resposta);

            } else if (comando.equals("2")) {  // Criar arquivo
                // Recebendo a resposta do servidor
                String resposta = dis.readUTF();
                System.out.println("Servidor: " + resposta);

                // Lendo o nome do arquivo a ser criado
                String nomeArquivo = teclado.nextLine();
                dos.writeUTF(nomeArquivo);  // Enviando o nome do arquivo ao servidor

                // Recebendo a resposta sobre a criação do arquivo
                resposta = dis.readUTF();
                System.out.println("Servidor: " + resposta);

            } else if (comando.equals("3")) {  // Criar diretório
                // Recebendo a resposta do servidor
                String resposta = dis.readUTF();
                System.out.println("Servidor: " + resposta);

                // Lendo o caminho do diretório a ser criado
                String caminhoDiretorio = teclado.nextLine();
                dos.writeUTF(caminhoDiretorio);  // Enviando o caminho do diretório ao servidor

                // Recebendo a resposta sobre a criação do diretório
                resposta = dis.readUTF();
                System.out.println("Servidor: " + resposta);

            } else if (comando.equals("4")) {  // Excluir arquivo
                // Recebendo a resposta do servidor
                String resposta = dis.readUTF();
                System.out.println("Servidor: " + resposta);

                // Lendo o caminho do arquivo a ser excluído
                String caminhoArquivo = teclado.nextLine();
                dos.writeUTF(caminhoArquivo);  // Enviando o caminho do arquivo ao servidor

                // Recebendo a resposta sobre a exclusão do arquivo
                resposta = dis.readUTF();
                System.out.println("Servidor: " + resposta);

            } else if (comando.equals("5")) {  // Listar arquivos no diretório
                // Recebendo a resposta do servidor solicitando o caminho do diretório
                String resposta = dis.readUTF();
                System.out.println("Servidor: " + resposta);

    

                // Recebendo a lista de arquivos do servidor
                resposta = dis.readUTF();
                System.out.println("Servidor: " + resposta);

            } else if (comando.equals("6")) {  // Sair
                // Recebendo a resposta de encerramento
                String resposta = dis.readUTF();
                System.out.println("Servidor: " + resposta);
                break;  // Encerra o laço e a conexão

            } else {
                // Caso o comando seja inválido
                System.out.println("Comando inválido! Tente novamente.");
            }
        }

        // Fechando o socket e os fluxos
        dos.close();
        dis.close();
        socket.close();
        teclado.close();
    }
}
