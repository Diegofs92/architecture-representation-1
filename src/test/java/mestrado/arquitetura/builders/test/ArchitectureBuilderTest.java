package mestrado.arquitetura.builders.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import mestrado.arquitetura.builders.ArchitectureBuilder;
import mestrado.arquitetura.helpers.ModelElementHelper;
import mestrado.arquitetura.helpers.StereotypeHelper;
import mestrado.arquitetura.helpers.test.TestHelper;
import mestrado.arquitetura.representation.AbstractionInterElementRelationship;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.AssociationEnd;
import mestrado.arquitetura.representation.AssociationInterClassRelationship;
import mestrado.arquitetura.representation.Class;
import mestrado.arquitetura.representation.Concern;
import mestrado.arquitetura.representation.DependencyComponentInterfaceRelationship;
import mestrado.arquitetura.representation.DependencyInterClassRelationship;
import mestrado.arquitetura.representation.Element;
import mestrado.arquitetura.representation.GeneralizationInterClassRelationship;
import mestrado.arquitetura.representation.InterClassRelationship;
import mestrado.arquitetura.representation.InterElementRelationship;
import mestrado.arquitetura.representation.Method;
import mestrado.arquitetura.representation.Package;
import mestrado.arquitetura.representation.RealizationInterClassRelationship;
import mestrado.arquitetura.representation.Variability;

import org.junit.Before;
import org.junit.Test;


public class ArchitectureBuilderTest extends TestHelper {
	
	private Architecture architecture;
	private Architecture architecture2;
	private Architecture architecture4;
	private Architecture architecture5;
	
	private Package package1;
	
	@Before
	public void setUp() throws Exception{
		String uriToArchitecture = getUrlToModel("testArch");
		architecture = new ArchitectureBuilder().create(uriToArchitecture);
		package1 = getPackageByName("Package1");
		
		String uriToArchitecture2 = getUrlToModel("association");
		architecture2 = new ArchitectureBuilder().create(uriToArchitecture2);
		
		String uriToArchitecture4 = getUrlToModel("generalizationArch");
		architecture4 = new ArchitectureBuilder().create(uriToArchitecture4);
		
		String uriToArchitecture5 = getUrlToModel("dependency");
		architecture5 = new ArchitectureBuilder().create(uriToArchitecture5);
	}
	
	@Test
	public void shouldHaveTwoConcerns(){
		assertEquals("Architecture should have two conecern", 2, architecture.getConcerns().size());
	}
	
	@Test
	public void shouldNotBeNull() throws Exception{
		assertNotNull("Architecture should NOT be null", architecture);
	}
	
	@Test
	public void shouldHaveAName() throws Exception{
		assertEquals("Architecture name should be 'modelVariability", "testArch", architecture.getName());
	}
	
	@Test
	public void shouldHaveLoadPackages() throws Exception{
		assertNotNull(architecture.getPackages());
		assertEquals("Architecture should have two package", 2, architecture.getPackages().size());
	}
	
	@Test
	public void shouldHaveCorrectNameForPackage(){
		assertContains(architecture.getPackages(), "Package1");
	}	
	
	@Test
	public void packageShouldContainTwoClasses(){
		hasClassesNames(package1, "Class1", "Class2", "Bar");
	}
	
	public Package getPackageByName(String name){
		List<Package> packges = architecture.getPackages();
		for (Package p : packges) {
			if(name.equalsIgnoreCase(p.getName()))
				return p;
		}
		return null;
	}
	
	@Test
	public void shouldHaveMandatoryStereotype(){
		Class class1 = package1.getClasses().get(0);
		assertEquals("mandatory", class1.getVariantType().toString());
	}
	
	@Test
	public void shouldHaveAClassBarWithOneAttribute(){
		 Class barKlass = package1.getClasses().get(2);
		 assertEquals("String",barKlass.getAttributes().get(0).getType());
		 assertEquals("name",barKlass.getAttributes().get(0).getName());
	}
	
	@Test
	public void shouldHaveOneMethod(){
		 Class class1 =package1.getClasses().get(0);
		 
		 assertEquals("Class1", class1.getName());
		 assertEquals(1, class1.getMethods().size());
		 assertEquals("foo", class1.getMethods().get(0).getName());
		 assertEquals("String", class1.getMethods().get(0).getReturnType());
		 assertEquals(3, class1.getMethods().get(0).getParameters().size());
		 assertEquals("name", class1.getMethods().get(0).getParameters().get(1).getName());
		 assertEquals("String", class1.getMethods().get(0).getParameters().get(1).getType());
		 assertEquals("Description", class1.getMethods().get(0).getParameters().get(2).getName());
		 assertEquals("String", class1.getMethods().get(0).getParameters().get(2).getType());
	}
	
