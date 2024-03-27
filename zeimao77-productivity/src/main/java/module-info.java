module zeimao77.productivity {

    exports top.zeimao77.product.cmd;
    exports top.zeimao77.product.config;
    exports top.zeimao77.product.converter;
    exports top.zeimao77.product.dict;
    exports top.zeimao77.product.dingding;
    exports top.zeimao77.product.email;
    exports top.zeimao77.product.exception;
    exports top.zeimao77.product.factory;
    exports top.zeimao77.product.fileio;
    exports top.zeimao77.product.fileio.iexcel;
    exports top.zeimao77.product.fileio.oexcel;
    exports top.zeimao77.product.fileio.serialize;
    exports top.zeimao77.product.http;
    exports top.zeimao77.product.jobs;
    exports top.zeimao77.product.json;
    exports top.zeimao77.product.main;
    exports top.zeimao77.product.model;
    exports top.zeimao77.product.mysql;
    exports top.zeimao77.product.postgresql;
    exports top.zeimao77.product.redis;
    exports top.zeimao77.product.security;
    exports top.zeimao77.product.sql;
    exports top.zeimao77.product.tree;
    exports top.zeimao77.product.util;
    exports top.zeimao77.product.xml;
    exports top.zeimao77.product.log4j2 to org.apache.logging.log4j.core;

    requires org.slf4j;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires static org.apache.logging.log4j;
    requires static org.apache.logging.log4j.core;
    requires static redis.clients.jedis;
    requires static org.apache.commons.pool2;
    requires static com.zaxxer.hikari;
    requires static java.sql;
    requires static jakarta.mail;
    requires static org.apache.poi.poi;
    requires static org.apache.poi.ooxml;
    requires static jdk.unsupported;
    requires static java.net.http;
    requires static org.apache.commons.beanutils;
    requires static com.fasterxml.jackson.datatype.jsr310;



}