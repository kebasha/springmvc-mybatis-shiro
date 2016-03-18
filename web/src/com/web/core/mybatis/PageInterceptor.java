package com.web.core.mybatis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;

import com.google.common.base.Objects;
import com.web.core.ResponseData;


/**
 * ͨ������<code>StatementHandler</code>��<code>prepare</code>��������дsql���ʵ�������ҳ��
 * �Ϲ�أ�ǩ����Ҫ���ص�����ֻ���ǽӿڡ�
 *
 */
@Intercepts({@Signature(type = StatementHandler.class, 
		method = "prepare", args = {Connection.class})})
public class PageInterceptor implements Interceptor {
	protected final transient Log log = LogFactory.getLog(getClass());
	
    private static final ObjectFactory DEFAULT_OBJECT_FACTORY = 
    	new DefaultObjectFactory();
    private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = 
    	new DefaultObjectWrapperFactory();
    /*
     *  ��Ҫ���ص�ID(����ƥ��)
     */
    private static String DEFAULT_PAGE_SQL_ID = ".*Page$"; 

    @SuppressWarnings("unchecked")
	public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaStatementHandler = MetaObject.
        	forObject(statementHandler, DEFAULT_OBJECT_FACTORY,
                DEFAULT_OBJECT_WRAPPER_FACTORY);
        /*
         *  ������������(����Ŀ������ܱ�������������أ��Ӷ��γɶ�δ���
         *  ͨ�����������ѭ�����Է������ԭʼ�ĵ�Ŀ����)
         */
        while (metaStatementHandler.hasGetter("h")) {
            Object object = metaStatementHandler.getValue("h");
            metaStatementHandler = MetaObject.forObject(object, 
            		DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);
        }
        /*
         *  �������һ����������Ŀ����
         */
        while (metaStatementHandler.hasGetter("target")) {
            Object object = metaStatementHandler.getValue("target");
            metaStatementHandler = MetaObject.forObject(object, 
            		DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);
        }

        // property��mybatis settings�ļ�������
        Configuration configuration = (Configuration) metaStatementHandler.
        	getValue("delegate.configuration");

        // ����pageSqlId
        String pageSqlId = configuration.getVariables().getProperty("pageSqlId");
        
        if (null == pageSqlId || "".equals(pageSqlId)) {
            log.warn("Property pageSqlId is not setted,use default '.*Page$' ");
            pageSqlId = DEFAULT_PAGE_SQL_ID;
        }
        
       // String dialect = configuration.getVariables().getProperty("dialect");
        
        MappedStatement mappedStatement = (MappedStatement)
                metaStatementHandler.getValue("delegate.mappedStatement");
        
        /*
         *  ֻ��д��Ҫ��ҳ��sql��䡣ͨ��MappedStatement��IDƥ�䣬
         *  Ĭ����д��Page��β��MappedStatement��sql
         */
        if (mappedStatement.getId().matches(pageSqlId)) {
            BoundSql boundSql = (BoundSql) metaStatementHandler.
            	getValue("delegate.boundSql");
            Object parameterObject = boundSql.getParameterObject();
            if(parameterObject == null){
            	throw new NullPointerException("parameters is null!");
            }else{
            	Map<String, Object> map = (Map)parameterObject;
            	String sql = boundSql.getSql();
            	ResponseData r = (ResponseData)map.get("data");
            	int page = Integer.parseInt(map.get("page").toString());
            	int rows = Integer.parseInt(map.get("rows").toString());
            	if (parameterObject == null) {
                    throw new NullPointerException("parameterObject is null!");
                } else {
                    // ��дsql
                    StringBuilder pageSql = new StringBuilder();
                    pageSql.append("SELECT * FROM ( SELECT ROWNUM RN,T.* FROM ( ");
                    pageSql.append(sql);
                    pageSql.append(") T WHERE ROWNUM <= ");
                    pageSql.append(rows * page);
                    pageSql.append(") WHERE RN > ");
                    pageSql.append(rows * (page - 1));
                    //String pageSql = sql + " LIMIT " + page.getRows()*(page.getPage()-1) + "," + page.getRows();
                    metaStatementHandler.setValue("delegate.boundSql.sql", pageSql.toString());
                    // ���������ҳ�󣬾Ͳ���Ҫmybatis���ڴ��ҳ�ˣ����������������������
                    metaStatementHandler.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET);
                    metaStatementHandler.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);
                }
                Connection connection = (Connection) invocation.getArgs()[0];
                int total = setPageParameter(sql, connection, mappedStatement, boundSql);
                r.getData().put("total", total);
            }
        }
        // ��ִ��Ȩ������һ��������
        return invocation.proceed();
    }

    public Object plugin(Object target) {
        // ��Ŀ������StatementHandler����ʱ���Ű�װĿ���࣬����ֱ�ӷ���Ŀ�걾��,����Ŀ�걻����Ĵ���
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    public void setProperties(Properties properties) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
    
    /**
     * ��ȡ�ܼ�¼��
     * @param sql
     * @param connection
     * @param mappedStatement
     * @param boundSql
     * @param page
     */
    private int setPageParameter(String sql, Connection connection, 
    		MappedStatement mappedStatement, BoundSql boundSql){
    	String countSql = "select count(1) from (" + sql + ")";
    	PreparedStatement psta = null;
    	ResultSet rs = null;
    	int totalCount = 0;
    	try {
			psta = connection.prepareStatement(countSql);
			BoundSql countBs = new BoundSql(mappedStatement.getConfiguration(), 
					countSql, boundSql.getParameterMappings(), boundSql.getParameterObject());
			
			ParameterHandler parameterHandler = new DefaultParameterHandler(
					mappedStatement, boundSql.getParameterObject(), boundSql);
			parameterHandler.setParameters(psta);
			
			rs = psta.executeQuery();
			if(rs.next()){
				totalCount = rs.getInt(1);
			}
		} catch (Exception e) {
			
		} finally {
			try {
				rs.close();
				psta.close();
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return totalCount;
    }
    

}

