	@Test
	public void shouldHaveAEmptyStringTypeWhenNotTypeFoundForAttribute(){
		Class klassClass2 = package1.getClasses().get(1);
		assertEquals("", klassClass2.getAttributes().get(0).getType());
		assertEquals("age", klassClass2.getAttributes().get(0).getName());
	}
	
	@Test
	public void shouldClass2HaveTwoAttributes(){
		Class klassClass2 = package1.getClasses().get(1);
		assertEquals(2, klassClass2.getAttributes().size());
	}
	
	@Test
	public void shoulClassdBeAbastract(){
		Class klass = package1.getClasses().get(2);
		assertTrue("class should be abstract", klass.isAbstract());
	}
	
	@Test
	public void shouldClassNotBeAbstract(){
		Class klass = package1.getClasses().get(1);
		assertFalse("class should not be abstract", klass.isAbstract());
	}
	
	@Test
	public void shouldContainsAClassWithConcern(){
		List<Concern> concerns =  package1.getClasses().get(0).getConcerns();
		assertFalse(concerns.isEmpty());
		assertEquals("Persistence", concerns.get(1).getName());
	}
	
	@Test
	public void shouldContainTwoConcerns(){
		List<Concern> concerns = package1.getClasses().get(0).getConcerns();
		assertEquals(2, concerns.size());
		assertEquals("Persistence",  concerns.get(1).getName());
		assertEquals("sorting",  concerns.get(0).getName());
	}
	
	@Test
	public void testWithoutPackages() throws Exception{
		String uriToArchitecture = getUrlToModel("semPacote");
		architecture = new ArchitectureBuilder().create(uriToArchitecture);
		assertNotNull(architecture);
		assertEquals(2, architecture.getClasses().size());
		assertEquals("Foo", architecture.getClasses().get(0).getName());
		
		assertEquals(1, architecture.getPackages().size());
		assertEquals("pacote1", architecture.getPackages().get(0).getName());
		Class klassBar = architecture.getPackages().get(0).getClasses().get(0);
		assertEquals("Bar", klassBar.getName());
	}

	@Test
	public void shoudlElementHaveAParentPackage() throws Exception{
		String uriToArchitecture = getUrlToModel("semPacote");
		architecture = new ArchitectureBuilder().create(uriToArchitecture);
		Class klassBar = architecture.getPackages().get(0).getClasses().get(0);
		assertNotNull(klassBar.getParent());
		assertEquals("Class should belongs to package 'pacote1'", "pacote1", klassBar.getParent().getName());
	}
	
	@Test
	public void shouldAClassWithoutPackageBelongsToNONEParent() throws Exception{
		String uriToArchitecture = getUrlToModel("semPacote");
		architecture = new ArchitectureBuilder().create(uriToArchitecture);
		Class fooKlass = architecture.getClasses().get(0);
		
		assertEquals(null, fooKlass.getParent());
	}
	
	@Test
	public void shouldMethodHaveAParentClass(){
		Class class1 = package1.getClasses().get(0);
		assertNotNull(class1.getMethods().get(0));
		assertNotNull(class1.getParent());
		assertEquals("Class1", class1.getMethods().get(0).getParent().getName());
	}
	
	@Test
	public void shouldAttributeHaveAParentClass(){
		Class klassClass2 = package1.getClasses().get(1);
		Element parentKlassClass2 = klassClass2.getAttributes().get(0).getParent();
		assertNotNull(parentKlassClass2);
		assertEquals("Class2", parentKlassClass2.getName());
	}
	
	@Test
	public void shouldMethodhaveAParrentClass(){
		Class class1 = package1.getClasses().get(0);
		Method fooMethod = class1.getMethods().get(0);
		assertNotNull(fooMethod.getParent());
		assertEquals("Class1", fooMethod.getParent().getName());
	}
	
	@Test
	public void shouldLoadPackagesInsidePackage(){
	  	assertEquals("Pacote2 should contains two packges", 2, getPackageByName("Pacote2").getNestedPackages().size());
	  	assertEquals("Pacote1DentroDoPacote2", getPackageByName("Pacote2").getNestedPackages().get(0).getName());
	}
	
