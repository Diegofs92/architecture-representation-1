package mestrado.arquitetura.parser;

import mestrado.arquitetura.exceptions.CustonTypeNotFound;
import mestrado.arquitetura.exceptions.InvalidMultiplictyForAssociationException;
import mestrado.arquitetura.exceptions.NodeNotFound;

public class AssociationOperations {
	
	private DocumentManager documentManager;
	private ElementXmiGenerator elementXmiGenerator;
	
	private String idClassOwnnerAssociation;
	private String idClassDestinationAssociation;
	
	private String multiplicityClassOwnner;
	private String multiplicityClassDestination;

	public AssociationOperations(DocumentManager documentManager){
		this.documentManager = documentManager;
		elementXmiGenerator = new ElementXmiGenerator(documentManager);
	}
	
	public AssociationOperations createAssociation() {
		return new AssociationOperations(documentManager);
	}

	public AssociationOperations betweenClass(String idClass) {
		this.idClassOwnnerAssociation = idClass;
		return this;
	}

	public AssociationOperations withMultiplicy(String multiplicity) {
		if(this.idClassDestinationAssociation != null)
			this.multiplicityClassDestination = multiplicity;
		else if(this.idClassOwnnerAssociation != null)
			this.multiplicityClassOwnner = multiplicity;
		return this;
	}

	public AssociationOperations andClass(String idClass) {
		this.idClassDestinationAssociation = idClass;
		return this;
	}
	
	public String build() throws CustonTypeNotFound, NodeNotFound, InvalidMultiplictyForAssociationException{
		//Refactoring, document.getNewName is common for many classes
		final AssociationNode associationNode = new AssociationNode(this.documentManager.getDocUml(), this.documentManager.getDocNotation(), documentManager.getModelName());
		
		mestrado.arquitetura.parser.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() throws NodeNotFound, InvalidMultiplictyForAssociationException {
				associationNode.createAssociation(idClassOwnnerAssociation, idClassDestinationAssociation, multiplicityClassDestination, multiplicityClassOwnner);
			}
		});
		
		return associationNode.getIdAssocation();
	}

}