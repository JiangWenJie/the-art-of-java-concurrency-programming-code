package com.jwj.c04;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 一个简单的Web服务器
 */
public class SimpleHttpServer {

    static ThreadPool<HttpRequestHandler> threadPool=new DefaultThreadPool<HttpRequestHandler>(1);

    //SimpleHttpServer的根路径
    static String basePath;

    static ServerSocket serverSocket;

    //服务监听端口
    static int port=8080;

    public static void setPort(int port){
        if (port>0){
            SimpleHttpServer.port=port;
        }
    }

    public static void setBasePath(String basePath){
        if (basePath!=null&&new File(basePath).exists()&&new File(basePath).isDirectory()){
            SimpleHttpServer.basePath=basePath;
        }
    }

    public static void start() throws Exception {
        serverSocket=new ServerSocket(port);
        Socket socket=null;
        while ((socket=serverSocket.accept())!=null){
            threadPool.execute(new HttpRequestHandler(socket));
        }
        serverSocket.close();
    }

    static class HttpRequestHandler implements Runnable{
        private Socket socket;

        public HttpRequestHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            String line=null;
            BufferedReader br=null;
            BufferedReader reader=null;
            PrintWriter out=null;
            InputStream in=null;

            try {
                reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String header=reader.readLine();

                String filePath=basePath+header.split("")[1];
                out=new PrintWriter(socket.getOutputStream());
                if (filePath.endsWith("jpg")||filePath.endsWith("ico")){
                    in=new FileInputStream(filePath);
                    ByteArrayOutputStream baos=new ByteArrayOutputStream();
                    int i=0;
                    while ((i=in.read())!=-1){
                        baos.write(i);
                    }
                    byte[] array=baos.toByteArray();
                    out.println("HTTP/1.1 200 ok");
                    out.println("Server:Molly");
                    out.println("Content-Type： text/html;charset=UTF-8");
                    out.println("");
                    while ((line=br.readLine())!=null){
                        out.println(line);
                    }
                }
                out.flush();
            } catch (IOException e) {
                out.println("HTTP/1.1 500");
                out.println("");
                out.flush();
                e.printStackTrace();
            }finally {
                close(br,in,reader,out,socket);
            }
        }
    }

    private static void close(Closeable... closeables){
        if (closeables!=null){
            for (Closeable closeable:closeables) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
