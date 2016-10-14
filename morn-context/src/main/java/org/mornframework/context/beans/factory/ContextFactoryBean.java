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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.mornframework.context.annotation.Action;
import org.mornframework.context.annotation.Component;
import org.mornframework.context.annotation.Dao;
import org.mornframework.context.annotation.Inject;
import org.mornframework.context.annotation.Interceptor;
import org.mornframework.context.annotation.Service;
import org.mornframework.context.annotation.Value;
import org.mornframework.context.beans.annotation.Beans;
import org.mornframework.context.beans.annotation.Element;
import org.mornframework.context.beans.annotation.Entry;
import org.mornframework.context.beans.exception.BeanInitializeException;
import org.mornframework.context.beans.extend.BeanPostProcessor;
import org.mornframework.context.beans.extend.FactoryBeanAware;
import org.mornframework.context.beans.extend.InitializingBean;
import org.mornframework.context.beans.extend.annotation.InitMethod;
import org.mornframework.context.support.ApplicationProperties;
import org.mornframework.context.util.ClassUtil;
import org.mornframework.context.util.StringUtils;

/**
 * @author Jeff.Li
 * @date 2016年9月28日
 * 1.初始化上下文Bean,包括基本注解以及配置@Beans--@Bean
 */
public class ContextFactoryBean extends AbstractFactoryBean{

	public ContextFactoryBean(String basePackage){
		beans = new LinkedHashMap<String, Object>();
		prototypeClasses = new HashMap<String, Class<?>>();
		beanDefinitions = new HashMap<String, BeanDefinition>();
		beanPostProcessorList = new ArrayList<BeanPostProcessor>();
		scanContextClasss(basePackage);
		BeanHolder.factoryBean = this;
	}
	
	@Override
	public void createContextBeans() {
		if(contextClasss == null || contextClasss.size()  == 0){
			LOG.info("Application context not scan java source file ");
			return;
		}
		
		for (Class<?> clazz : annotationClasss) {
			String beanName = getBeanName(clazz);
			if(isSingleton(clazz)){
				createBean(beanName,clazz,true);
			}
			else{
				prototypeClasses.put(beanName, clazz);
			}
		}
		
		for(Map.Entry<String, BeanDefinition> entry : beanDefinitions.entrySet()){
			BeanDefinition beanDefinition = entry.getValue();
			if(SCOPE_SINGLETON.equals(beanDefinition.getScope())){
				createBean(entry.getKey(),beanDefinition,true);
			}
		}
	
	}
	
