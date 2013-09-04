package arquitetura.representation;

import java.util.ArrayList;
import java.util.List;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import arquitetura.exceptions.AttributeNotFoundException;
import arquitetura.exceptions.MethodNotFoundException;
import arquitetura.helpers.UtilResources;
import arquitetura.representation.relationship.Relationship;
import arquitetura.touml.VisibilityKind;
import arquitetura.touml.Types.Type;

/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class Class extends Element {
	
	static Logger LOGGER = LogManager.getLogger(Class.class.getName());
	
	private boolean isAbstract;
	private final List<Attribute> attributes = new ArrayList<Attribute>();
	private final List<Method> methods = new ArrayList<Method>();
	
	/**
	 * 
	 * @param architecture
	 * @param name
	 * @param isVariationPoint
	 * @param variantType
	 * @param isAbstract
	 * @param parent
	 * @param interfacee
	 * @param packageName
	 */
	public Class(Architecture architecture, String name, Variant variantType, boolean isAbstract, String namespace, String id) {
		
		super(architecture, name, variantType, "klass", namespace, id);
		setAbstract(isAbstract);
	}

	public Class(Architecture architecture, String name, String id) {
		this(architecture, name,  null, false,  UtilResources.createNamespace(getArchitecture().getName(), name), id);
	}

	public Attribute createAttribute(String name, Type type, VisibilityKind visibility) {
		String id = UtilResources.getRandonUUID();
		//Attribute a = new Attribute(getArchitecture(), name, VisibilityKind..toString(), type, getArchitecture().getName()+"::"+this.getName(), UtilResources.getRandonUUID());
		Attribute a = new Attribute(getArchitecture(), name, visibility.toString(), type.getName(), getArchitecture().getName()+"::"+this.getName(), id);
		getAllAttributes().add(a);
		getArchitecture().getAllIds().add(id);
		return a;
	}

	public void setAttribute(Attribute attr){
		this.attributes.add(attr);
	}
	
	public List<Attribute> getAllAttributes() {
		return attributes;
	}
	
	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}

	public boolean isAbstract() {
		return isAbstract;
	}
	
	public List<Method> getAllMethods() {
		return methods;
	}


	public void removeAttribute(Attribute att) {
		removeIdOfElementFromList(att	.getId());
		getAllAttributes().remove(att);
	}

	public Attribute findAttributeByName(String name) throws AttributeNotFoundException {
		String message = "Attribute '" + name + "' not found in class '"+ this.getName() +"'.\n";
		
		for(Attribute att : getAllAttributes())
			if (name.equalsIgnoreCase(att.getName()))
				return att;
		LOGGER.info(message);
		throw new AttributeNotFoundException(message);
	}

	public void moveAttributeToClass(Attribute att, Class destinationKlass) {
		if (!getAllAttributes().contains(att)) return;
		removeAttribute(att);
		destinationKlass.getAllAttributes().add(att);
		att.setNamespace(getArchitecture().getName() + "::" + destinationKlass.getName());
	}
	

	public Method createMethod(String name, String type, boolean isAbstract, List<ParameterMethod> parameters) {
		if (!methodExistsOnClass(name, type)){
			String id = UtilResources.getRandonUUID();
			Method method = new Method(getArchitecture(), name, type, this.getName(), isAbstract, id);
			if(parameters != null)
				method.getParameters().addAll(parameters);
			getAllMethods().add(method);
			getArchitecture().getAllIds().add(id);
			return method;
		}
		return null; //TODO exp
	}

	private boolean methodExistsOnClass(String name, String type) {
		for(Method m : getAllMethods()){
			if((name.equalsIgnoreCase(m.getName())) && (type.equalsIgnoreCase(m.getReturnType()))){
				LOGGER.info("Method '"+ name + ":"+type + "' currently created in class '"+this.getName()+"'.\n");
				return true;
			}
		}
		
		return false;
	}

	//TODO verificar metodos com mesmo nome e tipo diferente
	public Method findMethodByName(String name) throws MethodNotFoundException {
		String message = "Method '" + name + "' not found in class '"+ this.getName() +"'.\n";
		
		for(Method m : getAllMethods())
			if((name.equalsIgnoreCase(m.getName())))
				return m;
		
		LOGGER.info(message);
		throw new MethodNotFoundException(message);
	}

	public void moveMethodToClass(Method m, arquitetura.representation.Class destinationKlass) {
		if (!getAllMethods().contains(m)) return;
		
		removeMethod(m);
		m.setNamespace(getArchitecture().getName() + "::" + destinationKlass.getName());
		destinationKlass.getAllMethods().add(m);
	}

	public void removeMethod(Method foo) {
		removeIdOfElementFromList(foo.getId());
		getAllMethods().remove(foo);
	}

	private void removeIdOfElementFromList(String id) {
		getArchitecture().getAllIds().remove(id);
	}

	public List<Method> getAllAbstractMethods() {
		List<Method> abstractMethods = new ArrayList<Method>();
		
		for(Method m : getAllMethods())
			if (m.isAbstract()) abstractMethods.add(m);
		
		return abstractMethods;
	}

	public List<Relationship> getRelationships() {
		List<Relationship> relations = new ArrayList<Relationship>();

		for (Relationship relationship : getArchitecture().getAllRelationships())
			if(this.getIdsRelationships().contains(relationship.getId()))
				relations.add(relationship);
		
		return relations;
	}

	public boolean dontHaveAnyRelationship() {
		
		if(getIdsRelationships().size() == 0){
			return true;
		}else{
			return false;
		}
	}

	public void updateId(String id) {
		super.id = id;
		
	}

	public Object getAllStereotype() {
		return null;
	}
	
//	/**
//	 * se classe for um ponto de variação retorna todas as variantes
//	 * @return 
//	 */
//	public List<Element> getVariants() {
//		List<Element> variants = new ArrayList<Element>();
//		if(this.isVariationPoint()){
//			List<Variability> variabilities = getArchitecture().getAllVariabilities();
//			for (Variability variability : variabilities) {
//				for(String element : variability.getVariants()){
//					try {
//						variants.add(getArchitecture().findClassByName(element));
//					} catch (ClassNotFound e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}
//		return variants;
//	}
	
}