	@Test
	public void shouldNestedPackagesHaveTwoClasses(){
		Package pkg = getPackageByName("Pacote2");
		Package p = pkg.getNestedPackages().get(0);
		assertNotNull(p);
		assertEquals(2,p.getClasses().size());
		hasClassesNames(p, "Person", "City");
	}
	
	@Test
	public void shouldNEstedPackage2HaveOneClass(){
		Package pkg = getPackageByName("Pacote2");
		Package pacote2DentrodoPacote2 = pkg.getNestedPackages().get(1);
		assertNotNull(pacote2DentrodoPacote2);
		assertEquals(1, pacote2DentrodoPacote2.getClasses().size());
		hasClassesNames(pacote2DentrodoPacote2, "Bar2");
	}
	
	@Test
	public void shouldHaveOneVariability(){
		assertEquals(1, architecture.getVariability().size());
	}
	
	@Test
	public void shouldVariabilityBelongToClass1(){
		Variability variability = architecture.getVariability().get(0);
		assertNotNull(variability);
		
		assertEquals("Class2", variability.getOwnerClass());
	}
	
	@Test
	public void shouldVariabilityHaveCorrectAttributesValues(){ 
		Variability variability = architecture.getVariability().get(0);
		assertEquals("nameClass2Variability", variability.getName());
		assertEquals("1", variability.getMinSelection());
		assertEquals("2", variability.getMaxSelection());
		assertTrue(variability.allowAddingVar());
		assertEquals("Bar", variability.getVariationPoints().get(0).getVariants().get(0).getName());
		assertEquals("Class2", variability.getVariationPoints().get(0).getVariationPointElement().getName());
	}
	
	@Test
	public void testVariationPointToString(){
		Variability variability = architecture.getVariability().get(0);
		assertEquals("Variants: Bar", variability.getVariationPoints().get(0).toString());
	}
	
	// Associations Tests //TODO Move from here
	
	@Test
	public void shouldHaveTwoAssociations(){
		 List<InterClassRelationship> relationships = architecture2.getInterClassRelationships();
		 int associationCount = 0;
		 for (InterClassRelationship interClassRelationship : relationships) {
			if (interClassRelationship instanceof AssociationInterClassRelationship)
				associationCount++;
		}
		assertEquals("Architecture should contain 4 associations", 4, associationCount);
	}
	
	
	@Test
	public void testAssociations() throws Exception{ 

		assertNotNull(architecture2);
		
		List<InterClassRelationship> r = architecture2.getInterClassRelationships();
		assertNotNull(r);
		assertTrue(r.get(0) instanceof AssociationInterClassRelationship);

		AssociationInterClassRelationship association = (AssociationInterClassRelationship) r.get(0);
		List<AssociationEnd> participants = association.getParticipants();
		
		assertEquals(2, participants.size());
		
		assertNotNull(association.getParticipants());
		assertEquals("none", association.getParticipants().get(0).getAggregation());
		assertFalse(association.getParticipants().get(0).isNavigable());
		assertEquals("Class2", participants.get(0).getCLSClass().getName());
		
		assertEquals("none", association.getParticipants().get(1).getAggregation());
		assertTrue(association.getParticipants().get(1).isNavigable());
		assertEquals("Class1", participants.get(1).getCLSClass().getName());
	}
	
	@Test
	public void testAssociation2(){
		List<InterClassRelationship> r = architecture2.getInterClassRelationships();
		AssociationInterClassRelationship association = (AssociationInterClassRelationship) r.get(1);
		List<AssociationEnd> participants = association.getParticipants();
		
		assertNotNull(association);
		
		assertEquals(2, participants.size());
		
		assertEquals("none", association.getParticipants().get(0).getAggregation());
		assertFalse(association.getParticipants().get(0).isNavigable());
		assertEquals("Class3", participants.get(0).getCLSClass().getName());
		
		assertEquals("none", association.getParticipants().get(1).getAggregation());
		assertTrue(association.getParticipants().get(1).isNavigable());
		assertEquals("Class4", participants.get(1).getCLSClass().getName());
		
	}
	
