package data_structure_def;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Graph {

    private List<Node> nodeList;
    Random r;

    public Graph(){
        r = new Random();
    }

    public void addVertex(Node node) {
        if(nodeList == null)
            nodeList = new ArrayList<>();
        else
            for(Node n : nodeList)
                if(n.getVertex().equals(node.getVertex()))
                    return;
        this.nodeList.add(node);
    }

    public Node getVertex(String label) {
        for(Node n : nodeList){
            if(n.getVertex().equals(label)){
                return n;
            }
        }
        return null;
    }

    public void addEdge(Edge edge) {
        String src = edge.getSrc();
        for(Node graphNode : nodeList) {
            String vertex = graphNode.getVertex();
            if(src.equals(vertex)) {
                if(graphNode.getEdgeSet() != null){
                    for(Edge graphEdge : graphNode.getEdgeSet()){
                        if(edge.equals(graphEdge)){
                            graphEdge.weightinc();
                            return;
                        }
                    }
                }
            edge.setWeight(1);
            //graphNode.getEdgeSet().add(edge);
            graphNode.add(edge);
            return;
            }
        }
    }

    public void add_new_node_and_edge(Node front, Node now) {
        addVertex(now);
        if(front != null){
            Edge edge = new Edge();
            edge.setSrc(front.getVertex());
            edge.setDst(now.getVertex());
            addEdge(edge);
        }
    }

    public boolean node_edge_node(String word1,String word2) {
        Node node1=getVertex(word1);
        Node node2=getVertex(word2);
        for(Edge edge : node1.getEdgeSet())
            if(getVertex(edge.getDst()).equals(node2))
                return true;
        return false;
    }

    public int node_weight_node(String word1, String word2){
        Node node1 = getVertex(word1);
        Node node2 = getVertex(word2);
        for(Edge edge : node1.getEdgeSet() )
            if(getVertex(edge.getDst()).equals(node2))
                return edge.getWeight();
        return 0;
    }

    public void graph_build_file(File file) {
        try{
            Node front = null;
            FileInputStream f1 = new FileInputStream(file);
            StringBuilder word = new StringBuilder(30);
            for (int i = 0; i < file.length(); i++) {
                char ch=(char)(f1.read());
                if(ch >= 'a' && ch <= 'z')
                    word.append(ch);
                else if(ch >= 'A' && ch <= 'Z')
                    word.append((char)(ch + 32));
                else if(word.length()>0){
                    Node now = new Node();
                    now.setVertex(word.toString());
                    add_new_node_and_edge(front, now);
                    front = now;
                    word.delete(0, word.length());
                }
            }
            if(word.length()>0){
                Node now = new Node();
                now.setVertex(word.toString());
                add_new_node_and_edge(front, now);
            }
            f1.close();
        }
        catch (IOException e) {
            System.out.println("发生错误！");
        }
    }

    public void showDirectedGraph() {
        for(Node graphnode : nodeList){
            System.out.println("当前节点为：" + graphnode.getVertex());
            System.out.println("该节点连接的边数为：" + graphnode.getEdgeSet().size() + "，它们是： ");
            for(Edge edge : graphnode.getEdgeSet())
                System.out.println(edge.output());
            System.out.println();
        }
    }

    public String queryBridgeWords(String word1, String word2) {
        StringBuilder words = new StringBuilder();
        try{
            Node node1 = getVertex(word1);
            Node node2 = getVertex(word2);
            int count = 0;
            Node bridge;

            if(node1 == null && node2 == null)
                throw new Custom_Exception(word1, word2, 1);
            else if(node1 == null)
                throw new Custom_Exception(word1, word2, 2);
            else if(node2 == null)
                throw new Custom_Exception(word1, word2, 3);

            for(Edge edge : node1.getEdgeSet()){
                bridge = getVertex(edge.getDst());
                for(Edge graphedge : bridge.getEdgeSet()){
                    if(graphedge.getDst().equals(word2)){
                        words = words.append(bridge.getVertex()).append(",");
                        count ++;
                    }
                }
            }

            if(count == 0)
                throw new Custom_Exception(word1, word2, 4);
            else if(count == 1){
                words.deleteCharAt(words.length()-1);
                return words.toString();
            }
            else {
                int front = 0;
                for(int i = 0 ; i < words.lastIndexOf(","); i++)
                    if(words.charAt(i) == ',')
                        front = i;
                words.insert(front, " and ");
                //words = words.substring(0, front) + " and " + words.substring(front + 1, words.length() - 1);
                return words.toString();
            }
        }catch(Custom_Exception e){
            return e.out_message();
        }
    }

    public String generateNewText(String inputText){
        String word1 = "", word2 = "", words = "", temp;
        int i = 0;
        for(int j = 0; j < inputText.length() + 1; j++){
            if(j == inputText.length() || inputText.charAt(j) == ' '){
                if(word1.isEmpty()){
                    word1 = inputText.substring(i,j);
                    words = word1;
                }
                else if(word2.isEmpty())
                    word2 = inputText.substring(i,j);
                else{
                    word1 = word2;
                    word2 = inputText.substring(i,j);
                }
                i = j + 1;
                if(!word1.isEmpty() && !word2.isEmpty()) {
                    temp = queryBridgeWords(word1,word2);
                    if(temp.indexOf('!') == -1){
                        String[] res = temp.split(",| and ");
                        if(res.length != 1){
                            //Random r = new Random();
                            int random = r.nextInt(res.length - 1);
                            words = words + " " + res[random] + " " + word2;
                        }
                        else{
                            words = words + " " + res[0] + " " + word2;
                        }
                    }
                    else
                        words = words + " " + word2;
                }
            }
        }
        return words;
    }

    public String calcShortestPath(String word1, String word2){
        ArrayList<String> ppath = new ArrayList<>();//存路径
        int n = nodeList.size();

        HashMap< String, Integer> dist = new HashMap<>();//用于存word1到对应节点的最短路径
        HashMap< String, String > path = new HashMap<>();//用于存该最短路径
        HashMap< String, Integer> visit = new HashMap<>();//用于存储该节点是否已经标记

        for(Node graphnode : nodeList){
            dist.put(graphnode.getVertex(),Integer.MAX_VALUE);
            path.put(graphnode.getVertex(),"NULL");
            visit.put(graphnode.getVertex(),0);
        }

        dist.put(word1 , 0);
        path.put(word1 , word1);

        for(int i = 0 ; i < n ; i ++){
            int min = Integer.MAX_VALUE;
            String temp = "";
            for(String key : dist.keySet()){
                if(visit.get(key) == 0 && dist.get(key) < min){
                    temp = key;
                    min = dist.get(key);
                }
            }
            visit.put(temp , 1);//遍历顶点，找到此时最短点并标记,然后进行dist和path的更新
            for(String key:dist.keySet()){
                if(visit.get(key) == 0 && node_edge_node(temp,key) &&
                        dist.get(temp) + node_weight_node(temp,key) < dist.get(key) ){
                    dist.put(key , dist.get(temp) + node_weight_node(temp , key));
                    path.put(key , temp);
                }
            }
        }

        String i = word2;
        if(visit.get(word2) == 0)
            System.out.println("无法到达该节点！");
        while(!i.equals(word1)){
            ppath.add(path.get(i));
            i = path.get(i);
        }
        Collections.reverse(ppath);
        StringBuilder builder = new StringBuilder();
        for (String pos : ppath) {
            builder.append(pos + " -> ");
        }
        builder.append(word2);
        return builder.toString();
    }

    public String randomWalk(){
        String word = " ";

        //Random r = new Random();//以系统自身时间为种子数
        int i = r.nextInt(nodeList.size());
        Node node = nodeList.get(i);//得到随机start
        word = node.getVertex();
        List <Edge> finishedge = new ArrayList<>();//用于存储已经遍历的边
        int count = 0;

        Thread mainThread = Thread.currentThread();//当前线程
        Thread inputThread = new Thread(() -> {
            try{
                while (!Thread.currentThread().isInterrupted()) {
                    Scanner scanner = new Scanner(System.in,"utf-8");
                    if (System.in.available() == 0) {
                        continue;
                    }
                    // 执行Scanner等待输入的逻辑
                    String input = scanner.nextLine();
                    if (input.isEmpty()) {
                        mainThread.interrupt();
                    }
                }
            }catch (IOException e){
                return;
            }
        });//子线程检查输入来停止线程
        inputThread.start();

        try{
            System.out.println("初始点为 " + word);
            while(true){
                TimeUnit.SECONDS.sleep(1);

                count ++;

                if(node.getEdgeSet().isEmpty()){
                    System.out.println("没有出边！退出随机游走！");
                    inputThread.interrupt();
                    break;                      //没有出边
                }

                int j = r.nextInt(node.getEdgeSet().size());
                ArrayList<Edge> edgelist = new ArrayList<Edge>(node.getEdgeSet());//将edgeset变成list然后随机取一个edge
                Edge temp = edgelist.get(j);//随机取边
                Node next = getVertex(temp.getDst());
                word = word + " " + next.getVertex();

                System.out.println("已进行随机游走" + count + "次，当前字符串为：" + word);

                if(finishedge.contains(temp)){
                    System.out.println("边有重复！退出随机游走！");
                    inputThread.interrupt();
                    break;//有重复
                }
                finishedge.add(temp);
                node = next;
            }
            return word;
        }catch(InterruptedException e){
            System.out.println("输入中断！退出随机游走！");
            inputThread.interrupt();
            return word;
        }
    }
}

class Custom_Exception extends Exception{
    private String message;
    public Custom_Exception(String word1, String word2,int i){
        switch (i){
            case 1:
                this.message = "No \"" + word1 + "\" and \"" + word2 + "\" in the graph!";
                break;
            case 2:
                this.message = "No \"" + word1 + "\" in the graph!";
                break;
            case 3:
                this.message = "No \"" + word2 + "\" in the graph!";
                break;
            case 4:
                this.message = "No bridge words from \"" + word1 + "\" to \"" + word2 + "\"!";
                break;
            default:
                break;
        }
    }
    public String out_message(){
        return this.message;
    }
}
