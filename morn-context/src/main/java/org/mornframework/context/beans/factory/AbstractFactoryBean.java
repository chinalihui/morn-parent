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

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.mornframework.context.annotation.Scope;
import org.mornframework.context.beans.annotation.Bean;
import org.mornframework.context.beans.annotation.Beans;
import org.mornframework.context.beans.annotation.Property;
import org.mornframework.context.beans.exception.BeanInitializeException;
import org.mornframework.context.beans.extend.BeanPostProcessorChain;
import org.mornframework.context.beans.extend.RegisterAnnotationResolve;
import org.mornframework.context.support.ApplicationProperties;
import org.mornframework.context.util.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jeff.Li
 * @date 2016年9月28日
 */
public abstract class AbstractFactoryBean implements FactoryBean{
	
	/**
	 * 上下文bean实例
	 */
	protected Map<String, Object> beans;
	
	/**
	 * 上下文所有的class
	 */
	protected List<Class<?>> contextClasss;
	
	/**
	 * 上下文中所有的bean对象描述
	 */
	protected Map<String,BeanDefinition> beanDefinitions;
	
	/**
	 * 上下文中所有BeanPostProcessr
	 */
	protected List<BeanPostProcessorChain> beanPostProcessorList;
	
	/**
	 * 应用自定义注解扩展解释器
	 */
	protected Map<Class<?>, RegisterAnnotationResolve> registerAnnotationResoleMap;
	
	/**
	 * 上下文中所有的注解类
	 */
	protected List<Class<?>> contextAnnotation;
	
	/**
	 * 上下文中所有带注解的Class
	 */
	protected Set<Class<?>> annotationClasses;
	
	/**
	 * 默认加载属性文件
	 */
	protected String PROPERTIES_JDBC_PATH = "jdbc.properties";
	protected String PROPERTIES_SYSTEM_PATH = "system.properties";
	protected String PROPERTIES_INFO_PATH = "info.properties";
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
	}
	
	public void resoleContextBeanConfig(){
		for(Class<?> clazz : contextClasss){
			if(isContextAnnotation(clazz)){
				if(clazz.getAnnotation(Beans.class) != null){
					if(LOG.isDebugEnabled()){
						LOG.debug("[Application Context @Beans:" + clazz.getName()+"]");
					}
					processAnnotationBeans(clazz);
				} else {
					String beanName = getBeanName(clazz);
					BeanDefinition beanDefinition = new BeanDefinition();
					beanDefinition.setBeanName(beanName);
					beanDefinition.setBeanClass(clazz);
					beanDefinition.setBeanFlag(CONTEXT_BEAN_FLAG_ANNOS);
					Scope scope = clazz.getAnnotation(Scope.class);
					if(scope == null || SCOPE_SINGLETON.equals(scope.value())){
						beanDefinition.setScope(SCOPE_SINGLETON);
					}
					beanDefinitions.put(beanName,beanDefinition);
					annotationClasses.add(clazz);
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
			BeanDefinition beanDefinition = beanDefinitions.get(name);
			if(beanDefinition != null){
				object = createBean(name,beanDefinition);
			}
		}
		return object;
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
				
				try{
					BeanDefinition beanDefinition = new BeanDefinition(beanName, Class.forName(beanClass));
					beanDefinition.setPropertys(propertyMap);
					beanDefinition.setScope(bean.scope());
					beanDefinition.setParent(bean.parent());
					beanDefinition.setBeanFlag(CONTEXT_BEAN_FLAG_BEANS);
					beanDefinitions.put(beanName, beanDefinition);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
	public void loadPropertiesToMap(InputStream in){
		if(in != null){
			Properties prop = new Properties();
			try {
				prop.load(in);
				Set<String> keys = prop.stringPropertyNames();
				for(String key : keys)
					ApplicationProperties.properties.put(key, prop.getProperty(key));
				prop.clone();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void initProperties(String propertiesPath){
		loadPropertiesToMap(this.getClass().getResourceAsStream("/" + PROPERTIES_SYSTEM_PATH));
		loadPropertiesToMap(this.getClass().getResourceAsStream("/" + PROPERTIES_JDBC_PATH));
		loadPropertiesToMap(this.getClass().getResourceAsStream("/" + PROPERTIES_INFO_PATH));
		
		if(StringUtils.isEmpty(propertiesPath)){
			return;
		}
		String[] paths = propertiesPath.split(",");
		for(String path : paths){
			loadPropertiesToMap(this.getClass().getResourceAsStream(path.trim()));
		}
	}
	
	public Set<Class<?>> getAnnotationClasses(){
		return annotationClasses;
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
	 * 创建bean
	 * @param beanName
	 * @param beanDefinition
	 * @return
	 */
	public abstract Object createBean(String beanName,BeanDefinition beanDefinition);
	
}
