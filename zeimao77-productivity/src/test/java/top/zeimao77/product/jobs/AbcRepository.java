package top.zeimao77.product.jobs;

import top.zeimao77.product.mysql.AbstractRepository;
import top.zeimao77.product.mysql.SQL;
import top.zeimao77.product.mysql.SimpleMysql;

public class AbcRepository extends AbstractRepository<Abc,Long> {

    public AbcRepository(SimpleMysql simpleMysql) {
        super(simpleMysql,"abc");
    }

    @Override
    protected Long insert(SQL sql, Abc abc) {
        sql.insert(tableName)
                .addValues("a",abc.getA())
                .addValues("b",abc.getB())
                .addValues("c",abc.getC())
                .addValues("d",abc.getD())
                .addValues("e",abc.getE())
                .addValues("f",abc.getF())
                .addValues("g",abc.getG())
                .addValues("h",abc.getH())
                .addValues("i",abc.getI())
                .addValues("j",abc.getJ())
                .addValues("k",abc.getK())
                .addValues("l",abc.getL())
                .addValues("m",abc.getM())
                .addValues("n",abc.getN())
                .addValues("o",abc.getO())
                .addValues("p",abc.getP())
                .addValues("q",abc.getQ())
                .addValues("r",abc.getR())
                .addValues("s",abc.getS())
                .addValues("t",abc.getT())
                .addValues("u",abc.getU())
                .addValues("v",abc.getV())
                .addValues("w",abc.getW())
                .addValues("z",abc.getZ())
                .endValues();
        return abc.getA();
    }

    @Override
    protected void update(SQL sql, Abc abc) {

    }

    @Override
    protected void delete(SQL sql, Long id) {

    }

    @Override
    protected void get(SQL sql, Long id) {

    }
}
