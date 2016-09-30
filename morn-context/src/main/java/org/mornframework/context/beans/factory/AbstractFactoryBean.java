/**
* Copyright 2016-2026 the original author or authors.
*
* Welcome hot like Java Person,Contribution code for org.
* This framework Has been published on Git
*     https://github.com/chinalihui/morn-parent,
*     https://git.oschina.net/osjeff/morn-parent
* 
* If there are problems or ideas can be submitted to the
*     https://github.com/chinalihui/morn-parent/issues
*
*                                    Thanks.
*/

package org.mornframework.context.beans.factory;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.mornframework.context.annotation.Scope;
import org.mornframework.context.util.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jeff.Li
 * @date 2016年9月28日
 */
public abstract class AbstractFactoryBean implements FactoryBean{
	
	protected Map<String, Object> beans;
	protected Map<String, Class<?>> prototypeClasses;
	protected List<Class<?>> contextClasss;
	protected List<Class<?>> annotationClasss;
	protected final Logger LOG = LoggerFactory.getLogger(getClass());
	
	/**
	 * 
	 * @param basePackage
	 */
	public void scanContextClasss(String basePackage){
		if(StringUtils.isEmpty(basePackage)){
			throw new RuntimeException("Please set scanPackage in "
					+ " web.xml filter mornMvc init-param ");
		}
		
		String[] packages = basePackage.split(",");
		contextClasss = new LinkedList<Class<?>>();
		for(String pkg : packages){
			if(pkg != null && pkg.length() > 0)
				contextClasss.addAll(ClassUtil.getClasses(pkg));
		}
		
		annotationClasss = new LinkedList<Class<?>>();
		for(Class<?> clazz : contextClasss){
			if(isContextAnnotation(clazz)){
				annotationClasss.add(clazz);
			}
		}
	}
	
	public Object getBean(String name){
		Object object = beans.get(name);
		if(object == null){
			object = createBean(prototypeClasses.get(name));
		}
		return object;
	}
	
	public List<Class<?>> getAnnotationClasss(){
		return annotationClasss;
	}
	
	public boolean isSingleton(Class<?> clazz){
		Scope scope = clazz.getAnnotation(Scope.class);
		if(scope == null){
			return true;
		}
		
		if(!SCOPE_PROTOTYPE.equals(scope.value())){
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param clazz
	 * @return
	 */
	public abstract boolean isContextAnnotation(Class<?> clazz);
	
	/**
	 * 
	 */
	public abstract void createContextBeans();
	
	/**
	 * 获取bean别名
	 * @param clazz
	 * @return
	 */
	public abstract String getBeanName(Class<?> clazz);
	
	/**
	 * 
	 * @param clazz
	 * @return
	 */
	public abstract Object createBean(Class<?> clazz);
	
}
