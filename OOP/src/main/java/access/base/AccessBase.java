package access.base;

// public crosses packages, protected adds subclass access, package-private stays in its package and private stays within the enclosing top-level class
public class AccessBase {
    public int publicValue = 1;
    protected int protectedValue = 2;
    int packageValue = 3;
    private int privateValue = 4;

    public int privateThroughMethod() { return privateValue; }

    public static int samePackageAccess() {
        return new PackagePeer().read(new AccessBase());
    }
}

class PackagePeer {
    int read(AccessBase value) {
        return value.protectedValue + value.packageValue;
    }
}
