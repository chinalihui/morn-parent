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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.mornframework.context.annotation.Action;
import org.mornframework.context.annotation.Component;
import org.mornframework.context.annotation.Dao;
import org.mornframework.context.annotation.Inject;
import org.mornframework.context.annotation.Interceptor;
import org.mornframework.context.annotation.Service;
import org.mornframework.context.annotation.Value;
import org.mornframework.context.support.ApplicationProperties;
import org.mornframework.context.util.ClassUtil;
import org.mornframework.context.util.StringUtils;

/**
 * @author Jeff.Li
 * @date 2016年9月28日
 */
public class ContextFactoryBean extends AbstractFactoryBean{

	public ContextFactoryBean(String basePackage){
		scanContextClasss(basePackage);
		beans = new LinkedHashMap<String, Object>();
		prototypeClasses = new HashMap<String, Class<?>>();
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
		try {
			 beanObject = clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		Field[] fields = clazz.getDeclaredFields();
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
					bean = findBean(fieldType);
				}
				
				if(bean == null){
					Class<?> fieldClass = findContextClasss(fieldType);
					if(fieldClass != null){
						bean = createBean(fieldClass);
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
				String fieldValue = ApplicationProperties.properties.get(value.value());
				try {
					field.setAccessible(true);
					field.set(beanObject, fieldValue);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		if(isSingleton){
			beans.put(beanName, beanObject);
		}
		return beanObject;
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
		if(clazz.getAnnotation(Component.class) != null ||
		   clazz.getAnnotation(Interceptor.class) != null ||
		   clazz.getAnnotation(Action.class) != null ||
		   clazz.getAnnotation(Service.class) != null ||
		   clazz.getAnnotation(Dao.class) != null){
			return true;
		}
		return false;
	}

}