	@Test
	public void testMultiplicityAssociationRelationship(){
		List<InterClassRelationship> r = architecture2.getInterClassRelationships();
		AssociationInterClassRelationship association = (AssociationInterClassRelationship) r.get(1);
		
		assertEquals("1", association.getParticipants().get(1).getMultiplicity().getLowerValue());
		assertEquals("*", association.getParticipants().get(1).getMultiplicity().getUpperValue());
		assertEquals("1..*", association.getParticipants().get(1).getMultiplicity().toString() );
	}
	
	@Test
	public void testMultiplicityAssociationRelationship2(){
		List<InterClassRelationship> r = architecture2.getInterClassRelationships();
		AssociationInterClassRelationship association = (AssociationInterClassRelationship) r.get(0);
		
		assertEquals("1", association.getParticipants().get(0).getMultiplicity().getLowerValue());
		assertEquals("1", association.getParticipants().get(0).getMultiplicity().getUpperValue());
		assertEquals("1..1", association.getParticipants().get(0).getMultiplicity().toString() );
	}
	
	@Test
	public void shouldContainCompositeAssociation() throws Exception{
		List<InterClassRelationship> r = architecture2.getInterClassRelationships();
		AssociationInterClassRelationship associationComposite = (AssociationInterClassRelationship) r.get(2);
		List<AssociationEnd> participants = associationComposite.getParticipants();
		
		assertFalse(associationComposite.getParticipants().get(0).isNavigable());
		assertEquals("Class5", participants.get(0).getCLSClass().getName());
		
		assertEquals("composite", associationComposite.getParticipants().get(1).getAggregation());
		assertFalse(associationComposite.getParticipants().get(1).isNavigable());
		assertEquals("Class6", participants.get(1).getCLSClass().getName());
		assertEquals("none", associationComposite.getParticipants().get(0).getAggregation()); //TODO rever nome do metodo getAggregation para getTypeAssociation?
	
		assertEquals("0..*",associationComposite.getParticipants().get(0).getMultiplicity().toString());
		assertEquals("1..1",associationComposite.getParticipants().get(1).getMultiplicity().toString());
	}
	
	@Test
	public void shouldContainAggregationAssociation(){
		List<InterClassRelationship> r = architecture2.getInterClassRelationships();
		AssociationInterClassRelationship aggregation = (AssociationInterClassRelationship) r.get(3);
		List<AssociationEnd> participants = aggregation.getParticipants();
		
		assertFalse(aggregation.getParticipants().get(0).isNavigable());
		assertEquals("Class7", participants.get(0).getCLSClass().getName());
		
		assertFalse(aggregation.getParticipants().get(1).isNavigable());
		assertEquals("Class8", participants.get(1).getCLSClass().getName());
		
		assertEquals("Aggregation", aggregation.getParticipants().get(0).getAggregation());
		assertFalse(aggregation.getParticipants().get(1).isNavigable());
		
		assertEquals("1..1", aggregation.getParticipants().get(1).getMultiplicity().toString());
		assertEquals("1..*", aggregation.getParticipants().get(0).getMultiplicity().toString());	
	}
	
	@Test
	public void testAssociationWithThreeClasses() throws Exception{
		String uriToArchitecture = getUrlToModel("complexAssociation");
		Architecture architecture3 = new ArchitectureBuilder().create(uriToArchitecture);
		List<InterClassRelationship> r = architecture3.getInterClassRelationships();
		
		assertNotNull(architecture3);
		assertEquals("Should Contains Two Relationships", 2, r.size());
		assertEquals("Should Contains Three Classes", 3, architecture3.getClasses().size());
		
		AssociationInterClassRelationship association1 = (AssociationInterClassRelationship) r.get(0);
		AssociationInterClassRelationship association2 = (AssociationInterClassRelationship) r.get(1);
		
		assertNotNull(association1);
		assertNotNull(association2);
		
		assertEquals(2,association1.getParticipants().size());
		assertEquals(2, association2.getParticipants().size());
		
		List<AssociationEnd> a = association1.getParticipants();
		List<AssociationEnd> b = association2.getParticipants();
		Class klass3 = a.get(0).getCLSClass();
		Class klass2 = a.get(1).getCLSClass();
		
		Class klass1 = b.get(1).getCLSClass();
		Class kllass2a = b.get(0).getCLSClass();
		
		assertEquals("Class1", klass1.getName());
		assertEquals("Class2", kllass2a.getName());
		assertEquals("Class3", klass3.getName());
		assertEquals("Class2", klass2.getName());
		assertTrue(a.get(1).isNavigable());
		assertFalse(a.get(0).isNavigable());
	}
	