	public Object createBean(String beanName,BeanDefinition beanDefinition){
		return createBean(beanName,beanDefinition,SCOPE_SINGLETON.equals(beanDefinition.getScope()));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object createBean(String beanName,BeanDefinition beanDefinition,boolean isSingleton){
		Object beanObject = beans.get(beanName);
		if(beanObject != null){
			return beanObject;
		}
		try{
			String beanClass = beanDefinition.getBeanClass();
			Class<?> clazz = Class.forName(beanClass);
			beanObject = clazz.newInstance();
			Map<String, PropertyDefinition> propertyMap = beanDefinition.getPropertys();
			if(propertyMap != null && propertyMap.size() > 0){
				for(Map.Entry<String, PropertyDefinition> entryPropertyDefinition : propertyMap.entrySet()){
					PropertyDefinition propertyDefinition = entryPropertyDefinition.getValue();
					String fieldName = entryPropertyDefinition.getKey();
					Object value = null;
					boolean valueFlag = false;
					String strValue = propertyDefinition.getValue();
					if(StringUtils.isNotEmpty(strValue)){
						valueFlag = true;
						value = strValue;
					}
					
					if(value == null){
						String refBeanName = propertyDefinition.getRef();
						if(StringUtils.isNotEmpty(refBeanName)){
							value = beans.get(refBeanName);
							/**
							 * 从@Bean集合中获取,如果存在则创建
							 */
							if(value == null){
								BeanDefinition refBeanDefinition = beanDefinitions.get(refBeanName);
								if(refBeanDefinition != null){
									value = createBean(refBeanName,refBeanDefinition,SCOPE_SINGLETON.equals(refBeanDefinition.getScope()));
								}
							}
							
							if(value == null){
								Class<?> fieldClass = clazz.getDeclaredField(fieldName).getType();
								value = findContextBean(fieldClass);
							}
							
							if(value == null){
								throw new BeanInitializeException("[Application Context] Initialize Bean:"
										+ "" + beanClass + " exception ,reason is property '" + fieldName + "'"
												+ " ref " + refBeanName + " in not found or not initialize ");
							}
						}
					}
					
					if(value == null){
						Element[] elements = propertyDefinition.getList();
						if(elements != null && elements.length > 0){
							List<String> list = new ArrayList<String>();
							for(Element element : elements) list.add(element.value());
							value = list;
						}
					}
					
					if(value == null){ 
						Entry[] entrys = propertyDefinition.getMap();
						if(entrys != null && entrys.length > 0){
							Map map = new HashMap();
							for(Entry entry : entrys) map.put(entry.key(),entry.value());
							value = map;
						}
					}
					
					if(value != null){
						Class<?> fieldType = clazz.getDeclaredField(fieldName).getType();
						Method method = clazz.getDeclaredMethod("set"+StringUtils.firstToUpperCase(fieldName),fieldType);
						if(valueFlag){
							if(StringUtils.includeProperty(strValue)){
								strValue = ApplicationProperties.properties.get(StringUtils.propertyKey(strValue));
								value = ClassUtil.parseValue(strValue, fieldType);
							}
							else{
								value = ClassUtil.parseValue(strValue, fieldType);
							}
						}
						method.invoke(beanObject, value);
					}
					
				}
			}
			
			/**
			 * 通过注解设置属性
			 */
			setterBeanAnnotation(beanObject, clazz.getDeclaredFields());
			
			if(isSingleton){
				beans.put(beanName, beanObject);
				
				/**
				 * 扩展创建bean方法
				 */
				extendCreateBean(beanObject);
			}
		} catch (Exception e) {
			throw new BeanInitializeException(e);
		}
		return beanObject;
	}
	
	public Object createBean(Class<?> clazz){
		if(clazz == null) return null;
		return createBean(getBeanName(clazz),clazz);
	}
	
	public Object createBean(String beanName,Class<?> clazz){
		if(clazz == null) return null;
		return createBean(beanName,clazz,isSingleton(clazz));
	}
	
	public Object createBean(String beanName,Class<?> clazz,boolean isSingleton){
		if(clazz == null) return null;
		if(beans.containsKey(beanName)){
			return beans.get(beanName);
		}
		
		Object beanObject = null;
		try{
			beanObject = clazz.newInstance();
			/**
			 * 通过注解设置属性
			 */
			setterBeanAnnotation(beanObject,clazz.getDeclaredFields());
			
			if(isSingleton){
				beans.put(beanName, beanObject);
				
				/**
				 * 扩展创建bean方法
				 */
				extendCreateBean(beanObject);
			}
		}catch(Exception e){
			throw new BeanInitializeException(e);
		}
		return beanObject;
	}
	
	public void setterBeanAnnotation(Object beanObject,Field[] fields){
		for(Field field : fields){
			Inject inject = field.getAnnotation(Inject.class);
			Value value = null;
			if(inject != null){
				Class<?> fieldType = field.getType();
				String name = inject.value();
				if(StringUtils.isEmpty(name)){
					name = field.getName();
				}
				
				Object bean = beans.get(name);
				
				if(bean == null){
					bean = findContextBean(fieldType);
				}
				
				if(bean == null){
					BeanDefinition beanDefinition = beanDefinitions.get(name);
					if(beanDefinition != null){
						bean = createBean(name, beanDefinition, SCOPE_SINGLETON.equals(beanDefinition.getScope()));
					}
				}
			
				if(bean != null){
					try {
						field.setAccessible(true);
						field.set(beanObject, bean);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
			else if((value = field.getAnnotation(Value.class)) != null && ClassUtil.isJavaClass(field.getType())){
				String vlaueStr = value.value();
				if(StringUtils.isNotEmpty(vlaueStr) && StringUtils.includeProperty(vlaueStr)){
					vlaueStr = StringUtils.propertyKey(vlaueStr);
					String fieldValue = ApplicationProperties.properties.get(vlaueStr);
					Object objectValue = ClassUtil.parseValue(fieldValue, field.getType());
					try {
						field.setAccessible(true);
						field.set(beanObject, objectValue);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public Object findContextBean(Class<?> fieldType){
		/**
		 * 从已经实例化的Bean中找对应类型
		 */
		Object bean = findBean(fieldType);
		
		/**
		 * 从基本注解Bean中找类型匹配的Bean
		 */
		if(bean == null){
			Class<?> fieldClass = findContextClasss(fieldType);
			if(fieldClass != null){
				bean = createBean(fieldClass);
			}
		}
		return bean;
	}
	
	public String getBeanName(Class<?> clazz){
		Annotation[] annottions = clazz.getAnnotations();
		String beanName = null;
		for (Annotation annotation : annottions) {
			if(annotation instanceof Action){
				beanName = ((Action) annotation).value();
				break;
			}
			else if(annotation instanceof Service){
				beanName = ((Service) annotation).value();
				break;
			}
			else if(annotation instanceof Dao){
				beanName = ((Dao) annotation).value();
				break;
			}
			else if(annotation instanceof Component){
				beanName = ((Component) annotation).value();
				break;
			}
		}
		if(StringUtils.isEmpty(beanName)){
			beanName = StringUtils.firstToLowerCase(clazz.getSimpleName());
		}
		return beanName;
	}
	
	public Object findBean(Class<?> type){
		boolean isInterface = type.isInterface();
		Object beanObject = null;
		for (Map.Entry<String, Object> bean : beans.entrySet()) {
			Object beanObj = bean.getValue();
			if(isInterface){
				Class<?>[] interfaces = beanObj.getClass().getInterfaces();
				if(interfaces != null){
					for (Class<?> interf : interfaces) {
						if(type == interf){
							beanObject =  beanObj;
						}
					}
				}
			}
			else if(beanObj.getClass() == type){
				beanObject = beanObj;
			}
		}
		return beanObject;
	}
	
	public Class<?> findContextClasss(Class<?> type){
		boolean isInterface = type.isInterface();
		for (Class<?>  clazz : annotationClasss) {
			if(isInterface){
				Class<?>[] interfaces = clazz.getInterfaces();
				if(interfaces != null){
					for (Class<?> interf : interfaces) {
						if(type == interf){
							return clazz;
						}
					}
				}
			}
			else if(clazz == type){
				return clazz;
			}
		}
		return null;
	}
	
	public boolean isContextAnnotation(Class<?> clazz){ 
		return clazz.getAnnotation(Component.class) != null ||
			   clazz.getAnnotation(Interceptor.class) != null ||
		       clazz.getAnnotation(Action.class) != null ||
		       clazz.getAnnotation(Service.class) != null ||
		       clazz.getAnnotation(Dao.class) != null || 
		       clazz.getAnnotation(Beans.class) != null;
	}
	
	public void extendCreateBean(Object beanObject)throws Exception{
		/**
		 * 辅助为bean设置上下文FactoryBean
		 */
		if(beanObject instanceof FactoryBeanAware){
			((FactoryBeanAware) beanObject ).setFactoryBean(this);
		}
		
		/**
		 * 调用其初始化bean方法
		 */
		if(beanObject instanceof InitializingBean){
			((InitializingBean) beanObject ).afterPropertiesSet();
		}
		
		/**
		 * 如果存在@InitMethod注解 则调用
		 */
		Method[] methods = beanObject.getClass().getDeclaredMethods();
		if(methods != null && methods.length > 0){
			for(Method method : methods){
				if(method.getAnnotation(InitMethod.class) != null){
					method.invoke(beanObject, new Object[]{});
					break;
				}
			}
		}
	}

}
