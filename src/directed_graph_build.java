//package data_structure_def;

import data_structure_def.Graph;
//import sun.security.provider.certpath.Vertex;

import java.io.*;
import java.util.Scanner;

public class directed_graph_build {
    public static void main(String[] args){
        System.out.println("请输入要打开的文本文件的路径及其位置：");
        Scanner sc = new Scanner(System.in,"utf-8");
        String filename = sc.nextLine();
        Graph graph = new Graph();
        File file=new File(filename);
        graph.graph_build_file(file);
        print_menu();
        while (true) {
            System.out.println("请输入要做的操作（从0到6）：");
            switch (sc.nextInt()) {
                case 0:
                    print_menu();
                    break;
                case 1:
                    graph.showDirectedGraph();
                    break;
                case 2:
                    Scanner scan = new Scanner(System.in,"utf-8");
                    System.out.println("请输入第一个词：");
                    String word1 = scan.nextLine();
                    System.out.println("请输入第二个词：");
                    String word2 = scan.nextLine();
                    String words = graph.queryBridgeWords(word1,word2);
                    if(words.indexOf('!') != -1)
                        System.out.println(words);
                    else if(words.indexOf(' ') == -1)
                        System.out.println("The bridge words from \"" + word1 + "\" to \"" + word2 + "\" is: " + words);
                    else
                        System.out.println("The bridge words from \"" + word1 + "\" to \"" + word2 +"\" are: " + words);
                    break;
                case 3:
                    Scanner scan3 = new Scanner(System.in,"utf-8");
                    System.out.println("请输入新文本：");
                    String inputText = scan3.nextLine();
                    System.out.println(graph.generateNewText(inputText));
                    break;
                case 4:
                    Scanner scan4 = new Scanner(System.in,"utf-8");
                    System.out.println("请输入第一个词：");
                    String word3 = scan4.nextLine();
                    System.out.println("请输入第二个词：");
                    String word4 = scan4.nextLine();
                    System.out.println(graph.calcShortestPath(word3 , word4));
                    break;
                case 5:
                    String file_context = graph.randomWalk();
                    String file_output = "output.txt";
                    try{
                        File output_file = new File(file_output);
                        boolean flag = output_file.createNewFile();
                        if(!flag)
                            System.out.println("File create failed");
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file_output,
                                true), "utf-8"));
                        //FileWriter writer = new FileWriter(output_file);
                        writer.write(file_context);
                        writer.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    break;
                case 6:
                    return;
                default:
                    System.out.println("错误输入！合法的输入范围为0-6！菜单如下：");
                    print_menu();
            }
        }

    }

    public static void print_menu(){
        System.out.println("0.显示菜单");
        System.out.println("1.显示有向图");
        System.out.println("2.查询桥接词");
        System.out.println("3.根据桥接词生成新文本");
        System.out.println("4.显示两个单词之间的最短路径");
        System.out.println("5.随机游走");
        System.out.println("6.退出");
    }
}