package arquitetura.touml;


import org.w3c.dom.Node;

import arquitetura.exceptions.NotSuppportedOperation;
import arquitetura.helpers.XmiHelper;
import arquitetura.representation.Architecture;

/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class GeneralizationOperations extends XmiHelper implements Relationship  {
	
	
	private DocumentManager documentManager;
	
	private String general;
	private String client;
	private String name;
	private Architecture a; // Nao preciso aqui
	
	public GeneralizationOperations(DocumentManager doc) {
		this.documentManager = doc;
	}

	public GeneralizationOperations(DocumentManager documentManager, Architecture a) {
		this.documentManager = documentManager;
		this.a = a;
	}

	public Relationship createRelation(String name) {
		this.name = name;
		return this;
	}

	/**
	 * A client
	 */
	public Relationship between(String idElement) throws NotSuppportedOperation {
		if(isElementAClass(idElement)){
			this.client = idElement;
			return this;
		}else{
			throw new NotSuppportedOperation("Cannot create generaliazation with package");
		}
	}
	
	/**
	 * A general
	 */
	public Relationship and(String idElement) throws NotSuppportedOperation {
		if(isElementAClass(idElement)){
			this.general = idElement;
			return this;
		}else{
			throw new NotSuppportedOperation("Cannot create generaliazation with package");
		}
	}


	private boolean isElementAClass(String idElement) {
		Node element = findByID(documentManager.getDocUml(), idElement, "packagedElement");
		if ("uml:Class".equalsIgnoreCase(element.getAttributes().getNamedItem("xmi:type").getNodeValue()))
				return true;
		return false;
	}

	public String build() {
		final GeneralizationNode generalizationNode = new GeneralizationNode(this.documentManager, this.general, this.client, this.name);
		
		arquitetura.touml.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() {
				generalizationNode.createGeneralization();
			}
		});
		
		return "";
	}

	public Relationship withMultiplicy(String string)throws NotSuppportedOperation {
		return null;
	}

}