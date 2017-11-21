package com.ysz.ec.inventory.configuration;

import com.ysz.ec.common.mybatis.dao.MyBatisDAO;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * <B>描述：</B><br/> <B>作者：</B> carl.yu <br/> <B>时间：</B> 2017/11/20 <br/> <B>版本：</B><br/>
 */
@Configuration
@Profile("!test")
public class MyBatisConfiguration {

  @Resource
  private DataSource dataSource;

  @Bean
  public MyBatisDAO myBatisDAO() throws Exception {
    MyBatisDAO myBatisDAO = new MyBatisDAO();
    myBatisDAO.setSqlSessionFactory(createSqlSessionFactory(dataSource, "com.ysz.ec.inventory" +
        ".core.entity", "classpath:mybatis/mapper/*/*.xml"));
    return myBatisDAO;
  }

  @Bean
  public TransactionTemplate transactionTpl() {
    return createTransactionTemplate(dataSource);
  }


  /**
   * 创建 mybatis 的 template
   *
   * @param datasource     数据源
   * @param aliasPackage   entity别名源路径
   * @param mapperResource mapper路径
   */
  private static SqlSessionFactory createSqlSessionFactory(
      DataSource datasource,
      String aliasPackage,
      String mapperResource)
      throws Exception {
    SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
    sessionFactory.setDataSource(datasource);
    sessionFactory.setTypeAliasesPackage(aliasPackage);
    PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    org.springframework.core.io.Resource[] resources = resolver.getResources(mapperResource);
    sessionFactory.setMapperLocations(resources);
    return sessionFactory.getObject();
  }


  /**
   * 创建 spring 事务管理器
   *
   * @param dataSource 数据源
   */
  private static TransactionTemplate createTransactionTemplate(DataSource dataSource) {
    DataSourceTransactionManager dsm = new DataSourceTransactionManager(dataSource);
    return new TransactionTemplate(dsm);
  }
}
