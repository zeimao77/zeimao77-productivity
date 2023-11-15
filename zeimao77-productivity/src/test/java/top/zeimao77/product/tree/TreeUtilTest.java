package top.zeimao77.product.tree;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class TreeUtilTest extends BaseMain {
    @Test
    void createTree() {
        List<Line> lines = Arrays.asList(
                new Line(1, 0, "1")
                , new Line(2, 1, "1-2")
                , new Line(3, 1, "1-3")
                , new Line(4, 0, "4")
                , new Line(5, 4, "4-5")
                , new Line(6, 5, "4-5-6")
        );
        // 列表转树
        List<Tree> trees = TreeUtil.createTree(new ArrayList<>(lines), o -> new Tree(o.id, o.parentId, o.name));
        for (Tree tree : trees) {
            // 对树剪枝 如果返回true节点将会被剪掉
            TreeUtil.trimTree(tree,o->"6".equals(o.__getNodeId()));
            // 遍历树
<<<<<<< HEAD
            TreeUtil.forTree(tree,t -> logger.info(t.toString()));
=======
            TreeUtil.forTree(tree,o -> logger.info(o.toString()));
>>>>>>> main
        }

    }

    public static class Line implements TreeLine {

        private int id;
        private int parentId;
        private String name;

        public Line(int id, int parentId, String name) {
            this.id = id;
            this.parentId = parentId;
            this.name = name;
        }

        @Override
        public String __getParentId() {
            return String.valueOf(parentId);
        }

        @Override
        public String __getNodeId() {
            return String.valueOf(id);
        }

    }

    public static class Tree implements TreeNode<Tree>{

        private int id;
        private int parentId;
        private String name;
        private List<Tree> childs = new ArrayList<>();

        public Tree(int id, int parentId, String name) {
            this.id = id;
            this.parentId = parentId;
            this.name = name;
        }

        @Override
        public String __getParentId() {
            return String.valueOf(parentId);
        }

        @Override
        public String __getNodeId() {
            return String.valueOf(id);
        }

        @Override
        public Tree __parent() {
            return null;
        }

        @Override
        public List<Tree> childs() {
            return this.childs;
        }

        @Override
        public boolean addChild(Tree tree) {
            return this.childs.add(tree);
        }

        @Override
        public String toString() {
            return "Tree{" +
                    "id=" + id +
                    ", parentId=" + parentId +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}