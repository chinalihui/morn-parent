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

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.mornframework.context.annotation.Scope;
import org.mornframework.context.beans.annotation.Bean;
import org.mornframework.context.beans.annotation.Beans;
import org.mornframework.context.beans.annotation.Property;
import org.mornframework.context.beans.exception.BeanInitializeException;
import org.mornframework.context.beans.extend.BeanPostProcessor;
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
	protected Map<String,BeanDefinition> beanDefinitions;
	protected List<BeanPostProcessor> beanPostProcessorList;
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
				if(clazz.getAnnotation(Beans.class) != null){
					if(LOG.isDebugEnabled()){
						LOG.debug("[Application Context @Beans:" + clazz.getName()+"]");
					}
					processAnnotationBeans(clazz);
				} else {
					annotationClasss.add(clazz);
				}
			}
		}
	}
	
	/**
	 * 根据Bean名称获取上下文中的Bean实例,
	 * 如果未获取到,调用相应的创建Bean方法后返回
	 */
	public Object getBean(String name){
		Object object = beans.get(name);
		if(object == null){
			object = createBean(prototypeClasses.get(name));
		}
		
		if(object == null){
			BeanDefinition beanDefinition = beanDefinitions.get(name);
			if(beanDefinition != null){
				object = createBean(name,beanDefinition);
			}
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
	 * 解析@Beans注解中的@Bean,将其转换为BeanDefinition描述
	 * @param clazz
	 */
	public void processAnnotationBeans(Class<?> clazz){
		Field[] fields = clazz.getDeclaredFields();
		for(Field field : fields){
			Bean bean = field.getAnnotation(Bean.class);
			if(bean != null){
				String beanName = bean.name();
				if(StringUtils.isEmpty(beanName)){
					beanName = field.getName();
				}
				
				String beanClass = bean.classes();
				if(StringUtils.isEmpty(beanClass)){
					beanClass = field.getType().getName();
				}
				
				Map<String, PropertyDefinition> propertyMap = null;
				Property[] propertys = bean.propertys();
				if(propertys != null && propertys.length > 0){
					propertyMap = new HashMap<String, PropertyDefinition>();
					for(Property property : propertys){
						if(StringUtils.isEmpty(property.name())){
							throw new BeanInitializeException("[Application Context] Initialize Bean:"+beanName + ""
									+ " exception ,reson in property name is empty ");
						}
						PropertyDefinition propertyDefinition = new PropertyDefinition(
								property.name(),property.value(),property.ref(),property.list(),property.map());
						propertyMap.put(property.name(),propertyDefinition);
					}
				}
				
				BeanDefinition beanDefinition = new BeanDefinition(beanName, beanClass);
				beanDefinition.setPropertys(propertyMap);
				beanDefinition.setScope(bean.scope());
				beanDefinition.setParent(bean.parent());
				beanDefinitions.put(beanName, beanDefinition);
			}
		}
	}
	
	/**
	 * 判断该class是否为上下文中定义的注解
	 * @param clazz
	 * @return
	 */
	public abstract boolean isContextAnnotation(Class<?> clazz);
	
	/**
	 * 创建上线文Bean
	 */
	public abstract void createContextBeans();
	
	/**
	 * 获取bean别名
	 * @param clazz
	 * @return
	 */
	public abstract String getBeanName(Class<?> clazz);
	
	/**
	 * 通过框架中的原始注解创建
	 * @param clazz
	 * @return
	 */
	public abstract Object createBean(Class<?> clazz);
	
	/**
	 * 创建bean
	 * @param beanName
	 * @param beanDefinition
	 * @return
	 */
	public abstract Object createBean(String beanName,BeanDefinition beanDefinition);
	
}
