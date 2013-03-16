package mestrado.arquitetura.representation;

/**
 * 
 * @author edipofederle
 *
 * Package -----> Interface
 */
public class DependencyPackageInterfaceRelationship extends InterElementRelationship {

	private Package pkg;
	private Class interfacee;

	public DependencyPackageInterfaceRelationship(Class interfacee, Package pkg) {
		setInterface(interfacee);
		setPackage(pkg);
		pkg.addRequiredInterface(interfacee);
	}

	public Package getPackage() {
		return pkg;
	}
	public void setPackage(Package pkg) {
		this.pkg = pkg;
	}

	public Class getInterface() {
		return interfacee;
	}

	public void setInterface(Class interfacee) {
		this.interfacee = interfacee;
	}
}