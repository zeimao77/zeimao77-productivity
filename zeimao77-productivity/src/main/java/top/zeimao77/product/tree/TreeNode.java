package top.zeimao77.product.tree;

import java.util.List;

/**
 * 树节点
 * @param <T> 实现类自身类型
 */
public interface TreeNode<T extends TreeNode> {
    String __getParentId();
    String __getNodeId();
    T __parent();

    /**
     * @return 子节点集合
     */
    List<T> childs();
    boolean addChild(T t);
}

