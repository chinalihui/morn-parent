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
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
import org.mornframework.context.beans.extend.BeanPostProcessorChain;
import org.mornframework.context.beans.extend.FactoryBeanAware;
import org.mornframework.context.beans.extend.InitializingBean;
import org.mornframework.context.beans.extend.RegisterAnnotationResolve;
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

	@SuppressWarnings("serial")
	public ContextFactoryBean(String basePackage,String propertiesPath){
		beans = new LinkedHashMap<String, Object>();
		beanDefinitions = new HashMap<String, BeanDefinition>();
		beanPostProcessorList = new ArrayList<BeanPostProcessorChain>();
		annotationClasses = new HashSet<Class<?>>();
		registerAnnotationResoleMap = new ConcurrentHashMap<Class<?>, RegisterAnnotationResolve>();
		contextAnnotation = new ArrayList<Class<?>>(){{
			add(Component.class);
			add(Action.class);
			add(Service.class);
			add(Dao.class);
			add(Beans.class);
			add(Interceptor.class);
		}};
		
		/**
		 * 初始化加载属性文件
		 */
		initProperties(propertiesPath);
		
		/**
		 * 根据web.xml指定的package扫描Class
		 */
		scanContextClasss(basePackage);
		
		/**
		 * 将扫描的Class转换为BeanDefinition
		 */
		resoleContextBeanConfig();
		
		/**
		 * 注册解析自定义注解类
		 */
		findRegisterAnnotationResole();
		
		/**
		 * 将自定义注解的Class转换为BeanDefinition
		 */
		resoleContextBeanConfig();
		
		/**
		 * 注册BeanPostProcessor
		 */
		registerBeanPostProcessor();
		
		BeanHolder.factoryBean = this;
	}
	
	@Override
	public void createContextBeans() {
		if(contextClasss == null || contextClasss.size()  == 0){
			LOG.info("Application context not scan java source file ");
			return;
		}
		
		for(Map.Entry<String, BeanDefinition> entry : beanDefinitions.entrySet()){
			BeanDefinition beanDefinition = entry.getValue();
			/**
			 * If bean is singeton
			 */
			if(SCOPE_SINGLETON.equals(beanDefinition.getScope())){
				createBean(entry.getKey(),beanDefinition,true);
			}
		}
	
	}
	
	public Object createBean(String beanName,BeanDefinition beanDefinition){
		return createBean(beanName,beanDefinition,SCOPE_SINGLETON.equals(beanDefinition.getScope()));
	}
	
	public Object createBean(String beanName,BeanDefinition beanDefinition,boolean isSingleton){
		Object beanObject = beans.get(beanName);
		if(beanObject != null){
			return beanObject;
		}
		try{
			Class<?> clazz = beanDefinition.getBeanClass();
			if(beanDefinition.getBeanFlag() == CONTEXT_BEAN_FLAG_ANNOS){
				
				/**
				 * 找是否有自定义注解解释类
				 */
				RegisterAnnotationResolve registerAnnotationResolve = null;
				boolean existRegisterAnnotation = false;
				Annotation[] annotations = clazz.getAnnotations();
				for (Annotation annotation : annotations) {
					registerAnnotationResolve = registerAnnotationResoleMap.get(annotation.annotationType());
					if(registerAnnotationResolve != null){
						existRegisterAnnotation = true;
						break;
					}
				}
				
				/**
				 * 执行自定义注解处理
				 * 
				 */
				if(existRegisterAnnotation){
					beanDefinition = registerAnnotationResolve.process(beanDefinition);
					beanDefinitions.put(beanName, beanDefinition);
					clazz =  beanDefinition.getBeanClass();
				}
			}
			
			beanObject = clazz.newInstance();
			/**
			 * 辅助为bean设置上下文FactoryBean
			 */
			if(beanObject instanceof FactoryBeanAware && isSingleton){
				((FactoryBeanAware) beanObject ).setFactoryBean(this);
			}
			
			/**
			 * 通过注入设置属性
			 */
			setBeanPropertyValues(beanDefinition,clazz,beanObject);
			
			/**
			 * 通过注解设置属性
			 */
			setterBeanAnnotation(beanObject, clazz.getDeclaredFields());
			
			if(isSingleton){
				beans.put(beanName, beanObject);
				
				/**
				 * 循环执行BeanPostProcessor.postProcessBeforeInitialization()
				 */
				for(BeanPostProcessorChain beanPostProcessorChain : beanPostProcessorList){
					beanPostProcessorChain.getBeanPostProcessor().postProcessBeforeInitialization(beanObject, beanName);
				}
				
				/**
				 * 扩展创建bean方法
				 */
				extendCreateBean(beanObject);
				
				/**
				 * 循环执行BeanPostProcessor.postProcessAfterInitialization()
				 */
				for(BeanPostProcessorChain beanPostProcessorChain : beanPostProcessorList){
					beanPostProcessorChain.getBeanPostProcessor().postProcessAfterInitialization(beanObject, beanName);
				}
			}
		} catch (Exception e) {
			throw new BeanInitializeException(e);
		}
		return beanObject;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setBeanPropertyValues(BeanDefinition beanDefinition
			,Class<?> clazz,Object beanObject) throws Exception{
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
							value = findContextBean(fieldClass,fieldName);
						}
						
						if(value == null){
							throw new BeanInitializeException("[Application Context] Initialize Bean:"
									+ "" + clazz + " exception ,reason is property '" + fieldName + "'"
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
					bean = findContextBean(fieldType,name);
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
	
	public Object findContextBean(Class<?> fieldType,String fieldName){
		/**
		 * 从已经实例化的Bean中找对应类型
		 */
		Object bean = findBean(fieldType);
		
		/**
		 * 从基本注解Bean中找类型匹配的Bean
		 */
		if(bean == null){
			BeanDefinition beanDefinition = beanDefinitions.get(fieldName);
			if(beanDefinition != null){
				bean = createBean(fieldName,beanDefinition);
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
	
	public boolean isContextAnnotation(Class<?> clazz){
		for(Class<?> annot : contextAnnotation){
			Annotation[] annots = clazz.getAnnotations();
			for (Annotation annotation : annots) {
				if(annotation.annotationType() == annot){
					return true;
				}
			}
		}
		return false;
	}
	
	public void extendCreateBean(Object beanObject)throws Exception{
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
	
	public void findRegisterAnnotationResole(){
		Class<?> registerAnnotationClass = RegisterAnnotationResolve.class;
		for(Map.Entry<String, BeanDefinition> entry : beanDefinitions.entrySet()){
			BeanDefinition beanDefinition = entry.getValue();
			Class<?> beanClazz = beanDefinition.getBeanClass();
			Class<?>[] interfaces = beanClazz.getInterfaces();
			boolean isRegisterAnnotaion = false;
			for(Class<?> interfa : interfaces){
				if(interfa == registerAnnotationClass){
					isRegisterAnnotaion = true;
					break;
				}
			}
			
			if(isRegisterAnnotaion){
				Object resoleAnnotationObject = null; 
				resoleAnnotationObject = createBean(beanDefinition.getBeanName(), beanDefinition);
				
				RegisterAnnotationResolve annotationResolve = ( (RegisterAnnotationResolve) resoleAnnotationObject);
				Class<?> annotationClass = annotationResolve.getAnnotationClass();
				if(annotationClass == null){
					throw new BeanInitializeException("[Application Context] create annotation resole:"
							+ " " + beanClazz + " getAnnotationClass is null ");
				}
				contextAnnotation.add(annotationClass );
				registerAnnotationResoleMap.put(annotationClass ,annotationResolve);
			}
		}
	}
	
	public void registerBeanPostProcessor(){
		Map<Integer, Boolean> orderMap = new HashMap<Integer, Boolean>();
		Class<?> beanPostProcessorClass = BeanPostProcessor.class;
		for(Map.Entry<String, BeanDefinition> entry : beanDefinitions.entrySet()){
			BeanDefinition beanDefinition = entry.getValue();
			Class<?> beanClazz = beanDefinition.getBeanClass();
			Class<?>[] interfaces = beanClazz.getInterfaces();
			boolean isBeanPostProcessor = false;
			for(Class<?> interfa : interfaces){
				if(interfa == beanPostProcessorClass){
					isBeanPostProcessor = true;
					break;
				}
			}
			
			if(isBeanPostProcessor){
				Object beanPostProcessorObj = null; 
				beanPostProcessorObj = createBean(beanDefinition.getBeanName(), beanDefinition);
				
				BeanPostProcessor beanPostProcessor = ( (BeanPostProcessor) beanPostProcessorObj);
				int order = beanPostProcessor.getOrder();
				if(orderMap.containsKey(order)){
					throw new BeanInitializeException("[Application Context] BeanPostProcessor "
							+ " " + beanClazz + " order " + order + " exist ");
				}
				
				orderMap.put(order, true);
				beanPostProcessorList.add(new BeanPostProcessorChain(order, beanPostProcessor));
			}
		}
	}

}