	// Associations Tests END //TODO Move from here

	// Generalization Tests

	
	@Test
	public void shouldLoadGeneralization() throws Exception{
		List<InterClassRelationship> relations = architecture4.getInterClassRelationships();
		assertEquals("Should contains six classes", 6, architecture4.getClasses().size());
		
		assertEquals(3, architecture4.getInterClassRelationships().size());
		
		GeneralizationInterClassRelationship generalization = (GeneralizationInterClassRelationship) relations.get(0);
		assertNotNull(generalization);
		assertEquals("Student", generalization.getChild().getName());
		assertEquals("Person", generalization.getParent().getName());
	}
	
	@Test
	public void shouldReplaceAParentClass() throws Exception{
		
		List<InterClassRelationship> relations = architecture4.getInterClassRelationships();
		Element professorKlass = architecture4.findElementByName("Professor");
		assertNotNull(professorKlass);
		assertEquals("Professor", professorKlass.getName());
		
		GeneralizationInterClassRelationship generalization = (GeneralizationInterClassRelationship) relations.get(0);
		
		assertEquals("Student", generalization.getChild().getName());
		generalization.replaceChild((Class) professorKlass);
		assertEquals("Professor", generalization.getChild().getName());
		
	}
	
	@Test
	public void shouldReplaceChildClass() throws Exception{
		List<InterClassRelationship> relations = architecture4.getInterClassRelationships();
		Element professorKlass = architecture4.findElementByName("Professor");
		assertNotNull(professorKlass);
		assertEquals("Professor", professorKlass.getName());
		
		GeneralizationInterClassRelationship generalization = (GeneralizationInterClassRelationship) relations.get(0);
		
		assertEquals("Person", generalization.getParent().getName());
		generalization.replaceParent((Class) professorKlass);
		assertEquals("Professor", generalization.getParent().getName());
	}
	
	@Test
	public void shouldLoadGeneralizationWithTwoChildreen(){
		List<InterClassRelationship> relations = architecture4.getInterClassRelationships();
		assertEquals(3, relations.size());
		assertEquals("Parent", ((GeneralizationInterClassRelationship)relations.get(1)).getParent().getName());
		assertEquals("Child1", ((GeneralizationInterClassRelationship)relations.get(1)).getChild().getName());
		assertEquals("Child2", ((GeneralizationInterClassRelationship)relations.get(2)).getChild().getName());
	}
	
	// Generalization Tests End
	
	// Dependency Tests 
	
	@Test
	public void shouldLoadDependency() throws Exception{
		List<InterClassRelationship> relations = architecture5.getInterClassRelationships();
		DependencyInterClassRelationship dependency = (DependencyInterClassRelationship) relations.get(0);
		
		assertNotNull(dependency);
		assertEquals("Supplier Should be Class1", "Class1", dependency.getSupplier().getName());
		assertEquals("Client Should be Class2", "Class2", dependency.getAllClientsForSupplierClass().get(0).getName());
		assertEquals("Dependency name should be Dependency1", "Dependency1", dependency.getName());
	}
	
	@Test
	public void shouldDependencyNameBeEmptyWhenNull() throws Exception{
		assertNotNull(architecture5);
		
		List<InterClassRelationship> relations = architecture5.getInterClassRelationships();
		DependencyInterClassRelationship dependency = (DependencyInterClassRelationship) relations.get(0);
		dependency.setName(null);
		assertEquals("Dependency name should be empty", "", dependency.getName());
	}
	
	@Test
	public void shouldDependencyContainsTwoClients() throws Exception{
		
		List<InterClassRelationship> relations = architecture5.getInterClassRelationships();
		
		DependencyInterClassRelationship d2 = ((DependencyInterClassRelationship)relations.get(1));
		DependencyInterClassRelationship d3 = ((DependencyInterClassRelationship)relations.get(2));

		assertEquals("Dependency2", d2.getName());
		assertEquals("Dependency3", d3.getName());
		assertEquals(2, d3.getAllClientsForSupplierClass().size());
		assertEquals("Class4", d3.getAllClientsForSupplierClass().get(0).getName());
		assertEquals("Class5", d3.getAllClientsForSupplierClass().get(1).getName());
	}
	
