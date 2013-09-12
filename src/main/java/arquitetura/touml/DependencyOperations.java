package arquitetura.touml;

import arquitetura.exceptions.NotSuppportedOperation;
import arquitetura.representation.Architecture;

/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class DependencyOperations implements Relationship {
	
	private DocumentManager documentManager;
	
	private String clientElement;
	private String supplierElement;
	private String name;
	private Architecture a;
	
	public DependencyOperations(DocumentManager doc, Architecture a) {
		this.documentManager = doc;
		this.a = a;
	}

	public DependencyOperations(DocumentManager documentManager2, String name2) {
		this.documentManager = documentManager2;
		this.name = name2;
	}

	public Relationship createRelation(String name) {
		if(("".equals(name) || name == null)) name = "dependency";
		return new DependencyOperations(this.documentManager, name);
	}

	public Relationship between(String idElement) {
		this.clientElement = idElement;
		return this;
	}

	public Relationship and(String idElement) {
		this.supplierElement = idElement;
		return this;
	}

	public String build() {
		final DependencyNode dependencyNode = new DependencyNode(this.documentManager, this.name, this.clientElement, this.supplierElement,a);
		
		arquitetura.touml.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() {
				dependencyNode.createDependency("dependency");
			}
		});
		
		return ""; //TODO return id;

	}

	public Relationship withMultiplicy(String string) throws NotSuppportedOperation {
		throw new NotSuppportedOperation("Dependency dont have multiplicy");
	}

}