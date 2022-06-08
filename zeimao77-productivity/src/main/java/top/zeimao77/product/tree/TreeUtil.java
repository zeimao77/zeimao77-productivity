package top.zeimao77.product.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class TreeUtil {

    /**
     * 列表转森林数据结构
     * @param lines 节点列表
     * @param fun 将一个行节点为转为树节点的方法
     * @return
     */
    public static <T extends TreeLine,W extends TreeNode> List<W> createTree(List<T> lines, Function<T,W> fun) {
        List<W> tree = new ArrayList<>();
        for (Iterator<T> iterator = lines.iterator(); iterator.hasNext();) {
            T line = iterator.next();
            if("0".equals(line.__getParentId())) {
                W treeNode = fun.apply(line);
                tree.add(treeNode);
                iterator.remove();
            }
        }
        for (TreeNode treeNode : tree) {
            createChild(treeNode,lines,fun);
        }
        return tree;
    }

    private static <T extends TreeLine,W extends TreeNode> void createChild(TreeNode treeNode, List<T> lines, Function<T,W> fun){
        for (Iterator<T> iterator = lines.iterator(); iterator.hasNext();) {
            T line = iterator.next();
            if(treeNode.__getNodeId().equals(line.__getParentId())) {
                W tnode = fun.apply(line);
                treeNode.addChild(tnode);
                iterator.remove();
            }
        }
        List<TreeNode> childs = treeNode.childs();
        if(childs != null && !childs.isEmpty()) {
            for (TreeNode child : childs) {
                createChild(child,lines,fun);
            }
        }
    }

    /**
     * 遍历一个树
     * @param node 树顶级节点
     * @param con 树的消费者
     * @param <T> 节点
     */
    public static <T extends TreeNode> void forTree(T node, Consumer<T> con) {
        List<T> childs = node.childs();
        if(childs != null && !childs.isEmpty()) {
            for (T child : childs) {
                forTree(child,con);
            }
        }
        con.accept(node);
    }

    /**
     * 对单树进行修剪，从叶子开始
     * @param node 树节点
     * @param pred 如果返回true节点将会被移除
     */
    public static boolean trimTree(TreeNode node, Predicate<TreeNode> pred) {
        List<TreeNode> childs = node.childs();
        if(childs != null && !childs.isEmpty()) {
            for(Iterator<TreeNode> iterator = childs.iterator(); iterator.hasNext();) {
                TreeNode next = iterator.next();
                if(trimTree(next, pred)) {
                    iterator.remove();
                }
            }
        }
        return pred.test(node);
    }

}
