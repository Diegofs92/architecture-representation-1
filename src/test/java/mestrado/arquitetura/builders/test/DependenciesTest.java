	package mestrado.arquitetura.builders.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import mestrado.arquitetura.builders.ArchitectureBuilder;
import mestrado.arquitetura.helpers.test.TestHelper;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.Class;
import mestrado.arquitetura.representation.DependencyInterClassRelationship;
import mestrado.arquitetura.representation.Element;

import org.junit.Before;
import org.junit.Test;

public class DependenciesTest extends TestHelper {
	
	
	private Architecture arch;
	
	@Before
	public void setUp() throws Exception{
		arch = givenAArchitecture("dependency");
	}

	@Test
	public void shouldLoadDependency() throws Exception {
		DependencyInterClassRelationship dependency = arch.getAllDependencyInterClass().get(0);

		assertNotNull(dependency);
		assertEquals("Supplier Should be Class1", "Class1", dependency.getSupplier().getName());
		assertEquals("Client Should be Class2", "Class2", dependency.getAllClientsForSupplierClass().get(0).getName());
		assertEquals("Dependency name should be Dependency1", "Dependency1", dependency.getName());
	}
	
	/**
	 * @see <a href="http://d.pr/i/q4QO">Modelo usado no teste (Imagem)</a>
	 * @throws Exception
	 */
	@Test
	public void shouldLoadDependencyClassInsidePackageToClassOutsidePackage() throws Exception {
		String uriToArchitecture2 = getUrlToModel("classPacote");
		Architecture a = new ArchitectureBuilder().create(uriToArchitecture2);

		assertNotNull(a);
		Element class1 = a.getAllDependencyInterClass().get(0).getClient();
		Element class2 = a.getAllDependencyInterClass().get(0).getSupplier();
		assertEquals("Class1", class1.getName());
		assertEquals("Class2", class2.getName());
	}

	/**
	 * @see <a href="http://d.pr/i/uVOY">Modelo usado no teste (Imagem)</a>
	 * @throws Exception
	 */
	@Test
	public void shouldLoadDependencyClassOutsidePackageToClassInsidePackage()	throws Exception {
		Architecture a =givenAArchitecture("dependencyClassOutsidePackageToClassInside");

		assertNotNull(a);

		assertEquals(1, a.getAllDependencyInterClass().size());
		assertEquals("Class1", a.getAllDependencyInterClass().get(0).getClient().getName());
		assertEquals("model", a.getAllDependencyInterClass().get(0).getClient().getNamespace());
		assertEquals("Class2", a.getAllDependencyInterClass().get(0).getSupplier().getName());
		assertEquals("model::Package1", a.getAllDependencyInterClass().get(0).getSupplier().getNamespace());

	}

	@Test
	public void shouldDependencyNameBeEmptyWhenNull() throws Exception {

		DependencyInterClassRelationship dependency = arch.getAllDependencyInterClass().get(0);
		dependency.setName(null);
		assertEquals("Dependency name should be empty", "",	dependency.getName());
	}
	
	
	@Test
	public void shouldDependencyContainsTwoClients() throws Exception {


		DependencyInterClassRelationship d2 = arch.getAllDependencyInterClass().get(1);
		DependencyInterClassRelationship d3 = arch.getAllDependencyInterClass().get(2);

		assertEquals("Dependency2", d2.getName());
		assertEquals("Dependency3", d3.getName());
		assertEquals(2, d3.getAllClientsForSupplierClass().size());
		assertEquals("Class4", d3.getAllClientsForSupplierClass().get(0).getName());
		assertEquals("Class5", d3.getAllClientsForSupplierClass().get(1).getName());
	}

	@Test
	public void shouldDependencyContainsTwoSuppliers() throws Exception {


		DependencyInterClassRelationship d4 = arch.getAllDependencyInterClass().get(3);
		DependencyInterClassRelationship d5 = arch.getAllDependencyInterClass().get(4);

		assertEquals("Dependency4", d4.getName());
		assertEquals("Dependency5", d5.getName());

		assertEquals(2, d4.getAllSuppliersForClientClass().size());
		assertEquals("Class7", d4.getAllSuppliersForClientClass().get(0).getName());
		assertEquals("Class8", d4.getAllSuppliersForClientClass().get(1).getName());
		assertEquals("Class7", d5.getAllSuppliersForClientClass().get(0)
				.getName());
		assertEquals("Class8", d5.getAllSuppliersForClientClass().get(1)
				.getName());
	}

	@Test
	public void shouldReplaceClientDependency() throws Exception {

		Class klass = (Class) arch.findElementByName("replaceClass");

		DependencyInterClassRelationship dependency = arch.getAllDependencyInterClass().get(0);
		assertEquals("Class2", dependency.getClient().getName());

		dependency.replaceClient(klass);

		assertEquals("replaceClass", dependency.getClient().getName());
	}

	@Test
	public void shouldReplaceSupplierDependency() throws Exception {
		Class klass = (Class) arch.findElementByName("replaceClass");

		DependencyInterClassRelationship dependency = arch.getAllDependencyInterClass().get(0);
		assertEquals("Class1", dependency.getSupplier().getName());

		dependency.replaceSupplier(klass);

		assertEquals("replaceClass", dependency.getSupplier().getName());
	}
	
	@Test
	public void shouldLoadInterElementDependency() throws Exception {
		String uriToArchitecture8 = getUrlToModel("dependency2");
		Architecture architecture8 = new ArchitectureBuilder()
				.create(uriToArchitecture8);

		DependencyInterClassRelationship dependencyInterElement = architecture8.getAllDependencyInterClass().get(0);

		assertNotNull(dependencyInterElement);

		assertEquals("Class1", dependencyInterElement.getSupplier().getName());
		Element intefacee = dependencyInterElement.getSupplier();
		assertTrue(intefacee.isInterface());
		assertNotNull(dependencyInterElement.getClient());
		assertEquals("Package1", dependencyInterElement.getClient().getName());
	}

	

	
	@Test
	public void shouldLoadDependencyClassPackage() throws Exception{
		Architecture a = givenAArchitecture("classPackageDependency");
		
		DependencyInterClassRelationship dependencyInterElement = a.getAllDependencyInterClass().get(0);

		assertNotNull(dependencyInterElement);
		
		assertEquals("Class2", dependencyInterElement.getClient().getName());
		assertEquals("Package1", dependencyInterElement.getSupplier().getName());
		
	}


}