	@Test
	public void shouldDependencyContainsTwoSuppliers() throws Exception{
		
		List<InterClassRelationship> relations = architecture5.getInterClassRelationships();
		
		DependencyInterClassRelationship d4 = ((DependencyInterClassRelationship)relations.get(3));
		DependencyInterClassRelationship d5 = ((DependencyInterClassRelationship)relations.get(4));

		assertEquals("Dependency4", d4.getName());
		assertEquals("Dependency5", d5.getName());
		
		assertEquals(2,d4.getAllSuppliersForClientClass().size());
		assertEquals("Class7", d4.getAllSuppliersForClientClass().get(0).getName());
		assertEquals("Class8", d4.getAllSuppliersForClientClass().get(1).getName());
		assertEquals("Class7", d5.getAllSuppliersForClientClass().get(0).getName());
		assertEquals("Class8", d5.getAllSuppliersForClientClass().get(1).getName());
	}
	
	@Test
	public void shouldReplaceClienteDependency() throws Exception{
		List<InterClassRelationship> relations = architecture5.getInterClassRelationships();
		
		Class klass = (Class) architecture5.findElementByName("replaceClass");
		
		DependencyInterClassRelationship dependency = (DependencyInterClassRelationship) relations.get(0);
		assertEquals("Class2", dependency.getClient().getName());
		
		dependency.replaceClient(klass);
		
		assertEquals("replaceClass", dependency.getClient().getName());
	}
	@Test
	public void shouldReplaceSupplierDependency() throws Exception{
		List<InterClassRelationship> relations = architecture5.getInterClassRelationships();
		Class klass = (Class) architecture5.findElementByName("replaceClass");
		
		DependencyInterClassRelationship dependency = (DependencyInterClassRelationship) relations.get(0);
		assertEquals("Class1", dependency.getSupplier().getName());
		
		dependency.replaceSupplier(klass);
		
		assertEquals("replaceClass", dependency.getSupplier().getName());
	}
	
	// Dependency Tests End
	
	@Test
	public void shouldLoadRealizations() throws Exception{
		String uriToArchitecture6 = getUrlToModel("realization");
		Architecture architecture6 = new ArchitectureBuilder().create(uriToArchitecture6);
		assertNotNull(architecture6);
		
		List<InterClassRelationship> relations = architecture6.getInterClassRelationships();
		RealizationInterClassRelationship realization = (RealizationInterClassRelationship) relations.get(0);
		
		assertNotNull(realization);
		
		assertEquals("Source Element should be Class2", "Class2", realization.getSourceElement().getName());
		assertEquals("Specific Element should be Class1", "Class1", realization.getSpecificElement().getName());
		
	}
	
	@Test
	public void shouldLoadAbstractionInterElement() throws Exception{
		String uriToArchitecture7 = getUrlToModel("abstractionInterElement");
		Architecture architecture7 = new ArchitectureBuilder().create(uriToArchitecture7);
		
		assertNotNull(architecture7);
		
		List<InterElementRelationship> relations = architecture7.getInterElementRelationships();
		AbstractionInterElementRelationship abstractionInterElement = (AbstractionInterElementRelationship) relations.get(0);
		
		assertNotNull(abstractionInterElement);
		assertEquals("Supplier should be Package1Supplier", "Package1Supplier", abstractionInterElement.getChild().getName());
		assertEquals("Supplier should be myInterfaceClient", "myInterfaceClient", abstractionInterElement.getParent().getName());
		//TODO somente entre pacote e interface???
	}
	
	
	@Test
	public void shouldLoadInterElementDependency() throws Exception{
		String uriToArchitecture8 = getUrlToModel("dependency2");
		Architecture architecture8 = new ArchitectureBuilder().create(uriToArchitecture8);
		
		List<InterElementRelationship> relations = architecture8.getInterElementRelationships();
		DependencyComponentInterfaceRelationship dependencyInterElement = (DependencyComponentInterfaceRelationship) relations.get(0);
		
		assertNotNull(dependencyInterElement);
		
		assertNotNull(dependencyInterElement.getInterface());
		assertEquals("Class1", dependencyInterElement.getInterface().getName());
		Class intefacee = dependencyInterElement.getInterface();
		assertTrue(intefacee.isInterface());
		assertNotNull(dependencyInterElement.getPackage());
		assertEquals("Package1", dependencyInterElement.getPackage().getName());
	}

}