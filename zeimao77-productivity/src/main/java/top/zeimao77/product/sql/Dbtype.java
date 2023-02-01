package top.zeimao77.product.sql;

public interface Dbtype {

     Integer ALL = 0x7FFFFFFF;   /* 所有数据库 */

     Integer MYSQL = 0x00000001;  /* ORACLE MYSQL */
     Integer ORACLE = 0x00000002;  /* Oracle */
     Integer SQLITE = 0x00000004;  /* SQLite */
     Integer POSTGRESQL = 0x00000008;  /* PostgreSQL */
     Integer SQLSERVER = 0x00000010;  /* Microsoft SQL server */
     Integer ACCESS = 0x00000020;  /* Microsoft Access */
     Integer MARIADB = 0x0000040;  /* MariaDB */


}
